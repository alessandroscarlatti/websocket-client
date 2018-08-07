package com.scarlatti.ws.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
public class RpcSyncManagerCountDownImpl implements RpcSyncManager {
    private CountDownLatch readyLatch = new CountDownLatch(1);
    private CountDownLatch completeLatch = new CountDownLatch(1);
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
    public void notifyComplete() {
        completeLatch.countDown();
    }

    @Override
    public void awaitReady(long timeout, TimeUnit timeUnit) {
        try {
            if (!readyLatch.await(timeout, timeUnit)) {
                throw new RuntimeException("Unable to initiate rpc.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("awaitReady interrupted", e);
        }
    }

    @Override
    public void awaitKilled(long timeout, TimeUnit timeUnit) {
        try {
            killedLatch.await(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new RuntimeException("awaitReady interrupted", e);
        }
    }

    @Override
    public void awaitComplete(long timeout, TimeUnit timeUnit) {
        try {
            completeLatch.await(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new RuntimeException("awaitReady interrupted", e);
        }
    }

    @Override
    public void close() throws IOException {
    }
}
