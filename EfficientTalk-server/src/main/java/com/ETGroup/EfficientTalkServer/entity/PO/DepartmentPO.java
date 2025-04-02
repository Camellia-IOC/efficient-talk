package com.ETGroup.EfficientTalkServer.entity.PO;

import lombok.Data;

@Data
public class DepartmentPO {
    /**
     * ID
     */
    private String id;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 所属组织ID
     */
    private String orgId;
    
    /**
     * 父级组织ID（为NULL则为根组织）
     */
    private String parentId;
    
    /**
     * 组织层级
     */
    private Byte level;
    
    /**
     * 所属标签
     */
    private String tag;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
    
    /**
     * 管理员用户ID
     */
    private String admin;
}