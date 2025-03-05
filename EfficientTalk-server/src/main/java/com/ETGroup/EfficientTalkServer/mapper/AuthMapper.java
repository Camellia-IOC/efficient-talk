package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.PO.PermissionPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ETGroup.EfficientTalkServer.entity.PO.UserPO;
import com.ETGroup.EfficientTalkServer.entity.DTO.auth.UserLoginDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface AuthMapper extends BaseMapper<UserPO> {
    /**
     * 获取指定用户密码
     *
     * @param userId 用户ID
     *
     * @return 用户信息
     */
    String getPasswordById(@Param("userId") String userId);
    
    /**
     * 登录时返回用户id信息
     *
     * @param userId 用户id
     *
     * @return 用户id信息
     */
    UserLoginDTO getUserLoginDTO(@Param("userId") String userId);
    
    /**
     * 获取指定用户权限列表
     *
     * @param userId 用户ID
     *
     * @return 权限列表
     */
    ArrayList<PermissionPO> getPermissionByUserId(@Param("userId") String userId);
    
    /**
     * 获取指定用户权限ID列表
     *
     * @param userId 用户ID
     *
     * @return 权限列表
     */
    ArrayList<String> getUserPermissionIdList(@Param("userId") String userId);
}
