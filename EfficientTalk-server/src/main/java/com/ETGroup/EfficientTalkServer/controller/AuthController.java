package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.request.auth.LoginRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.auth.RegisterRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.auth.LoginResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.auth.RegisterResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.service.auth.AuthService;
import com.ETGroup.EfficientTalkServer.service.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "鉴权相关接口", description = "鉴权相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private AuthService authService;
    
    @Resource
    private MessageService messageService;
    
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ResponseData<LoginResponseVO> userLogin (@RequestBody LoginRequestParam param) {
        LoginResponseVO response = authService.userLogin(param);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ResponseData<RegisterResponseVO> userRegister (@RequestBody RegisterRequestParam param) {
        RegisterResponseVO response = authService.userRegister(param);
        if (response.getUserId() == null) {
            return ResponseData.error(ResponseConfig.REGISTER_FAILED);
        }
        else {
            // 创建新用户注册消息
            if (!messageService.createRegisterMessage(response.getUserId(),param.getUsername())) {
                log.warn("注册消息生成失败");
            }
            
            return ResponseData.success(response);
        }
    }
}
