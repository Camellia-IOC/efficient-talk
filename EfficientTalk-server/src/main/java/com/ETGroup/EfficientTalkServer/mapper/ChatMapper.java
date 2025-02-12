package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ChatMapper {
    // 保存聊天记录
    Integer saveChatHistory(ChatRecordDTO record);
    
    // 缓存聊天记录
    Integer cacheChatHistory(ChatRecordDTO record);
    
    // 获取缓存的聊天记录
    ArrayList<ChatRecordDTO> getChatHistoryCache(String userId);
    
    // 删除缓存的聊天记录
    Integer deleteChatHistoryCache(String userId);
}
