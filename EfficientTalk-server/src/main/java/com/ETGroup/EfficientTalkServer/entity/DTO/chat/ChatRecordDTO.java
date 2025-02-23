package com.ETGroup.EfficientTalkServer.entity.DTO.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRecordDTO {
    // 聊天记录ID
    private String id;
    // 发送者ID
    private String sender;
    // 接收者ID
    private String receiver;
    // 消息类型
    private String type;
    // 图片名
    private String imageName;
    // 图片类型
    private String imageType;
    // 图片大小
    private String imageSize;
    // 文件ID
    private String fileId;
    // 文件名
    private String fileName;
    // 文件类型
    private String fileType;
    // 文件大小
    private String fileSize;
    // 消息内容
    private String content;
    // 发送时间
    private LocalDateTime time;
}
