import Dexie from "dexie";

export const db = new Dexie("EfficientTalk");

db.version(1).stores({
    /**
     * 当前登录的用户信息
     * @param {string} key 唯一键
     * @param {string} id 用户id
     * @param {string} username 用户名
     * @param {string} avatar 用户头像
     * @param {string} org_id 组织id
     * @param {string} dept_id 部门id
     * @param {string} job_id 职位id
     */
    curUser: "key, user_id, username, avatar, org_id, dept_id, job_id",

    /**
     * 登录过的用户信息
     * @param {string} id 用户id
     * @param {string} username 用户名
     * @param {string} avatar 用户头像
     * @param {string} org_id 组织id
     * @param {string} dept_id 部门id
     * @param {string} job_id 职位id
     */
    users: "id, username, avatar, org_id, dept_id, job_id",

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
     * @param {string} file_id 文件id
     * @param {string} file_name 文件名
     * @param {string} file_type 文件类型
     * @param {string} file_size 文件大小
     * @param {string} content 消息内容
     * @param {string} time 时间
     */
    chatHistory: "id, sender, receiver, type, file_id, file_name, file_type, file_size, content, time, [sender+receiver]"
});