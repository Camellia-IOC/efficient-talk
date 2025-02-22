import dayjs from "dayjs";
import isToday from "dayjs/plugin/isToday";
import isYesterday from "dayjs/plugin/isYesterday";

// 扩展dayjs插件
dayjs.extend(isToday);
dayjs.extend(isYesterday);

/**
 * 格式化消息时间
 * @param time 时间戳或日期字符串
 * @param position 所属位置
 * @returns {string} 格式化后的时间字符串
 */
export const formatMessageTime = (time, position) => {
    // 检查传入参数是否为空或无效
    if (!time) {
        return "";
    }

    const messageTime = dayjs(time);
    const curTime = dayjs();

    // 无效时间处理
    if (!messageTime.isValid()) {
        return "非法日期";
    }

    // 判断是否是当日的消息
    if (messageTime.isToday()) {
        return messageTime.format("HH:mm");
    }

    // 判断是否是昨天的消息
    if (messageTime.isYesterday()) {
        if (position === "chat-list") {
            return "昨天";
        }
        if (position === "chat-detail") {
            return "昨天 " + messageTime.format("HH:mm");
        }
    }

    // 判断是否是近7天的消息
    if (curTime.diff(messageTime, "day") <= 7) {
        const dayWeek = messageTime.day();
        const weekNames = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
        return weekNames[dayWeek];
    }

    // 判断是否是今年的消息
    if (curTime.isSame(messageTime, "year")) {
        return messageTime.format("MM-DD");
    }

    // 其他情况（往年消息）
    return messageTime.format("YYYY-MM-DD");
};

/**
 * 格式化日期
 * @param time 时间戳或日期字符串
 * @returns {string} 格式化后的日期字符串
 */
export const formatDate = (time) => {
    const formatTime = dayjs(time);
    return formatTime.format("YYYY-MM-DD HH:mm:ss");
}