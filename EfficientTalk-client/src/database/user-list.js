import { db } from "./db.js";

/**
 * 获取登录过的用户列表
 */
export const getUserHistoryList = async () => {
    return await db.users.toArray();
};