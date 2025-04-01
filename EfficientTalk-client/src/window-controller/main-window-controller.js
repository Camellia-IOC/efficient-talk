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

    /**
     * 显示通知
     * @param params 通知参数
     */
    static showNotification = (params) => {
        systemController.showNotification(params);
    };

    /**
     * 从云端加载用户设置
     * @param params 用户Id/用户设置
     */
    static loadSystemSettingConfig = (params) => {
        return systemController.loadSystemSettingConfig(params);
    };

    /**
     * 获取系统设置
     * @param userId 用户ID
     * @returns {Promise<Object>} 系统设置
     */
    static getSystemSettingConfig = (userId) => {
        return systemController.getSystemSettingConfig(userId);
    };

    /**
     * 获取指定的系统设置
     * @param params 参数
     * @returns {Promise} 系统设置
     */
    static getAssignedSystemSettingConfig = (params) => {
        return systemController.getAssignedSystemSettingConfig(params);
    };

    /**
     * 设置系统设置
     * @param config 系统设置
     */
    static setSystemSettingConfig = (config) => {
        systemController.setSystemSettingConfig(config);
    };

    /**
     * 选择系统路径
     * @returns 系统路径
     */
    static selectSystemPath = () => {
        return systemController.selectSystemPath();
    };
}