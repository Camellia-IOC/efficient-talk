package com.ETGroup.EfficientTalkServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
public class EfficientTalkServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EfficientTalkServerApplication.class, args);
    }
}
