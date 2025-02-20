package com.ETGroup.EfficientTalkServer.websocket;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import com.ETGroup.EfficientTalkServer.service.chat.ChatService;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/chat/{userId}")
@Component
public class ChatEndpoint {
    private ChatService chatService;
    
    private static AutowireCapableBeanFactory beanFactory;
    
    @Resource
    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        ChatEndpoint.beanFactory = beanFactory;
    }
    
    // 保存正在连接的会话，key为用户ID，value为Session对象
    private static final Map<String, Session> sessionStorage = new ConcurrentHashMap<>();
    
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        log.info("连接打开:{}", userId);
        
        // 保存当前用户的会话
        sessionStorage.put(userId, session);
        
        // 获取ChatService实例
        this.chatService = beanFactory.getBean(ChatService.class);
    }
    
    //    private void broadcastAllUsers(String message) {
    //        try {
    //            Set<Map.Entry<String, Session>> entries = sessionStorage.entrySet();
    //
    //            for (Map.Entry<String, Session> entry : entries) {
    //                // 获取到所有用户对应的 session 对象
    //                Session session = entry.getValue();
    //
    //                // 使用 getBasicRemote() 方法发送同步消息
    //                session.getBasicRemote()
    //                       .sendText(message);
    //            }
    //        }
    //        catch (Exception exception) {
    //            log.error(exception.toString());
    //        }
    //    }
    
    @OnMessage
    public void onMessage(String message) {
        log.info("收到消息:{}", message);
        try {
            // 提取消息
            ChatRecordDTO msg = JSON.parseObject(message, ChatRecordDTO.class);
            if (chatService.saveChatHistory(msg) == 1) {
                log.info("保存聊天记录成功");
            }
            // 如果接收者不是发送者，则转发消息
            if (!msg.getReceiver()
                    .equals(msg.getSender())) {
                Session session = sessionStorage.get(msg.getReceiver());
                if (session == null) {
                    if (chatService.cacheChatHistory(msg) == 1) {
                        log.info("缓存聊天记录成功");
                    }
                }
                else {
                    session.getBasicRemote()
                           .sendText(message);
                }
            }
        }
        catch (Exception exception) {
            log.error(exception.toString());
        }
    }
    
    @OnClose
    public void onClose(Session session) {
        // 从会话存储中移除当前会话
        for (Map.Entry<String, Session> entry : sessionStorage.entrySet()) {
            if (entry.getValue()
                     .equals(session)) {
                sessionStorage.remove(entry.getKey());
                break;
            }
        }
        log.info("连接关闭:{}", session);
    }
}
