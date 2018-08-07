package com.scarlatti.ws.client;

import com.scarlatti.ws.client.model.WsRpcDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<String> invoke() throws Exception {

        WebSocketRpcTemplate rpcTemplate = new WebSocketRpcTemplate("ws://localhost:8080/rpc/connect", details);

        rpcTemplate.invoke("/rpc/time");

        return ResponseEntity.ok("sent.");
    }

    @GetMapping("/invokeForString")
    public ResponseEntity<String> invokeForString() throws Exception {

        WebSocketRpcTemplate rpcTemplate = new WebSocketRpcTemplate("ws://localhost:8080/rpc/connect", details);

        String string = rpcTemplate.invokeForString("/rpc/time");

        return ResponseEntity.ok(string);
    }

    @GetMapping("/invokeForBytes")
    public ResponseEntity<String> invokeForBytes() throws Exception {

        WebSocketRpcTemplate rpcTemplate = new WebSocketRpcTemplate("ws://localhost:8080/rpc/connect", details);

        byte[] bytes = rpcTemplate.invokeForBytes("/rpc/time");

        return ResponseEntity.ok(new String(bytes));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(e.getMessage());
    }

    @GetMapping("/nextGen")
    public ResponseEntity<String> nextGenInvokeForBytes() throws Exception {

        WebSocketRpcTemplate rpcTemplate = new WebSocketRpcTemplate("ws://localhost:8081/rpc/connect", null);

        StompSessionHandler sessionHandler = new SimpleWsSessionHandler() {
            @Override
            protected void handleFrameInternal(StompHeaders headers, Object payload) {
                if (headers.containsKey("end"))
                    disconnect();
                else
                    send("/rpc/time", "stop");
            }
        };

        rpcTemplate.converse("/rpc/time", sessionHandler);

        rpcTemplate.invokeForBytes("/rpc/time");

        return ResponseEntity.ok("nothing");
    }

}
