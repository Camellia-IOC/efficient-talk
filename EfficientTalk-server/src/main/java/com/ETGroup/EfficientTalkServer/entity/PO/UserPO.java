package com.pigstory.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("users")
public class UserPO {
    @TableId(value = "user_id",type= IdType.AUTO)
    private Long userId;
    
    @TableField("username")
    private String username;
    
    @TableField("password")
    private String password;
    
    @TableField("avatar")
    private String avatar;
    
    @TableField("intro")
    private String intro;
    
    @TableField("sex")
    private Integer sex;
    
    @TableField("mate")
    private Long mate;
    
    @TableField("zone")
    private Long zone;
    
    @TableField("birthday")
    private Date birthday;
    
    @TableField("token")
    private String token;
}
