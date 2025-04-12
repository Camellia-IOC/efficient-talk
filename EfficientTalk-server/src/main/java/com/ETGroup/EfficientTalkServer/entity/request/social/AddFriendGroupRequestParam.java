package com.ETGroup.EfficientTalkServer.entity.request.social;

import lombok.Data;

@Data
public class AddFriendGroupRequestParam {
    private String userId;
    private String groupName;
}
