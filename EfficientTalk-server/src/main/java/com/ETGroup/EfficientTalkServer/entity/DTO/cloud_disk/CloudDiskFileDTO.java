package com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
