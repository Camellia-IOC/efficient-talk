package com.ETGroup.EfficientTalkServer.entity.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseMessage implements Serializable {
    /**
     * 响应状态码
     */
    private Integer code;
    
    /**
     * 响应信息
     */
    private String message;
    
    /**
     * 响应成功
     *
     * @return 响应信息体
     */
    public static ResponseMessage success() {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.code = ResponseConfig.SUCCESS.getCode();
        responseMessage.message = ResponseConfig.SUCCESS.getMsg();
        return responseMessage;
    }
    
    /**
     * 响应失败
     *
     * @param responseConfig 响应信息配置
     * @return 响应信息体
     */
    public static ResponseMessage error(ResponseConfig responseConfig) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.code = responseConfig.getCode();
        responseMessage.message = responseConfig.getMsg();
        return responseMessage;
    }
}
