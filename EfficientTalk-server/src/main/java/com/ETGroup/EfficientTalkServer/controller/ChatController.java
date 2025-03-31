package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.PO.ChatListPO;
import com.ETGroup.EfficientTalkServer.entity.request.chat.CreateChatGroupRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.chat.SaveChatListRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.chat.ChatFileListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.chat.ChatHistoryResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.chat.UploadChatFileResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.FileDownloadInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.mapper.ChatMapper;
import com.ETGroup.EfficientTalkServer.service.chat.ChatService;
import com.ETGroup.EfficientTalkServer.utils.FileUtils;
import com.ETGroup.EfficientTalkServer.utils.OSSUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Tag(name = "通讯相关接口", description = "通讯相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Resource
    private ChatService chatService;
    
    @Resource
    private ChatMapper chatMapper;
    
    @Resource
    private FileUtils fileUtils;
    
    @Resource
    private OSSUtils ossUtils;
    
    @Operation(summary = "保存对话列表")
    @PutMapping("/saveChatList")
    public ResponseData<Object> saveChatList(@RequestBody SaveChatListRequestParam param) {
        if (chatService.saveChatList(param)) {
            return ResponseData.success(ResponseConfig.SUCCESS);
        }
        return ResponseData.error(ResponseConfig.SAVE_CHAT_LIST_FAILED);
    }
    
    @Operation(summary = "获取对话列表")
    @GetMapping("/getChatList")
    public ResponseData<ChatListPO> getChatList(@RequestParam String userId) {
        return ResponseData.success(chatService.getChatList(userId));
    }
    
    @Operation(summary = "获取聊天记录")
    @GetMapping("/getChatHistory")
    public ResponseData<ChatHistoryResponseVO> getChatHistory(@RequestParam String userId,
                                                              @RequestParam String friendId,
                                                              @RequestParam(required = false) Integer pageIndex,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam(required = false) LocalDateTime lastTime,
                                                              @RequestParam(defaultValue = "false") boolean isGroup) {
        ChatHistoryResponseVO response = new ChatHistoryResponseVO();
        if (isGroup) {
            response.setChatHistory(chatService.getGroupChatHistory(userId, friendId, pageIndex, pageSize, lastTime));
        }
        else {
            response.setChatHistory(chatService.getChatHistory(userId, friendId, pageIndex, pageSize, lastTime));
        }
        return ResponseData.success(response);
    }
    
    @Operation(summary = "获取缓存的聊天记录")
    @GetMapping("/getChatHistoryCache")
    public ResponseData<ChatHistoryResponseVO> getChatHistoryCache(@RequestParam String userId) {
        ChatHistoryResponseVO response = new ChatHistoryResponseVO();
        response.setChatHistory(chatService.getChatHistoryCache(userId));
        return ResponseData.success(response);
    }
    
    @Operation(summary = "删除聊天记录")
    @DeleteMapping("/deleteChatHistory")
    public ResponseData<Void> deleteChatHistory(@RequestParam("idList") ArrayList<String> idList,
                                                @RequestParam(defaultValue = "false") boolean isGroup) {
        if (chatService.deleteChatHistory(idList)) {
            return ResponseData.success();
        }
        else {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "保存聊天文件")
    @PostMapping("/uploadChatFile")
    public ResponseData<UploadChatFileResponseVO> uploadChatFile(@RequestParam String fileId,
                                                                 @RequestParam String fileName,
                                                                 @RequestParam String fileType,
                                                                 @RequestParam Long fileSize,
                                                                 @RequestParam String sender,
                                                                 @RequestParam String receiver,
                                                                 @RequestParam MultipartFile file,
                                                                 @RequestParam(defaultValue = "false") boolean isGroup) {
        String filePath = chatService.uploadChatFile(fileId, fileName, fileType, fileSize, sender, receiver, file, isGroup);
        if (filePath != null) {
            UploadChatFileResponseVO response = new UploadChatFileResponseVO();
            response.setFilePath(filePath);
            return ResponseData.success(response);
        }
        else {
            return ResponseData.error(ResponseConfig.RESOURCE_UPLOAD_FAILED);
        }
    }
    
    @Operation(summary = "保存聊天图片")
    @PostMapping("/uploadChatImage")
    public ResponseData<UploadChatFileResponseVO> uploadChatImage(@RequestParam String imageId,
                                                                  @RequestParam String imageName,
                                                                  @RequestParam String imageType,
                                                                  @RequestParam Long imageSize,
                                                                  @RequestParam String sender,
                                                                  @RequestParam String receiver,
                                                                  @RequestParam MultipartFile image,
                                                                  @RequestParam(defaultValue = "false") boolean isGroup) {
        String filePath = chatService.uploadChatImage(imageId, imageName, imageType, imageSize, sender, receiver, image, isGroup);
        if (filePath != null) {
            UploadChatFileResponseVO response = new UploadChatFileResponseVO();
            response.setFilePath(filePath);
            return ResponseData.success(response);
        }
        else {
            return ResponseData.error(ResponseConfig.RESOURCE_UPLOAD_FAILED);
        }
    }
    
    @Operation(summary = "分页获取聊天文件列表")
    @GetMapping("/getChatFileList")
    public ResponseData<ChatFileListResponseVO> getChatFileList(@RequestParam String userId,
                                                                @RequestParam Integer pageIndex,
                                                                @RequestParam Integer pageSize) {
        ChatFileListResponseVO response = chatService.getChatFileList(userId, pageIndex, pageSize);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "分类获取聊天记录")
    @GetMapping("/getChatHistoryByType")
    public ResponseData<ChatHistoryResponseVO> getChatHistoryByType(@RequestParam String userId,
                                                                    @RequestParam(required = false) String friendId,
                                                                    @RequestParam(required = false) Integer pageIndex,
                                                                    @RequestParam Integer pageSize,
                                                                    @RequestParam String type,
                                                                    @RequestParam(required = false) String searchKey,
                                                                    @RequestParam(required = false) LocalDateTime lastTime,
                                                                    @RequestParam(defaultValue = "false") boolean isGroup) {
        ChatHistoryResponseVO response = new ChatHistoryResponseVO();
        response.setChatHistory(chatService.getChatHistoryByType(userId, friendId, pageIndex, pageSize, type, searchKey, lastTime, isGroup));
        return ResponseData.success(response);
    }
    
    @Operation(summary = "获取聊天文件Blob")
    @GetMapping("/getChatFileBlob")
    public ResponseEntity<byte[]> getChatFileBlob(@RequestParam String fileId,
                                                  @RequestParam String type,
                                                  @RequestParam(defaultValue = "false") boolean isGroup,
                                                  @RequestParam(required = false) String groupId) {
        String bucketName;
        String objectId;
        String objectName;
        Map<String, String> objectInfo;
        try {
            if (type.equals("media")) {
                bucketName = isGroup ? ossUtils.getChatGroupImageBucketName(groupId) : ossUtils.getChatImageBucketName();
                objectInfo = isGroup ? chatMapper.getGroupChatImageInfo(fileId) : chatMapper.getChatImageInfo(fileId);
                objectId = fileId + "." + objectInfo.get("imageType");
                objectName = objectInfo.get("imageName");
            }
            else if (type.equals("file")) {
                bucketName = isGroup ? ossUtils.getChatGroupFileBucketName(groupId) : ossUtils.getChatFileBucketName();
                objectInfo = isGroup ? chatMapper.getGroupChatFileInfo(fileId) : chatMapper.getChatFileInfo(fileId);
                objectId = fileId + "." + objectInfo.get("fileType");
                objectName = objectInfo.get("fileName");
            }
            else {
                bucketName = null;
                objectId = null;
                objectName = null;
            }
            return fileUtils.getOSSFileBlob(bucketName, objectId, objectName);
        }
        catch (Exception e) {
            log.error("获取聊天文件Blob失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(summary = "创建群聊")
    @PostMapping("/createChatGroup")
    public ResponseData<Boolean> createChatGroup(@RequestBody CreateChatGroupRequestParam param) {
        Boolean response = chatService.createChatGroup(param);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "获取云盘文件下载地址")
    @GetMapping("/getChatFileDownloadUrl")
    public ResponseData<FileDownloadInfoResponseVO> getChatFileDownloadUrl(@RequestParam String fileId, @RequestParam Boolean isGroup) {
        FileDownloadInfoResponseVO response;
        try {
            if (isGroup) {
                response = chatMapper.getChatGroupFileDownloadInfo(fileId);
            }
            else {
                response = chatMapper.getChatFileDownloadInfo(fileId);
            }
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取云盘文件下载地址失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
}
