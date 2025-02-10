package com.ETGroup.EfficientTalkServer.entity.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserLoginDTO {
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("userName")
    private String userName;
    
    @JsonProperty("avatar")
    private String avatar;
}
