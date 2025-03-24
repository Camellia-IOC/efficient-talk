import { defineStore } from "pinia";
import { ref } from "vue";

// 当前登录用户信息全局配置
export const useCurLoginUserStore = defineStore("cur-login-user-store", () => {
    // 用户基本信息
    const curLoginUser = ref({
        userId: "",
        userName: "",
        userAvatar: "",
        orgId: "",
        deptId: "",
        jobId: "",
    });

    // 应用启动时初始化用户数据
    const initUserData = (userData) => {
        curLoginUser.value.userId = userData.userId;
        curLoginUser.value.userName = userData.userName;
        curLoginUser.value.userAvatar = userData.userAvatar;
        curLoginUser.value.orgId = userData.orgId;
        curLoginUser.value.deptId = userData.deptId;
        curLoginUser.value.jobId = userData.jobId;
    };

    return {
        curLoginUser,
        initUserData,
    };
});