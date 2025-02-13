package com.ETGroup.EfficientTalkServer.entity.response.social;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.NewFriendBasicInfoDTO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class NewFriendsResponseVO {
    ArrayList<NewFriendBasicInfoDTO> resultList;
}
