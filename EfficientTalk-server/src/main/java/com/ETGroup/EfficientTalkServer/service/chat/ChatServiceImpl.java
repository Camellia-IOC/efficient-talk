package com.ETGroup.EfficientTalkServer.service.chat;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatFileListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.*;
import com.ETGroup.EfficientTalkServer.entity.request.chat.CreateChatGroupRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.chat.SaveChatListRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.chat.ChatFileListResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.ChatMapper;
import com.ETGroup.EfficientTalkServer.mapper.SocialMapper;
import com.ETGroup.EfficientTalkServer.utils.OSSUtils;
import com.ETGroup.EfficientTalkServer.utils.RedisUtils;
import com.ETGroup.EfficientTalkServer.utils.UUIDUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private ChatMapper chatMapper;
    
    @Resource
    private SocialMapper socialMapper;
    
    @Resource
    private OSSUtils ossUtils;
    
    @Resource
    private RedisUtils redisUtils;
    
    /**
     * 保存聊天记录
     *
     * @param record 聊天记录
     */
    @Override
    public void saveChatHistory(ChatRecordDTO record) {
        try {
            chatMapper.saveChatHistory(record);
        }
        catch (Exception e) {
            log.error("保存聊天记录失败", e);
            throw e;
        }
    }
    
    /**
     * 缓存聊天记录
     *
     * @param record 聊天记录
     */
    @Override
    public void cacheChatHistory(ChatRecordDTO record) {
        try {
            chatMapper.cacheChatHistory(record);
        }
        catch (Exception e) {
            log.error("缓存聊天记录失败", e);
            throw e;
        }
    }
    
    /**
     * 缓存群聊聊天记录
     *
     * @param record 聊天记录
     */
    @Override
    public void cacheChatGroupHistory(ChatRecordDTO record) {
        try {
            chatMapper.cacheChatGroupHistory(record);
        }
        catch (Exception e) {
            log.error("缓存群聊聊天记录失败", e);
            throw e;
        }
    }
    
    /**
     * 获取缓存的聊天记录
     *
     * @param userId 用户id
     *
     * @return 聊天记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArrayList<ChatRecordDTO> getChatHistoryCache(String userId) {
        ArrayList<ChatRecordDTO> chatHistoryCache = chatMapper.getChatHistoryCache(userId);
        for (ChatRecordDTO record : chatHistoryCache) {
            record.setIsCache(true);
        }
        chatMapper.deleteChatHistoryCache(userId);
        return chatHistoryCache;
    }
    
    /**
     * 获取缓存的群聊聊天记录
     *
     * @param userId 用户id
     *
     * @return 聊天记录
     */
    @Override
    public ArrayList<ChatRecordDTO> getChatGroupHistoryCache(String userId) {
        ArrayList<ChatRecordDTO> chatHistoryCache = chatMapper.getChatGroupHistoryCache(userId);
        for (ChatRecordDTO record : chatHistoryCache) {
            record.setIsCache(true);
        }
        chatMapper.deleteChatHistoryCache(userId);
        return chatHistoryCache;
    }
    
    /**
     * 删除聊天记录缓存
     *
     * @param userId 用户ID
     */
    @Override
    public void deleteChatHistoryCache(String userId) {
        try {
            chatMapper.deleteChatHistoryCache(userId);
            chatMapper.deleteChatGroupHistoryCache(userId);
        }
        catch (Exception e) {
            log.error("删除缓存失败", e);
            throw e;
        }
    }
    
    /**
     * 保存对话列表
     *
     * @param param 保存参数
     *
     * @return 是否保存成功
     */
    @Override
    public Boolean saveChatList(SaveChatListRequestParam param) {
        Integer result = chatMapper.saveChatList(param);
        return result == 1 || result == 2;
    }
    
    /**
     * 获取对话列表
     *
     * @param userId 用户ID
     *
     * @return 对话列表
     */
    @Override
    public ChatListPO getChatList(String userId) {
        ChatListPO result = chatMapper.getChatList(userId);
        if (result == null) {
            ChatListPO chatList = new ChatListPO();
            chatList.setUserId(userId);
            chatList.setChatList("{\"vipList\":[],\"commonList\":[]}");
            if (chatMapper.createChatList(chatList) != 1) {
                log.error("创建对话列表失败");
            }
            return chatList;
        }
        return result;
    }
    
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
    @Override
    public ArrayList<ChatRecordDTO> getChatHistory(String userId, String friendId, Integer pageIndex, Integer pageSize, LocalDateTime lastTime) {
        return chatMapper.getChatHistory(userId, friendId, pageIndex, pageSize, lastTime);
    }
    
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
    @Override
    public ArrayList<ChatRecordDTO> getGroupChatHistory(String userId, String groupId, Integer pageIndex, Integer pageSize, LocalDateTime lastTime) {
        return chatMapper.getGroupChatHistory(userId, groupId, pageIndex, pageSize, lastTime);
    }
    
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
    @Override
    public String uploadChatFile(String fileId,
                                 String fileName,
                                 String fileType,
                                 Long fileSize,
                                 String sender,
                                 String receiver,
                                 MultipartFile file,
                                 Boolean isGroup) {
        try {
            String bucketName;
            if (isGroup) {
                bucketName = ossUtils.getChatGroupFileBucketName(receiver);
            }
            else {
                bucketName = ossUtils.getChatFileBucketName();
            }
            
            if (ossUtils.isBucketExist(bucketName)) {
                String filePath = ossUtils.getObjectUrl(bucketName, ossUtils.upload(bucketName, fileId, file));
                LocalDateTime createTime = LocalDateTime.now();
                ChatFilePO chatFile = new ChatFilePO();
                
                chatFile.setFileId(fileId);
                chatFile.setFileName(fileName);
                chatFile.setFilePath(filePath);
                chatFile.setFileType(fileType);
                chatFile.setFileSize(fileSize);
                chatFile.setSender(sender);
                chatFile.setReceiver(receiver);
                chatFile.setCreateTime(createTime.toString());
                
                if (isGroup) {
                    if (chatMapper.uploadGroupChatFile(chatFile) == 1) {
                        return filePath;
                    }
                }
                else {
                    if (chatMapper.uploadChatFile(chatFile) == 1) {
                        return filePath;
                    }
                }
            }
            return null;
        }
        catch (Exception e) {
            log.error("保存聊天文件失败:{}", e.toString());
            return null;
        }
    }
    
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
    @Override
    public String uploadChatImage(String imageId,
                                  String imageName,
                                  String imageType,
                                  Long imageSize,
                                  String sender,
                                  String receiver,
                                  MultipartFile image,
                                  Boolean isGroup) {
        try {
            String bucketName;
            if (isGroup) {
                bucketName = ossUtils.getChatGroupImageBucketName(receiver);
            }
            else {
                bucketName = ossUtils.getChatImageBucketName();
            }
            
            if (ossUtils.isBucketExist(bucketName)) {
                String filePath = ossUtils.getObjectUrl(bucketName, ossUtils.upload(bucketName, imageId, image));
                LocalDateTime createTime = LocalDateTime.now();
                
                ChatImagePO chatImage = new ChatImagePO();
                chatImage.setImageId(imageId);
                chatImage.setImageName(imageName);
                chatImage.setImagePath(filePath);
                chatImage.setImageType(imageType);
                chatImage.setImageSize(imageSize);
                chatImage.setSender(sender);
                chatImage.setReceiver(receiver);
                chatImage.setCreateTime(createTime.toString());
                
                if (isGroup) {
                    if (chatMapper.uploadGroupChatImage(chatImage) == 1) {
                        return filePath;
                    }
                }
                else {
                    if (chatMapper.uploadChatImage(chatImage) == 1) {
                        return filePath;
                    }
                }
            }
            return null;
        }
        catch (Exception e) {
            log.error("保存聊天图片失败:{}", e.toString());
            return null;
        }
    }
    
    /**
     * 获取聊天文件列表
     *
     * @param userId    用户ID
     * @param pageIndex 页码
     * @param pageSize  每页大小
     *
     * @return 聊天文件列表
     */
    @Override
    public ChatFileListResponseVO getChatFileList(String userId, Integer pageIndex, Integer pageSize) {
        ChatFileListResponseVO response = new ChatFileListResponseVO();
        Integer total = chatMapper.getChatFileTotal(userId);
        ArrayList<ChatFileListItemDTO> chatFileList = chatMapper.getChatFileList(userId, pageIndex, pageSize);
        response.setTotal(total);
        response.setChatFileList(chatFileList);
        return response;
    }
    
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
    @Override
    public ArrayList<ChatRecordDTO> getChatHistoryByType(String userId,
                                                         String friendId,
                                                         Integer pageIndex,
                                                         Integer pageSize,
                                                         String type,
                                                         String searchKey,
                                                         LocalDateTime lastTime,
                                                         Boolean isGroup) {
        if (isGroup) {
            return chatMapper.getGroupChatHistoryByType(userId, friendId, pageIndex, pageSize, type, searchKey, lastTime);
        }
        else {
            return chatMapper.getChatHistoryByType(userId, friendId, pageIndex, pageSize, type, searchKey, lastTime);
        }
    }
    
    /**
     * 删除聊天记录
     *
     * @param idList  聊天记录ID
     * @param isGroup 是否为群聊
     *
     * @return 是否删除成功
     */
    @Override
    public boolean deleteChatHistory(ArrayList<String> idList, Boolean isGroup) {
        if (isGroup) {
            for (String id : idList) {
                if (chatMapper.deleteGroupChatHistory(id) != 1) {
                    return false;
                }
            }
        }
        else {
            for (String id : idList) {
                if (chatMapper.deleteChatHistory(id) != 1) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 创建群聊
     *
     * @param param 创建群聊参数
     *
     * @return 是否创建成功
     */
    @Override
    public Boolean createChatGroup(CreateChatGroupRequestParam param) {
        try {
            ChatGroupPO chatGroup = new ChatGroupPO();
            chatGroup.setGroupId("group-" + UUIDUtils.generateUUID());
            chatGroup.setGroupName(param.getGroupName());
            chatGroup.setOrgId(param.getOrgId());
            chatGroup.setCreator(param.getCreator());
            chatGroup.setCreateTime(LocalDateTime.now());
            
            if (socialMapper.createChatGroup(chatGroup) == 1) {
                for (String memberId : param.getMemberList()) {
                    ChatGroupMemberPO chatGroupMember = new ChatGroupMemberPO();
                    chatGroupMember.setId(UUIDUtils.generateUUID());
                    chatGroupMember.setGroupId(chatGroup.getGroupId());
                    chatGroupMember.setUserId(memberId);
                    chatGroupMember.setCreateTime(LocalDateTime.now());
                    socialMapper.addChatGroupMember(chatGroupMember);
                    
                    // 将成员存入redis
                    redisUtils.setAdd("chat_group:member:" + chatGroup.getGroupId(), memberId);
                }
            }
            
            Boolean imageBucketFlag = ossUtils.createBucket(ossUtils.getChatGroupImageBucketName(chatGroup.getGroupId()));
            Boolean fileBucketFlag = ossUtils.createBucket(ossUtils.getChatGroupFileBucketName(chatGroup.getGroupId()));
            return imageBucketFlag && fileBucketFlag;
        }
        catch (Exception e) {
            log.error("创建群聊失败:{}", e.toString());
            return false;
        }
    }
    
    /**
     * 保存群聊聊天记录
     *
     * @param record 聊天记录
     */
    @Override
    public void saveGroupChatHistory(ChatRecordDTO record) {
        try {
            chatMapper.saveGroupChatHistory(record);
        }
        catch (Exception e) {
            log.error("保存群聊聊天记录失败", e);
            throw e;
        }
    }
}
