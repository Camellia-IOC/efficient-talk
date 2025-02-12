package com.ETGroup.EfficientTalkServer.entity.response.user;

import lombok.Data;

@Data
public class UserDetailResponseVO {
    // 用户ID
    private String userId;
    // 用户名
    private String userName;
    // 用户头像
    private String userAvatar;
    // 工号
    private Integer employeeId;
    // 用户所属部门ID
    private String deptId;
    // 用户所属部门
    private String deptName;
    // 用户职位ID
    private String jobId;
    // 用户职位
    private String jobName;
}
