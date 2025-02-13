package com.ETGroup.EfficientTalkServer.utils;

import java.util.UUID;

// UUID工具类
public class UUIDUtils {
    /**
     * 生成UUID
     * @return UUID
     */
    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
