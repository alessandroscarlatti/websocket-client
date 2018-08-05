package com.scarlatti.ws.client;

import com.scarlatti.ws.client.factory.WsRpcFactory;
import com.scarlatti.ws.client.model.WsRpcDetails;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.*;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 * <p>
 * the callable that actually executes the websocket communications.
 * It will contain the callback for logging.
 * <p>
 * It will assume the user will assume completion when the future returns.
 * It will assume the user will assume error when the future throws
 * an {@link ExecutionException}
 */
class WsRpcCallable implements Callable<byte[]> {

    private WebSocketStompClient client;
    private WsRpcSessionHandler sessionHandler;
    private WsRpcDetails details;
    private boolean invoked;
    private WsRpcFactory factory;

    public WsRpcCallable(WsRpcFactory factory, WsRpcDetails details) {
        this.factory = factory;
        this.details = details;
    }

    /**
     * @return the return value from the RPC, as a byte array, or null if no return value.
     * @throws Exception if the rpc fails for any reason.
     */
    @Override
    public byte[] call() throws TimeoutException, Exception {
        validateInvocation();

        // create the client and session handler
        client = factory.getWebSocketClient();
        sessionHandler = factory.getSessionHandler();

        // connect to the server
        client.connect(details.getAddress(), sessionHandler);

        // invoke the remote procedure
        sessionHandler.getSyncManager().awaitReady(details.getInvokeTimeoutMs(), TimeUnit.MILLISECONDS);
        sessionHandler.invoke();

        // wait for completion
        sessionHandler.getSyncManager().awaitComplete(details.getProcTimeoutMs(), TimeUnit.MILLISECONDS);
        return sessionHandler.getResult();
    }

    // todo force caller to handle timeout exception?
    public void kill() {
        sessionHandler.kill();
        sessionHandler.getSyncManager().awaitKilled(details.getKillTimeoutMs(), TimeUnit.MILLISECONDS);
    }

    private void validateInvocation() {
        if (invoked)
            throw new IllegalStateException("May not invoke the websocket rpc implementation more than once.");

        invoked = true;
    }
}
