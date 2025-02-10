package com.ETGroup.EfficientTalkServer.entity.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestParam {
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("password")
    private String password;
}
