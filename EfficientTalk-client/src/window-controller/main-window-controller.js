/** 主窗口控制器 */
export default class MainWindowController {
    /**
     * 隐藏主窗口
     */
    static hideMainWindow = () => {
        windowController.hide();
    };

    /**
     * 显示主窗口
     */
    static showMainWindow = () => {
        windowController.show();
    };

    /**
     * 关闭主窗口
     */
    static closeMainWindow = () => {
        windowController.close();
    };

    /**
     * 最小化主窗口
     */
    static minimizeMainWindow = () => {
        windowController.minimize();
    };

    /**
     * 最大化主窗口
     */
    static maximizeMainWindow = () => {
        windowController.maximize();
    };

    /**
     * 恢复主窗口大小
     */
    static recoverMainWindow = () => {
        windowController.recover();
    };

    /**
     * 系统登录
     */
    static systemLogin = () => {
        systemController.login().then();
    };

    /**
     * 系统退出
     */
    static systemLogout = () => {
        systemController.logout().then();
    };

    /**
     * 复制到剪贴板
     * @param content 复制的内容
     */
    static copyToClipboard = (content) => {
        systemController.copyToClipboard(content);
    };

    /**
     * 从剪贴板读取内容
     * @returns {Promise} 读取到的内容
     */
    static readFromClipboard = () => {
        return systemController.readFromClipboard().then((result) => {
            return result;
        }).catch((error) => {
            console.error("读取剪贴板内容失败：" + error);
            return "";
        });
    };

    /**
     * 下载文件
     * @param params 下载参数
     */
    static download = (params) => {
        systemController.download(params);
    };
}