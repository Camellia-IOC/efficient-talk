package com.pigstory.entity.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestParam {
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("password")
    private String password;
}
