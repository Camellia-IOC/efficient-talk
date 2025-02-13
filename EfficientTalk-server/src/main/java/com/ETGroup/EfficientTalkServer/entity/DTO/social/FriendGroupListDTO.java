package com.ETGroup.EfficientTalkServer.entity.DTO.social;

import lombok.Data;

import java.util.ArrayList;

@Data
public class FriendGroupListDTO {
    private String groupId;
    private String groupName;
    ArrayList<FriendListItemDTO> friendList;
    
    public FriendGroupListDTO() {}
    
    public FriendGroupListDTO(String groupId, String groupName, ArrayList<FriendListItemDTO> friendList) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.friendList = friendList;
    }
}
