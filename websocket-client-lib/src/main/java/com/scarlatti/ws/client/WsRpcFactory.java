package com.scarlatti.ws.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.converter.StringMessageConverter;
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

    private ObjectMapper objectMapper = new ObjectMapper();
    private List<ExecutorService> executorServices = new ArrayList<>();
    private List<WsRpcSyncManager> syncManagers = new ArrayList<>();

    protected WsRpcSessionHandler getSessionHandler(WsRpcDetails details) {
        ExecutorService service = getExecutorService();
        executorServices.add(service);
        WsRpcSyncManager syncManager = syncManager();
        syncManagers.add(syncManager);
        return new WsRpcSessionHandler(details, objectMapper, service, syncManager);
    }

    protected WebSocketStompClient getWebSocketClient() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new StringMessageConverter());
        return stompClient;
    }

    protected ExecutorService getExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    protected WsRpcSyncManager syncManager() {
        return new WsRpcSyncManagerCountDownImpl();
    }

    @Override
    public void close() throws IOException {
        for (ExecutorService service : executorServices) {
            service.shutdown();
        }

        for (WsRpcSyncManager syncManager : syncManagers) {
            syncManager.close();
        }
    }
}
