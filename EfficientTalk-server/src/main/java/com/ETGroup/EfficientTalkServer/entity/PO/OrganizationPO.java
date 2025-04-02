package com.ETGroup.EfficientTalkServer.entity.PO;

import lombok.Data;

@Data
public class OrganizationPO {
    private String orgId;
    private String orgName;
    private String orgLogo;
    private String diskId;
    private String createTime;
    private String owner;
}
