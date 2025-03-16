package com.ETGroup.EfficientTalkServer.service.ai;

import com.ETGroup.EfficientTalkServer.entity.PO.AiChatHistoryRecordPO;
import com.ETGroup.EfficientTalkServer.entity.PO.AiChatSessionPO;
import com.ETGroup.EfficientTalkServer.entity.response.ai.AiChatHistoryResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.ai.AiSessionListResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.AiMapper;
import com.ETGroup.EfficientTalkServer.utils.UUIDUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AiServiceImpl implements AiService {
    @Resource
    private AiMapper aiMapper;
    
    /**
     * 新建AI会话
     *
     * @param session 请求参数
     *
     * @return 会话ID
     */
    @Override
    public String createAiSession(AiChatSessionPO session) {
        session.setSessionId(UUIDUtils.generateUUID());
        session.setUpdateTime(LocalDateTime.now());
        boolean result = aiMapper.createAiSession(session) == 1;
        if (result) {
            return session.getSessionId();
        }
        else {
            return null;
        }
    }
    
    /**
     * 保存AI对话记录
     *
     * @param record 请求参数
     *
     * @return 是否保存成功
     */
    @Override
    public boolean saveAiChatHistory(AiChatHistoryRecordPO record) {
        return aiMapper.saveAiChatHistory(record) == 1;
    }
    
    /**
     * 获取AI对话记录
     *
     * @param sessionId 会话ID
     *
     * @return 对话记录
     */
    @Override
    public AiChatHistoryResponseVO getAiChatHistory(String sessionId) {
        AiChatHistoryResponseVO result = new AiChatHistoryResponseVO();
        result.setRecordList(aiMapper.getAiChatHistory(sessionId));
        return result;
    }
    
    /**
     * 获取AI会话列表
     *
     * @param userId 用户ID
     *
     * @return 会话列表
     */
    @Override
    public AiSessionListResponseVO getAiSessionList(String userId) {
        AiSessionListResponseVO result = new AiSessionListResponseVO();
        result.setSessionList(aiMapper.getAiSessionList(userId));
        return result;
    }
}
