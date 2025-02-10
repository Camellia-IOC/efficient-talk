import axios from 'axios'
import {message} from "ant-design-vue";

// 基础axios实例
class BaseAxiosInstance {
    /** axios实例 */
    instance;

    /**
     * 构造axios实例
     *
     * @param serverIP 服务器IP
     * @param servicePort 服务端口
     * @param timeout 超时时间
     */
    constructor(serverIP, servicePort, timeout) {
        this.instance = axios.create({
            baseURL: `http://${serverIP}:${servicePort}`,
            timeout: timeout
        })

        // 配置请求拦截器
        this.instance.interceptors.request.use(
            (config) => {
                // 请求预处理
                config.headers = config.headers || {}
                return config
            },
            (error) => {
                // 错误处理
                return Promise.reject(error)
            }
        )

        // 配置响应拦截器
        this.instance.interceptors.response.use(
            (response) => {
                // 响应数据预处理
                return response
            },
            (error) => {
                // TODO: 发布时要删除以下代码
                // 错误处理
                if (error.response) {
                    message.error(
                        error.response.data.status + ' ' + error.response.data.error,
                        1000
                    ).then()
                }
                return Promise.reject(error)
            }
        )
    }

    /** get请求 */
    get = (config) => {
        return this.instance({
            ...config,
            method: 'get',
            params: config.data
        })
    }

    /** post请求 */
    post = (config) => {
        return this.instance({
            ...config,
            method: 'post',
            data: config.data
        })
    }

    /** put请求 */
    put = (config) => {
        return this.instance({
            ...config,
            method: 'put',
            data: config.data
        })
    }

    /** delete请求 */
    delete = (config) => {
        return this.instance({
            ...config,
            method: 'delete',
            params: config.data
        })
    }

    /** download请求 */
    download = (config) => {
        return this.instance({
            ...config,
            method: 'get',
            responseType: 'blob',
            params: config.data
        })
    }
}

export default BaseAxiosInstance
