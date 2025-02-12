package com.ETGroup.EfficientTalkServer;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
public class EfficientTalkServerApplication {
    @Resource
    private ApplicationContext applicationContext;
    
    @Bean
    public AutowireCapableBeanFactory autowireCapableBeanFactory() {
        return applicationContext.getAutowireCapableBeanFactory();
    }
    
    public static void main(String[] args) {
        SpringApplication.run(EfficientTalkServerApplication.class, args);
    }
}
