package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.PO.ChatListPO;
import com.ETGroup.EfficientTalkServer.entity.request.chat.SaveChatListRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.chat.ChatFileListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.chat.ChatHistoryResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.chat.UploadChatFileResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "通讯相关接口", description = "通讯相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Resource
    private ChatService chatService;
    
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
    public ResponseData<ChatHistoryResponseVO> getChatHistory(@RequestParam String userId, @RequestParam String friendId,
                                                              @RequestParam Integer pageSize, @RequestParam Integer pageIndex) {
        ChatHistoryResponseVO response = new ChatHistoryResponseVO();
        response.setChatHistory(chatService.getChatHistory(userId, friendId, pageSize, pageIndex));
        return ResponseData.success(response);
    }
    
    @Operation(summary = "获取缓存的聊天记录")
    @GetMapping("/getChatHistoryCache")
    public ResponseData<ChatHistoryResponseVO> getChatHistoryCache(@RequestParam String userId) {
        ChatHistoryResponseVO response = new ChatHistoryResponseVO();
        response.setChatHistory(chatService.getChatHistoryCache(userId));
        return ResponseData.success(response);
    }
    
    @Operation(summary = "保存聊天文件")
    @PostMapping("/uploadChatFile")
    public ResponseData<Object> uploadChatFile(@RequestParam String fileId,
                                               @RequestParam String fileName,
                                               @RequestParam String fileType,
                                               @RequestParam Long fileSize,
                                               @RequestParam String sender,
                                               @RequestParam String receiver,
                                               @RequestParam MultipartFile file) {
        try {
            String filePath = chatService.uploadChatFile(fileId, fileName, fileType, fileSize, sender, receiver, file);
            UploadChatFileResponseVO response = new UploadChatFileResponseVO();
            response.setFilePath(filePath);
            return ResponseData.success(response);
        }
        catch (IOException e) {
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
}
