package com.scarlatti.ws.client;

import java.util.concurrent.*;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
public class WsRpcFutureTask extends FutureTask<byte[]> {

    private WsRpcCallable callable;
    private WsRpcDetails details;

    public WsRpcFutureTask(WsRpcCallable callable, WsRpcDetails details) {
        super(callable);

        this.callable = callable;
        this.details = details;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {

        // first try to cancel the remote procedure
        callable.kill();

        return super.cancel(mayInterruptIfRunning);
    }
}
