import { defineStore } from "pinia";
import { ref } from "vue";
import { useUserDataStore } from "./UserDataStore.js";

// WebSocket全局配置
export const useWebSocketStore = defineStore("web-socket-store", () => {
    // 连接基本信息
    const url = ref("ws://localhost:18080/chat/");
    const socket = ref(null);

    // 用户信息
    const userDataStore = useUserDataStore();

    // 应用启动时初始化用户数据
    const initSocket = () => {
        socket.value = new WebSocket(url.value + userDataStore.userId);
        socket.value.onopen = () => {
            console.log("WebSocket连接成功");
        };
        socket.value.onmessage = (message) => {
            const messageData = JSON.parse(message.data);
            console.log("收到服务器消息：", messageData);

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

    return {
        socket,
        initSocket,
    };
});