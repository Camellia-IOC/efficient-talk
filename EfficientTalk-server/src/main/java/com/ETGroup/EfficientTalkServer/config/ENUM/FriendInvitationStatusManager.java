package com.ETGroup.EfficientTalkServer.config.ENUM;

import lombok.Getter;

@Getter
public enum FriendInvitationStatusManager {
    DEFAULT(-1, "待处理"),
    REFUSE(0, "拒绝"),
    AGREE(1, "接受");
    
    private final int status;
    
    private final String description;
    
    FriendInvitationStatusManager (int status, String description) {
        this.status = status;
        this.description = description;
    }
}
