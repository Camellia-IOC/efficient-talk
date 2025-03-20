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
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/chat/{userId}")
@Component
public class ChatEndpoint {
    // 引入聊天服务
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
        
        // TODO 从消息缓存队列中获取聊天记录，并发送给用户
    }
    
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
    public void onClose(Session session) throws IOException {
        // 从会话存储中移除当前会话
        Iterator<Map.Entry<String, Session>> iterator = sessionStorage.entrySet()
                                                                      .iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Session> entry = iterator.next();
            if (entry.getValue()
                     .equals(session)) {
                // 在删除之前关闭 Session
                Session sessionToClose = entry.getValue();
                if (sessionToClose.isOpen()) {
                    sessionToClose.close();
                }
                iterator.remove();
                break;
            }
        }
        
        log.info("连接关闭:{}", session);
    }
}
