// 窗口主体
const {
    app,
    BrowserWindow,
    session
} = require("electron");

// 托盘
const {
    Tray,
    Menu,
    nativeImage
} = require("electron");

// 异步通信
const {ipcMain} = require("electron");

// const {useWebSocketStore} = require("../src/store/WebSocketStore.js");
// const websocketStore = useWebSocketStore();

const path = require("node:path");

const sessionId = "1";

// 项目地址
const projectUrl = "http://localhost:5173/";

// 主窗口对象
let mainWindow = null;

// 子窗口管理器
let childWidowManager = {
    userAuthWindow: null,
    aiAssistantWindow: null
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
    if (childWidowManager[params.name] === null) {
        let childWindow = new BrowserWindow({
            width: params.width,
            height: params.height,
            minWidth: params.width,
            minHeight: params.height,
            maxWidth: params.width,
            maxHeight: params.height,
            parent: mainWindow,
            webPreferences: {
                preload: path.join(__dirname, "preload.cjs")
            },
            frame: false
        });

        childWindow.loadURL(projectUrl + "#" + params.url)
                   .then(); // hash路由

        // 程序启动后开启开发者工具
        childWindow.webContents.openDevTools();

        childWidowManager[params.name] = childWindow;
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
       const icon = nativeImage.createFromPath("./src/assets/icon.png");
       let tray = new Tray(icon);
       tray.setContextMenu(contextMenu);
       tray.setToolTip("测试应用");
       tray.setTitle("This is my title");

       // 创建窗口
       createWindow();
   });

// 关闭应用程序
app.on("window-all-closed", () => {
    if (process.platform !== "darwin") {
        // websocketStore.socket.close();
        app.quit();
    }
});

// 窗口相关操作
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

// 子窗口相关操作
// 打开子窗口
ipcMain.handle("child-window-open", (e, param) => {
    openChildWindow(param);
});

// 关闭子窗口
ipcMain.handle("child-window-close", (e, windowName) => {
    childWidowManager[windowName].close();
    childWidowManager[windowName] = null;
    // childWidowManager[windowName].hide();
});

// 业务功能
ipcMain.handle("app-login", (e, param) => {
    mainWindow.close();
    mainWindow = new BrowserWindow({
        width: 1060,
        height: 760,
        minWidth: 1060,
        minHeight: 760,
        webPreferences: {
            preload: path.join(__dirname, "preload.cjs"),
            session: session.fromPartition(`persist:${sessionId}`)
        },
        // 是否使用自带标题栏
        frame: false,
    });

    // 程序启动后开启开发者工具
    mainWindow.webContents.openDevTools();

    mainWindow.loadURL(projectUrl + "#/app/chat")
              .then();
});

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
        // 是否使用自带标题栏
        frame: false,
        resizable: false,
    });

    // 程序启动后开启开发者工具
    // mainWindow.webContents.openDevTools();

    mainWindow.loadURL(projectUrl + "#/auth")
              .then();
});