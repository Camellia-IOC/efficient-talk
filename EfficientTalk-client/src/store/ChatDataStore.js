import { defineStore } from "pinia";
import {
    computed,
    ref
} from "vue";
import SocialApi from "../api/modules/SocialApi.js";
import UserApi from "../api/modules/UserApi.js";
import { saveChatList } from "../database/chat-list.js";
import ChatApi from "../api/modules/ChatApi.js";
import { useCurLoginUserStore } from "./CurLoginUserStore.js";

// 当前登录用户信息全局配置
export const useChatDataStore = defineStore("chat-data-store", () => {
    // 用户基本信息
    const chatList = ref({
        vipList: [],
        commonList: []
    });

    // 当前登录用户
    const curLoginUserStore = useCurLoginUserStore();

    // 当前聊天对象
    const curChatId = ref("");
    const curSelectedChatInfo = ref(null)

    // 未读消息总数
    const totalUnreadCount = computed(() => {
        let count = 0;
        for (let i = 0; i < chatList.value.vipList.length; i++) {
            count += chatList.value.vipList[i].unreadCount;
        }
        for (let i = 0; i < chatList.value.commonList.length; i++) {
            count += chatList.value.commonList[i].unreadCount;
        }
        return count;
    });

    // 根据消息类型转换消息内容
    const translateMessageContent = (type, message) => {
        // 根据消息类型修改消息内容
        switch (type) {
            case "image":
                return "[图片]";
            case "file":
                return "[文件]";
        }
        return message;
    };

    // 保存聊天列表到服务器
    const saveCloudChatList = async (userId, chatListJson) => {
        // 保存聊天列表到服务器
        const response = await ChatApi.saveChatList({
            userId: userId,
            chatListJson: chatListJson
        });

        const res = response.data;
        if (res.code === 0) {
            console.log("保存聊天列表到服务器成功");
        }
        else {
            console.error("保存聊天列表到服务器失败");
        }
    };

    // 保存聊天列表
    const handleSaveChatList = () => {
        const chatListJson = JSON.stringify(chatList.value);
        // 保存至本地
        saveChatList(curLoginUserStore.curLoginUser.userId, chatListJson).then();
        // 保存至云端
        saveCloudChatList(curLoginUserStore.curLoginUser.userId, chatListJson).then();
    };

    /**
     * 接收消息
     * @param message 消息
     */
    const receiveMessage = async (message) => {
        // 获取发送人ID
        let receiverId;
        if (message.isGroup) {
            receiverId = message.receiver;
        }
        else {
            receiverId = message.sender;
        }

        // 遍历消息列表，修改相应的元素内容
        let existFlag = false;
        for (let i = 0; i < chatList.value.vipList.length; i++) {
            if (chatList.value.vipList[i].userId === receiverId) {
                chatList.value.vipList[i].lastMessage = translateMessageContent(message.type, message.content);
                chatList.value.vipList[i].lastMessageTime = message.time;

                // 如果发送消息的用户不是当前聊天对象，则增加未读消息数
                if (curChatId.value !== receiverId) {
                    chatList.value.vipList[i].unreadCount++;
                }

                // 将对应元素提到数组第一个
                chatList.value.vipList.unshift(chatList.value.vipList.splice(i, 1)[0]);
                existFlag = true;
                break;
            }
        }
        if (!existFlag) {
            for (let i = 0; i < chatList.value.commonList.length; i++) {
                if (chatList.value.commonList[i].userId === receiverId) {
                    chatList.value.commonList[i].lastMessage = translateMessageContent(message.type, message.content);
                    chatList.value.commonList[i].lastMessageTime = message.time;

                    // 如果发送消息的用户不是当前聊天对象，则增加未读消息数
                    if (curChatId.value !== receiverId) {
                        chatList.value.commonList[i].unreadCount++;
                    }

                    // 将对应元素提到数组第一个
                    chatList.value.commonList.unshift(chatList.value.commonList.splice(i, 1)[0]);
                    existFlag = true;
                    break;
                }
            }
        }

        // 如果不存在该用户，则添加到聊天列表中
        if (!existFlag) {
            let newMessage = {
                userId: message.sender,
                userName: null,
                userAvatar: null,
                lastMessage: message.content,
                lastMessageTime: message.time,
                unreadCount: 1,
                isGroup: message.isGroup,
                creator: null
            };

            if (message.isGroup) {
                newMessage.userId = message.receiver;

                const response = await SocialApi.getChatGroupBasicInfo({
                    groupId: message.receiver
                });

                const res = response.data;
                if (res.code === 0) {
                    newMessage.userName = res.data.groupName;
                    newMessage.creator = res.data.creator;
                }
            }
            else {
                // 获取用户基本信息
                await UserApi.getUserBasicInfo({
                    userId: message.sender
                }).then((response) => {
                    const res = response.data;
                    if (res.code === 0) {
                        const data = res.data;
                        if (data != null) {
                            newMessage.userName = data.userName;
                            newMessage.userAvatar = data.userAvatar;
                        }
                    }
                }).catch(() => {
                    console.error("获取用户基本信息失败");
                    newMessage.userName = "未知用户";
                    // TODO 默认头像需要替换
                    newMessage.userAvatar = "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";
                });
            }

            chatList.value.commonList.unshift(newMessage);
        }

        handleSaveChatList();
    };

    /**
     * 发送消息
     * @param message 消息
     */
    const sendMessage = (message) => {
        const receiver = message.receiver;

        // 遍历消息列表，修改相应的元素内容
        let existFlag = false;
        for (let i = 0; i < chatList.value.vipList.length; i++) {
            if (chatList.value.vipList[i].userId === receiver) {
                existFlag = true;
                // 根据消息类型修改消息内容
                chatList.value.vipList[i].lastMessage = translateMessageContent(message.type, message.content);
                chatList.value.vipList[i].lastMessageTime = message.time;
                // 将对应元素提到数组第一个
                chatList.value.vipList.unshift(chatList.value.vipList.splice(i, 1)[0]);
                break;
            }
        }
        if (!existFlag) {
            for (let i = 0; i < chatList.value.commonList.length; i++) {
                if (chatList.value.commonList[i].userId === receiver) {
                    existFlag = true;
                    // 根据消息类型修改消息内容
                    chatList.value.commonList[i].lastMessage = translateMessageContent(message.type, message.content);
                    chatList.value.commonList[i].lastMessageTime = message.time;
                    // 将对应元素提到数组第一个
                    chatList.value.commonList.unshift(chatList.value.commonList.splice(i, 1)[0]);
                    break;
                }
            }
        }

        if (!existFlag) {
            // TODO 转发消息后需要更新聊天列表
        }

        // 保存聊天列表至本地和云端
        handleSaveChatList();
    };

    return {
        chatList,
        curChatId,
        curSelectedChatInfo,
        totalUnreadCount,
        receiveMessage,
        sendMessage,
        handleSaveChatList,
        translateMessageContent
    };
});