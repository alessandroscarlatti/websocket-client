package com.scarlatti.ws.client;

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
class WsRpcCallable<V> implements Callable<V> {

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
     * @return the return value from the RPC, if any.
     * @throws Exception if the rpc fails for any reason.
     */
    @Override
    public V call() throws Exception {
        validateInvocation();

        // create the client and session handler
        client = factory.getWebSocketClient();
        sessionHandler = factory.getSessionHandler(details);

        // start the waiting thread...

        // connect to the server and invoke the remote procedure
        client.connect(details.getAddress(), sessionHandler);

        // wait for connection...countdown latch
        sessionHandler.invoke();

        // todo wait for the session handler to signal error or finished, or we reach our timeout limit...

        return null;
    }

    public void kill() {
        // todo wait for kill to complete...
        // start countdown latch
        sessionHandler.kill();
    }

    private void validateInvocation() {
        if (invoked)
            throw new IllegalStateException("May not invoke the websocket rpc implementation more than once.");

        invoked = true;
    }
}
