package com.ETGroup.EfficientTalkServer.entity.request.user;

import lombok.Data;

@Data
public class SaveUserSystemSettingRequestParam {
    private String userId;
    private String config;
}
