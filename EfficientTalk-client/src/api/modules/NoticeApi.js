import { noticeService as service } from "../AxiosRequestManager.js";

export default class NoticeApi {
    // 请求前置路径
    static requestPath = "/api/notice";

    /**
     * 发布组织公告
     * @param data
     */
    static publishOrgNotice = (data) => {
        return service.post({
            url: this.requestPath + "/publishOrgNotice",
            data
        });
    };

    /**
     * 获取组织公告列表
     * @param data
     */
    static getOrgNoticeList = (data) => {
        return service.get({
            url: this.requestPath + "/getOrgNoticeList",
            data
        });
    };

    /**
     * 已读通知
     * @param data
     */
    static readOrgNotice = (data) => {
        return service.post({
            url: this.requestPath + "/readOrgNotice",
            data
        });
    };

    /**
     * 删除组织公告
     * @param data
     */
    static deleteOrgNotice = (data) => {
        return service.delete({
            url: this.requestPath + "/deleteOrgNotice",
            data
        });
    };

    /**
     * 获取组织公告已读记录
     * @param data
     */
    static getOrgNoticeReadHistory = (data) => {
        return service.get({
            url: this.requestPath + "/getOrgNoticeReadHistory",
            data
        });
    };
}