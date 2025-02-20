package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.*;
import com.ETGroup.EfficientTalkServer.entity.PO.FriendGroupPO;
import com.ETGroup.EfficientTalkServer.entity.PO.FriendInvitationPO;
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
}
