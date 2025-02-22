package com.ETGroup.EfficientTalkServer.entity.response.chat;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatFileListItemDTO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ChatFileListResponseVO {
    private Integer total;
    private ArrayList<ChatFileListItemDTO> chatFileList;
}
