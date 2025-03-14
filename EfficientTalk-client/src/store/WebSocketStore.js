import { defineStore } from "pinia";
import { ref } from "vue";
import { saveChatRecord } from "../database/chat-history.js";
import UserApi from "../api/modules/UserApi.js";
import { message } from "ant-design-vue";

// WebSocket全局配置
export const useWebSocketStore = defineStore("web-socket-store", () => {
    // 连接基本信息
    const url = ref("ws://localhost:18080/chat/");
    /** @type {Ref<WebSocket>} */
    const socket = ref(null);
    const curLoginUserId = ref(null);

    // 在线状态
    const onlineState = ref("OUTLINE");

    // 应用启动时初始化用户数据
    const initSocket = (userId) => {
        curLoginUserId.value = userId;
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
            message.error("服务器连接已断开").then();
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

    // 发送消息
    const sendMessage = (message) => {
        if (socket.value !== null) {
            socket.value.send(JSON.stringify(message));
        }
    };

    // 切换在线状态
    const switchOnlineState = (state) => {
        // 如果当前状态与要切换的状态相同，则不执行任何操作
        if (state === onlineState.value) {
            return;
        }

        if (state === "OUTLINE") {
            closeSocket();
        }
        else if (state === "ONLINE") {
            if (socket.value === null) {
                initSocket(curLoginUserId.value);

                if (onlineState.value === "OUTLINE") {
                    message.success("连接服务器成功").then();
                }
            }
        }

        // 更新用户在线状态
        onlineState.value = state;
        UserApi.setUserOnlineState({
            userId: curLoginUserId.value,
            onlineState: state
        });
    };

    return {
        socket,
        onlineState,
        initSocket,
        closeSocket,
        sendMessage,
        switchOnlineState,
    };
});