import { defineStore } from "pinia";
import { ref } from "vue";

// TODO:解决多窗口不共享问题
export const useUserDataStore = defineStore("user-data-store", () => {
    // 是否登录
    const isLogin = ref(false);

    // 用户基本信息
    const userId = ref();
    const userName = ref();
    const userAvatar = ref();
    const isVip = ref();

    // 设置登录状态
    const setLoginState = (state) => {
        if (state === true) {
            isLogin.value = true;
            window.localStorage.setItem("isLogin", "true");
        } else {
            isLogin.value = false;
            window.localStorage.setItem("isLogin", "false");
        }
    };

    // 应用启动时初始化用户数据
    const initUserData = (userData) => {
        userId.value = userData.userId;
        userName.value = userData.userName;
        userAvatar.value = userData.userAvatar;
        isVip.value = userData.isVIP;
    };

    return {
        isLogin,
        userId,
        userName,
        userAvatar,
        isVip,
        setLoginState,
        initUserData,
    };
});