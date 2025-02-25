package com.ETGroup.EfficientTalkServer.entity.response.cloud_disk;

import lombok.Data;

@Data
public class CloudDiskBasicInfoResponseVO {
    private String diskId;
    private String orgId;
    private Long diskSize;
    private Long diskCapacity;
}
