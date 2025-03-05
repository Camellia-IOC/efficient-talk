import { appStoreService as service } from "../AxiosRequestManager.js";

export default class AppStoreApi {
    // 请求前置路径
    static requestPath = "/api/appStore";

    /**
     * 获取应用商店列表
     * @param data 请求数据
     */
    static getAppList = (data) => {
        return service.get({
            url: this.requestPath + "/getAppList",
            data
        });
    };
}
