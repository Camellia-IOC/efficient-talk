package com.ETGroup.EfficientTalkServer.websocket;

import com.ETGroup.EfficientTalkServer.utils.ChatMessageUtils;
import com.ETGroup.EfficientTalkServer.websocket.message.Message;
import com.alibaba.fastjson2.JSON;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/chat/{userId}")
@Component
public class ChatEndpoint {
    // 保存正在连接的会话，key为用户ID，value为Session对象
    private static final Map<String, Session> sessionStorage = new ConcurrentHashMap<>();
    
    /**
     * 建立websocket连接后，被调用
     *
     * @param session Session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) throws IOException {
        //        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        
        //        String user = (String) this.httpSession.getAttribute("currentUser");
        //        if (user != null) {
        //            onlineUsers.put(user, session);
        //        }
        log.error("连接打开");
        System.out.println(userId);
        
        sessionStorage.put(userId, session);
        
//        session.getBasicRemote()
//               .sendText("连接成功");
        
        // 通知所有用户，当前用户上线了
        //        String message = ChatMessageUtils.getMessage(true, null, "");
        //        broadcastAllUsers(message);
    }
    
    private void broadcastAllUsers(String message) {
        try {
            Set<Map.Entry<String, Session>> entries = sessionStorage.entrySet();
            
            for (Map.Entry<String, Session> entry : entries) {
                // 获取到所有用户对应的 session 对象
                Session session = entry.getValue();
                
                // 使用 getBasicRemote() 方法发送同步消息
                session.getBasicRemote()
                       .sendText(message);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /**
     * 浏览器发送消息到服务端时该方法会被调用，也就是私聊
     * 张三  -->  李四
     *
     * @param message String
     */
    @OnMessage
    public void onMessage(String message) {
        log.error("收到消息:{}", message);
        try {
            // 提取消息
            Message msg = JSON.parseObject(message, Message.class);
            // 如果接收者不是发送者，则转发消息
            if (!msg.getReceiver()
                    .equals(msg.getSender())) {
                Session session = sessionStorage.get(msg.getReceiver());
                session.getBasicRemote()
                       .sendText(message);
            }
            // 将消息推送给指定的用户
            //            Message msg = JSON.parseObject(message, Message.class);
            
            // 获取消息接收方的用户名
            //            String toName = msg.getToName();
            //            String tempMessage = msg.getMessage();
            
            // 获取消息接收方用户对象的 session 对象
            //            Session session = onlineUsers.get(toName);
            //            String currentUser = (String) this.httpSession.getAttribute("currentUser");
            //            String messageToSend = ChatMessageUtils.getMessage(false, currentUser, tempMessage);
            
            //            session.getBasicRemote()
            //                   .sendText(messageToSend);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /**
     * 断开 websocket 连接时被调用
     *
     * @param session Session
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        log.error("连接关闭");
        // 1.从 onlineUsers 中删除当前用户的 session 对象，表示当前用户已下线
        //        String user = (String) this.httpSession.getAttribute("currentUser");
        //        if (user != null) {
        //            Session remove = onlineUsers.remove(user);
        //            if (remove != null) {
        //                remove.close();
        //            }
        //
        //            session.close();
        //        }
        
        // 2.通知其他用户，当前用户已下线
        // 注意：不是发送类似于 xxx 已下线的消息，而是向在线用户重新发送一次当前在线的所有用户
        String message = ChatMessageUtils.getMessage(true, null, "");
        broadcastAllUsers(message);
    }
}
