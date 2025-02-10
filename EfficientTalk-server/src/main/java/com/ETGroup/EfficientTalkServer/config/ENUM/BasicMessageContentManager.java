package com.pigstory.config.ENUM;

import lombok.Getter;

@Getter
public enum BasicMessageContentManager {
    CONTENT_MANAGER("");
    
    private final String content;
    
    BasicMessageContentManager (String content) {
        this.content = content;
    }
}
