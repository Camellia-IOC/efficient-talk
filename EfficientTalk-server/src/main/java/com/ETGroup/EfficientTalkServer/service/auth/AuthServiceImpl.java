package com.ETGroup.EfficientTalkServer.service.auth;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ETGroup.EfficientTalkServer.entity.PO.UserPO;
import com.ETGroup.EfficientTalkServer.entity.request.auth.LoginRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.auth.RegisterRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.auth.LoginResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.auth.RegisterResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.AuthMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, UserPO> implements AuthService {
    @Resource
    private AuthMapper authMapper;
    
    /**
     * 用户登录
     *
     * @param param 请求参数
     *
     * @return permission 是否允许登录
     */
    @Override
    public LoginResponseVO userLogin(LoginRequestParam param) {
        UserPO userData = authMapper.getPasswordById(param.getUserId());
        LoginResponseVO loginResponseVO = new LoginResponseVO();
        
        if (userData == null) {
            loginResponseVO.setPermission(false);
        }
        else {
            loginResponseVO.setPermission(Objects.equals(param.getPassword(), userData.getPassword()));
            if (loginResponseVO.getPermission()) {
                loginResponseVO.setUserData(authMapper.getUserLoginDTO(param.getUserId()));
            }
        }
        
        return loginResponseVO;
    }
    
    /**
     * 用户注册
     *
     * @param param 请求参数
     *
     * @return userID 用户ID
     */
    @Override
    public RegisterResponseVO userRegister(RegisterRequestParam param) {
        UserPO user = new UserPO();
        RegisterResponseVO registerResponseVO = new RegisterResponseVO();
        
        user.setUsername(param.getUsername());
        user.setPassword(param.getPassword());
        
        if (authMapper.insert(user) == 1) {
            registerResponseVO.setUserId(user.getUserId());
        }
        else {
            registerResponseVO.setUserId(null);
        }
        
        return registerResponseVO;
    }
}
