package com.ETGroup.EfficientTalkServer.service.chat;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface ChatService {
    /**
     * 保存聊天记录
     *
     * @param record 聊天记录
     *
     * @return 保存成功的条数
     */
    Integer saveChatHistory(ChatRecordDTO record);
    
    /**
     * 缓存聊天记录
     * @param record 聊天记录
     * @return 缓存成功的条数
     */
    Integer cacheChatHistory(ChatRecordDTO record);
    
    /**
     * 获取缓存的聊天记录
     *
     * @param userId 用户id
     *
     * @return 聊天记录
     */
    ArrayList<ChatRecordDTO> getChatHistoryCache(String userId);
}
