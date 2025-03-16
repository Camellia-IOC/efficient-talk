import OpenAI from "openai";

// API调用配置
const config = {
    model: "qwen-max",
    stream: true
};

// 智能助手OpenAI 实例
const AI = new OpenAI({
    apiKey: "sk-f84ddff43804489ca694b4732ee003d5",
    baseURL: "https://dashscope.aliyuncs.com/compatible-mode/v1",
    dangerouslyAllowBrowser: true
});

// 消息对象
class aiChatMessage {
    role;
    content;

    constructor(role, content) {
        this.role = role;
        this.content = content;
    }
}

/**
 * 发送消息
 * @param {aiChatMessage[]} messages 历史消息
 * @returns {Promise} 响应流
 */
export const askAiQuestion = async (messages) => {
    return AI.chat.completions.create({
        model: config.model,
        messages: messages,
        stream: config.stream,
        stream_options: {
            include_usage: true
        }
    });
};

/**
 * 小易帮写
 * @param message
 * @returns {Promise}
 */
export const writerHelperWork = async (message) => {
    const messages = [
        {
            role: "system",
            content: "你是一个写作专家，并且非常擅长和人交流，请你根据我之后提出的需求，帮助我撰写一段文本，语言表达要流畅且通俗易懂，直接回答文本内容即可，不需要Markdown格式，并且直接给出答案，不需要再询问"
        },
        {
            role: "user",
            content: message
        }
    ];
    return AI.chat.completions.create({
        model: config.model,
        messages: messages,
        stream: config.stream,
        stream_options: {
            include_usage: true
        }
    });
};

/**
 * 小易帮阅
 * @param message
 * @returns {Promise}
 */
export const textReaderHelperWork = async (message) => {
    const messages = [
        {
            role: "system",
            content: "你是一个阅读专家，非常擅长阅读文本材料并进行总结和解释，请你根据我提供的内容，帮我先总结后解释，语言表达要流畅且通俗易懂，在回答的内容中对重点进行突出显示，并且直接给出答案，不需要再询问"
        },
        {
            role: "user",
            content: message
        }
    ];
    return AI.chat.completions.create({
        model: config.model,
        messages: messages,
        stream: config.stream,
        stream_options: {
            include_usage: true
        }
    });
};

/**
 * 小易快速回复
 * @param message
 * @returns {Promise}
 */
export const replyHelperWork = async (message) => {
    const messages = [
        {
            role: "system",
            content: "你是一个交际达人，非常擅长和人交流，请你根据我提供的他人发送给我的内容，帮助我撰写内容的回复，语言表达要流畅且通俗易懂，并且直接给出我需要回复的内容，不需要再询问"
        },
        {
            role: "user",
            content: message
        }
    ];
    return AI.chat.completions.create({
        model: config.model,
        messages: messages,
        stream: config.stream,
        stream_options: {
            include_usage: true
        }
    });
};

