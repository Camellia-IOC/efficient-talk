import { defineStore } from "pinia";
import { ref } from "vue";

// 当前登录用户信息全局配置
export const useUserDataStore = defineStore("user-data-store", () => {
    // 用户基本信息
    const userId = ref("");
    const userName = ref("");
    const userAvatar = ref("");

    // 应用启动时初始化用户数据
    const initUserData = (userData) => {
        userId.value = userData.userId;
        userName.value = userData.userName;
        userAvatar.value = userData.avatar;
    };

    return {
        userId,
        userName,
        userAvatar,
        initUserData,
    };
});