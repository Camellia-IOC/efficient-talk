import ChildWindowController from "../child-window-controller.js";

/**
 * 打开AI助手窗口
 * @param {object} data 窗口配置数据
 * @param {number} width 窗口宽度
 * @param {number} height 窗口高度
 * @param {boolean} isChild 是否为子窗口
 */
export const openAiAssistantChildWindow = (data, width = 800, height = 600, isChild = false) => {
    const windowName = "aiAssistantWindow";
    const routerUrl = "child-window-ai-assistant";
    const url = `/child-window?windowName=${windowName}&url=${routerUrl}`;
    const config = {
        title: "小易",
        data: data
    };
    ChildWindowController.openChildWindow(windowName, url, config, width, height, isChild);
};

/**
 * 打开聊天记录窗口
 * @param {object} data 窗口配置数据
 * @param {number} width 窗口宽度
 * @param {number} height 窗口高度
 * @param {boolean} isChild 是否为子窗口
 */
export const openChatHistoryChildWindow = (data, width = 800, height = 600, isChild = false) => {
    const windowName = "chatHistoryWindow";
    const routerUrl = "child-window-chat-history";
    const url = `/child-window?windowName=${windowName}&url=${routerUrl}`;
    const config = {
        title: "聊天记录",
        data: data
    };
    ChildWindowController.openChildWindow(windowName, url, config, width, height, isChild);
};

/**
 * 打开文件预览窗口
 * @param {object} data 窗口配置数据
 * @param {number} width 窗口宽度
 * @param {number} height 窗口高度
 * @param {boolean} isChild 是否为子窗口
 */
export const openFilePreviewChildWindow = (data, width = 1200, height = 900, isChild = false) => {
    const windowName = "filePreviewWindow";
    const routerUrl = "child-window-file-preview";
    const url = `/child-window?windowName=${windowName}&url=${routerUrl}`;
    const config = {
        title: "文件预览",
        data: data
    };
    ChildWindowController.openChildWindow(windowName, url, config, width, height, isChild);
};

/**
 * 打开媒体文件预览窗口
 * @param {object} data 窗口配置数据
 * @param {number} width 窗口宽度
 * @param {number} height 窗口高度
 * @param {boolean} isChild 是否为子窗口
 */
export const openMediaFilePreviewChildWindow = (data, width = 800, height = 600, isChild = false) => {
    const windowName = "mediaFilePreviewWindow";
    const routerUrl = "child-window-media-file-preview";
    const url = `/child-window?windowName=${windowName}&url=${routerUrl}`;
    const config = {
        title: "媒体文件预览",
        data: data
    };
    ChildWindowController.openChildWindow(windowName, url, config, width, height, isChild);
};