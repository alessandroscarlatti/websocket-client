package com.scarlatti.ws.client.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scarlatti.ws.client.*;
import com.scarlatti.ws.client.model.WsRpcDetails;
import com.scarlatti.ws.client.converter.WsRpcJacksonMessageConverter;
import com.scarlatti.ws.client.converter.WsRpcMessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
public class WsRpcFactory implements Closeable {

    private List<ExecutorService> executorServices = new ArrayList<>();
    private List<RpcSyncManager> syncManagers = new ArrayList<>();
    private WsRpcMessageFactory messageFactory;
    private WsRpcMessageConverter messageConverter;
    private WsRpcDetails details;

    public WsRpcFactory(WsRpcDetails details) {
        this.details = details;
        messageFactory = messageFactory();
        messageConverter = messageConverter();
    }

    public StompSessionHandler getSessionHandler() {
        ExecutorService executorService = getExecutorService();
        executorServices.add(executorService);
        RpcSyncManager syncManager = syncManager();
        syncManagers.add(syncManager);
        return new RpcSessionHandler(
            details,
            messageConverter,
            messageFactory,
            executorService,
            syncManager);
    }

    public WebSocketStompClient getWebSocketClient() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new StringMessageConverter());
        return stompClient;
    }

    public ExecutorService getExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    public RpcSyncManager syncManager() {
        return new RpcSyncManagerCountDownImpl();
    }

    public WsRpcMessageFactory messageFactory() {
        return new DefaultWsRpcMessageFactory(details);
    }

    public WsRpcMessageConverter messageConverter() {
        return new WsRpcJacksonMessageConverter(new ObjectMapper());
    }

    @Override
    public void close() throws IOException {
        for (ExecutorService service : executorServices) {
            service.shutdown();
        }

        for (RpcSyncManager syncManager : syncManagers) {
            syncManager.close();
        }
    }

    public WsRpcMessageFactory getMessageFactory() {
        return messageFactory;
    }

    public WsRpcMessageConverter getMessageConverter() {
        return messageConverter;
    }
}
