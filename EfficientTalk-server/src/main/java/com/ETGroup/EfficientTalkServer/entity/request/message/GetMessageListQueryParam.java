package com.ETGroup.EfficientTalkServer.entity.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetMessageListQueryParam {
    @JsonProperty("id")
    private Long id;
}
