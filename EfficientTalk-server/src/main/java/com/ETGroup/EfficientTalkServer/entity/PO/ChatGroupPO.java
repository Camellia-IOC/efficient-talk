package com.ETGroup.EfficientTalkServer.entity.PO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatGroupPO {
    /**
     * 群聊ID
     */
    private String groupId;
    
    /**
     * 群聊名称
     */
    private String groupName;
    
    /**
     * 创建人ID
     */
    private String creator;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}