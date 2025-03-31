package com.ETGroup.EfficientTalkServer.entity.DTO.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRecordDTO {
    // 聊天记录ID
    private String id;
    // 发送者ID
    private String sender;
    // 发送者头像(群聊用)
    private String senderAvatar = null;
    // 发送者名称(群聊用)
    private String senderName = null;
    // 接收者ID
    private String receiver;
    // 消息类型
    private String type;
    // 图片名
    private String imageName;
    // 图片类型
    private String imageType;
    // 图片大小
    private Long imageSize;
    // 文件ID
    private String fileId;
    // 文件名
    private String fileName;
    // 文件类型
    private String fileType;
    // 文件大小
    private Long fileSize;
    // 消息内容
    private String content;
    // 发送时间
    private LocalDateTime time;
    // 是否为群聊消息
    private Boolean isGroup;
    // 是否选中(前端用)
    private Boolean isSelected = false;
    // 是否为缓存消息(前端用)
    private Boolean isCache = false;
}
