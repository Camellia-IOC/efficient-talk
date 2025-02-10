package com.ETGroup.EfficientTalkServer.service.social;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.FriendListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.response.social.FriendListResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.SocialMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class SocialServiceImpl implements SocialService {
    @Resource
    private SocialMapper socialMapper;
    
    /**
     * 获取好友列表
     *
     * @param userId 用户ID
     *
     * @return 好友列表
     */
    @Override
    public FriendListResponseVO getFriendList(String userId) {
        ArrayList<FriendListItemDTO> friendList = socialMapper.getFriendList(userId);
        FriendListResponseVO response = new FriendListResponseVO();
        response.setFriendList(friendList);
        return response;
    }
}
