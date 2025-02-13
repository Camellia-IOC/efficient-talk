package com.ETGroup.EfficientTalkServer.entity.DTO.social;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendInvitationListItemDTO {
    private String invitationId;
    private String senderId;
    private String receiverId;
    private String friendName;
    private String friendAvatar;
    private String message;
    private String groupId;
    private LocalDateTime inviteTime;
    private LocalDateTime handleTime;
    private Integer state;
}
