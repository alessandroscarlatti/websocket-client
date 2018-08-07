package com.scarlatti.ws.client;

import com.scarlatti.ws.client.converter.WsRpcMessageConverter;
import com.scarlatti.ws.client.factory.WsRpcMessageFactory;
import com.scarlatti.ws.client.model.WsRpcControlMessage;
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
public class RpcSessionHandler extends SimpleWsSessionHandler {

    private static final Logger log = LoggerFactory.getLogger(RpcSessionHandler.class);

    private WsRpcDetails details;
    private WsRpcMessageConverter messageMapper;
    private ExecutorService executor;
    private RpcSyncManager syncManager;
    private WsRpcMessageFactory messageFactory;
    private byte[] result;

    public RpcSessionHandler(WsRpcDetails details,
                             WsRpcMessageConverter messageMapper,
                             WsRpcMessageFactory messageFactory,
                             ExecutorService executor,
                             RpcSyncManager syncManager) {
        this.details = details;
        this.messageMapper = messageMapper;
        this.executor = executor;
        this.syncManager = syncManager;
        this.messageFactory = messageFactory;
    }

    public RpcSessionHandler(RpcSyncManager syncManager) {
        this.syncManager = syncManager;
    }

    /**
     * Invoke the remote procedure.
     * This is an asynchronous operation.
     */
    public void invoke() {
        WsRpcControlMessage message = messageFactory.invokeMessage();
        String string = messageMapper.convertControlMessageToString(message);
        send(details.getControl(), string);
    }

    /**
     * Kill the remote procedure.
     * This is an asynchronous operation.
     */
    public void kill() {
        WsRpcControlMessage message = messageFactory.killMessage();
        String string = messageMapper.convertControlMessageToString(message);
        send(details.getControl(), string);
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
        String status = headers.get(details.getStatusHeader()).get(0);  // todo configure this hardcoded value
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
