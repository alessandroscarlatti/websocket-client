package com.scarlatti.ws.client.model;

import java.util.function.Consumer;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 * <p>
 * Class to configure connection details,
 * as well as rpc event notifications.
 */
public class DefaultWsRpcDetails implements WsRpcDetails {
    private String address;
    private String control = CONTROL;
    private String status = STATUS;
    private String statusHeader = STATUS_HEADER;
    private String invoke = INVOKE;
    private String commandHeader = COMMAND_HEADER;
    private String kill = KILL;
    private String running = RUNNING;
    private String complete = COMPLETE;
    private String failed = FAILED;
    private String killed = KILLED;
    private long invokeTimeoutMs = INVOKE_TIMEOUT_MS;
    private long procTimeoutMs = PROC_TIMEOUT_MS;
    private long killTimeoutMs = KILL_TIMEOUT_MS;
    private Consumer<String> logger = noOpLogger();

    public static final String CONTROL = "/";
    public static final String STATUS = "/status";
    public static final String STATUS_HEADER = "Status";
    public static final String INVOKE = "INVOKE";
    public static final String COMMAND_HEADER = "Command";
    public static final String KILL = "KILL";
    public static final String RUNNING = "RUNNING";
    public static final String COMPLETE = "COMPLETE";
    public static final String FAILED = "FAILED";
    public static final String KILLED = "KILLED";
    public static final long INVOKE_TIMEOUT_MS = 5000;
    public static final long PROC_TIMEOUT_MS = 60000;
    public static final long KILL_TIMEOUT_MS = 5000;

    private static Consumer<String> noOpLogger() {
        return (s) -> {
        };
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
    public String getFailed() {
        return failed;
    }

    public void setFailed(String failed) {
        this.failed = failed;
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
    public String getStatusHeader() {
        return statusHeader;
    }

    public void setStatusHeader(String statusHeader) {
        this.statusHeader = statusHeader;
    }

    @Override
    public String getCommandHeader() {
        return commandHeader;
    }

    public void setCommandHeader(String commandHeader) {
        this.commandHeader = commandHeader;
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
