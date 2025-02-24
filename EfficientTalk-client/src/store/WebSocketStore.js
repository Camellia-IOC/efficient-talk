import { defineStore } from "pinia";
import { ref } from "vue";
import { saveChatRecord } from "../database/chat-history.js";

// WebSocket全局配置
export const useWebSocketStore = defineStore("web-socket-store", () => {
    // 连接基本信息
    const url = ref("ws://localhost:18080/chat/");
    const socket = ref(null);

    // 在线状态
    const onlineState = ref("OUTLINE");

    // 应用启动时初始化用户数据
    const initSocket = (userId) => {
        socket.value = new WebSocket(url.value + userId);
        socket.value.onopen = () => {
            console.log("WebSocket连接成功");
            switchOnlineState("ONLINE");
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
            switchOnlineState("OUTLINE");
        };
        socket.value.onerror = (error) => {
            console.error("WebSocket连接发生错误：", error);
            switchOnlineState("OUTLINE");
        };
    };

    // 关闭WebSocket连接
    const closeSocket = () => {
        if (socket.value !== null) {
            socket.value.close();
            socket.value = null;
        }
    };

    // 切换在线状态
    const switchOnlineState = (state) => {
        onlineState.value = state;

        if (state === "OUTLINE") {
            closeSocket();
        }
        else if (state === "ONLINE") {
            if (socket.value === null) {
                initSocket("1");
            }
        }
    };

    return {
        socket,
        onlineState,
        initSocket,
        closeSocket,
        switchOnlineState,
    };
});