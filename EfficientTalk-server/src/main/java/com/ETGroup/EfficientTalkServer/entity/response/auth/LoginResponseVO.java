package com.ETGroup.EfficientTalkServer.entity.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ETGroup.EfficientTalkServer.entity.DTO.auth.UserLoginDTO;
import lombok.Data;

@Data
public class LoginResponseVO {
    @JsonProperty("permission")
    private Boolean permission;
    
    @JsonProperty("userData")
    private UserLoginDTO userData;
    
    private String systemConfig;
}
