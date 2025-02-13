package com.ETGroup.EfficientTalkServer.entity.DTO.social;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddFriendRecordDTO {
    private String userRecordId;
    private String friendRecordId;
    private String userId;
    private String friendId;
    private String userGroupId;
    private String friendGroupId;
    private LocalDateTime createTime;
}
