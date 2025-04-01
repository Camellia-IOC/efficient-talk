const {
    contextBridge,
    ipcRenderer
} = require("electron");

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
    close: (windowName) => ipcRenderer.invoke("child-window-close", windowName),
    minimize: (windowName) => ipcRenderer.invoke("child-window-minimize", windowName),
    maximize: (windowName) => ipcRenderer.invoke("child-window-maximize", windowName),
    recover: (windowName) => ipcRenderer.invoke("child-window-recover", windowName),
    getWindowConfig: (windowName) => ipcRenderer.invoke("child-window-get-config", windowName)
});

// 应用窗口控制器
contextBridge.exposeInMainWorld("appWindowController", {
    open: (params) => ipcRenderer.invoke("app-window-open", params),
    close: (appId) => ipcRenderer.invoke("app-window-close", appId),
    minimize: (appId) => ipcRenderer.invoke("app-window-minimize", appId),
    maximize: (appId) => ipcRenderer.invoke("app-window-maximize", appId),
    recover: (appId) => ipcRenderer.invoke("app-window-recover", appId),
    getWindowConfig: (appId) => ipcRenderer.invoke("app-window-get-config", appId)
});

// 系统控制器
contextBridge.exposeInMainWorld("systemController", {
    login: (params) => ipcRenderer.invoke("system-login", params),
    logout: (params) => ipcRenderer.invoke("system-logout", params),
    copyToClipboard: (content) => ipcRenderer.invoke("copy-to-clipboard", content),
    readFromClipboard: () => ipcRenderer.invoke("read-from-clipboard"),
    download: (params) => ipcRenderer.invoke("download", params),
    showNotification: (params) => ipcRenderer.invoke("show-notification", params),
    loadSystemSettingConfig: (config) => ipcRenderer.invoke("load-system-setting-config", config),
    getSystemSettingConfig: (userId) => ipcRenderer.invoke("get-system-setting-config", userId),
    setSystemSettingConfig: (config) => ipcRenderer.invoke("set-system-setting-config", config),
    selectSystemPath: () => ipcRenderer.invoke("select-system-path"),
});