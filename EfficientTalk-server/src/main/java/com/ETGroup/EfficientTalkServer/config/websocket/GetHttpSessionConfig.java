package com.ETGroup.EfficientTalkServer.config.websocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

/**
 * 获取HttpSession，这样的话，ChatEndpoint类就能操作HttpSession
 */
public class GetHttpSessionConfig extends ServerEndpointConfig.Configurator {
    
    @Override
    public void modifyHandshake(ServerEndpointConfig serverEndpointConfig, HandshakeRequest request, HandshakeResponse response) {
        // 获取 HttpSession 对象
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        
        // 将 httpSession 对象保存起来，存到 ServerEndpointConfig 对象中
        // 在 ChatEndpoint 类的 onOpen 方法就能通过 EndpointConfig 对象获取在这里存入的数据
        serverEndpointConfig.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
    
}
