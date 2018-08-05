package com.scarlatti.ws.client;

import com.scarlatti.ws.client.model.DefaultWsRpcDetails;
import com.scarlatti.ws.client.model.WsRpcDetails;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Sunday, 8/5/2018
 */
@Configuration
public class Config {

    @Bean
    @ConfigurationProperties("websocket")
    public WsRpcDetails wsRpcDetails() {
        return new DefaultWsRpcDetails();
    }
}
