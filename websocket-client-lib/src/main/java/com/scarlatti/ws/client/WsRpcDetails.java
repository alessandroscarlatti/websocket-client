package com.scarlatti.ws.client;

import java.util.function.Consumer;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
public interface WsRpcDetails {

    String getAddress();
    String getWsConnectPath();
    String getCommand();
    String getStatus();
    String getInvoke();
    String getKill();
    String getLog();
    String getRunning();
    String getComplete();
    String getError();
    String getKilled();
    String getInvokeMessage();
    String getKillMessage();
    long getInvokeTimeoutMs();
    long getProcTimeoutMs();
    long getKillTimeoutMs();
    Consumer<String> getLogger();
}
