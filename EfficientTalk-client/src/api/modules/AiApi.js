import { aiService as service } from "../AxiosRequestManager.js";

export default class AiApi {
    // 请求前置路径
    static requestPath = "/api/ai";

    /**
     * 保存对话记录
     * @param data 对话记录
     */
    static saveAiChatHistory = (data) => {
        return service.post({
            url: this.requestPath + "/saveAiChatHistory",
            data
        });
    };

    /**
     * 获取对话记录
     * @param data
     */
    static getAiChatHistory = (data) => {
        return service.get({
            url: this.requestPath + "/getAiChatHistory",
            data
        });
    };
}
