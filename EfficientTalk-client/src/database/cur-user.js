import { db } from "./db.js";

/**
 * 保存当前登录的用户信息
 * @param userData 用户数据
 */
export const saveCurUserData = async (userData) => {
    // 保存登录记录
    await db.users.put({
        id: userData.userId,
        username: userData.userName,
        avatar: userData.userAvatar,
        org_id: userData.orgId,
        dept_id: userData.deptId,
        job_id: userData.jobId
    });

    // 记录当前登录的用户
    await db.curUser.put({
        key: "CUR_USER",
        user_id: userData.userId,
        username: userData.userName,
        avatar: userData.userAvatar,
        org_id: userData.orgId,
        dept_id: userData.deptId,
        job_id: userData.jobId
    });
};

/**
 * 获取当前登录的用户信息
 * @return {Promise<*>} 当前登录的用户信息
 */
export const getCurUserData = async () => {
    const curUser = await db.curUser.where("key").equals("CUR_USER").toArray();
    if (curUser.length > 0) {
        return {
            userId: curUser[0].user_id,
            userName: curUser[0].username,
            userAvatar: curUser[0].avatar,
            orgId: curUser[0].org_id,
            deptId: curUser[0].dept_id,
            jobId: curUser[0].job_id
        };
    }
    return null;
};