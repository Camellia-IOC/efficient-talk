package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.request.user.SetOnlineStateRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.entity.response.user.UserBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.user.UserDetailResponseVO;
import com.ETGroup.EfficientTalkServer.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户相关接口", description = "用户相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;
    
    @Operation(summary = "获取用户详细信息")
    @GetMapping("/getUserDetail")
    public ResponseData<UserDetailResponseVO> getUserDetail(@RequestParam String curLoginUserId, @RequestParam String userId) {
        UserDetailResponseVO response = userService.getUserDetail(curLoginUserId, userId);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "获取用户基本信息")
    @GetMapping("/getUserBasicInfo")
    public ResponseData<UserBasicInfoResponseVO> getUserBasicInfo(@RequestParam String userId) {
        UserBasicInfoResponseVO response = userService.getUserBasicInfo(userId);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "设置用户在线状态")
    @PutMapping("/setUserOnlineState")
    public ResponseData<Object> setUserOnlineState(@RequestBody SetOnlineStateRequestParam param) {
        if (userService.setUserOnlineState(param.getUserId(), param.getOnlineState()) == 1) {
            return ResponseData.success();
        }
        else {
            return ResponseData.error(ResponseConfig.LOGIN_FAILED);
        }
    }
}
