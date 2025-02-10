package com.ETGroup.EfficientTalkServer.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("users")
public class UserPO {
    @TableId(value = "id")
    private String userId;
    
    @TableField("username")
    private String username;
    
    @TableField("password")
    private String password;
    
    @TableField("avatar")
    private String avatar;
    
    @TableField("deptId")
    private String deptId;
    
    @TableField("register_time")
    private LocalDateTime registerTime;
    
    @TableField("sex")
    private Integer sex;
    
    @TableField("state")
    private Integer state;
    
    @TableField("token")
    private String token;
}
