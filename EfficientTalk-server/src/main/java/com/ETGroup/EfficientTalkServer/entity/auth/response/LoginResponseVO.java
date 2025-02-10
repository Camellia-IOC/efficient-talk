package com.pigstory.entity.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pigstory.entity.auth.dto.UserLoginDTO;
import lombok.Data;

@Data
public class LoginResponseVO {
    @JsonProperty("permission")
    private Boolean permission;
    
    @JsonProperty("userData")
    private UserLoginDTO userData;
}
