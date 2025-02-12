package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.response.user.UserBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.user.UserDetailResponseVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     * 获取用户详细信息
     *
     * @param userId 用户ID
     *
     * @return 用户详细信息
     */
    UserDetailResponseVO getUserDetail(String userId);
    
    /**
     * 获取用户基本信息
     *
     * @param userId 用户ID
     *
     * @return 用户基本信息
     */
    UserBasicInfoResponseVO getUserBasicInfo(String userId);
}
