package com.ETGroup.EfficientTalkServer.service.chat;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatListPO;
import com.ETGroup.EfficientTalkServer.entity.request.chat.CreateChatGroupRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.chat.SaveChatListRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.chat.ChatFileListResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
     * 保存群聊聊天记录
     *
     * @param record 聊天记录
     *
     * @return 保存成功条数
     */
    Integer saveGroupChatHistory(ChatRecordDTO record);
    
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
     * 删除聊天记录缓存
     *
     * @param userId 用户ID
     *
     * @return 是否删除成功
     */
    Boolean deleteChatHistoryCache(String userId);
    
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
     * @param pageIndex 页码
     * @param pageSize  每页大小
     * @param lastTime  最早一条记录的时间
     *
     * @return 聊天记录
     */
    ArrayList<ChatRecordDTO> getChatHistory(String userId, String friendId, Integer pageIndex, Integer pageSize, LocalDateTime lastTime);
    
    /**
     * 获取群聊聊天记录
     *
     * @param userId    用户ID
     * @param groupId   群聊ID
     * @param pageIndex 页码
     * @param pageSize  每页大小
     * @param lastTime  最早一条记录的时间
     *
     * @return 聊天记录
     */
    ArrayList<ChatRecordDTO> getGroupChatHistory(String userId, String groupId, Integer pageIndex, Integer pageSize, LocalDateTime lastTime);
    
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
     * @param isGroup  是否为群聊
     *
     * @return 文件路径
     */
    String uploadChatFile(String fileId,
                          String fileName,
                          String fileType,
                          Long fileSize,
                          String sender,
                          String receiver,
                          MultipartFile file,
                          Boolean isGroup);
    
    /**
     * 保存聊天图片
     *
     * @param imageId   图片ID
     * @param imageName 图片名
     * @param imageType 图片类型
     * @param imageSize 图片大小
     * @param sender    发送人
     * @param receiver  接收人
     * @param image     图片
     * @param isGroup   是否为群聊
     *
     * @return 图片路径
     */
    String uploadChatImage(String imageId,
                           String imageName,
                           String imageType,
                           Long imageSize,
                           String sender,
                           String receiver,
                           MultipartFile image,
                           Boolean isGroup);
    
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
    
    /**
     * 分类获取聊天记录
     *
     * @param userId    用户ID
     * @param friendId  好友ID
     * @param pageIndex 页码
     * @param pageSize  每页大小
     * @param type      消息类型
     * @param searchKey 搜索关键词
     * @param lastTime  最早一条记录的时间
     * @param isGroup   是否为群聊
     *
     * @return 聊天记录
     */
    ArrayList<ChatRecordDTO> getChatHistoryByType(String userId,
                                                  String friendId,
                                                  Integer pageIndex,
                                                  Integer pageSize,
                                                  String type,
                                                  String searchKey,
                                                  LocalDateTime lastTime,
                                                  Boolean isGroup);
    
    /**
     * 删除聊天记录
     *
     * @param idList  聊天记录ID
     * @param isGroup 是否为群聊
     *
     * @return 是否删除成功
     */
    boolean deleteChatHistory(ArrayList<String> idList, Boolean isGroup);
    
    /**
     * 创建群聊
     *
     * @param param 创建群聊参数
     *
     * @return 是否创建成功
     */
    Boolean createChatGroup(CreateChatGroupRequestParam param);
}
