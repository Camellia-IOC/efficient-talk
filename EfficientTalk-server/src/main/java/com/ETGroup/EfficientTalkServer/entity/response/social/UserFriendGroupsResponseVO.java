package com.ETGroup.EfficientTalkServer.entity.response.social;

import com.ETGroup.EfficientTalkServer.entity.PO.FriendGroupPO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class UserFriendGroupsResponseVO {
    ArrayList<FriendGroupPO> friendGroups;
}
