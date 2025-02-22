import {
    createRouter,
    createWebHashHistory
} from "vue-router";
import AuthView from "../views/AuthView.vue";
import IndexView from "../views/IndexView.vue";

const router = createRouter({
    // history: createWebHistory(import.meta.env.BASE_URL),
    // TODO:用了hash模式，之后可能会有问题
    history: createWebHashHistory(),
    routes: [
        {
            // 初始路由重定向
            path: "/",
            redirect: "/auth"
        },
        {
            // 登录模块
            path: "/auth",
            name: "auth",
            component: AuthView
        },
        {
            // 主界面模块
            path: "/app",
            name: "app",
            component: IndexView,
            redirect: {
                name: "chat"
            },
            children: [
                {
                    // 消息
                    path: "chat",
                    name: "chat",
                    component: () => import("../views/ChatView.vue")
                },
                {
                    // 通讯录
                    path: "contacts",
                    name: "contacts",
                    component: () => import("../views/ContactsView.vue")
                },
                {
                    // 云盘
                    path: "cloud-disk",
                    name: "cloud-disk",
                    component: () => import("../views/CloudDiskView.vue"),
                    children: [
                        {
                            // 云盘-我的文件
                            path: "my-files",
                            name: "my-files",
                            component: () => import("../views/EmptyView.vue")
                        },
                        {
                            // 云盘-组织文件
                            path: "org-files",
                            name: "org-files",
                            component: () => import("../views/EmptyView.vue")
                        },
                        {
                            // 云盘-聊天文件
                            path: "chat-files",
                            name: "chat-files",
                            component: () => import("../views/cloud-disk/ChatCloudDiskView.vue")
                        }
                    ]
                },
                {
                    // 应用中心
                    path: "app-store",
                    name: "app-store",
                    component: () => import("../views/EmptyView.vue")
                }
            ]
        },
        // {
        //     // 用户个人信息页
        //     path: "/user-home",
        //     name: "user-home",
        //     component: () => import("../views/UserHomeView.vue")
        // },
        // {
        //     path: "/ai-assistant",
        //     name: "ai-assistant",
        //     component: () => import("../views/AiAssistantView.vue")
        //
        // }
    ]
});

export default router;
