package com.scarlatti.ws.client;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
public interface RpcSyncManager extends Closeable {

    void notifyReady();
    void notifyKilled();
    void notifyComplete();

    void awaitReady(long timeout, TimeUnit timeUnit);
    void awaitKilled(long timeout, TimeUnit timeUnit);
    void awaitComplete(long timeout, TimeUnit timeUnit);
}
