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

// 主窗口对象
let mainWindow = null;

// 子窗口管理器
let childWidowManager = {
    // AI聊天窗口
    aiAssistantWindow: null,
    // 历史记录窗口
    chatHistoryWindow: null,
};

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
    });

    // 程序启动后开启开发者工具
    // mainWindow.webContents.openDevTools();

    mainWindow.loadURL(projectUrl)
              .then();
};

// 新建子窗口
const openChildWindow = (params) => {
    if (childWidowManager[params.windowName] === null || childWidowManager[params.windowName] === undefined) {
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
        });

        childWindow.loadURL(projectUrl + "#" + params.url)
                   .then(); // hash路由

        // 程序启动后开启开发者工具
        childWindow.webContents.openDevTools();

        // 保存子窗口对象
        childWidowManager[params.windowName] = {
            windowObject: childWindow,
            config: params.config
        };
    }
};

app.commandLine.appendSwitch("--disable-http-cache");
app.whenReady()
   .then(() => {
       // 托盘图标功能
       const contextMenu = Menu.buildFromTemplate([
           {
               label: "Item1",
               type: "radio"
           },
           {
               label: "Item2",
               type: "radio"
           },
           {
               label: "Item3",
               type: "radio",
               checked: true
           },
           {
               label: "Item4",
               type: "radio"
           }
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

// 窗口相关操作 start ####################################################################################################
// 隐藏窗口
ipcMain.handle("window-hide", () => {
    mainWindow.hide();
});

// 显示窗口
ipcMain.handle("window-show", () => {
    mainWindow.show();
});

// 关闭窗口
ipcMain.handle("window-close", () => {
    mainWindow.close();
});

// 最小化窗口
ipcMain.handle("window-minimize", () => {
    mainWindow.minimize();
});

// 最大化窗口
ipcMain.handle("window-maximize", () => {
    mainWindow.maximize();
});

// 恢复窗口大小
ipcMain.handle("window-recover", () => {
    mainWindow.restore();
});
// 窗口相关操作 end ######################################################################################################

// 子窗口相关操作 start ###################################################################################################
// 打开子窗口
ipcMain.handle("child-window-open", (e, param) => {
    openChildWindow(param);
});

// 关闭子窗口
ipcMain.handle("child-window-close", (e, windowName) => {
    childWidowManager[windowName].windowObject.close();
    childWidowManager[windowName] = null;
});

// 最小化子窗口
ipcMain.handle("child-window-minimize", (e, windowName) => {
    childWidowManager[windowName].windowObject.minimize();
});

// 最大化子窗口
ipcMain.handle("child-window-maximize", (e, windowName) => {
    childWidowManager[windowName].windowObject.maximize();
});

// 恢复子窗口大小
ipcMain.handle("child-window-recover", (e, windowName) => {
    childWidowManager[windowName].windowObject.restore();
});

// 获取子窗口数据
ipcMain.handle("child-window-get-config", (e, windowName) => {
    return childWidowManager[windowName].config;
});
// 子窗口相关操作 end ###################################################################################################

// 业务功能 start ########################################################################################################
// 登录
ipcMain.handle("app-login", (e, param) => {
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
    });

    // 程序启动后开启开发者工具
    mainWindow.webContents.openDevTools();

    mainWindow.loadURL(projectUrl + "#/app")
              .then();
});

// 登出
ipcMain.handle("app-logout", (e, param) => {
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
    });

    // 程序启动后开启开发者工具
    // mainWindow.webContents.openDevTools();

    mainWindow.loadURL(projectUrl + "#/auth")
              .then();

    // TODO 主窗口关闭或退出登录时关闭所有子窗口
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