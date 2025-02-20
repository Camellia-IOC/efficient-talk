const {
    contextBridge,
    ipcRenderer
} = require("electron");

// TODO:频道改成xxx-xxx格式,然后把前端中的调用方法封装一下
// 主窗口控制器
contextBridge.exposeInMainWorld("windowController", {
    hide: () => ipcRenderer.invoke("window-hide"),
    show: () => ipcRenderer.invoke("window-show"),
    close: () => ipcRenderer.invoke("window-close"),
    minimize: () => ipcRenderer.invoke("window-minimize"),
    maximize: () => ipcRenderer.invoke("window-maximize"),
    recover: () => ipcRenderer.invoke("window-recover"),
});

// 子窗口控制器
contextBridge.exposeInMainWorld("childWindowController", {
    open: (params) => ipcRenderer.invoke("child-window-open", params),
    close: (windowName) => ipcRenderer.invoke("child-window-close", windowName)
});

// 应用控制器
contextBridge.exposeInMainWorld("appController", {
    login: (params) => ipcRenderer.invoke("app-login", params),
    logout: (params) => ipcRenderer.invoke("app-logout", params)
});

// 文件选择器
contextBridge.exposeInMainWorld("fileSelector", {
    selectFile: (params) => ipcRenderer.invoke("select-file", params),
    selectFiles: (params) => ipcRenderer.invoke("select-files", params),
});

// 文件控制器
contextBridge.exposeInMainWorld("fileController",{
    checkIsExist: (path) => ipcRenderer.invoke("file-check-is-exist", path),
})

// 通知控制器
contextBridge.exposeInMainWorld("notificationController", {
    showNotification: (params) => ipcRenderer.invoke("show-notification", params),
});