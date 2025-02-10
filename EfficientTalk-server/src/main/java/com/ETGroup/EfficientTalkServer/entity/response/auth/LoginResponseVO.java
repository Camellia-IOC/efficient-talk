package com.ETGroup.EfficientTalkServer.entity.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ETGroup.EfficientTalkServer.entity.auth.dto.UserLoginDTO;
import lombok.Data;

@Data
public class LoginResponseVO {
    @JsonProperty("permission")
    private Boolean permission;
    
    @JsonProperty("userData")
    private UserLoginDTO userData;
}
