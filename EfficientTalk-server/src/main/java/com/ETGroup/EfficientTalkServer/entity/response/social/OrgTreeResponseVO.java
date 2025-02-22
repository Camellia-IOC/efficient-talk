package com.ETGroup.EfficientTalkServer.entity.response.social;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.OrgTreeDeptNodeDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.social.OrgTreeUserNodeDTO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class OrgTreeResponseVO {
    private ArrayList<OrgTreeDeptNodeDTO> deptList;
    private ArrayList<OrgTreeUserNodeDTO> userList;
}
