package com.ETGroup.EfficientTalkServer.entity.response.social;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.FriendInvitationListItemDTO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class FriendInvitationListResponseVO {
    private ArrayList<FriendInvitationListItemDTO> invitationList;
}
