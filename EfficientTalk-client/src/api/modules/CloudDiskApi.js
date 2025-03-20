import { cloudDiskService as service } from "../AxiosRequestManager.js";

export default class CloudDiskApi {
    // 请求前置路径
    static requestPath = "/api/cloudDisk";

    /**
     * 获取云盘基础信息
     * @param data
     */
    static getCloudDiskBasicInfo = (data) => {
        return service.get({
            url: this.requestPath + "/getCloudDiskBasicInfo",
            data
        });
    };

    /**
     * 按层级获取云盘内容
     * @param data
     */
    static getCloudDiskLevelContent = (data) => {
        return service.get({
            url: this.requestPath + "/getCloudDiskLevelContent",
            data
        });
    };

    /**
     * 上传组织云盘文件
     * @param data
     */
    static uploadOrgCloudDiskFile = (data) => {
        return service.upload({
            url: this.requestPath + "/uploadOrgCloudDiskFile",
            data
        });
    };

    /**
     * 创建组织云盘文件夹
     * @param data
     */
    static createOrgCloudDiskFolder = (data) => {
        return service.post({
            url: this.requestPath + "/createOrgCloudDiskFolder",
            data
        });
    };

    /**
     * 删除组织云盘文件夹
     * @param data 请求数据
     */
    static deleteOrgCloudDiskFolder = (data) => {
        return service.delete({
            url: this.requestPath + "/deleteOrgCloudDiskFolder",
            data
        });
    };

    /**
     * 删除组织云盘文件
     * @param data 请求数据
     */
    static deleteOrgCloudDiskFile = (data) => {
        return service.delete({
            url: this.requestPath + "/deleteOrgCloudDiskFile",
            data
        });
    };

    /**
     * 重命名组织云盘文件
     * @param data 请求数据
     */
    static renameOrgCloudDiskFile = (data) => {
        return service.put({
            url: this.requestPath + "/renameOrgCloudDiskFile",
            data
        });
    };

    /**
     * 重命名组织云盘文件夹
     * @param data
     */
    static renameOrgCloudDiskFolder = (data) => {
        return service.put({
            url: this.requestPath + "/renameOrgCloudDiskFolder",
            data
        });
    };

    /**
     * 获取云盘文件
     * @param data
     */
    static getCloudDiskFileBlob = (data) => {
        return service.download({
            url: this.requestPath + "/getCloudDiskFileBlob",
            data
        });
    };
}