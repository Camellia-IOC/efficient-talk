package com.ETGroup.EfficientTalkServer.websocket.message;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用于封装浏览器发送给服务端的消息数据
 */
@Data
public class Message {
    // 发送人ID
    private String sender;
    // 接收人ID
    private String receiver;
    // 消息类型
    private String type;
    // 消息内容
    private String content;
    // 消息发送时间
    private LocalDateTime time;
}