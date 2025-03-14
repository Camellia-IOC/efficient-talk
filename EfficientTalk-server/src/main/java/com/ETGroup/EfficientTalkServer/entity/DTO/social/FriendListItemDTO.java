package com.ETGroup.EfficientTalkServer.entity.DTO.social;

import lombok.Data;

@Data
public class FriendListItemDTO {
    private String userId;
    private String userName;
    private String userAvatar;
    private String deptId;
    private String deptName;
    private String groupId;
    private String groupName;
    private String onlineState;
}
