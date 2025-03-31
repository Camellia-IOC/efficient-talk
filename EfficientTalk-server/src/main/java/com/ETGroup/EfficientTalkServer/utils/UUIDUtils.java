package com.ETGroup.EfficientTalkServer.utils;

import java.time.LocalDate;
import java.util.UUID;

// UUID工具类
public class UUIDUtils {
    /**
     * 生成UUID
     *
     * @return UUID
     */
    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    
    /**
     * 生成带时间戳的UUID
     *
     * @return UUID
     */
    public static String generateTimeStampUUID() {
        String uuid = UUID.randomUUID()
                          .toString();
        uuid = LocalDate.now()
                        .toString() + '-' + uuid;
        return uuid;
    }
}
