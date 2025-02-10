package com.ETGroup.EfficientTalkServer.entity.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequestParam {
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;
}
