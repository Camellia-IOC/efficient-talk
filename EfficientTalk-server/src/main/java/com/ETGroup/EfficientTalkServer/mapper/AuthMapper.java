package com.ETGroup.EfficientTalkServer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ETGroup.EfficientTalkServer.entity.PO.UserPO;
import com.ETGroup.EfficientTalkServer.entity.DTO.auth.UserLoginDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper extends BaseMapper<UserPO> {
    /**
     * 获取指定用户密码
     *
     * @param userId 用户ID
     *
     * @return 用户信息
     */
    @Select("SELECT password as password FROM users WHERE id = #{userId}")
    UserPO getPasswordById (@Param("userId") String userId);
    
    /**
     * 登录时返回用户id信息
     * @param userId 用户id
     * @return 用户id信息
     */
    @Select("SELECT id as userId, username as userName, avatar as userAvatar FROM users WHERE id=#{userId}")
    UserLoginDTO getUserLoginDTO(@Param("userId") String userId);
}
