package com.ETGroup.EfficientTalkServer.entity.response.ai;

import com.ETGroup.EfficientTalkServer.entity.PO.AiChatHistoryRecordPO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class AiChatHistoryResponseVO {
    private ArrayList<AiChatHistoryRecordPO> recordList;
}
