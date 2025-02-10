package com.ETGroup.EfficientTalkServer.entity.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MessageListResponseVO {
    @JsonProperty("messageList")
    List<MessageResponseVO> messageList;
}
