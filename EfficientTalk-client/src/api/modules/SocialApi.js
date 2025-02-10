import { socialService as service } from "../AxiosRequestManager.js";

export default class SocialApi {
    // 请求前置路径
    static requestPath = "/api/social";

    /**
     * 获取好友列表
     * @param data 请求数据
     */
    static getFriendList = (data) => {
        return service.get({
            url: this.requestPath + "/getFriendList",
            data
        });
    };

    /**
     * 获取组织架构树
     * @param data 请求数据
     */
    static getOrgTree = (data) => {
        return service.post({
            url: this.requestPath + "/getOrgTree",
            data
        });
    };
}
