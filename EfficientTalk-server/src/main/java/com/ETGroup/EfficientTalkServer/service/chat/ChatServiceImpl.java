package com.ETGroup.EfficientTalkServer.service.chat;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import com.ETGroup.EfficientTalkServer.mapper.ChatMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private ChatMapper chatMapper;
    
    /**
     * 保存聊天记录
     *
     * @param record 聊天记录
     *
     * @return 保存成功的条数
     */
    @Override
    public Integer saveChatHistory(ChatRecordDTO record) {
        return chatMapper.saveChatHistory(record);
    }
    
    /**
     * 缓存聊天记录
     *
     * @param record 聊天记录
     *
     * @return 缓存成功的条数
     */
    @Override
    public Integer cacheChatHistory(ChatRecordDTO record) {
        return chatMapper.cacheChatHistory(record);
    }
    
    /**
     * 获取缓存的聊天记录
     *
     * @param userId 用户id
     *
     * @return 聊天记录
     */
    @Override
    public ArrayList<ChatRecordDTO> getChatHistoryCache(String userId) {
        ArrayList<ChatRecordDTO> chatHistoryCache = chatMapper.getChatHistoryCache(userId);
        chatMapper.deleteChatHistoryCache(userId);
        return chatHistoryCache;
    }
}
