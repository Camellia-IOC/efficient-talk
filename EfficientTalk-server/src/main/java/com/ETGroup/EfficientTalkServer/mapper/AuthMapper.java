package com.pigstory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pigstory.entity.PO.UserPO;
import com.pigstory.entity.auth.dto.UserLoginDTO;
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
    @Select("SELECT password as password FROM users WHERE user_id = #{userId}")
    UserPO getPasswordById (@Param("userId") Long userId);
    
    /**
     * 登录时返回用户id信息
     * @param userId 用户id
     * @return 用户id信息
     */
    @Select("SELECT user_id as userId,zone as zoneId FROM users WHERE user_id=#{userId}")
    UserLoginDTO getUserLoginDTO(@Param("userId") Long userId);
}
