import { db } from "./db.js";

/**
 * 保存当前登录的用户信息
 * @param userId 用户id
 * @param userName 用户名
 * @param userAvatar 用户头像
 */
export const saveCurUserData = async (userId, userName, userAvatar) => {
    // 记录当前登录的用户
    await db.curUser.put({
        key: "CUR_USER",
        user_id: userId,
        username: userName,
        avatar: userAvatar
    }).then(() => {
        console.log("保存登录用户信息成功");
    }).catch(error => {
        console.log("保存登录用户信息失败", error);
    });

    // 保存登录记录
    await db.users.put({
        id: userId,
        username: userName,
        avatar: userAvatar
    }).then(() => {
        console.log("保存登录记录成功");
    }).catch(error => {
        console.log("保存登录记录失败", error);
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
            userAvatar: curUser[0].avatar
        };
    }
    return null;
};