package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.PO.AiChatHistoryRecordPO;
import com.ETGroup.EfficientTalkServer.entity.PO.AiChatSessionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface AiMapper {
    /**
     * 创建AI会话
     *
     * @param aiChatSession 会话信息
     *
     * @return 是否成功
     */
    Integer createAiSession(AiChatSessionPO aiChatSession);
    
    /**
     * 保存AI对话记录
     *
     * @param aiChatHistoryRecord 对话记录
     *
     * @return 是否成功
     */
    Integer saveAiChatHistory(AiChatHistoryRecordPO aiChatHistoryRecord);
    
    /**
     * 获取AI对话记录
     *
     * @param sessionId 会话ID
     *
     * @return 对话记录
     */
    ArrayList<AiChatHistoryRecordPO> getAiChatHistory(@Param("sessionId") String sessionId);
    
    /**
     * 获取AI会话列表
     *
     * @param userId 用户ID
     *
     * @return 会话列表
     */
    ArrayList<AiChatSessionPO> getAiSessionList(@Param("userId") String userId);
}
