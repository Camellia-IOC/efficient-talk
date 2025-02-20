import { db } from "./db.js";

/**
 * 保存当前登录的用户信息
 * @param userId 用户id
 * @param userName 用户名
 * @param userAvatar 用户头像
 * @param orgId 组织id
 * @param deptId 部门id
 * @param jobId 职位id
 */
export const saveCurUserData = async (userId, userName, userAvatar, orgId, deptId, jobId) => {
    // 保存登录记录
    await db.users.put({
        id: userId,
        username: userName,
        avatar: userAvatar,
        org_id: orgId,
        dept_id: deptId,
        job_id: jobId
    });

    // 记录当前登录的用户
    await db.curUser.put({
        key: "CUR_USER",
        user_id: userId,
        username: userName,
        avatar: userAvatar,
        org_id: orgId,
        dept_id: deptId,
        job_id: jobId
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