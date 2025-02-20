package com.ETGroup.EfficientTalkServer.entity.request.chat;

import lombok.Data;

@Data
public class SaveChatListRequestParam {
    private String userId;
    private String ChatListJson;
}
