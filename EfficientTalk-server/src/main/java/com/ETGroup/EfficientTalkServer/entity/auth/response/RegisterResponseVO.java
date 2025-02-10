package com.pigstory.entity.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterResponseVO {
    @JsonProperty("userId")
    private Long userId;
}
