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

    /**
     * 获取好友邀请列表
     * @param data 请求数据
     */
    static getFriendInvitationList = (data) => {
        return service.get({
            url: this.requestPath + "/getFriendInvitationList",
            data
        });
    };

    /**
     * 删除好友
     * @param data 请求数据
     */
    static deleteFriend = (data) => {
        return service.delete({
            url: this.requestPath + "/deleteFriend",
            data
        });
    };

    /**
     * 创建好友邀请
     * @param data
     */
    static createFriendInvite = (data) => {
        return service.post({
            url: this.requestPath + "/createFriendInvite",
            data
        });
    };

    /**
     * 处理好友邀请
     * @param data
     */
    static handleFriendInvite = (data) => {
        return service.put({
            url: this.requestPath + "/handleFriendInvite",
            data
        });
    }

    /**
     * 获取好友分组
     * @param data
     */
    static getUserFriendGroups = (data) => {
        return service.get({
            url: this.requestPath + "/getUserFriendGroups",
            data
        });
    }

    /**
     * 搜索新朋友
     * @param data
     */
    static searchNewFriend = (data) => {
        return service.get({
            url: this.requestPath + "/searchNewFriend",
            data
        });
    }
}
