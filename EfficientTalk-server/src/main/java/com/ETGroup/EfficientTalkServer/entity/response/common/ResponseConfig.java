package com.ETGroup.EfficientTalkServer.entity.response.common;

import lombok.Getter;

@Getter
public enum ResponseConfig {
    // 标准正确状态码
    SUCCESS(0, "success"),
    
    // 登录注册模块：1xx
    LOGIN_FAILED(100, "登录时发生错误，请重新尝试"),
    REGISTER_FAILED(101, "注册时发生错误，请重新尝试"),
    
    // 用户模块：2xx
    USER_NOT_LOGIN(200, "用户未登录"),
    
    // 社交模块：3xx
    HANDLE_INVITATION_FAILED(300, "处理邀请失败"),
    DELETE_FRIEND_FAILED(301, "删除好友失败"),
    INVITE_FRIEND_FAILED(302, "发送好友申请失败"),
    
    // 资源模块：4xx
    RESOURCE_UPLOAD_FAILED(400, "资源上传失败，请重新尝试"),
    RESOURCE_DELETE_FAILED(401, "资源删除失败，请重新尝试"),
    RESOURCE_DOWNLOAD_FAILED(402, "资源下载失败，请重新尝试"),
    
    // 留言板模块：5xx
    MESSAGE_BOARD_POST_FAILED(500, "留言失败，请重新尝试"),
    MESSAGE_BOARD_TOGGLE_LIKE_STATUS_FAILED(501, "点赞/取消点赞失败，请重新尝试"),
    MESSAGE_BOARD_DELETE_FAILED(502, "删除留言失败，请重新尝试");
    
    private final int code;
    private final String msg;
    
    ResponseConfig(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
