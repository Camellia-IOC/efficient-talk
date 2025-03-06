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
                            // 云盘-最近文件
                            path: "recent-files",
                            name: "recent-files",
                            component: () => import("../views/EmptyView.vue")
                        },
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
                            component: () => import("../views/cloud-disk/OrgCloudDiskView.vue")
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
                    // 通知公告
                    path: "notification",
                    name: "notification",
                    component: () => import("../views/EmptyView.vue")
                },
                {
                    // 应用中心
                    path: "app-store",
                    name: "app-store",
                    component: () => import("../views/AppStoreView.vue")
                }
            ]
        },
        {
            // 子窗口模块
            path: "/child-window",
            name: "/child-window",
            component: () => import("../views/ChildWindowContainerView.vue"),
            children: [
                {
                    // 聊天记录
                    path: "child-window-chat-history",
                    name: "child-window-chat-history",
                    component: () => import("../views/child-window/ChatHistoryExplorerView.vue")
                },
                {
                    // AI助手
                    path: "child-window-ai-assistant",
                    name: "child-window-ai-assistant",
                    component: () => import("../views/child-window/AIAssistantView.vue")
                },
                {
                    // 文件预览
                    path: "child-window-file-preview",
                    name: "child-window-file-preview",
                    component: () => import("../views/child-window/FilePreviewView.vue")
                },
                {
                    // 媒体文件预览
                    path: "child-window-media-file-preview",
                    name: "child-window-media-file-preview",
                    component: () => import("../views/child-window/MediaFilePreviewView.vue")
                }
            ]
        },
        {
            // 应用窗口模块
            path: "/app-window",
            name: "/app-window",
            component: () => import("../views/AppWindowContainerView.vue")
        }
    ]
});

export default router;
