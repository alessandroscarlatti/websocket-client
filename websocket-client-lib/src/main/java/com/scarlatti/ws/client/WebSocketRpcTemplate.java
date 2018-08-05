package com.scarlatti.ws.client;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 *
 * Template for performing RPC with a websocket message broker.
 *
 * User can invoke remote procedure.
 * User can listen for live messages.
 * User can listen for error.
 * User can listen for success.
 *
 * Invocation should be wrapped in a {@link java.util.concurrent.Future }
 */
public class WebSocketRpcTemplate {

    private WsRpcFactory factory;
    private WsRpcDetails details;

    // todo provide default factory and default details.

    public WebSocketRpcTemplate(WsRpcFactory factory, WsRpcDetails details) {
        this.factory = factory;
        this.details = details;
    }

    public <V> Future<V> invokeRpc() {
        Callable<V> callable = new WsRpcCallable<V>(factory, details);
        return new WsRpcFutureTask<>(callable);
    }
}
