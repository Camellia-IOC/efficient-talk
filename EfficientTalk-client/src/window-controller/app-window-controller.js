/** 应用窗口控制器 */
export default class AppWindowController {
    /**
     * 打开应用窗口
     * @param {string} appId 应用ID
     * @param {object} config 窗口配置数据(title, url, data)
     * @param {number} width 窗口宽度
     * @param {number} height 窗口高度
     * @param {boolean} isChild 是否为子窗口
     */
    static openAppWindow = (appId, config, width = 800, height = 600, isChild = false) => {
        appWindowController.open({
            appId: appId,
            width: width,
            height: height,
            isChild: isChild,
            config: config
        });
    };

    /**
     * 关闭应用窗口
     * @param {string} appId 应用ID
     */
    static closeAppWindow = (appId) => {
        appWindowController.close(appId);
    };

    /**
     * 最小化应用窗口
     * @param {string} appId 应用ID
     */
    static minimizeAppWindow = (appId) => {
        appWindowController.minimize(appId);
    };

    /**
     * 最大化应用窗口
     * @param {string} appId 应用ID
     */
    static maximizeAppWindow = (appId) => {
        appWindowController.maximize(appId);
    };

    /**
     * 恢复应用窗口大小
     * @param {string} appId 应用ID
     */
    static recoverAppWindow = (appId) => {
        appWindowController.recover(appId);
    };

    /**
     * 获取应用窗口配置数据
     * @param {string} appId 应用ID
     */
    static getAppWindowConfig = (appId) => {
        return appWindowController.getWindowConfig(appId);
    };
}