const {contextBridge, ipcRenderer} = require("electron");

// TODO:频道改成xxx-xxx格式,然后把前端中的调用方法封装一下
contextBridge.exposeInMainWorld("windowController", {
    close: () => ipcRenderer.invoke("window-close"),
    minimize: () => ipcRenderer.invoke("window-minimize"),
    maximize: () => ipcRenderer.invoke("window-maximize"),
    recover: () => ipcRenderer.invoke("window-recover"),
});

contextBridge.exposeInMainWorld("childWindowController", {
    open: (params) => ipcRenderer.invoke("child-window-open", params),
    close: (windowName) => ipcRenderer.invoke("child-window-close", windowName)
});