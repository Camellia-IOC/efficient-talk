package com.pigstory.service.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pigstory.entity.PO.UserPO;
import com.pigstory.entity.auth.request.LoginRequestParam;
import com.pigstory.entity.auth.request.RegisterRequestParam;
import com.pigstory.entity.auth.response.LoginResponseVO;
import com.pigstory.entity.auth.response.RegisterResponseVO;
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
