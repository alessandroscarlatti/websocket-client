package com.scarlatti.ws.client.converter;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Sunday, 8/5/2018
 */
public interface WsRpcMessageConverter {
    Object convertControlMessageFromString(String message);
    String convertControlMessageToString(Object payload);
    Object convertStatusMessageFromString(String message);
    String convertStatusMessageToString(Object payload);
}
