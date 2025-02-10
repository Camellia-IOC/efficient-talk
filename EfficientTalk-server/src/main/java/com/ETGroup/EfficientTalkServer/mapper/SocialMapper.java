package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.FriendListItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface SocialMapper {
    // 获取好友列表
    ArrayList<FriendListItemDTO> getFriendList (String userId);
}
