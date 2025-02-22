package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatFileListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatFilePO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatListPO;
import com.ETGroup.EfficientTalkServer.entity.request.chat.SaveChatListRequestParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface ChatMapper {
    // 保存聊天记录
    Integer saveChatHistory(ChatRecordDTO record);
    
    // 缓存聊天记录
    Integer cacheChatHistory(ChatRecordDTO record);
    
    // 获取缓存的聊天记录
    ArrayList<ChatRecordDTO> getChatHistoryCache(@Param("userId") String userId);
    
    // 删除缓存的聊天记录
    Integer deleteChatHistoryCache(@Param("userId") String userId);
    
    // 获取聊天记录
    ArrayList<ChatRecordDTO> getChatHistory(@Param("userId") String userId,
                                            @Param("friendId") String friendId,
                                            @Param("pageSize") Integer pageSize,
                                            @Param("pageIndex") Integer pageIndex);
    
    // 保存对话列表
    Integer saveChatList(SaveChatListRequestParam param);
    
    // 获取对话列表
    ChatListPO getChatList(String userId);
    
    // 保存聊天文件
    Integer uploadChatFile(ChatFilePO chatFile);
    
    // 获取聊天文件列表
    ArrayList<ChatFileListItemDTO> getChatFileList(@Param("userId") String userId,
                                                   @Param("pageIndex") Integer pageIndex,
                                                   @Param("pageSize") Integer pageSize);
    
    // 获取聊天文件总数
    Integer getChatFileTotal(@Param("userId") String userId);
}
