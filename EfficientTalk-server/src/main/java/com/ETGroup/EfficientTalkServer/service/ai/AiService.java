package com.ETGroup.EfficientTalkServer.service.ai;

import com.ETGroup.EfficientTalkServer.entity.PO.AiChatHistoryRecordPO;
import com.ETGroup.EfficientTalkServer.entity.PO.AiChatSessionPO;
import com.ETGroup.EfficientTalkServer.entity.response.ai.AiChatHistoryResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.ai.AiSessionListResponseVO;
import org.springframework.stereotype.Service;

@Service
public interface AiService {
    /**
     * 新建AI会话
     *
     * @param session 请求参数
     *
     * @return 会话ID
     */
    String createAiSession(AiChatSessionPO session);
    
    /**
     * 保存AI对话记录
     *
     * @param record 请求参数
     *
     * @return 是否保存成功
     */
    boolean saveAiChatHistory(AiChatHistoryRecordPO record);
    
    /**
     * 获取AI对话记录
     *
     * @param sessionId 会话ID
     *
     * @return 对话记录
     */
    AiChatHistoryResponseVO getAiChatHistory(String sessionId);
    
    /**
     * 获取AI会话列表
     *
     * @param userId 用户ID
     *
     * @return 会话列表
     */
    AiSessionListResponseVO getAiSessionList(String userId);
}
