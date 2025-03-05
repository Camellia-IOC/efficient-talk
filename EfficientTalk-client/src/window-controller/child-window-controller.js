/** 子窗口控制器 */
export default class ChildWindowController {
    /**
     * 打开子窗口
     * @param {string} windowName 窗口名称
     * @param {string} url 路由URL
     * @param {object} config 窗口配置数据
     * @param {number} width 窗口宽度
     * @param {number} height 窗口高度
     * @param {boolean} isChild 是否为子窗口
     */
    static openChildWindow = (windowName, url, config, width, height, isChild) => {
        childWindowController.open({
            windowName: windowName,
            width: width,
            height: height,
            isChild: isChild,
            url: url,
            config: config
        });
    };

    /**
     * 关闭子窗口
     * @param {string} windowName 窗口名称
     */
    static closeChildWindow = (windowName) => {
        childWindowController.close(windowName);
    };

    /**
     * 最小化子窗口
     * @param {string} windowName 窗口名称
     */
    static minimizeChildWindow = (windowName) => {
        childWindowController.minimize(windowName);
    };

    /**
     * 最大化子窗口
     * @param {string} windowName 窗口名称
     */
    static maximizeChildWindow = (windowName) => {
        childWindowController.maximize(windowName);
    };

    /**
     * 恢复子窗口大小
     * @param {string} windowName 窗口名称
     */
    static recoverChildWindow = (windowName) => {
        childWindowController.recover(windowName);
    };

    /**
     * 获取子窗口配置数据
     * @param {string} windowName 窗口名称
     */
    static getChildWindowConfig = (windowName) => {
        return childWindowController.getWindowConfig(windowName);
    };
}