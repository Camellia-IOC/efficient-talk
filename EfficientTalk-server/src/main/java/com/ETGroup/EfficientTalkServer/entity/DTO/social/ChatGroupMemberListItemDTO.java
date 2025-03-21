package com.ETGroup.EfficientTalkServer.entity.DTO.social;

import lombok.Data;

@Data
public class ChatGroupMemberListItemDTO {
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
