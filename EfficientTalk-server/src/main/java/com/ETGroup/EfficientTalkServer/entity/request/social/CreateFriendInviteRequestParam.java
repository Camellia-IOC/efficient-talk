package com.ETGroup.EfficientTalkServer.entity.request.social;

import lombok.Data;

@Data
public class CreateFriendInviteRequestParam {
    private String userId;
    private String friendId;
    private String groupId;
    private String message;
}
