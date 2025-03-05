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
}