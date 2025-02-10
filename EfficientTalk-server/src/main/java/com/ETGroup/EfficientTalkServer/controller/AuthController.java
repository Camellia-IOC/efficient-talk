package com.pigstory.controller;

import com.pigstory.entity.auth.request.LoginRequestParam;
import com.pigstory.entity.auth.request.RegisterRequestParam;
import com.pigstory.entity.auth.response.LoginResponseVO;
import com.pigstory.entity.auth.response.RegisterResponseVO;
import com.pigstory.entity.response.ResponseData;
import com.pigstory.entity.response.ResponseConfig;
import com.pigstory.logger.LoggerFactory;
import com.pigstory.service.auth.AuthService;
import com.pigstory.service.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户认证接口", description = "用户登录、用户注册")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("api/auth")
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
                LoggerFactory.logger.warn("注册消息生成失败");
            }
            
            return ResponseData.success(response);
        }
    }
}
