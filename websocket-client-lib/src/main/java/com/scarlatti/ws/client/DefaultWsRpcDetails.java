package com.scarlatti.ws.client;

import java.util.function.Consumer;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 *
 * Class to configure connection details,
 * as well as rpc event notifications.
 */
public class DefaultWsRpcDetails implements WsRpcDetails {
    // todo configure defaults
    private String address;
    private String wsConnectPath;
    private String command;
    private String status;
    private String invoke;
    private String kill;
    private String log;
    private String running;
    private String complete;
    private String error;
    private String killed;
    private String invokeMessage;
    private String killMessage;

    private Consumer<String> logger = noOpLogger();

    private static Consumer<String> noOpLogger() {
        return (s) -> {};
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getWsConnectPath() {
        return wsConnectPath;
    }

    public void setWsConnectPath(String wsConnectPath) {
        this.wsConnectPath = wsConnectPath;
    }

    @Override
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getInvoke() {
        return invoke;
    }

    public void setInvoke(String invoke) {
        this.invoke = invoke;
    }

    @Override
    public String getKill() {
        return kill;
    }

    public void setKill(String kill) {
        this.kill = kill;
    }

    @Override
    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @Override
    public String getRunning() {
        return running;
    }

    public void setRunning(String running) {
        this.running = running;
    }

    @Override
    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    @Override
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String getKilled() {
        return killed;
    }

    public void setKilled(String killed) {
        this.killed = killed;
    }

    @Override
    public String getInvokeMessage() {
        return invokeMessage;
    }

    public void setInvokeMessage(String invokeMessage) {
        this.invokeMessage = invokeMessage;
    }

    @Override
    public String getKillMessage() {
        return killMessage;
    }

    public void setKillMessage(String killMessage) {
        this.killMessage = killMessage;
    }

    @Override
    public Consumer<String> getLogger() {
        return logger;
    }

    public void setLogger(Consumer<String> logger) {
        this.logger = logger;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
