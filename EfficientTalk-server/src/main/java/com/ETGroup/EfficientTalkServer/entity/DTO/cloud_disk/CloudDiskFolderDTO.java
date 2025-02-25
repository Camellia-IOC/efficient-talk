package com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CloudDiskFolderDTO {
    private String id;
    private String folderId;
    private String folderName;
    private String parentId;
    private String orgId;
    private String diskId;
    private String creatorId;
    private String creatorName;
    private String creatorAvatar;
    private LocalDateTime updateTime;
}
