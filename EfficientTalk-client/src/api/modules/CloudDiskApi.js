import { cloudDiskService } from "../AxiosRequestManager.js";

export default class CloudDiskApi {
    // 请求前置路径
    static requestPath = "/api/cloudDisk";

    /**
     * 获取云盘基础信息
     * @param data
     */
    static getCloudDiskBasicInfo = (data) => {
        return cloudDiskService.get({
            url: this.requestPath + "/getCloudDiskBasicInfo",
            data
        });
    };

    /**
     * 按层级获取云盘内容
     * @param data
     */
    static getCloudDiskLevelContent = (data) => {
        return cloudDiskService.get({
            url: this.requestPath + "/getCloudDiskLevelContent",
            data
        });
    };
}