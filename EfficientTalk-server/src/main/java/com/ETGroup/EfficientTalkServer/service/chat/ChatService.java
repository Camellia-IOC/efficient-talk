package com.ETGroup.EfficientTalkServer.service.chat;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatFileListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatListPO;
import com.ETGroup.EfficientTalkServer.entity.request.chat.SaveChatListRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.chat.ChatFileListResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
     *
     * @param record 聊天记录
     *
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
    
    /**
     * 保存对话列表
     *
     * @param param 保存参数
     *
     * @return 是否保存成功
     */
    Boolean saveChatList(SaveChatListRequestParam param);
    
    /**
     * 获取对话列表
     *
     * @param userId 用户ID
     *
     * @return 对话列表
     */
    ChatListPO getChatList(String userId);
    
    /**
     * 获取聊天记录
     *
     * @param userId    用户ID
     * @param friendId  好友ID
     * @param pageSize  每页大小
     * @param pageIndex 页码
     *
     * @return 聊天记录
     */
    ArrayList<ChatRecordDTO> getChatHistory(String userId, String friendId, Integer pageSize, Integer pageIndex);
    
    /**
     * 保存聊天文件
     *
     * @param fileId   文件ID
     * @param fileName 文件名
     * @param fileType 文件类型
     * @param fileSize 文件大小
     * @param sender   发送人
     * @param receiver 接收人
     * @param file     文件
     *
     * @return 文件路径
     */
    String uploadChatFile(String fileId, String fileName, String fileType, Long fileSize, String sender, String receiver, MultipartFile file) throws IOException;

    /**
     * 获取聊天文件列表
     *
     * @param userId    用户ID
     * @param pageIndex 页码
     * @param pageSize  每页大小
     *
     * @return 聊天文件列表
     */
    ChatFileListResponseVO getChatFileList(String userId, Integer pageIndex, Integer pageSize);
}
