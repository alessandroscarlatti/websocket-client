package com.scarlatti.ws.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
public class WsRpcSyncManagerCountDownImpl implements WsRpcSyncManager {
    private CountDownLatch readyLatch = new CountDownLatch(1);
    private CountDownLatch killedLatch = new CountDownLatch(1);

    @Override
    public void notifyReady() {
        readyLatch.countDown();
    }

    @Override
    public void notifyKilled() {
        killedLatch.countDown();
    }

    @Override
    public void close() throws IOException {
    }
}
