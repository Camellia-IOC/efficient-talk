package com.ETGroup.EfficientTalkServer.config.ENUM;

import lombok.Getter;

@Getter
public enum ZoneStatusManager {
    CLOSE(0, "关闭"),
    ACTIVE(1, "正常"),
    BANNED(2, "封禁");
    
    private final int status;
    
    private final String description;
    
    ZoneStatusManager (int status, String description) {
        this.status = status;
        this.description = description;
    }
}
