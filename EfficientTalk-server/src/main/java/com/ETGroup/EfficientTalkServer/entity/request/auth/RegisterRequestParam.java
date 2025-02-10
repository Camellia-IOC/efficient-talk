package com.ETGroup.EfficientTalkServer.entity.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequestParam {
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;
}
