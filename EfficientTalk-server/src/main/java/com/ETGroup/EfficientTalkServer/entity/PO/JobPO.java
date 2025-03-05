package com.ETGroup.EfficientTalkServer.entity.PO;

import lombok.Data;

@Data
public class JobPO {
    /**
     * 职位ID
     */
    private String jobId;
    
    /**
     * 职位名称
     */
    private String jobName;
    
    /**
     * 组织ID
     */
    private String orgId;
    
    /**
     * 所属部门ID
     */
    private String deptId;
    
    /**
     * 角色ID
     */
    private String roleId;
}