package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.*;
import com.ETGroup.EfficientTalkServer.entity.PO.*;
import com.ETGroup.EfficientTalkServer.entity.request.social.CreateFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.social.HandleFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.social.UpdateFriendGroupRequestParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

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
    
    // 检查是否存在关联记录,返回关联邀请记录
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
    Integer removeChatGroupMember(@Param("userId") String userId, @Param("groupId") String groupId);
    
    /**
     * 根据名称获取组织成员列表
     *
     * @param orgId     组织ID
     * @param searchKey 搜索关键词
     *
     * @return 组织成员列表
     */
    ArrayList<OrgTreeUserNodeDTO> getOrgMemberListByName(@Param("orgId") String orgId, @Param("searchKey") String searchKey);
    
    /**
     * 创建群聊
     *
     * @param chatGroup 群聊对象
     *
     * @return 创建结果
     */
    Integer createChatGroup(ChatGroupPO chatGroup);
    
    /**
     * 添加群聊成员
     *
     * @param chatGroupMember 群聊成员对象
     *
     * @return 添加结果
     */
    Integer addChatGroupMember(ChatGroupMemberPO chatGroupMember);
    
    /**
     * 创建新的组织
     *
     * @param organization 组织对象
     */
    void createOrganization(OrganizationPO organization);
    
    /**
     * 检查组织ID是否存在
     *
     * @param orgId 组织ID
     *
     * @return 存在结果
     */
    Integer checkOrgIdExist(@Param("orgId") String orgId);
    
    /**
     * 创建新的部门
     *
     * @param department 部门对象
     */
    void createDepartment(DepartmentPO department);
    
    /**
     * 创建新的好友分组
     *
     * @param group 分组对象
     */
    void addFriendGroup(FriendGroupPO group);
    
    /**
     * 修改好友分组
     *
     * @param param 修改参数
     */
    void updateFriendGroup(UpdateFriendGroupRequestParam param);
    
    /**
     * 获取好友分组下的好友列表
     *
     * @param userId  用户ID
     * @param groupId 分组ID
     *
     * @return 好友列表
     */
    ArrayList<FriendListItemDTO> getFriendListByGroup(@Param("userId") String userId, @Param("groupId") String groupId);
    
    /**
     * 获取所有好友
     * @param userId 好友ID
     * @return 好友列表
     */
    ArrayList<FriendListItemDTO> getAllFriendList(@Param("userId") String userId);
    
    /**
     * 删除好友分组
     * @param userId 用户ID
     * @param groupId 分组ID
     */
    void deleteFriendGroup(@Param("userId") String userId,@Param("groupId") String groupId);
}
