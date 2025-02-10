package com.pigstory.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("messages")
public class MessagePO {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    
    @TableField("type")
    private Integer type;
    
    @TableField("origin")
    private Long origin;
    
    @TableField("receiver")
    private Long receiver;
    
    @TableField("title")
    private String title;
    
    @TableField("content")
    private String content;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("read_flag")
    private Integer readFlag;
}
