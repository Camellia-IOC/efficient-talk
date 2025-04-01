import { db } from "./db.js";
import { getCurrentTime } from "../utils/time-utils.js";

/**
 * 保存聊天记录
 * @param record 聊天记录对象
 * @param userId 用户id
 */
export const saveChatRecord = async (record, userId) => {
    console.error("聊天记录", record);
    await db.chatHistory.put({
        id: record.id,
        sender: record.sender,
        senderAvatar: record.senderAvatar,
        senderName: record.senderName,
        receiver: record.receiver,
        owner: userId,
        type: record.type,
        fileId: record.fileId,
        fileName: record.fileName,
        fileType: record.fileType,
        fileSize: record.fileSize,
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
 * @param isGroup 是否为群聊
 * @param lastTime 最后一条消息的时间
 * @param pageSize 每页大小
 * @returns {Promise<*>} 聊天记录数组
 */
export const getChatHistory = async (friendId, userId, isGroup, lastTime, pageSize) => {
    let chatHistory;
    let pageStartTime = lastTime === null ? getCurrentTime() : lastTime;
    if (isGroup) {
        chatHistory = await db.chatHistory
                              .orderBy("time")
                              .filter((record) => record.owner === userId && record.receiver === friendId && record.time < pageStartTime)
                              .limit(pageSize)
                              .sortBy("time");
    }
    else {
        chatHistory = await db.chatHistory
                              .orderBy("time")
                              .filter((record) => record.owner === userId && (record.sender === friendId && record.receiver === userId) || (record.sender === userId && record.receiver === friendId) && record.time < pageStartTime)
                              .limit(pageSize)
                              .sortBy("time");
    }
    if (chatHistory.length > 0) {
        return chatHistory;
    }
    return [];
};