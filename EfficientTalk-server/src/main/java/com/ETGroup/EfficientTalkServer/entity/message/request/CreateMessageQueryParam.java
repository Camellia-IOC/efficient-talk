package com.pigstory.entity.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateMessageQueryParam {
    @JsonProperty("type")
    private Integer type;
    
    @JsonProperty("origin")
    private Long origin;
    
    @JsonProperty("receiver")
    private Long receiver;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("content")
    private String content;
}
