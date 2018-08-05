package com.scarlatti.ws.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scarlatti.ws.client.model.WsRpcMessage;
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
    private ObjectMapper objectMapper;
    private ExecutorService executor;
    private WsRpcSyncManager syncManager;

    public WsRpcSessionHandler(WsRpcDetails details, ObjectMapper objectMapper, ExecutorService executor, WsRpcSyncManager syncManager) {
        this.details = details;
        this.objectMapper = objectMapper;
        this.executor = executor;
        this.syncManager = syncManager;
    }

    /**
     * Invoke the remote procedure.
     * This is an asynchronous operation.
     */
    void invoke() {
        // todo send kill message
        session.send(details.getInvoke(), details.getInvokeMessage());
    }

    /**
     * Kill the remote procedure.
     * This is an asynchronous operation.
     */
    void kill() {
        // todo send kill message
        session.send(details.getKill(), details.getKillMessage());
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("WsRpcSessionHandler.afterConnected() session = [{}], connectedHeaders = [{}]", session, connectedHeaders);
        session.subscribe("/topic/status", this);
        this.session = session;
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

        // we shouldn't be receiving messages we don't expect
        if (!headers.getDestination().equals(details.getStatus())) {
            log.warn("Unknown frame received: headers = [{}], payload = [{}]", headers, payload);
        }

        try {
            WsRpcMessage msg = objectMapper.readValue((String) payload, WsRpcMessage.class);
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
                // todo callable should ???
            }

            if (status.equals(details.getComplete())) {
                log.info("Remote procedure has completed.");
                // todo return value...
            }

            if (status.equals(details.getError())) {
                log.info("Remote procedure has encountered an error");
                // todo throw exception
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing rpc message " + payload);
        }
    }
}
