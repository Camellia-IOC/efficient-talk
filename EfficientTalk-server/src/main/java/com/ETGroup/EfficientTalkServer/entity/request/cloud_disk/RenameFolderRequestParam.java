package com.ETGroup.EfficientTalkServer.entity.request.cloud_disk;

import lombok.Data;

@Data
public class RenameFolderRequestParam {
    private String folderId;
    private String newFolderName;
}
