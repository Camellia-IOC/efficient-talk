package com.ETGroup.EfficientTalkServer.entity.PO;

import lombok.Data;

@Data
public class CloudDiskPO {
    private String diskId;
    private String orgId;
    private Long diskSize;
    private Long diskCapacity;
    private String diskPath;
}
