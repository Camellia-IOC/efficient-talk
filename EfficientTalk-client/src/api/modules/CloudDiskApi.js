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

    /**
     * 上传组织云盘文件
     * @param data
     */
    static uploadOrgCloudDiskFile = (data) => {
        return cloudDiskService.upload({
            url: this.requestPath + "/uploadOrgCloudDiskFile",
            data
        });
    };

    /**
     * 创建组织云盘文件夹
     * @param data
     */
    static createOrgCloudDiskFolder = (data) => {
        return cloudDiskService.post({
            url: this.requestPath + "/createOrgCloudDiskFolder",
            data
        });
    };
}