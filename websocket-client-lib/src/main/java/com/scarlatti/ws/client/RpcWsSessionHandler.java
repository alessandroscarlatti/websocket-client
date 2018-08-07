package com.scarlatti.ws.client;

import com.scarlatti.ws.client.model.WsRpcDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.concurrent.ExecutorService;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Monday, 8/6/2018
 */
public class RpcWsSessionHandler extends SimpleWsSessionHandler {

    private static final Logger log = LoggerFactory.getLogger(RpcWsSessionHandler.class);

    private WsRpcDetails details;
    private WsRpcInvocationDetails invocationDetails;
    private ExecutorService executor;
    private RpcSyncManager syncManager;
    private byte[] result;

    public RpcWsSessionHandler(WsRpcDetails details,
                               WsRpcInvocationDetails invocationDetails,
                               ExecutorService executor,
                               RpcSyncManager syncManager) {
        this.details = details;
        this.invocationDetails = invocationDetails;
        this.executor = executor;
        this.syncManager = syncManager;
    }

    /**
     * Invoke the remote procedure.
     * This is an asynchronous operation.
     */
    public void invoke() {
        StompHeaders headers = new StompHeaders();
        headers.add(details.getCommandHeader(), details.getInvoke());
        headers.setDestination(invocationDetails.getInvocationPath());
        send(headers, details.getInvoke());
    }

    /**
     * Kill the remote procedure.
     * This is an asynchronous operation.
     */
    public void kill() {
        StompHeaders headers = new StompHeaders();
        headers.add(details.getCommandHeader(), details.getKill());
        headers.setDestination(invocationDetails.getInvocationPath());
        send(headers, details.getKill());
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        super.afterConnected(session, connectedHeaders);
        session.subscribe(details.getStatus(), this);
        syncManager.notifyReady();
    }

    @Override
    protected void handleFrameInternal(StompHeaders headers, Object payload) {
        if (willHandleFrame(headers, payload))
            handleFilteredFrame(headers, payload);
    }

    protected boolean willHandleFrame(StompHeaders headers, Object payload) {
        boolean willHandleFrame = (headers.getDestination().equals(details.getStatus()));

        // we shouldn't be receiving messages we don't expect
        if (!willHandleFrame)
            log.warn("Unknown frame received: headers = [{}], payload = [{}]", headers, payload);

        return willHandleFrame;
    }

    protected void handleFilteredFrame(StompHeaders headers, Object payload) {
        String status = "";
        if (headers.containsKey(details.getStatusHeader())){
            status = headers.get(details.getStatusHeader()).get(0);
        }

        String msg = (String) payload;

        // the remote procedure is executing
        if (status.equals(details.getRunning())) {
            executor.submit(() -> {
                details.getLogger().accept(msg);
            });
        }

        // the remote procedure has aborted execution
        if (status.equals(details.getKilled())) {
            log.info("Remote procedure has been killed.");
            disconnect();
            syncManager.notifyKilled();
        }

        if (status.equals(details.getComplete())) {
            log.info("Remote procedure has completed.");
            result = msg.getBytes();
            disconnect();
            syncManager.notifyComplete();
        }

        if (status.equals(details.getFailed())) {
            log.info("Remote procedure has encountered an error");
            disconnect();
            throw new RuntimeException("Error in remote procedure: " + msg);
        }
    }

    public RpcSyncManager getSyncManager() {
        return syncManager;
    }

    public void setSyncManager(RpcSyncManager syncManager) {
        this.syncManager = syncManager;
    }

    public byte[] getResult() {
        return result;
    }
}
