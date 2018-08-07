package com.scarlatti.ws.client;

import com.scarlatti.ws.client.factory.WsRpcFactory;
import com.scarlatti.ws.client.model.DefaultWsRpcDetails;
import com.scarlatti.ws.client.model.WsRpcDetails;
import com.scarlatti.ws.client.model.WsRpcStatusMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import javax.management.monitor.StringMonitor;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Monday, 8/6/2018
 */
public class WebSocketRpcTemplate implements Closeable {

    private WsRpcDetails details;
    private String connectAddress;
    private WsRpcFactory factory;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketRpcTemplate(String connectAddress) {
        this.connectAddress = connectAddress;
        details = new DefaultWsRpcDetails();
        factory = new WsRpcFactory(details);
    }

    public WebSocketRpcTemplate(String connectAddress, WsRpcDetails details) {
        this.connectAddress = connectAddress;
        this.details = details;
        factory = new WsRpcFactory(details);
    }

    public void converse(String path, StompSessionHandler sessionHandler) {
        converseForBytes(path, sessionHandler);
    }

    public byte[] converseForBytes(String path, StompSessionHandler sessionHandler) {
        // todo implement this!
        return null;
    }

    public String converseForString(String path, StompSessionHandler sessionHandler) {
        return new String(converseForBytes(path, sessionHandler));
    }

    public void invoke(String path, Object... args) throws InterruptedException, ExecutionException {
        invokeForBytes(path, args);
    }

    public String invokeForString(String path, Object... args) throws InterruptedException, ExecutionException {
        byte[] bytes = invokeForBytes(path, args);
        if (bytes == null)
            return null;

        return new String(bytes);
    }

    public byte[] invokeForBytes(String path, Object... args) throws InterruptedException, ExecutionException {
        WsRpcInvocationDetails invocationDetails = new WsRpcInvocationDetails(connectAddress, path, args);
        WsRpcCallable callable = new WsRpcCallable(factory, details, invocationDetails);
        FutureTask<byte[]> rpc = new WsRpcFutureTask(callable);
        executorService.execute(rpc);
        return rpc.get();
    }

    // TODO on the "send" methods, try adding the status route as a parameter...

    // TODO then try an api that wraps the futureTask with the rpcTemplate.

    public void sendRunning() {
        sendRunning(details.getRunning());
    }

    public void sendRunning(String text) {
        sendStatusMessage(details.getRunning(), text);
    }

    public void sendFailed() {
        sendFailed(details.getFailed());
    }

    public void sendFailed(String text) {
        sendStatusMessage(details.getRunning(), text);
    }

    public void sendKilled() {
        sendKilled(details.getKilled());
    }

    public void sendKilled(String text) {
        sendStatusMessage(details.getKilled(), text);
    }

    public void sendComplete() {
        sendComplete(details.getComplete());
    }

    public void sendComplete(String text) {
        sendStatusMessage(details.getComplete(), text);
    }

    private void sendStatusMessage(String status, String message) {
        Objects.requireNonNull(simpMessagingTemplate, "SimpleMessagingTemplate required to send message");
        Map<String, Object> headers = new HashMap<>();
        headers.put(details.getStatusHeader(), status);
        simpMessagingTemplate.convertAndSend(details.getStatus(), message, headers);
    }

    @Override
    public void close() throws IOException {
        if (factory != null)
            factory.close();

        if (executorService != null)
            executorService.shutdown();
    }

    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
}
