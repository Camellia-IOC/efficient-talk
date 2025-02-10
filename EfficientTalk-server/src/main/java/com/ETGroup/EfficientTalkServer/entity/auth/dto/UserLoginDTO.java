package com.pigstory.entity.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserLoginDTO {
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("zoneId")
    private Long zoneId;
}
