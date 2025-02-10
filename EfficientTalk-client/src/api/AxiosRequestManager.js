import BaseAxiosInstance from "./BaseAxiosInstance.js";

// 服务器IP
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
/** 资源服务端口 */
const resourceServicePort = "18080";
/** 社交服务端口 */
const socialServicePort = "18080";
/** 留言板服务端口 */
const chatServicePort = "18080";

// 生成并导出axios实例
/** 认证服务实例 */
export const authService = new BaseAxiosInstance(serverIP, authServicePort, 10000);
/** 用户服务实例 */
export const userService = new BaseAxiosInstance(serverIP, userServicePort, 10000);
/** 资源服务实例 */
export const resourceService = new BaseAxiosInstance(serverIP, resourceServicePort, 10000);
/** 社交服务实例 */
export const socialService = new BaseAxiosInstance(serverIP, socialServicePort, 10000);
/** 留言板服务实例 */
export const chatService = new BaseAxiosInstance(serverIP, chatServicePort, 10000);