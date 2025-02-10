package com.ETGroup.EfficientTalkServer.config.ENUM;

import lombok.Getter;

@Getter
public enum BasicMessageTitleManager {
    TITLE_REGISTER_MESSAGE("注册消息"),
    TITLE_FOLLOW_MESSAGE("新的粉丝"),
    TITLE_ADD_FRIEND_MESSAGE("好友申请");
    
    private final String title;
    
    BasicMessageTitleManager (String title) {
        this.title = title;
    }
    }
