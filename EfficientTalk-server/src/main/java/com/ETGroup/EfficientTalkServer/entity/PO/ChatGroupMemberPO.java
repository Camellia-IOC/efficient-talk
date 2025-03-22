package com.ETGroup.EfficientTalkServer.entity.PO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatGroupMemberPO {
    /**
     * 唯一ID
     */
    private String id;
    
    /**
     * 群聊ID
     */
    private String groupId;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 加入时间
     */
    private LocalDateTime createTime;
}