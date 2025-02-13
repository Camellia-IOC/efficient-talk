package com.ETGroup.EfficientTalkServer.service.social;

import com.ETGroup.EfficientTalkServer.entity.request.social.CreateFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.social.HandleFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.social.FriendInvitationListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.social.FriendListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.social.NewFriendsResponseVO;
import org.springframework.stereotype.Service;

@Service
public interface SocialService {
    /**
     * 获取好友列表
     *
     * @param userId 用户ID
     *
     * @return 好友列表
     */
    FriendListResponseVO getFriendList(String userId);
    
    /**
     * 搜索新好友
     *
     * @param friendId 好友ID
     * @param friendName 好友名称
     * @param userId   用户ID
     * @param pageSize 页面大小
     * @param pageIndex 页面索引
     *
     * @return 新好友列表
     */
    NewFriendsResponseVO searchNewFriend(String friendId, String friendName, String userId, Integer pageSize, Integer pageIndex);
    
    /**
     * 获取好友申请列表
     *
     * @param userId 用户ID
     *
     * @return 申请列表
     */
    FriendInvitationListResponseVO getFriendInvitationList(String userId);
    
    /**
     * 发送好友申请
     *
     * @param param 申请参数
     *
     * @return 申请结果
     */
    Boolean createFriendInvite(CreateFriendInviteRequestParam param);
    
    /**
     * 处理好友申请
     *
     * @param param 处理参数
     *
     * @return 处理结果
     */
    Boolean handleFriendInvite(HandleFriendInviteRequestParam param);
    
    /**
     * 删除好友
     *
     * @param userId   用户ID
     * @param friendId 好友ID
     *
     * @return 删除结果
     */
    Boolean deleteFriend(String userId, String friendId);
}
