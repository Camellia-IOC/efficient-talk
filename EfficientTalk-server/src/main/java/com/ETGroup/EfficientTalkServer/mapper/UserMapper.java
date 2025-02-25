package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.response.user.UserBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.user.UserDetailResponseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    /**
     * 获取用户详细信息
     *
     * @param userId 用户ID
     *
     * @return 用户详细信息
     */
    UserDetailResponseVO getUserDetail(@Param("curLoginUserId") String curLoginUserId, @Param("userId") String userId);
    
    /**
     * 获取用户基本信息
     *
     * @param userId 用户ID
     *
     * @return 用户基本信息
     */
    UserBasicInfoResponseVO getUserBasicInfo(String userId);
    
    /**
     * 设置用户在线状态
     *
     * @param userId      用户ID
     * @param onlineState 用户状态
     *
     * @return 设置结果
     */
    Integer setUserOnlineState(@Param("userId") String userId, @Param("onlineState") String onlineState);
}
