package com.ETGroup.EfficientTalkServer.entity.response.cloud_disk;

import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFileDTO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CloudDiskFileListResponseVO {
    Integer total;
    ArrayList<CloudDiskFileDTO> fileList;
}
