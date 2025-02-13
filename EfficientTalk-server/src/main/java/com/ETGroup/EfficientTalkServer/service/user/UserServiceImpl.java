package com.ETGroup.EfficientTalkServer.service.user;

import com.ETGroup.EfficientTalkServer.entity.response.user.UserBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.user.UserDetailResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    
    /**
     * 获取用户详细信息
     *
     * @param userId 用户ID
     *
     * @return 用户详细信息
     */
    @Override
    public UserDetailResponseVO getUserDetail(String curLoginUserId, String userId) {
        return userMapper.getUserDetail(curLoginUserId, userId);
    }
    
    /**
     * 获取用户基本信息
     *
     * @param userId 用户ID
     *
     * @return 用户基本信息
     */
    @Override
    public UserBasicInfoResponseVO getUserBasicInfo(String userId) {
        return userMapper.getUserBasicInfo(userId);
    }
}
