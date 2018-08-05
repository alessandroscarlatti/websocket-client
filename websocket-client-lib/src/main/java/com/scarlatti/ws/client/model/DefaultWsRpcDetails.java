package com.scarlatti.ws.client.model;

import com.scarlatti.ws.client.WsRpcDetails;

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
    private String control;
    private String status;
    private String invoke;
    private String kill;
    private String log;
    private String running;
    private String complete;
    private String error;
    private String killed;
    private long invokeTimeoutMs;
    private long procTimeoutMs;
    private long killTimeoutMs;

    private Consumer<String> logger = noOpLogger();

    public static final String WS_CONNECT_PATH = "/connect";
    public static final String CONTROL = "/control"; // todo figure out what this actually is
    public static final String INVOKE = "/invoke";
    public static final String KILL = "/kill";
    public static final String RUNNING = "RUNNING";
    public static final String COMPLETE = "COMPLETE";
    public static final String KILL_MESSAGE = "KILL";
    public static final String KILLED = "KILLED";
//    public static final String INVOKE_=



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
    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
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

    @Override
    public long getKillTimeoutMs() {
        return killTimeoutMs;
    }

    public void setKillTimeoutMs(long killTimeoutMs) {
        this.killTimeoutMs = killTimeoutMs;
    }

    @Override
    public long getProcTimeoutMs() {
        return procTimeoutMs;
    }

    public void setProcTimeoutMs(long procTimeoutMs) {
        this.procTimeoutMs = procTimeoutMs;
    }

    @Override
    public long getInvokeTimeoutMs() {
        return invokeTimeoutMs;
    }

    public void setInvokeTimeoutMs(long invokeTimeoutMs) {
        this.invokeTimeoutMs = invokeTimeoutMs;
    }
}
