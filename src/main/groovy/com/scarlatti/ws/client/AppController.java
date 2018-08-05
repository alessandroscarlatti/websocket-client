package com.scarlatti.ws.client;

import com.scarlatti.ws.client.model.WsRpcDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutionException;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 8/4/2018
 */
@Controller
@RequestMapping("/api")
public class AppController {

    private WsRpcDetails details;

    public AppController(WsRpcDetails details) {
        this.details = details;
    }

    @GetMapping("/invoke")
    public ResponseEntity<String> send() throws Exception {

        WebSocketRpcTemplate rpcTemplate = new WebSocketRpcTemplate(details);

        rpcTemplate.invoke();

        return ResponseEntity.ok("sent.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
