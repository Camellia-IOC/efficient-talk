package com.ETGroup.EfficientTalkServer.websocket;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import com.ETGroup.EfficientTalkServer.mapper.SocialMapper;
import com.ETGroup.EfficientTalkServer.service.chat.ChatService;
import com.ETGroup.EfficientTalkServer.utils.RedisUtils;
import com.ETGroup.EfficientTalkServer.utils.UUIDUtils;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/chat/{userId}")
@Component
public class ChatEndpoint {
    // 引入服务
    private static AutowireCapableBeanFactory beanFactory;
    private ChatService chatService;
    private SocialMapper socialMapper;
    private RedisUtils redisUtils;
    
    @Resource
    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        ChatEndpoint.beanFactory = beanFactory;
    }
    
    // 保存正在连接的会话，key为用户ID，value为Session对象
    private static final Map<String, Session> sessionStorage = new ConcurrentHashMap<>();
    
    /**
     * 重置消息信息
     *
     * @param owner   消息所属用户ID
     * @param isCache 是否缓存
     */
    private void resetMessageInfo(String owner, Boolean isCache, ChatRecordDTO msg) {
        msg.setId(UUIDUtils.generateTimeStampUUID());
        msg.setOwner(owner);
        msg.setIsCache(isCache);
    }
    
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        log.info("用户:{} 已上线", userId);
        
        // 保存当前用户的会话
        sessionStorage.put(userId, session);
        
        // 获取服务实例
        this.chatService = beanFactory.getBean(ChatService.class);
        this.socialMapper = beanFactory.getBean(SocialMapper.class);
        this.redisUtils = beanFactory.getBean(RedisUtils.class);
        
        // 从消息缓存队列中获取聊天记录，并发送给用户
        try {
            String key = "chat_history_cache:" + userId;
            
            // 如果缓存命中
            if (redisUtils.getListLength(key) != null) {
                while (true) {
                    String message = (String) redisUtils.listLeftPop(key);
                    if (message != null) {
                        ChatRecordDTO msg = JSON.parseObject(message, ChatRecordDTO.class);
                        session.getBasicRemote()
                               .sendText(JSON.toJSONString(msg));
                        if (msg.getIsGroup()) {
                            chatService.saveGroupChatHistory(msg);
                        }
                        else {
                            chatService.saveChatHistory(msg);
                        }
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                // 获取数据库中的离线消息
                ArrayList<ChatRecordDTO> userMsgList = chatService.getChatHistoryCache(userId);
                if (!userMsgList.isEmpty()) {
                    for (ChatRecordDTO msg : userMsgList) {
                        msg.setOwner(userId);
                        msg.setIsCache(true);
                        session.getBasicRemote()
                               .sendText(JSON.toJSONString(msg));
                        chatService.saveChatHistory(msg);
                    }
                }
                
                ArrayList<ChatRecordDTO> groupMsgList = chatService.getChatGroupHistoryCache(userId);
                if (!groupMsgList.isEmpty()) {
                    for (ChatRecordDTO msg : groupMsgList) {
                        msg.setIsCache(true);
                        session.getBasicRemote()
                               .sendText(JSON.toJSONString(msg));
                        chatService.saveGroupChatHistory(msg);
                    }
                }
            }
            
            // 删除数据库中的缓存记录
            if (chatService.deleteChatHistoryCache(userId)) {
                log.info("清理聊天记录缓存成功");
            }
            else {
                log.error("清理聊天记录缓存失败");
            }
        }
        catch (IOException e) {
            log.error("获取缓存消息失败", e);
        }
    }
    
    @OnMessage
    public void onMessage(String message) {
        log.info("收到消息:{}", message);
        try {
            // 提取消息
            ChatRecordDTO msg = JSON.parseObject(message, ChatRecordDTO.class);
            
            // 如果是群聊消息
            if (msg.getIsGroup()) {
                msg.setOwner(msg.getSender());
                if (chatService.saveGroupChatHistory(msg) == 1) {
                    log.info("保存群聊聊天记录成功");
                }
                
                ArrayList<String> groupMemberIdList = socialMapper.getChatGroupMemberIdList(msg.getReceiver());
                for (String memberId : groupMemberIdList) {
                    // 如果当前成员是发送者，则跳过
                    if (memberId.equals(msg.getSender())) {
                        continue;
                    }
                    
                    Session session = sessionStorage.get(memberId);
                    if (session == null) {
                        // 设置消息信息
                        resetMessageInfo(memberId, true, msg);
                        String key = "chat_history_cache:" + memberId;
                        redisUtils.listRightPush(key, JSON.toJSONString(msg));
                        redisUtils.setKeyTimeout(key, 60 * 60 * 24 * 7);
                        chatService.cacheChatGroupHistory(msg);
                        log.info("缓存群聊聊天记录成功");
                    }
                    else {
                        // 设置消息信息
                        resetMessageInfo(memberId, false, msg);
                        session.getBasicRemote()
                               .sendText(JSON.toJSONString(msg));
                        chatService.saveGroupChatHistory(msg);
                    }
                }
            }
            else {
                msg.setOwner(msg.getSender());
                if (chatService.saveChatHistory(msg) == 1) {
                    log.info("保存聊天记录成功");
                }
                
                // 如果接收者不是发送者，则转发消息
                if (!msg.getReceiver()
                        .equals(msg.getSender())) {
                    Session session = sessionStorage.get(msg.getReceiver());
                    if (session == null) {
                        // 设置消息信息
                        resetMessageInfo(msg.getReceiver(), true, msg);
                        String key = "chat_history_cache:" + msg.getReceiver();
                        redisUtils.listRightPush(key, JSON.toJSONString(msg));
                        redisUtils.setKeyTimeout(key, 60 * 60 * 24 * 7);
                        chatService.cacheChatHistory(msg);
                    }
                    else {
                        resetMessageInfo(msg.getReceiver(), false, msg);
                        session.getBasicRemote()
                               .sendText(JSON.toJSONString(msg));
                        chatService.saveChatHistory(msg);
                    }
                }
            }
        }
        catch (Exception e) {
            log.error("消息处理失败", e);
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
    
    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }
}
