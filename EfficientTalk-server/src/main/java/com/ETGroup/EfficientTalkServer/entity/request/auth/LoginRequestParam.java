package com.ETGroup.EfficientTalkServer.entity.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestParam {
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("password")
    private String password;
}
