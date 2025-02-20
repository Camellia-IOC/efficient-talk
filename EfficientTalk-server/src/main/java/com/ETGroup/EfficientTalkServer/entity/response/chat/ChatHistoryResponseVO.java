package com.ETGroup.EfficientTalkServer.entity.response.chat;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ChatHistoryResponseVO {
    ArrayList<ChatRecordDTO> chatHistory;
}
