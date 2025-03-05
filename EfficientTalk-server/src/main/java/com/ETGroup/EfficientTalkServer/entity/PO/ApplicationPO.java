package com.ETGroup.EfficientTalkServer.entity.PO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApplicationPO {
    /**
     * 唯一标识ID
     */
    private String id;
    
    /**
     * 应用ID
     */
    private String appId;
    
    /**
     * 应用名称
     */
    private String appName;
    
    /**
     * 应用url
     */
    private String appUrl;
    
    /**
     * 图标url
     */
    private String iconUrl;
    
    /**
     * 所属组织ID
     */
    private String orgId;
    
    /**
     * 创建人ID
     */
    private String creator;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 应用类型(SYSTEM/ORG)
     */
    private String type;
}