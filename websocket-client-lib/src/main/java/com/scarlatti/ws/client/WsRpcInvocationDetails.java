package com.scarlatti.ws.client;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Monday, 8/6/2018
 */
public class WsRpcInvocationDetails {
    private String connectionPath;
    private String invocationPath;
    private Object[] params;

    public WsRpcInvocationDetails(String connectionPath, String invocationPath, Object[] params) {
        this.connectionPath = connectionPath;
        this.invocationPath = invocationPath;
        this.params = params;
    }

    public String getInvocationPath() {
        return invocationPath;
    }

    public void setInvocationPath(String invocationPath) {
        this.invocationPath = invocationPath;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
