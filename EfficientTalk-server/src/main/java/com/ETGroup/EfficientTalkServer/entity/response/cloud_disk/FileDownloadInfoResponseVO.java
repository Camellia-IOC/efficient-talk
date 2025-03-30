package com.ETGroup.EfficientTalkServer.entity.response.cloud_disk;

import lombok.Data;

@Data
public class FileDownloadInfoResponseVO {
    private String fileId;
    private String fileName;
    private String fileType;
    private String url;
}
