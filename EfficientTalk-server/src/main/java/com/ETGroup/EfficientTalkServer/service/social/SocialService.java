package com.ETGroup.EfficientTalkServer.service.social;

import com.ETGroup.EfficientTalkServer.entity.response.social.FriendListResponseVO;
import org.springframework.stereotype.Service;

@Service
public interface SocialService {
    /**
     * 获取好友列表
     * @param userId 用户ID
     * @return 好友列表
     */
    FriendListResponseVO getFriendList (String userId);
}
