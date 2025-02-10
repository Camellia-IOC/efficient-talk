package com.ETGroup.EfficientTalkServer.entity.response.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MessageListResponseVO {
    @JsonProperty("messageList")
    List<MessageResponseVO> messageList;
}
