package com.ETGroup.EfficientTalkServer.utils;

import com.ETGroup.EfficientTalkServer.websocket.message.ResponseMessage;
import com.alibaba.fastjson2.JSON;

public class ChatMessageUtils {
    
    /**
     * @param isSystemMessage 是否是系统消息，只有广播消息才是系统消息，如果是私聊消息的话，就不是系统消息
     * @param fromName        给谁发消息，如果是系统消息的话，这个参数不需要指定
     * @param message         消息的具体内容
     * @return String
     */
    public static String getMessage(boolean isSystemMessage, String fromName, Object message) {
        ResponseMessage responseMessage = new ResponseMessage();
        
        responseMessage.setSystem(isSystemMessage);
        responseMessage.setMessage(message);
        if (fromName != null) {
            responseMessage.setFromName(fromName);
        }
        
        return JSON.toJSONString(responseMessage);
    }
    
}
