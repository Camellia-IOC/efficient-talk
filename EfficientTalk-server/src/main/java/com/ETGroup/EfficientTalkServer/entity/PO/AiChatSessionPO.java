package com.ETGroup.EfficientTalkServer.entity.PO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AiChatSessionPO {
    /**
     * 会话ID
     */
    private String sessionId;
    
    /**
     * 会话主题
     */
    private String topic;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}