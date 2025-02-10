import { db } from "./db.js";

/**
 * 保存聊天列表
 * @param userId 所属用户id
 * @param chatList 聊天列表
 */
export const saveChatList = async (userId, chatList) => {
    await db.chatList.put({
        user_id: userId,
        chatList: chatList
    }).then(() => {
        console.log("保存聊天列表成功");
    }).catch(error => {
        console.log("保存聊天列表失败", error);
    });
};

/**
 * 获取聊天列表
 * @param userId
 * @return {Promise<*>} 聊天列表对象
 */
export const getChatList = async (userId) => {
    const chatList = await db.chatList.where("user_id").equals(userId).toArray();
    if (chatList.length > 0) {
        return JSON.parse(chatList[0].chatList);
    }
    return [];
};