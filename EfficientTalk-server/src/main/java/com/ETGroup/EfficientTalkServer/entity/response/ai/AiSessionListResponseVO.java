package com.ETGroup.EfficientTalkServer.entity.response.ai;

import com.ETGroup.EfficientTalkServer.entity.PO.AiChatSessionPO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class AiSessionListResponseVO {
    private ArrayList<AiChatSessionPO> sessionList;
}
