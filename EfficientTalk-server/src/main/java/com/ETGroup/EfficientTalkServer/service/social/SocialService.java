package com.ETGroup.EfficientTalkServer.service.social;

import com.ETGroup.EfficientTalkServer.entity.PO.OrganizationPO;
import com.ETGroup.EfficientTalkServer.entity.request.social.CreateFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.social.HandleFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.social.FriendInvitationListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.social.FriendListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.social.NewFriendsResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.social.OrgTreeResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
     * @param searchKey 搜索关键字
     * @param userId    用户ID
     * @param pageSize  页面大小
     * @param pageIndex 页面索引
     *
     * @return 新好友列表
     */
    NewFriendsResponseVO searchNewFriend(String searchKey, String userId, Integer pageSize, Integer pageIndex);
    
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
    
    /**
     * 获取组织树
     *
     * @param orgId    组织ID
     * @param parentId 父节点ID
     *
     * @return 好友分组列表
     */
    OrgTreeResponseVO getOrganizationTree(String orgId, String parentId);
    
    /**
     * 获取组织信息
     *
     * @param orgId 组织ID
     *
     * @return 组织信息
     */
    OrganizationPO getOrganizationInfo(String orgId);
    
    /**
     * 创建组织
     *
     * @param orgId   组织ID
     * @param orgName 组织名称
     * @param logo    组织LOGO
     * @param owner   组织创建者
     */
    void createOrganization(String orgId, String orgName, MultipartFile logo, String owner);
}
