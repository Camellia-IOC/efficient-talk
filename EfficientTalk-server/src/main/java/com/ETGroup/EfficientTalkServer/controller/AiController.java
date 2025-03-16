package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.PO.AiChatHistoryRecordPO;
import com.ETGroup.EfficientTalkServer.entity.PO.AiChatSessionPO;
import com.ETGroup.EfficientTalkServer.entity.response.ai.AiChatHistoryResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.ai.AiSessionListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.service.ai.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AI助手相关接口", description = "AI助手相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/ai")
public class AiController {
    @Resource
    private AiService aiService;
    
    @Operation(summary = "新建AI会话")
    @PostMapping("/createAiSession")
    public ResponseData<String> createAiSession(@RequestBody AiChatSessionPO param) {
        String sessionId = aiService.createAiSession(param);
        if (sessionId != null) {
            return ResponseData.success(sessionId);
        }
        else {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "获取AI会话列表")
    @GetMapping("/getAiSessionList")
    public ResponseData<AiSessionListResponseVO> getAiSessionList(@RequestParam String userId) {
        AiSessionListResponseVO sessionList = aiService.getAiSessionList(userId);
        if (sessionList != null) {
            return ResponseData.success(sessionList);
        }
        return ResponseData.error(ResponseConfig.ERROR);
    }
    
    @Operation(summary = "保存AI对话记录")
    @PostMapping("/saveAiChatHistory")
    public ResponseData<Void> saveAiChatHistory(@RequestBody AiChatHistoryRecordPO param) {
        if (aiService.saveAiChatHistory(param)) {
            return ResponseData.success();
        }
        return ResponseData.error(ResponseConfig.ERROR);
        
    }
    
    @Operation(summary = "获取AI对话记录")
    @GetMapping("/getAiChatHistory")
    public ResponseData<AiChatHistoryResponseVO> getAiChatHistory(@RequestParam String sessionId) {
        AiChatHistoryResponseVO history = aiService.getAiChatHistory(sessionId);
        if (history != null) {
            return ResponseData.success(history);
        }
        return ResponseData.error(ResponseConfig.ERROR);
    }
}
