// 窗口主体
const {
    app,
    BrowserWindow,
    session,
    dialog,
    Notification
} = require("electron");

// 托盘
const {
    Tray,
    Menu
} = require("electron");

// 异步通信
const {ipcMain} = require("electron");

// Node模块
const path = require("node:path");
const fs = require("node:fs");

// 会话ID
const sessionId = new Date().getTime().toString();

// 应用配置
const projectUrl = "http://localhost:5173/";
const iconPath = "./resources/logo.ico";

app.commandLine.appendSwitch("--disable-http-cache");
app.whenReady()
   .then(() => {
       // 托盘图标功能
       const contextMenu = Menu.buildFromTemplate([
           {
               label: "Item1",
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
        childWindow.webContents.openDevTools();

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
        transparent: true,
    });

    // 程序启动后开启开发者工具
    mainWindow.webContents.openDevTools();

    mainWindow.loadURL(projectUrl + "#/app")
              .then();
});

// 登出
ipcMain.handle("system-logout", (e, param) => {
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

// 处理文件选择(单选)
ipcMain.handle("select-file", async (event, params) => {
    const result = await dialog.showOpenDialog({
        properties: ["openFile", "multiSelections"],
        filters: params.filters,
        title: params.title,
    });

    if (result.canceled) {
        return [];
    }

    return result.filePaths;
});

// 处理文件选择(多选)
ipcMain.handle("select-files", async (event, params) => {
    const result = await dialog.showOpenDialog({
        properties: ["openFile", "multiSelections"],
        filters: params.filters,
        title: params.title,
    });

    if (result.canceled) {
        return [];
    }

    return result.filePaths;
});

// 判断文件是否存在
ipcMain.handle("file-check-is-exist", (event, path) => {
    return fs.existsSync(path);
});

// 显示通知
ipcMain.handle("show-notification", (event, params) => {
    new Notification({
        title: params.title,
        body: params.body,
        icon: params.icon,
    }).show();
});
// 业务功能 end ##########################################################################################################