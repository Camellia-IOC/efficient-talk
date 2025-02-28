package com.ETGroup.EfficientTalkServer.entity.request.cloud_disk;

import lombok.Data;

@Data
public class CreateCloudDiskFolderRequestParam {
    private String folderName;
    private String parentId;
    private String orgId;
    private String diskId;
    private String creator;
}
