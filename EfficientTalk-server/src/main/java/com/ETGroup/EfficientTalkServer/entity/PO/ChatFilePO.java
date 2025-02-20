package com.ETGroup.EfficientTalkServer.entity.PO;

import lombok.Data;

@Data
public class ChatFilePO {
    private String fileId;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String sender;
    private String receiver;
    private String createTime;
}
