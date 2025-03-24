package com.ETGroup.EfficientTalkServer.entity.PO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class OrgNoticePO {
    /**
     * 唯一ID
     */
    private String id;
    
    /**
     * 通知标题
     */
    private String title;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 组织ID
     */
    private String orgId;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 发布人ID
     */
    private String publisher;
    
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
}