package com.pigstory.entity.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseData<T> implements Serializable
{
    /**
     * 响应状态码
     */
    private Integer code;
    
    /**
     * 响应信息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    public static <T> ResponseData<T> success ()
    {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.code = ResponseConfig.SUCCESS.getCode();
        responseData.message = ResponseConfig.SUCCESS.getMsg();
        responseData.data = null;
        return responseData;
    }
    
    public static <T> ResponseData<T> success (T object)
    {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.code = ResponseConfig.SUCCESS.getCode();
        responseData.message = ResponseConfig.SUCCESS.getMsg();
        responseData.data = object;
        return responseData;
    }
    
    public static <T> ResponseData<T> error (ResponseConfig responseConfig)
    {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.code = responseConfig.getCode();
        responseData.message = responseConfig.getMsg();
        responseData.data = null;
        return responseData;
    }
}
