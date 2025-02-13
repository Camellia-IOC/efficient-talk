package com.ETGroup.EfficientTalkServer.entity.PO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendInvitationPO {
    // 邀请记录ID
    private String id;
    // 邀请人ID
    private String userId;
    // 被邀请人ID
    private String friendId;
    // 申请理由
    private String message;
    // 分组ID
    private String groupId;
    // 邀请时间
    private LocalDateTime inviteTime;
    // 处理时间
    private LocalDateTime handleTime;
    // 邀请状态
    private Integer state;
}
