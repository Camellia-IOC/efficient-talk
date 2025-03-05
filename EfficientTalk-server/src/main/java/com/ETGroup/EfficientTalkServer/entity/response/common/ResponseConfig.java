package com.ETGroup.EfficientTalkServer.entity.response.common;

import lombok.Getter;

@Getter
public enum ResponseConfig {
    // 临时错误码
    ERROR(-1, "请求错误"),
    
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
    RESOURCE_NAME_SAME_ERROR(403, "名称重复"),
    RESOURCE_NAME_INVALID_ERROR(404, "名称不合法"),
    
    // 聊天模块：5xx
    SAVE_CHAT_LIST_FAILED(500, "保存对话列表失败"),
    MESSAGE_BOARD_TOGGLE_LIKE_STATUS_FAILED(501, "点赞/取消点赞失败，请重新尝试"),
    MESSAGE_BOARD_DELETE_FAILED(502, "删除留言失败，请重新尝试");
    
    private final int code;
    private final String msg;
    
    ResponseConfig(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
