import { authService as service } from "../AxiosRequestManager.js";

export default class AuthApi {
    // 请求前置路径
    static requestPath = "/api/auth";

    /**
     * 用户登录
     * @param data 请求数据
     */
    static userLogin = (data) => {
        return service.post({
            url: this.requestPath + "/login",
            data
        });
    };

    /**
     * 用户注册
     * @param data 请求数据
     */
    static userRegister = (data) => {
        return service.post({
            url: this.requestPath + "/register",
            data
        });
    };
}
