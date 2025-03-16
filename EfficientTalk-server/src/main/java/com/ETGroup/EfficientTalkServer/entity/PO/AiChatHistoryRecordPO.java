package com.ETGroup.EfficientTalkServer.entity.PO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AiChatHistoryRecordPO {
    /**
     * 消息ID
     */
    private String id;
    
    /**
     * 所属会话ID
     */
    private String session;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 消息角色（system、user、assistant）
     */
    private String role;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 发送时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}