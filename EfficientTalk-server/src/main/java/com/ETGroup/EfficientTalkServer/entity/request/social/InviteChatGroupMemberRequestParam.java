package com.ETGroup.EfficientTalkServer.entity.request.social;

import lombok.Data;

import java.util.ArrayList;

@Data
public class InviteChatGroupMemberRequestParam {
    private String groupId;
    private ArrayList<String> idList;
}
