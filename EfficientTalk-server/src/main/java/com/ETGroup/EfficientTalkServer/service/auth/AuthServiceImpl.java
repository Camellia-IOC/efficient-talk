package com.ETGroup.EfficientTalkServer.service.auth;

import com.ETGroup.EfficientTalkServer.entity.PO.PermissionPO;
import com.ETGroup.EfficientTalkServer.mapper.UserMapper;
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

import java.util.ArrayList;

@Slf4j
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, UserPO> implements AuthService {
    @Resource
    private AuthMapper authMapper;
    
    @Resource
    private UserMapper userMapper;
    
    /**
     * 用户登录
     *
     * @param param 请求参数
     *
     * @return permission 是否允许登录
     */
    @Override
    public LoginResponseVO userLogin(LoginRequestParam param) {
        String password = authMapper.getPasswordById(param.getUserId());
        LoginResponseVO loginResponseVO = new LoginResponseVO();
        
        if (password == null) {
            loginResponseVO.setPermission(false);
        }
        else {
            loginResponseVO.setPermission(param.getPassword()
                                               .equals(password));
            if (loginResponseVO.getPermission()) {
                loginResponseVO.setUserData(authMapper.getUserLoginDTO(param.getUserId()));
                loginResponseVO.setSystemConfig(userMapper.getUserSystemSetting(param.getUserId()));
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
    
    /**
     * 检查用户是否有权限
     *
     * @param userId     用户ID
     * @param permission 权限
     *
     * @return permission 是否有权限
     */
    @Override
    public boolean hasPermission(String userId, String permission) {
        ArrayList<PermissionPO> userPermissions = authMapper.getPermissionByUserId(userId);
        for (PermissionPO userPermission : userPermissions) {
            if (userPermission.getPermissionName()
                              .equals(permission)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取用户权限
     *
     * @param userId 用户ID
     *
     * @return permission 权限列表
     */
    @Override
    public ArrayList<String> getUserPermission(String userId) {
        ArrayList<String> permissionIdList;
        permissionIdList = authMapper.getUserPermissionIdList(userId);
        return permissionIdList;
    }
}
