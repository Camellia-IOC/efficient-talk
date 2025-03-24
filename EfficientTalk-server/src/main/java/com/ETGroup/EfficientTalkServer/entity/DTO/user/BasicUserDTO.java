package com.ETGroup.EfficientTalkServer.entity.DTO.user;

import lombok.Data;

@Data
public class BasicUserDTO {
    private String userId;
    private String userName;
    private String userAvatar;
    private String orgId;
    private String orgName;
    private String deptId;
    private String deptName;
    private String jobId;
    private String jobName;
}
