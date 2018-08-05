package com.scarlatti.ws.client;

import com.scarlatti.ws.client.converter.WsRpcMessageConverter;
import com.scarlatti.ws.client.factory.WsRpcMessageFactory;
import com.scarlatti.ws.client.model.WsRpcControlMessage;
import com.scarlatti.ws.client.model.WsRpcDetails;
import com.scarlatti.ws.client.model.WsRpcStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
public class WsRpcSessionHandler implements StompSessionHandler {

    private static final Logger log = LoggerFactory.getLogger(WsRpcSessionHandler.class);

    private StompSession session;
    private WsRpcDetails details;
    private WsRpcMessageConverter messageMapper;
    private ExecutorService executor;
    private WsRpcSyncManager syncManager;
    private WsRpcMessageFactory messageFactory;
    private byte[] result;

    public WsRpcSessionHandler(WsRpcDetails details,
                               WsRpcMessageConverter messageConverter,
                               WsRpcMessageFactory messageFactory,
                               ExecutorService executor,
                               WsRpcSyncManager syncManager) {
        this.details = details;
        this.messageMapper = messageConverter;
        this.executor = executor;
        this.syncManager = syncManager;
        this.messageFactory = messageFactory;
    }

    /**
     * Invoke the remote procedure.
     * This is an asynchronous operation.
     */
    void invoke() {
        WsRpcControlMessage message = messageFactory.invokeMessage();
        String string = messageMapper.convertControlMessageToString(message);
        session.send(details.getControl(), string);
    }

    /**
     * Kill the remote procedure.
     * This is an asynchronous operation.
     */
    void kill() {
        WsRpcControlMessage message = messageFactory.killMessage();
        String string = messageMapper.convertControlMessageToString(message);
        session.send(details.getControl(), string);
    }

    /**
     * @return the return value, if any, of the remote procedure.
     */
    public byte[] getResult() {
        return result;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("WsRpcSessionHandler.afterConnected() session = [{}], connectedHeaders = [{}]", session, connectedHeaders);
        this.session = session;
        session.subscribe(details.getStatus(), this);
        syncManager.notifyReady();
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.info("WsRpcSessionHandler.handleException() session = [{}], command = [{}], headers = [{}], payload = [{}], exception = [{}]", session, command, headers, payload, exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.info("WsRpcSessionHandler.handleTransportError() session = [{}], exception = [{}]", session, exception);

        // todo throw exception in callable
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        log.info("WsRpcSessionHandler.getPayloadType() headers = [{}]", headers);
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("WsRpcSessionHandler.handleFrame() headers = [{}], payload = [{}]", headers, payload);

        if (!session.isConnected()) {
            log.warn("Ignoring message since session has been disconnected: {}", payload);
            return;
        }

        // we shouldn't be receiving messages we don't expect
        if (!headers.getDestination().equals(details.getStatus())) {
            log.warn("Unknown frame received: headers = [{}], payload = [{}]", headers, payload);
        }

        try {
            WsRpcStatusMessage msg = (WsRpcStatusMessage) messageMapper.convertStatusMessageFromString((String) payload);
            String status = msg.getStatus();

            // the remote procedure is executing
            if (status.equals(details.getRunning())) {
                executor.submit(() -> {
                    details.getLogger().accept(new String(msg.getContentBytes()));
                });
            }

            // the remote procedure has aborted execution
            if (status.equals(details.getKilled())) {
                log.info("Remote procedure has been killed.");
                session.disconnect();
                syncManager.notifyKilled();
            }

            if (status.equals(details.getComplete())) {
                log.info("Remote procedure has completed.");
                result = msg.getContentBytes();
                session.disconnect();
                syncManager.notifyComplete();
            }

            if (status.equals(details.getFailed())) {
                log.info("Remote procedure has encountered an error");
                session.disconnect();
                // todo throw exception in callable...
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing rpc message " + payload, e);
        }
    }

    public WsRpcSyncManager getSyncManager() {
        return syncManager;
    }
}
