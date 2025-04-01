import Dexie from "dexie";

export const db = new Dexie("EfficientTalk");

db.version(1).stores({
    /**
     * 当前登录的用户信息
     * @param {string} key 唯一键
     * @param {string} id 用户id
     * @param {string} username 用户名
     * @param {string} avatar 用户头像
     * @param {string} orgId 组织id
     * @param {string} deptId 部门id
     * @param {string} jobId 职位id
     */
    curUser: "key, userId, username, avatar, orgId, deptId, jobId",

    /**
     * 登录过的用户信息
     * @param {string} id 用户id
     * @param {string} username 用户名
     * @param {string} avatar 用户头像
     * @param {string} orgId 组织id
     * @param {string} deptId 部门id
     * @param {string} jobId 职位id
     */
    users: "id, username, avatar, orgId, deptId, jobId",

    /**
     * 聊天列表
     * @param {string} userId 用户id
     * @param {string} chatList 聊天列表json串
     */
    chatList: "userId, chatList",

    /**
     * 聊天记录
     * @param {string} id 聊天记录id
     * @param {string} sender 发送者id
     * @param {string} senderAvatar 发送者头像
     * @param {string} senderName 发送者名称
     * @param {string} receiver 接收者id
     * @param {string} owner 消息所属用户id
     * @param {string} type 消息类型
     * @param {string} fileId 文件id
     * @param {string} fileName 文件名
     * @param {string} fileType 文件类型
     * @param {string} fileSize 文件大小
     * @param {string} content 消息内容
     * @param {string} time 时间
     */
    chatHistory: "id, sender, senderAvatar, senderName, receiver, owner, type, fileId, fileName, fileType, fileSize, content, time"
});