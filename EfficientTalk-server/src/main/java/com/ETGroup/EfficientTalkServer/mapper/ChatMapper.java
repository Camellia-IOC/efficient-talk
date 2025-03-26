package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatFileListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatFilePO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatImagePO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatListPO;
import com.ETGroup.EfficientTalkServer.entity.request.chat.SaveChatListRequestParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface ChatMapper {
    // 保存聊天记录
    Integer saveChatHistory(ChatRecordDTO record);
    
    /**
     * 保存群聊聊天记录
     *
     * @param record 聊天记录
     *
     * @return 保存成功条数
     */
    Integer saveGroupChatHistory(ChatRecordDTO record);
    
    // 缓存聊天记录
    Integer cacheChatHistory(ChatRecordDTO record);
    
    // 获取缓存的聊天记录
    ArrayList<ChatRecordDTO> getChatHistoryCache(@Param("userId") String userId);
    
    // 删除缓存的聊天记录
    Integer deleteChatHistoryCache(@Param("userId") String userId);
    
    // 获取聊天记录
    ArrayList<ChatRecordDTO> getChatHistory(@Param("userId") String userId,
                                            @Param("friendId") String friendId,
                                            @Param("pageIndex") Integer pageIndex,
                                            @Param("pageSize") Integer pageSize,
                                            @Param("lastTime") LocalDateTime lastTime);
    
    // 获取群聊聊天记录
    ArrayList<ChatRecordDTO> getGroupChatHistory(@Param("userId") String userId,
                                                 @Param("groupId") String groupId,
                                                 @Param("pageIndex") Integer pageIndex,
                                                 @Param("pageSize") Integer pageSize,
                                                 @Param("lastTime") LocalDateTime lastTime);
    
    // 保存对话列表
    Integer saveChatList(SaveChatListRequestParam param);
    
    // 创建对话列表
    Integer createChatList(ChatListPO chatList);
    
    // 获取对话列表
    ChatListPO getChatList(String userId);
    
    // 保存聊天文件
    Integer uploadChatFile(ChatFilePO chatFile);
    
    /**
     * 上传群聊聊天文件
     *
     * @param chatFile 聊天文件
     *
     * @return 上传成功数量
     */
    Integer uploadGroupChatFile(ChatFilePO chatFile);
    
    // 保存聊天图片
    Integer uploadChatImage(ChatImagePO chatImage);
    
    /**
     * 上传群聊聊天图片
     *
     * @param chatImage 聊天图片
     *
     * @return 上传成功数量
     */
    Integer uploadGroupChatImage(ChatImagePO chatImage);
    
    // 获取聊天文件列表
    ArrayList<ChatFileListItemDTO> getChatFileList(@Param("userId") String userId,
                                                   @Param("pageIndex") Integer pageIndex,
                                                   @Param("pageSize") Integer pageSize);
    
    // 获取聊天文件总数
    Integer getChatFileTotal(@Param("userId") String userId);
    
    /**
     * 分类获取聊天记录
     *
     * @param userId    用户ID
     * @param friendId  好友ID
     * @param pageIndex 页码
     * @param pageSize  页面大小
     * @param type      消息类型
     * @param searchKey 搜索关键字
     * @param lastTime  最后时间
     *
     * @return 聊天记录
     */
    ArrayList<ChatRecordDTO> getChatHistoryByType(@Param("userId") String userId,
                                                  @Param("friendId") String friendId,
                                                  @Param("pageIndex") Integer pageIndex,
                                                  @Param("pageSize") Integer pageSize,
                                                  @Param("type") String type,
                                                  @Param("searchKey") String searchKey,
                                                  @Param("lastTime") LocalDateTime lastTime);
    
    /**
     * 分类获取群聊聊天记录
     *
     * @param groupId   用户ID
     * @param pageIndex 页码
     * @param pageSize  页面大小
     * @param type      消息类型
     * @param searchKey 搜索关键字
     * @param lastTime  最后时间
     *
     * @return 聊天记录
     */
    ArrayList<ChatRecordDTO> getGroupChatHistoryByType(@Param("groupId") String groupId,
                                                       @Param("pageIndex") Integer pageIndex,
                                                       @Param("pageSize") Integer pageSize,
                                                       @Param("type") String type,
                                                       @Param("searchKey") String searchKey,
                                                       @Param("lastTime") LocalDateTime lastTime);
    
    /**
     * 获取聊天文件路径
     *
     * @param fileId 文件id
     *
     * @return 文件路径
     */
    String getChatFilePath(@Param("fileId") String fileId);
    
    /**
     * 获取群聊聊天文件路径
     *
     * @param fileId 文件id
     *
     * @return 文件路径
     */
    String getGroupChatFilePath(@Param("fileId") String fileId);
    
    /**
     * 获取聊天媒体文件路径
     *
     * @param fileId 文件id
     *
     * @return 文件路径
     */
    String getChatMediaFilePath(@Param("fileId") String fileId);
    
    /**
     * 获取群聊聊天媒体文件路径
     *
     * @param fileId 文件id
     *
     * @return 文件路径
     */
    String getGroupChatMediaFilePath(@Param("fileId") String fileId);
    
    /**
     * 删除聊天记录
     *
     * @param messageId 消息id
     *
     * @return 删除条数
     */
    int deleteChatHistory(@Param("messageId") String messageId);
    
    /**
     * 获取聊天图片信息
     *
     * @param imageId 图片id
     *
     * @return 图片信息
     */
    Map<String, String> getChatImageInfo(@Param("imageId") String imageId);
    
    /**
     * 获取群聊聊天图片信息
     * @param imageId 图片id
     * @return 图片信息
     */
    Map<String, String> getGroupChatImageInfo(@Param("imageId") String imageId);
    
    /**
     * 获取聊天文件信息
     * @param fileId 文件id
     * @return 文件信息
     */
    Map<String, String> getChatFileInfo(@Param("fileId") String fileId);
    
    /**
     * 获取群聊聊天文件信息
     * @param fileId 文件id
     * @return 文件信息
     */
    Map<String, String> getGroupChatFileInfo(@Param("fileId") String fileId);
}
