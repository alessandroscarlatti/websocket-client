package com.scarlatti.ws.client.model;

import java.util.function.Consumer;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
public interface WsRpcDetails {

    /**
     * @return address to connect to.  For example, wss://localhost:8081/app
     */
    String getAddress();

    /**
     * @return the destination for control messages.
     */
    String getControl();

    /**
     * @return the destination for status messages.
     */
    String getStatus();

    /**
     * @return header name for the remote procedure status.
     */
    String getStatusHeader();

    /**
     * @return command name for invoking remote procedure.
     */
    String getInvoke();

    /**
     * @return command name for killing remote procedure.
     */
    String getKill();

    /**
     * @return status indicating remote procedure is executing.
     */
    String getRunning();

    /**
     * @return status indicating remote procedure is complete.
     */
    String getComplete();

    /**
     * @return status indicating remote procedure has encountered an error.
     */
    String getFailed();

    /**
     * @return status indicating remote procedure has been successfully killed.
     */
    String getKilled();  // todo do we need this?

    /**
     * @return how long to wait for remote procedure to initiate.
     */
    long getInvokeTimeoutMs();

    /**
     * @return maximum time to allow remote procedure to execute.
     */
    long getProcTimeoutMs();

    /**
     * @return maximum time to allow remote server to attempt to kill remote procedure.
     */
    long getKillTimeoutMs();

    /**
     * @return what to do with lines logged by remote procedure.
     */
    Consumer<String> getLogger();
}
