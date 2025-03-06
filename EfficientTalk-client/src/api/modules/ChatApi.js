import { chatService as service } from "../AxiosRequestManager.js";

export default class ChatApi {
    // 请求前置路径
    static requestPath = "/api/chat";

    /**
     * 获取聊天记录
     * @param data 请求数据
     */
    static getChatHistory = (data) => {
        return service.get({
            url: this.requestPath + "/getChatHistory",
            data
        });
    };

    /**
     * 保存对话列表
     * @param data 请求数据
     */
    static saveChatList = (data) => {
        return service.put({
            url: this.requestPath + "/saveChatList",
            data
        });
    };

    /**
     * 获取对话列表
     * @param data 请求数据
     */
    static getChatList = (data) => {
        return service.get({
            url: this.requestPath + "/getChatList",
            data
        });
    };

    /**
     * 获取聊天记录缓存
     * @param data
     */
    static getChatHistoryCache = (data) => {
        return service.get({
            url: this.requestPath + "/getChatHistoryCache",
            data
        });
    };

    /**
     * 上传聊天文件
     * @param data
     */
    static uploadChatFile = (data) => {
        return service.upload({
            url: this.requestPath + "/uploadChatFile",
            data
        });
    };

    /**
     * 上传聊天图片
     * @param data
     */
    static uploadChatImage = (data) => {
        return service.upload({
            url: this.requestPath + "/uploadChatImage",
            data
        });
    };

    /**
     * 获取聊天文件列表
     * @param data
     */
    static getChatFileList = (data) => {
        return service.get({
            url: this.requestPath + "/getChatFileList",
            data
        });
    };

    /**
     * 分类获取聊天记录
     * @param data 请求数据
     */
    static getChatHistoryByType = (data) => {
        return service.get({
            url: this.requestPath + "/getChatHistoryByType",
            data
        });
    };

    /**
     * 获取聊天文件
     * @param data
     */
    static getChatFileBlob = (data) => {
        return service.download({
            url: this.requestPath + "/getChatFileBlob",
            data
        });
    };
}