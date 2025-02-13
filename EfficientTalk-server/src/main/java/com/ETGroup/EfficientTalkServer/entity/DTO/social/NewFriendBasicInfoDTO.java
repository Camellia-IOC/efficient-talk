package com.ETGroup.EfficientTalkServer.entity.DTO.social;

import lombok.Data;

@Data
public class NewFriendBasicInfoDTO {
    private String userId;
    private String userName;
    private String userAvatar;
    private String deptId;
    private String deptName;
    private String jobId;
    private String jobName;
    private Integer sex;
    private Boolean isFriend;
}
