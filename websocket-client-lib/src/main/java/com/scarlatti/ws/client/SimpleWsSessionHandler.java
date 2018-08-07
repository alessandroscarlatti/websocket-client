package com.scarlatti.ws.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Monday, 8/6/2018
 */
public class SimpleWsSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SimpleWsSessionHandler.class);

    private StompSession session;
    private StompHeaders defaultHeaders;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.session = session;
//        defaultHeaders = connectedHeaders;
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        session.disconnect();
        throw new RuntimeException("Error handling frame: [" + headers + "] [" + new String(payload) + "]", exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        session.disconnect();
        throw new RuntimeException("Transport error during session " + session.getSessionId(), exception);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        if (session.isConnected())
            handleFrameInternal(headers, payload);

    }

    protected void handleFrameInternal(StompHeaders headers, Object payload) {
    }

    protected void send(String destination, Object payload) {
        if (session.isConnected())
            session.send(destination, payload);
    }

    protected void send(StompHeaders headers, Object payload) {
        if (session.isConnected())
            session.send(headers, payload);
    }

    protected void disconnect() {
        if (session.isConnected())
            session.disconnect();
    }

    protected StompHeaders getDefaultHeaders() {
        return defaultHeaders;
    }
}
