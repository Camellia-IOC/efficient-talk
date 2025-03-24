import BaseAxiosInstance from "./BaseAxiosInstance.js";

/**
 * 服务器IP
 * @return local 127.0.0.1
 * @return cloud 47.122.40.126
 */
const serverIP = "127.0.0.1";
// const serverIP = "47.122.40.126";

// 微服务端口
/** 认证服务端口 */
const authServicePort = "18080";
/** 用户服务端口 */
const userServicePort = "18080";
/** 云盘服务端口 */
const cloudDiskServicePort = "18080";
/** 社交服务端口 */
const socialServicePort = "18080";
/** 聊天服务端口 */
const chatServicePort = "18080";
/** 应用商店服务端口 */
const appStoreServicePort = "18080";
/** AI服务端口 */
const aiServicePort = "18080";
/** 通知待办服务端口 */
const noticeServicePort = "18080";

// 生成并导出axios实例
/** 认证服务实例 */
export const authService = new BaseAxiosInstance(serverIP, authServicePort, 10000);
/** 用户服务实例 */
export const userService = new BaseAxiosInstance(serverIP, userServicePort, 10000);
/** 云盘服务实例 */
export const cloudDiskService = new BaseAxiosInstance(serverIP, cloudDiskServicePort, 10000);
/** 社交服务实例 */
export const socialService = new BaseAxiosInstance(serverIP, socialServicePort, 10000);
/** 聊天服务实例 */
export const chatService = new BaseAxiosInstance(serverIP, chatServicePort, 10000);
/** 应用商店服务实例 */
export const appStoreService = new BaseAxiosInstance(serverIP, appStoreServicePort, 10000);
/** AI服务实例 */
export const aiService = new BaseAxiosInstance(serverIP, aiServicePort, 10000);
/** 通知待办服务实例 */
export const noticeService = new BaseAxiosInstance(serverIP, noticeServicePort, 10000);