import { userService as service } from "../AxiosRequestManager.js";

export default class UserApi {
    // 请求前置路径
    static requestPath = "/api/user";

    /**
     * 获取用户详细信息
     * @param data 请求数据
     */
    static getUserDetail = (data) => {
        return service.get({
            url: this.requestPath + "/getUserDetail",
            data
        });
    };

    /**
     * 获取用户基本信息(用户名+头像)
     * @param data 请求数据
     */
    static getUserBasicInfo = (data) => {
        return service.get({
            url: this.requestPath + "/getUserBasicInfo",
            data
        });
    };

    /**
     * 设置用户在线状态
     * @param data
     */
    static setUserOnlineState = (data) => {
        return service.put({
            url: this.requestPath + "/setUserOnlineState",
            data
        });
    };

    /**
     * 保存用户系统设置
     * @param data
     */
    static saveUserSystemSetting = (data) => {
        return service.post({
            url: this.requestPath + "/saveUserSystemSetting",
            data
        });
    };

    /**
     * 获取用户系统设置
     * @param data
     */
    static getUserSystemSetting = (data) => {
        return service.get({
            url: this.requestPath + "/getUserSystemSetting",
            data
        });
    };
}