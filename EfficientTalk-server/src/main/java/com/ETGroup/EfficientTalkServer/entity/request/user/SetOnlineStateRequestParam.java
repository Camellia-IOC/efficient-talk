package com.ETGroup.EfficientTalkServer.entity.request.user;

import lombok.Data;

@Data
public class SetOnlineStateRequestParam {
    private String userId;
    private String onlineState;
}
