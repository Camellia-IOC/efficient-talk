package com.ETGroup.EfficientTalkServer.entity.request.cloud_disk;

import lombok.Data;

@Data
public class RenameFileRequestParam {
    private String fileId;
    private String newFileName;
}
