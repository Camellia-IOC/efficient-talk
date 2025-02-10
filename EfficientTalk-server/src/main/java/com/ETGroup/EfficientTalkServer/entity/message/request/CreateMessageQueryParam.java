package com.ETGroup.EfficientTalkServer.entity.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateMessageQueryParam {
    @JsonProperty("type")
    private Integer type;
    
    @JsonProperty("origin")
    private String origin;
    
    @JsonProperty("receiver")
    private String receiver;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("content")
    private String content;
}
