package com.scarlatti.ws.client.factory;

import com.scarlatti.ws.client.model.WsRpcDetails;
import com.scarlatti.ws.client.model.WsRpcControlMessage;
import com.scarlatti.ws.client.model.WsRpcStatusMessage;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
public class DefaultWsRpcMessageFactory implements WsRpcMessageFactory {

    private WsRpcDetails details;

    public DefaultWsRpcMessageFactory(WsRpcDetails details) {
        this.details = details;
    }

    @Override
    public WsRpcControlMessage invokeMessage() {
        return new WsRpcControlMessage(details.getInvoke());
    }

    @Override
    public WsRpcControlMessage killMessage() {
        return new WsRpcControlMessage(details.getKill());
    }

    @Override
    public WsRpcStatusMessage runningMessage() {
        return new WsRpcStatusMessage(details.getRunning());
    }

    @Override
    public WsRpcStatusMessage failedMessage() {
        return new WsRpcStatusMessage(details.getFailed());
    }

    @Override
    public WsRpcStatusMessage killedMessage() {
        return new WsRpcStatusMessage(details.getKilled());
    }

    @Override
    public WsRpcStatusMessage completeMessage() {
        return new WsRpcStatusMessage(details.getComplete());
    }
}
