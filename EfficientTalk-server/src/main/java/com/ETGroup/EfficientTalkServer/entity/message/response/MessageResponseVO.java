package com.pigstory.entity.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageResponseVO {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("type")
    private Integer type;
    
    @JsonProperty("origin")
    private Integer origin;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("avatar")
    private String avatar;
    
    @JsonProperty("receiver")
    private Integer receiver;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("content")
    private String content;
    
    @JsonProperty("createTime")
    private String createTime;
    
    @JsonProperty("readFlag")
    private boolean readFlag;
}
