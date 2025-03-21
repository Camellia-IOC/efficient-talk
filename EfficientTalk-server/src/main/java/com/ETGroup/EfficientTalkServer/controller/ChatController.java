package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.PO.ChatListPO;
import com.ETGroup.EfficientTalkServer.entity.request.chat.SaveChatListRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.chat.ChatFileListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.chat.ChatHistoryResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.chat.UploadChatFileResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.mapper.ChatMapper;
import com.ETGroup.EfficientTalkServer.service.chat.ChatService;
import com.ETGroup.EfficientTalkServer.utils.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
        String filePath = chatService.uploadChatFile(fileId, fileName, fileType, fileSize, sender, receiver, file);
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
        String filePath = chatService.uploadChatImage(imageId, imageName, imageType, imageSize, sender, receiver, image);
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
                                                                    @RequestParam String friendId,
                                                                    @RequestParam(required = false) Integer pageIndex,
                                                                    @RequestParam Integer pageSize,
                                                                    @RequestParam String type,
                                                                    @RequestParam(required = false) String searchKey,
                                                                    @RequestParam(required = false) LocalDateTime lastTime
    ) {
        ChatHistoryResponseVO response = new ChatHistoryResponseVO();
        response.setChatHistory(chatService.getChatHistoryByType(userId, friendId, pageIndex, pageSize, type, searchKey, lastTime));
        return ResponseData.success(response);
    }
    
    @Operation(summary = "获取聊天文件Blob")
    @GetMapping("/getChatFileBlob")
    public ResponseEntity<byte[]> getChatFileBlob(@RequestParam String fileId,
                                                  @RequestParam String type,
                                                  @RequestParam(defaultValue = "false") boolean isGroup) {
        String filePath;
        if (type.equals("media")) {
            filePath = chatMapper.getChatMediaFilePath(fileId);
        }
        else if (type.equals("file")) {
            filePath = chatMapper.getChatFilePath(fileId);
        }
        else {
            filePath = null;
        }
        
        return FileUtils.getFileBlob(filePath);
    }
}
