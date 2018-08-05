package com.scarlatti.ws.client.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scarlatti.ws.client.model.WsRpcControlMessage;
import com.scarlatti.ws.client.model.WsRpcStatusMessage;
import org.springframework.messaging.converter.MessageConversionException;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
public class WsRpcJacksonMessageConverter implements WsRpcMessageConverter {

    private ObjectMapper objectMapper;

    public WsRpcJacksonMessageConverter() {
        objectMapper = new ObjectMapper();
    }

    public WsRpcJacksonMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object convertControlMessageFromString(String message) {
        try {
            return objectMapper.readValue(message, WsRpcControlMessage.class);
        } catch (Exception e) {
            throw new MessageConversionException("Unable to read JSON: " + message, e);
        }
    }

    @Override
    public String convertControlMessageToString(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing message: " + payload, e);
        }
    }

    @Override
    public Object convertStatusMessageFromString(String message) {
        try {
            return objectMapper.readValue(message, WsRpcStatusMessage.class);
        } catch (Exception e) {
            throw new MessageConversionException("Unable to read JSON: " + message, e);
        }
    }

    @Override
    public String convertStatusMessageToString(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing message: " + payload, e);
        }
    }
}
