package com.ETGroup.EfficientTalkServer.entity.response.social;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.FriendListItemDTO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class FriendListResponseVO {
    // 好友列表
    ArrayList<FriendListItemDTO> friendList;
}
