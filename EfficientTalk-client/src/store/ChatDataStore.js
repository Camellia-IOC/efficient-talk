import { defineStore } from "pinia";
import {
    computed,
    ref
} from "vue";

// 当前登录用户信息全局配置
export const useChatDataStore = defineStore("chat-data-store", () => {
    // 用户基本信息
    const chatList = ref({
        vipList: [],
        commonList: []
    });

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

    return {
        chatList,
        totalUnreadCount
    };
});