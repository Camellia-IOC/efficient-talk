package com.ETGroup.EfficientTalkServer.entity.response.social;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.FriendGroupListDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.social.FriendListItemDTO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class FriendListResponseVO {
    // 好友总数
    private Integer total;
    // 好友列表
    ArrayList<FriendGroupListDTO> groupList;
}
