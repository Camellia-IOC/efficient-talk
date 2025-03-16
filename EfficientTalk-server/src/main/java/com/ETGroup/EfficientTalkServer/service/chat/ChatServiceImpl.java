package com.ETGroup.EfficientTalkServer.service.chat;

import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatFileListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.chat.ChatRecordDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatFilePO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatImagePO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatListPO;
import com.ETGroup.EfficientTalkServer.entity.request.chat.SaveChatListRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.chat.ChatFileListResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.ChatMapper;
import com.ETGroup.EfficientTalkServer.mapper.SocialMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private ChatMapper chatMapper;
    
    @Resource
    private SocialMapper socialMapper;
    
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
            chatList.setChatList("{\"vipList\":[ ],\"commonList\":[ ]}");
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
    @Override
    public String uploadChatFile(String fileId,
                                 String fileName,
                                 String fileType,
                                 Long fileSize,
                                 String sender,
                                 String receiver,
                                 MultipartFile file) {
        try {
            String savePath = System.getProperty("user.dir") + "\\resources\\chat_files\\";
            File saveFile = new File(savePath + file.getOriginalFilename());
            file.transferTo(saveFile);
            String filePath = savePath + file.getOriginalFilename();
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
            
            if (chatMapper.uploadChatFile(chatFile) == 1) {
                return filePath;
            }
            return null;
        }
        catch (IOException e) {
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
                                  MultipartFile image) {
        try {
            String savePath = System.getProperty("user.dir") + "\\resources\\chat_images\\";
            File saveFile = new File(savePath + image.getOriginalFilename());
            image.transferTo(saveFile);
            String filePath = savePath + image.getOriginalFilename();
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
            
            if (chatMapper.uploadChatImage(chatImage) == 1) {
                return filePath;
            }
            return null;
        }
        catch (IOException e) {
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
                                                         LocalDateTime lastTime) {
        return chatMapper.getChatHistoryByType(userId, friendId, pageIndex, pageSize, type, searchKey, lastTime);
    }
    
    /**
     * 删除聊天记录
     *
     * @param idList 聊天记录ID
     *
     * @return 是否删除成功
     */
    @Override
    public boolean deleteChatHistory(ArrayList<String> idList) {
        for (String id : idList) {
            if (chatMapper.deleteChatHistory(id) != 1) {
                return false;
            }
        }
        return true;
    }
}
