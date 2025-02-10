import Dexie from "dexie";

export const db = new Dexie("EfficientTalk");
db.version(1).stores({
    /**
     * 当前登录的用户信息
     * @param {string} key 唯一键
     * @param {string} id 用户id
     * @param {string} username 用户名
     * @param {string} avatar 用户头像
     */
    curUser: "key, user_id, username, avatar",
    /**
     * 登录过的用户信息
     * @param {string} id 用户id
     * @param {string} username 用户名
     * @param {string} avatar 用户头像
     */
    users: "id, username, avatar",
    /**
     * 聊天列表
     * @param {string} user_id 用户id
     * @param {string} chat_list 聊天列表json串
     */
    chatList: "user_id, chat_list",
    /**
     * 聊天记录
     * @param {string} id 聊天记录id
     * @param {string} sender 发送者id
     * @param {string} receiver 接收者id
     * @param {string} type 消息类型
     * @param {string} content 消息内容
     * @param {string} time 时间
     */
    chatHistory: "id, sender, receiver, type, content, time, [sender+receiver]"
});