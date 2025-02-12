import { defineStore } from "pinia";
import { ref } from "vue";
import { saveChatRecord } from "../database/chat-history.js";

// WebSocket全局配置
export const useWebSocketStore = defineStore("web-socket-store", () => {
    // 连接基本信息
    const url = ref("ws://localhost:18080/chat/");
    const socket = ref(null);

    // 应用启动时初始化用户数据
    const initSocket = (userId) => {
        socket.value = new WebSocket(url.value + userId);
        socket.value.onopen = () => {
            console.log("WebSocket连接成功");
        };
        socket.value.onmessage = (message) => {
            const messageData = JSON.parse(message.data);
            console.log("收到服务器消息：", messageData);

            // 保存聊天记录
            saveChatRecord(messageData).then();

            // 广播消息
            window.dispatchEvent(new CustomEvent("messageReceive", {
                detail: messageData
            }));
        };
        socket.value.onclose = () => {
            console.log("WebSocket连接已关闭");
        };
        socket.value.onerror = (error) => {
            console.error("WebSocket连接发生错误：", error);
        };
    };

    // 关闭WebSocket连接
    const closeSocket = () => {
        socket.value.close();
    }

    return {
        socket,
        initSocket,
        closeSocket
    };
});