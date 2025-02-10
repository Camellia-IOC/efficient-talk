package com.pigstory.config.ENUM;

import lombok.Getter;

@Getter
public enum PathManager {
    /**
     * 头像存储根目录路径
     */
    AVATAR_ROOT_PATH("/"),
    
    /**
     * 图片资源存储根目录路径
     */
    RESOURCE_ROOT_PATH("/resources/");
    
    private final String path;
    
    PathManager (String path) {
        this.path = path;
    }
}
