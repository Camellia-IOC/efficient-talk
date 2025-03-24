package com.ETGroup.EfficientTalkServer.entity.PO;

import lombok.Data;

@Data
public class OrgNoticeReadHistoryPO {
    /**
     * 唯一ID
     */
    private String id;
    
    /**
     * 通知ID
     */
    private String noticeId;
    
    /**
     * 用户ID
     */
    private String userId;
}