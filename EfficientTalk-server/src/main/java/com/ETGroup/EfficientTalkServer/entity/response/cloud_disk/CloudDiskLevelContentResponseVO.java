package com.ETGroup.EfficientTalkServer.entity.response.cloud_disk;

import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFileDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFolderDTO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CloudDiskLevelContentResponseVO {
    private Integer folderCount;
    private ArrayList<CloudDiskFolderDTO> folders;
    private Integer fileCount;
    private ArrayList<CloudDiskFileDTO> files;
}
