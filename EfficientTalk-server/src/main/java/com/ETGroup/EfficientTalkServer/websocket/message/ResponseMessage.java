package com.ETGroup.EfficientTalkServer.websocket.message;

import lombok.Data;

@Data
public class ResponseMessage {
    private boolean isSystem;
    private String fromName;
    private Object message;// 如果是系统消息是数组
    
    public ResponseMessage() {
    
    }
    
    public ResponseMessage(boolean isSystem, String fromName, Object message) {
        this.isSystem = isSystem;
        this.fromName = fromName;
        this.message = message;
    }
}
