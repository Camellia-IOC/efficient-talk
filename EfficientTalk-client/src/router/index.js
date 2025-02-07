import {
    createRouter,
    createWebHashHistory
} from "vue-router";
import IndexView from "../views/IndexView.vue";

const router = createRouter({
    // history: createWebHistory(import.meta.env.BASE_URL),
    // TODO:用了hash模式，之后可能会有问题
    history: createWebHashHistory(),
    routes: [
        {
            // 初始路由重定向
            path: "/",
            redirect: "/app"
        },
        // {
        //     // 登录模块
        //     path: "/auth",
        //     name: "auth",
        //     component: () => import("../views/AuthView.vue"),
        // },
        {
            // 主界面模块
            path: "/app",
            name: "app",
            component: IndexView,
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
                // {
                //     // 播客
                //     path: "podcasts",
                //     name: "podcasts",
                //     component: () => import("../views/EmptyView.vue")
                // },
                // {
                //     // 漫游
                //     path: "roaming",
                //     name: "roaming",
                //     component: () => import("../views/EmptyView.vue")
                // },
                // {
                //     // 动态
                //     path: "news",
                //     name: "news",
                //     component: () => import("../views/EmptyView.vue")
                // },
                // {
                //     // 我喜欢的音乐
                //     path: "my-favors",
                //     name: "my-favors",
                //     component: () => import("../views/MyFavorsView.vue")
                // },
                // {
                //     // 最近播放
                //     path: "recent-history",
                //     name: "recent-history",
                //     component: () => import("../views/EmptyView.vue")
                // },
                // {
                //     // 我的播客
                //     path: "my-podcasts",
                //     name: "my-podcasts",
                //     component: () => import("../views/EmptyView.vue")
                // },
                // {
                //     // 我的收藏
                //     path: "my-collections",
                //     name: "my-collections",
                //     component: () => import("../views/EmptyView.vue")
                // },
                // {
                //     // 下载管理
                //     path: "download-manager",
                //     name: "download-manager",
                //     component: () => import("../views/EmptyView.vue")
                // },
                // {
                //     // 本地音乐
                //     path: "local-music",
                //     name: "local-music",
                //     component: () => import("../views/EmptyView.vue")
                // },
                // {
                //     // 我的音乐云盘
                //     path: "my-cloud-disk",
                //     name: "my-cloud-disk",
                //     component: () => import("../views/EmptyView.vue")
                // },
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
