import { db } from "./db.js";

/**
 * 保存聊天记录
 * @param record 聊天记录对象
 */
export const saveChatRecord = async (record) => {
    await db.chatHistory.put({
        id: record.id,
        sender: record.sender,
        receiver: record.receiver,
        type: record.type,
        content: record.content,
        time: record.time
    }).then(() => {
        console.log("保存聊天记录成功");
    }).catch(error => {
        console.log("保存聊天记录失败", error);
    });
};

/**
 * 获取聊天记录
 * @param friendId 好友id
 * @param userId 用户id
 * @returns {Promise<*>} 聊天记录数组
 */
export const getChatHistory = async (friendId, userId) => {
    const chatHistory = await db.chatHistory.where("[sender+receiver]")
                                .anyOf([[friendId, userId], [userId, friendId]])
                                .sortBy("time")
    if (chatHistory.length > 0) {
        return chatHistory;
    }
    return [];
};