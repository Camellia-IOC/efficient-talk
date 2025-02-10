package com.ETGroup.EfficientTalkServer.service.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ETGroup.EfficientTalkServer.entity.PO.UserPO;
import com.ETGroup.EfficientTalkServer.entity.request.auth.LoginRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.auth.RegisterRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.auth.LoginResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.auth.RegisterResponseVO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService extends IService<UserPO> {
    /**
     * 用户登录
     *
     * @param param 请求参数
     *
     * @return permission 是否允许登录
     */
    LoginResponseVO userLogin (LoginRequestParam param);
    
    /**
     * 用户注册
     *
     * @param param 请求参数
     *
     * @return userID 用户ID
     */
    RegisterResponseVO userRegister (RegisterRequestParam param);
}
