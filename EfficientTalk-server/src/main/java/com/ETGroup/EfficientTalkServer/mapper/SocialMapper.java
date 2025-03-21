package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.*;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatGroupPO;
import com.ETGroup.EfficientTalkServer.entity.PO.FriendGroupPO;
import com.ETGroup.EfficientTalkServer.entity.PO.FriendInvitationPO;
import com.ETGroup.EfficientTalkServer.entity.PO.OrganizationPO;
import com.ETGroup.EfficientTalkServer.entity.request.social.CreateFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.social.HandleFriendInviteRequestParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface SocialMapper {
    // 获取好友列表
    ArrayList<FriendListItemDTO> getFriendList(String userId);
    
    // 创建好友邀请
    Integer createFriendInvite(FriendInvitationPO invitation);
    
    // 处理好友邀请
    Integer handleFriendInvite(@Param("id") String id, @Param("state") Integer state, @Param("handleTime") LocalDateTime handleTime);
    
    // 获取指定用户的好友分组
    ArrayList<FriendGroupPO> getUserFriendGroups(@Param("userId") String userId);
    
    // 添加好友记录
    Integer addFriendRecord(AddFriendRecordDTO param);
    
    // 删除好友
    Integer deleteFriend(@Param("userId") String userId, @Param("friendId") String friendId);
    
    // 获取好友申请列表
    ArrayList<FriendInvitationListItemDTO> getFriendInvitationList(String userId);
    
    // 检查是否存在关联记录,返回关联邀请记录ID
    String checkRelativeInvitation(@Param("userId") String userId, @Param("friendId") String friendId);
    
    // 获取邀请记录状态
    Integer getInvitationState(@Param("id") String id);
    
    // 搜索新好友
    ArrayList<NewFriendBasicInfoDTO> searchNewFriend(@Param("searchKey") String searchKey,
                                                     @Param("userId") String userId,
                                                     @Param("pageSize") Integer pageSize,
                                                     @Param("pageIndex") Integer pageIndex);
    
    // 检查是否为好友
    Integer checkIsFriend(@Param("userId") String userId, @Param("friendId") String friendId);
    
    // 获取组织树中的部门节点
    ArrayList<OrgTreeDeptNodeDTO> getOrgTreeDeptNodeList(@Param("orgId") String orgId, @Param("parentId") String parentId);
    
    // 获取组织树中的用户节点
    ArrayList<OrgTreeUserNodeDTO> getOrgTreeUserNodeList(@Param("orgId") String orgId, @Param("parentId") String parentId);
    
    // 获取组织信息
    OrganizationPO getOrganizationInfo(@Param("orgId") String orgId);
    
    /**
     * 获取群聊列表
     *
     * @param userId 用户ID
     *
     * @return 群聊列表
     */
    ArrayList<ChatGroupListItemDTO> getChatGroupList(@Param("userId") String userId);
    
    /**
     * 获取群聊成员ID列表
     *
     * @param groupId 群聊ID
     *
     * @return 成员ID列表
     */
    ArrayList<String> getChatGroupMemberIdList(@Param("groupId") String groupId);
    
    /**
     * 获取群聊基本信息
     *
     * @param groupId 群聊ID
     *
     * @return 群聊基本信息
     */
    ChatGroupPO getChatGroupBasicInfo(@Param("groupId") String groupId);
    
    /**
     * 获取群聊成员列表
     *
     * @param groupId 群聊ID
     *
     * @return 成员列表
     */
    ArrayList<ChatGroupMemberListItemDTO> getChatGroupMemberList(@Param("groupId") String groupId);
    
    /**
     * 退出群聊
     *
     * @param userId  用户ID
     * @param groupId 群聊ID
     *
     * @return 退出结果
     */
    Integer quitChatGroup(@Param("userId") String userId, @Param("groupId") String groupId);
}
