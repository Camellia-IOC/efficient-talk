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
    };

    /**
     * 获取好友分组
     * @param data
     */
    static getUserFriendGroups = (data) => {
        return service.get({
            url: this.requestPath + "/getUserFriendGroups",
            data
        });
    };

    /**
     * 搜索新朋友
     * @param data
     */
    static searchNewFriend = (data) => {
        return service.get({
            url: this.requestPath + "/searchNewFriend",
            data
        });
    };

    /**
     * 获取组织架构树
     * @param data
     */
    static getOrganizationTree = (data) => {
        return service.get({
            url: this.requestPath + "/getOrganizationTree",
            data
        });
    };

    /**
     * 获取组织信息
     * @param data
     */
    static getOrganizationInfo = (data) => {
        return service.get({
            url: this.requestPath + "/getOrganizationInfo",
            data
        });
    };

    /**
     * 获取群聊列表
     * @param data
     */
    static getChatGroupList = (data) => {
        return service.get({
            url: this.requestPath + "/getChatGroupList",
            data
        });
    };

    /**
     * 获取群聊基本信息
     * @param data
     */
    static getChatGroupBasicInfo = (data) => {
        return service.get({
            url: this.requestPath + "/getChatGroupBasicInfo",
            data
        });
    };

    /**
     * 获取群聊成员列表
     * @param data
     */
    static getChatGroupMemberList = (data) => {
        return service.get({
            url: this.requestPath + "/getChatGroupMemberList",
            data
        });
    };

    /**
     * 退出群聊
     * @param data
     */
    static quitChatGroup = (data) => {
        return service.delete({
            url: this.requestPath + "/quitChatGroup",
            data
        });
    };

    /**
     * 移除群聊成员
     * @param data
     */
    static removeChatGroupMember = (data) => {
        return service.delete({
            url: this.requestPath + "/removeChatGroupMember",
            data
        });
    };

    /**
     * 根据名称获取组织成员
     * @param data
     */
    static getOrgMemberListByName = (data) => {
        return service.get({
            url: this.requestPath + "/getOrgMemberListByName",
            data
        });
    };

    /**
     * 获取群聊成员id列表
     * @param data
     */
    static getChatGroupMemberIdList = (data) => {
        return service.get({
            url: this.requestPath + "/getChatGroupMemberIdList",
            data
        });
    };

    /**
     * 邀请群聊成员
     * @param data
     */
    static inviteChatGroupMember = (data) => {
        return service.post({
            url: this.requestPath + "/inviteChatGroupMember",
            data
        });
    };

    /**
     * 创建组织
     * @param data
     */
    static createOrganization = (data) => {
        return service.post({
            url: this.requestPath + "/createOrganization",
            data
        });
    };

    /**
     * 根据分组获取好友列表
     * @param data
     * @returns {*}
     */
    static getFriendListByGroup = (data) => {
        return service.get({
            url: this.requestPath + "/getFriendListByGroup",
            data
        });
    };

    /**
     * 获取所有好友列表
     * @param data
     * @returns {*}
     */
    static getAllFriendList = (data) => {
        return service.get({
            url: this.requestPath + "/getAllFriendList",
            data
        });
    };

    /**
     * 更新好友分组
     * @param data
     * @returns {*}
     */
    static updateFriendGroup = (data) => {
        return service.put({
            url: this.requestPath + "/updateFriendGroup",
            data
        });
    };

    /**
     * 删除好友分组
     * @param data
     * @returns {*}
     */
    static deleteFriendGroup = (data) => {
        return service.delete({
            url: this.requestPath + "/deleteFriendGroup",
            data
        });
    };

    /**
     * 添加好友分组
     * @param data
     * @returns {*}
     */
    static addFriendGroup = (data) => {
        return service.post({
            url: this.requestPath + "/addFriendGroup",
            data
        });
    };
}
