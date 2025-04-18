// Electron功能模块
import {
    app,
    BrowserWindow,
    session,
    clipboard,
    Tray,
    Menu,
    ipcMain,
    Notification,
    nativeImage,
    dialog,
    shell
} from "electron";

// Node模块
import path from "node:path";
import { fileURLToPath } from "url";

// 持久化存储
import Store from "electron-store";

// 文件路径
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// 持久化存储仓库
const configStore = new Store();
// 仓库键表
const storeKeys = {
    users: "users",
    systemSettingConfig: "systemSettingConfig",
};
const defaultSystemSettingConfig = {
    // 系统通知模块
    moduleNotice: {
        enable: true,
    },
    // 存储模块
    moduleCache: {
        localCache: true,
    },
    // 文件模块
    moduleFile: {
        downloadPath: path.join(app.getPath("downloads"), "易飞讯")
    },
    moduleAuth: {
        autoLogin: false,
        acceptLicense: false
    }
};
const initStore = () => {
    if (!configStore.has(storeKeys.users)) {
        configStore.set(storeKeys.users, {});
    }
};

// 会话ID
const sessionId = new Date().getTime().toString();

// 应用配置
const projectUrl = "http://localhost:5173/";
const iconPath = "./resources/logo.ico";

app.commandLine.appendSwitch("--disable-http-cache");
app.whenReady()
   .then(() => {
       // 初始化仓库
       initStore();

       // 设置应用名称
       app.setAppUserModelId("易飞讯");

       // 托盘图标功能
       const contextMenu = Menu.buildFromTemplate([
           {
               label: "退出",
               type: "radio",
               checked: true
           },
       ]);
       const icon = path.join(__dirname, "./resources/logo.ico");
       let tray = new Tray(icon);
       tray.setContextMenu(contextMenu);
       tray.setToolTip("易飞讯");
       tray.setTitle("This is my title");

       // 创建窗口
       createWindow();
   });

// 关闭应用程序
app.on("window-all-closed", () => {
    if (process.platform !== "darwin") {
        app.quit();
    }
});

// 主窗口相关操作 start ###################################################################################################
// 主窗口对象
let mainWindow = null;

// 下载文件名称映射
let downloadFileNameMap = new Map();

// 创建主窗口
const createWindow = () => {
    mainWindow = new BrowserWindow({
        width: 340,
        height: 520,
        minWidth: 340,
        minHeight: 520,
        webPreferences: {
            preload: path.join(__dirname, "preload.cjs"),
            session: session.fromPartition(`persist:${sessionId}`)
        },
        icon: path.join(__dirname, iconPath),
        // 禁用大小调节
        resizable: false,
        // 是否使用自带标题栏
        frame: false,
        transparent: true,
    });

    // 程序启动后开启开发者工具
    // mainWindow.webContents.openDevTools();

    mainWindow.loadURL(projectUrl)
              .then();

    // 监听下载事件
    mainWindow.webContents.session.on("will-download", (event, item, webContents) => {
        // 匹配uuid，获取文件名称
        const regex = /\/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})(?=\.[^/]+|$)/i;
        const match = item.getURL().match(regex);
        const fileId = match ? match[1] : null;
        const fileName = downloadFileNameMap.get(fileId);
        downloadFileNameMap.delete(fileId);

        // TODO 修改为通用下载路径
        item.setSavePath(path.join(configStore.get(storeKeys.systemSettingConfig).moduleFile.downloadPath, fileName));

        item.on("updated", (event, state) => {
            if (state === "interrupted") {
                console.log("下载已中断，但可以恢复");
            }
            else if (state === "progressing") {
                if (item.isPaused()) {
                    console.log("下载已暂停");
                }
                else {
                    console.log(`Received bytes: ${item.getReceivedBytes()}`);
                }
            }
        });
        item.once("done", (event, state) => {
            if (state === "completed") {
                console.log("下载成功");
            }
            else {
                console.log(`下载失败: ${state}`);
            }
        });
    });
};

// 隐藏主窗口
ipcMain.handle("window-hide", () => {
    mainWindow.hide();
});

// 显示主窗口
ipcMain.handle("window-show", () => {
    mainWindow.show();
});

// 关闭主窗口
ipcMain.handle("window-close", () => {
    mainWindow.close();
});

// 最小化主窗口
ipcMain.handle("window-minimize", () => {
    mainWindow.minimize();
});

// 最大化主窗口
ipcMain.handle("window-maximize", () => {
    mainWindow.maximize();
});

// 恢复主窗口大小
ipcMain.handle("window-recover", () => {
    mainWindow.unmaximize();
});
// 主窗口相关操作 end #####################################################################################################

// 子窗口相关操作 start ###################################################################################################
// 子窗口管理器
let childWindowManager = {
    // AI聊天窗口
    aiAssistantWindow: null,
    // 历史记录窗口
    chatHistoryWindow: null,
};

// 新建子窗口
const openChildWindow = (params) => {
    if (childWindowManager[params.windowName] === null || childWindowManager[params.windowName] === undefined) {
        let childWindow = new BrowserWindow({
            width: params.width,
            height: params.height,
            parent: params.isChild ? mainWindow : null,
            webPreferences: {
                webSecurity: false,
                nodeIntegration: true,
                preload: path.join(__dirname, "preload.cjs"),
                session: session.fromPartition(`persist:${sessionId}`)
            },
            icon: path.join(__dirname, iconPath),
            // 禁用大小调节
            resizable: false,
            // 是否使用自带标题栏
            frame: false,
            transparent: true,
        });

        childWindow.loadURL(projectUrl + "#" + params.url)
                   .then();

        // 程序启动后开启开发者工具
        // childWindow.webContents.openDevTools();

        // 保存子窗口对象
        childWindowManager[params.windowName] = {
            windowObject: childWindow,
            config: params.config
        };
    }
    else {
        childWindowManager[params.windowName].windowObject.show();
    }
};

// 打开子窗口
ipcMain.handle("child-window-open", (e, param) => {
    openChildWindow(param);
});

// 关闭子窗口
ipcMain.handle("child-window-close", (e, windowName) => {
    childWindowManager[windowName].windowObject.close();
    childWindowManager[windowName] = null;
});

// 最小化子窗口
ipcMain.handle("child-window-minimize", (e, windowName) => {
    childWindowManager[windowName].windowObject.minimize();
});

// 最大化子窗口
ipcMain.handle("child-window-maximize", (e, windowName) => {
    childWindowManager[windowName].windowObject.maximize();
});

// 恢复子窗口大小
ipcMain.handle("child-window-recover", (e, windowName) => {
    childWindowManager[windowName].windowObject.unmaximize();
});

// 获取子窗口数据
ipcMain.handle("child-window-get-config", (e, windowName) => {
    return childWindowManager[windowName].config;
});
// 子窗口相关操作 end #####################################################################################################

// 应用窗口相关操作 start ##################################################################################################
// 应用窗口管理器
let appWindowManager = {};

// 新建应用窗口
const openAppWindow = (params) => {
    if (appWindowManager[params.appId] === null || appWindowManager[params.appId] === undefined) {
        let appWindow = new BrowserWindow({
            width: params.width,
            height: params.height,
            parent: params.isChild ? mainWindow : null,
            webPreferences: {
                webSecurity: false,
                nodeIntegration: true,
                preload: path.join(__dirname, "preload.cjs"),
                session: session.fromPartition(`persist:${sessionId}`)
            },
            icon: path.join(__dirname, iconPath),
            // 禁用大小调节
            resizable: false,
            // 是否使用自带标题栏
            frame: false,
            transparent: true,
        });

        appWindow.loadURL(projectUrl + "#" + `/app-window?appId=${params.appId}`)
                 .then(); // hash路由

        // 程序启动后开启开发者工具
        appWindow.webContents.openDevTools();

        // 保存子窗口对象
        appWindowManager[params.appId] = {
            windowObject: appWindow,
            config: params.config
        };
    }
    else {
        appWindowManager[params.appId].windowObject.show();
    }
};

// 打开应用窗口
ipcMain.handle("app-window-open", (e, param) => {
    openAppWindow(param);
});

// 关闭应用窗口
ipcMain.handle("app-window-close", (e, appId) => {
    appWindowManager[appId].windowObject.close();
    appWindowManager[appId] = null;
});

// 最小化应用窗口
ipcMain.handle("app-window-minimize", (e, appId) => {
    appWindowManager[appId].windowObject.minimize();
});

// 最大化应用窗口
ipcMain.handle("app-window-maximize", (e, appId) => {
    appWindowManager[appId].windowObject.maximize();
});

// 恢复应用窗口大小
ipcMain.handle("app-window-recover", (e, appId) => {
    appWindowManager[appId].windowObject.unmaximize();
});

// 获取应用窗口数据
ipcMain.handle("app-window-get-config", (e, appId) => {
    return appWindowManager[appId].config;
});
// 应用窗口相关操作 end ###################################################################################################

// 业务功能 start ########################################################################################################
// 关闭所有窗口
const closeAllWindow = () => {
    // 退出登录时关闭所有子窗口
    Object.keys(childWindowManager).forEach(windowName => {
        if (childWindowManager[windowName] !== null) {
            childWindowManager[windowName].windowObject.close();
            childWindowManager[windowName] = null;
        }
    });

    // 退出登录时关闭所有应用窗口
    Object.keys(appWindowManager).forEach(appId => {
        if (appWindowManager[appId] !== null) {
            appWindowManager[appId].windowObject.close();
            appWindowManager[appId] = null;
        }
    });
};

// 登录
ipcMain.handle("system-login", (e, param) => {
    mainWindow.close();
    mainWindow = new BrowserWindow({
        width: 1060,
        height: 760,
        minWidth: 1060,
        minHeight: 760,
        webPreferences: {
            webSecurity: false,
            nodeIntegration: true,
            preload: path.join(__dirname, "preload.cjs"),
            session: session.fromPartition(`persist:${sessionId}`)
        },
        icon: path.join(__dirname, iconPath),
        // 是否使用自带标题栏
        frame: false,
        resizable: true,
        transparent: true,
    });

    // 程序启动后开启开发者工具
    mainWindow.webContents.openDevTools();

    mainWindow.loadURL(projectUrl + "#/app")
              .then();
});

// 登出
ipcMain.handle("system-logout", (e, param) => {
    // 关闭所有窗口
    closeAllWindow();

    mainWindow.close();
    mainWindow = new BrowserWindow({
        width: 340,
        height: 520,
        minWidth: 340,
        minHeight: 520,
        webPreferences: {
            preload: path.join(__dirname, "preload.cjs"),
            session: session.fromPartition(`persist:${sessionId}`)
        },
        icon: path.join(__dirname, iconPath),
        // 是否使用自带标题栏
        frame: false,
        resizable: false,
        transparent: true,
    });

    // 程序启动后开启开发者工具
    // mainWindow.webContents.openDevTools();

    mainWindow.loadURL(projectUrl + "#/auth")
              .then();
});

// 复制到剪贴板
ipcMain.handle("copy-to-clipboard", (event, content) => {
    clipboard.writeText(content);
});

//从剪贴板读取内容
ipcMain.handle("read-from-clipboard", () => {
    return clipboard.readText();
});

// 下载文件
ipcMain.handle("download", (event, params) => {
    const fileName = params.fileName + "." + params.fileType;
    downloadFileNameMap.set(params.fileId, fileName);
    mainWindow.webContents.downloadURL(params.url);
});

// 显示通知
ipcMain.handle("show-notification", (event, params) => {
    if (configStore.get(`${storeKeys.users}.${params.userId}.${storeKeys.systemSettingConfig}`).moduleNotice.enable) {
        new Notification({
            title: params.title,
            body: params.body,
            icon: nativeImage.createFromPath(path.join(__dirname, iconPath))
        }).show();
    }
});

// 从云端加载用户系统设置
ipcMain.handle("load-system-setting-config", (event, params) => {
    if (params.config === null) {
        configStore.set(`${storeKeys.users}.${params.userId}.${storeKeys.systemSettingConfig}`, defaultSystemSettingConfig);
        return defaultSystemSettingConfig;
    }
    else {
        configStore.set(`${storeKeys.users}.${params.userId}.${storeKeys.systemSettingConfig}`, params.config);
    }
});

// 获取系统设置
ipcMain.handle("get-system-setting-config", (event, userId) => {
    return configStore.get(`${storeKeys.users}.${userId}.${storeKeys.systemSettingConfig}`);
});

// 获取指定的系统设置
ipcMain.handle("get-assigned-system-setting-config", (event, params) => {
    return configStore.get(`${storeKeys.users}.${params.userId}.${storeKeys.systemSettingConfig}.${params.module}.${params.key}`);
});

// 保存系统设置
ipcMain.handle("set-system-setting-config", (event, config) => {
    configStore.set(`${storeKeys.users}.${config.userId}.${storeKeys.systemSettingConfig}.${config.module}.${config.key}`, config.value);
});

// 选择系统路径
ipcMain.handle("select-system-path", (event) => {
    return dialog.showOpenDialogSync(mainWindow, {
        title: "选择路径",
        properties: ["openDirectory"]
    });
});

// 打开外部链接
ipcMain.handle("open-external-link", (event, url) => {
    shell.openExternal(url).then();
});
// 业务功能 end ##########################################################################################################