import MainWindowController from "../window-controller/main-window-controller.js";
import { message } from "ant-design-vue";

/** 获取选中的内容
 *
 * @returns {string} 选中内容
 */
export const getSelectedContent = () => {
    return window.getSelection().toString();
};

/** 复制内容到剪贴板
 *
 * @param {string} content 复制的内容
 */
export const copyToClipboard = (content) => {
    MainWindowController.copyToClipboard(content);
    message.success("复制成功").then();
};

/** 读取剪贴板中的内容
 *
 * @returns {string} 读取的内容
 */
export const readFromClipboard = () => {
    const content = MainWindowController.readFromClipboard();
    return content;
};