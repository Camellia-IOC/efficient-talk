package com.ETGroup.EfficientTalkServer.entity.request.social;

import lombok.Data;

@Data
public class HandleFriendInviteRequestParam {
    // 邀请ID
    private String invitationId;
    // 用户ID
    private String userId;
    // 好友ID
    private String friendId;
    // 我的分组ID
    private String userGroupId;
    // 对方的分组ID
    private String friendGroupId;
    // 处理结果
    private Boolean accept;
}
