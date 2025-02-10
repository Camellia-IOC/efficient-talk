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
const authServicePort = "1314";
/** 用户服务端口 */
const userServicePort = "1314";
/** 资源服务端口 */
const resourceServicePort = "1314";
/** 社交服务端口 */
const socialServicePort = "1314";
/** 留言板服务端口 */
const messageBoardServicePort = "1314";

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
export const messageBoardService = new BaseAxiosInstance(serverIP, messageBoardServicePort, 10000);