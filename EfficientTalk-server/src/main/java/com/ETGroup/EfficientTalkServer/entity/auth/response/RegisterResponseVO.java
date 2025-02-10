package com.ETGroup.EfficientTalkServer.entity.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterResponseVO {
    @JsonProperty("userId")
    private String userId;
}
