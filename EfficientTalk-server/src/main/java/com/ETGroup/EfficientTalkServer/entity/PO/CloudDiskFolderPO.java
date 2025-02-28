package com.ETGroup.EfficientTalkServer.entity.PO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CloudDiskFolderPO {
    private String folderId;
    private String folderName;
    private String parentId;
    private String orgId;
    private String diskId;
    private String creator;
    private LocalDateTime updateTime;
    private String folderPath;
}
