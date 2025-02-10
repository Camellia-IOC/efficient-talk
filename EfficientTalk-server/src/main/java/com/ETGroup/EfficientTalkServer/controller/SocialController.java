package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.entity.response.social.FriendListResponseVO;
import com.ETGroup.EfficientTalkServer.service.social.SocialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "社交相关接口", description = "社交相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/social")
public class SocialController {
    @Resource
    private SocialService socialService;
    
    @Operation(summary = "获取好友列表")
    @GetMapping("/getFriendList")
    public ResponseData<FriendListResponseVO> getFriendList(@RequestParam String userId) {
        FriendListResponseVO response = socialService.getFriendList(userId);
        return ResponseData.success(response);
    }
}
