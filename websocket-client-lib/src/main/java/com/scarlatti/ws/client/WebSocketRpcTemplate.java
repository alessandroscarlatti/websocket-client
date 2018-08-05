package com.scarlatti.ws.client;

import com.scarlatti.ws.client.factory.WsRpcFactory;
import com.scarlatti.ws.client.model.DefaultWsRpcDetails;
import com.scarlatti.ws.client.model.WsRpcDetails;
import com.scarlatti.ws.client.model.WsRpcStatusMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 * <p>
 * Template for performing RPC with a websocket message broker.
 * <p>
 * User can invoke remote procedure.
 * User can listen for live messages.
 * User can listen for error.
 * User can listen for success.
 * <p>
 * Invocation should be wrapped in a {@link java.util.concurrent.Future }
 */
public class WebSocketRpcTemplate implements Closeable {

    private WsRpcFactory factory;
    private WsRpcDetails details;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketRpcTemplate() {
        details = new DefaultWsRpcDetails();
        factory = new WsRpcFactory(details);
    }

    public WebSocketRpcTemplate(WsRpcDetails details) {
        this.details = details;
        factory = new WsRpcFactory(details);
    }

    public WebSocketRpcTemplate(WsRpcFactory factory, WsRpcDetails details) {
        this.factory = factory;
        this.details = details;
    }

    public void invoke() throws InterruptedException, ExecutionException {
        WsRpcCallable callable = new WsRpcCallable(factory, details);
        FutureTask<byte[]> rpc = new WsRpcFutureTask(callable, details);
        executorService.execute(rpc);
        rpc.get();
    }

    public String invokeForString() throws InterruptedException, ExecutionException {
        return new String(invokeForBytes());
    }

    public byte[] invokeForBytes() throws InterruptedException, ExecutionException {
        WsRpcCallable callable = new WsRpcCallable(factory, details);
        FutureTask<byte[]> rpc = new WsRpcFutureTask(callable, details);
        executorService.execute(rpc);
        return rpc.get();
    }

    public void sendRunning() {
        WsRpcStatusMessage message = factory.getMessageFactory().runningMessage();
        sendStatusMessage(message);
    }

    public void sendFailed() {
        WsRpcStatusMessage message = factory.getMessageFactory().runningMessage();
        sendStatusMessage(message);
    }

    public void sendKilled() {
        WsRpcStatusMessage message = factory.getMessageFactory().killedMessage();
        sendStatusMessage(message);
    }

    public void sendComplete() {
        WsRpcStatusMessage message = factory.getMessageFactory().completeMessage();
        sendStatusMessage(message);
    }

    private void sendStatusMessage(WsRpcStatusMessage message) {
        String string = factory.getMessageConverter().convertStatusMessageToString(message);
        simpMessagingTemplate.convertAndSend(details.getStatus(), string);
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
    }

    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
}
