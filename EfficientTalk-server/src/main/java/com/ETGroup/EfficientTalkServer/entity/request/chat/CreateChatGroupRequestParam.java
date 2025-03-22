package com.ETGroup.EfficientTalkServer.entity.request.chat;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CreateChatGroupRequestParam {
    private String groupName;
    private String creator;
    private String orgId;
    private ArrayList<String> memberList;
}
