package com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CloudDiskFileDTO {
    private String fileId;
    private String fileName;
    private String folderId;
    private String orgId;
    private String diskId;
    private String fileType;
    private Long fileSize;
    private String creatorId;
    private String creatorName;
    private String creatorAvatar;
    private LocalDateTime updateTime;
}
