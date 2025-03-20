package com.ETGroup.EfficientTalkServer.entity.DTO.social;

import com.ETGroup.EfficientTalkServer.entity.response.social.OrgTreeResponseVO;
import lombok.Data;

@Data
public class OrgTreeDeptNodeDTO {
    private String id;
    private String deptId;
    private String deptName;
    private String parentId;
    private Integer level;
    private String tag;
    private OrgTreeResponseVO children = null;
}
