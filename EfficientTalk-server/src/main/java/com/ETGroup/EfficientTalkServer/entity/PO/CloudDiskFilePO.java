package com.ETGroup.EfficientTalkServer.entity.PO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CloudDiskFilePO {
    private String fileId;
    private String fileName;
    private String folderId;
    private String orgId;
    private String diskId;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private String creator;
    private LocalDateTime updateTime;
}
