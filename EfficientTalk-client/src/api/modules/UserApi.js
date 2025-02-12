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
}