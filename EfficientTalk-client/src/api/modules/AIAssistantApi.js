import hmacSHA256 from "crypto-js/hmac-sha256";
import Base64 from "crypto-js/enc-base64";
import { useAiAssistantDataStore } from "../../store/AiAssistantDataStore.js";
import { ref } from "vue";

const aiAssistantDataStore = useAiAssistantDataStore();

// 定义WebSocket的URL
const AI_URL = aiAssistantDataStore.AI_URL;

// 你的应用ID和密钥信息，这些信息应该从讯飞开放平台控制台获取
const AI_APP_ID = aiAssistantDataStore.AI_APP_ID;
const AI_API_SECRET = aiAssistantDataStore.AI_API_SECRET;
const AI_API_KEY = aiAssistantDataStore.AI_API_KEY;

// AI回答内容
const aiAnswerResult = ref({
    role: "assistant",
    content: ""
});

// 获取鉴权url
const getRequestUrl = () => {
    const cur_time = new Date();
    const date = cur_time.toGMTString();

    let tmp = "host: " + "spark-api.xf-yun.com" + "\n";
    tmp += "date: " + date + "\n";
    tmp += "GET " + "/v1.1/chat" + " HTTP/1.1";

    // 使用crypto-js进行HMAC-SHA256加密
    const signature = Base64.stringify(hmacSHA256(tmp, AI_API_SECRET));

    // 构建authorization_origin字符串
    const authorization_origin = `api_key="${AI_API_KEY}", algorithm="hmac-sha256", headers="host date request-line", signature="${signature}"`;

    // 使用btoa()函数进行Base64编码
    const authorization = btoa(unescape(encodeURIComponent(authorization_origin)));

    // 构建查询参数对象
    const params = new URLSearchParams({
        authorization: authorization,
        date: date,
        host: "spark-api.xf-yun.com"
    });

    // 构建最终的URL
    return `${AI_URL}?${params.toString()}`;
};

export const sendMessage = (message) => {
    console.log(message);

    if (message !== null) {
        aiAssistantDataStore.chatHistory.push(message);
    }

    // 创建WebSocket连接
    aiAssistantDataStore.AiSocket = new WebSocket(getRequestUrl());

    // 当WebSocket连接打开时，发送鉴权信息
    aiAssistantDataStore.AiSocket.onopen = function (event) {
        // 构造鉴权信息
        const auth = {
            header: {
                app_id: AI_APP_ID
            },
            parameter: {
                chat: {
                    domain: "lite",
                    temperature: 0.5,
                    max_tokens: 1024,
                }
            },
            payload: {
                message: {
                    text: aiAssistantDataStore.chatHistory
                }
            }
        };

        console.log(JSON.stringify(auth));

        // 发送鉴权信息和请求数据
        aiAssistantDataStore.AiSocket.send(JSON.stringify(auth));
    };

    // 监听WebSocket消息事件，接收来自服务器的消息
    aiAssistantDataStore.AiSocket.onmessage = function (event) {
        const data = JSON.parse(event.data);
        console.log("Received data:", data);


        // 检查是否接收到完整的回复
        if (data.header.status === 0) {
            // 将空的新消息加入对话历史记录
            aiAnswerResult.value.content = "";
            aiAnswerResult.value.content = data.payload.choices.text[0].content;
        } else if (data.header.status === 1) {
            aiAnswerResult.value.content += data.payload.choices.text[0].content;
        } else if (data.header.status === 2) {
            aiAnswerResult.value.content += data.payload.choices.text[0].content;
            aiAssistantDataStore.chatHistory.push(aiAnswerResult.value);
        }
    };

    // 监听WebSocket错误事件
    aiAssistantDataStore.AiSocket.onerror = function (event) {
        console.error("WebSocket Error:", event);
    };

    // 监听WebSocket关闭事件
    aiAssistantDataStore.AiSocket.onclose = function (event) {
        console.log("WebSocket Closed:", event);
    };
};