package com.ETGroup.EfficientTalkServer.entity.DTO.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatFileListItemDTO {
    private String fileId;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String sender;
    private String senderName;
    private LocalDateTime createTime;
}
