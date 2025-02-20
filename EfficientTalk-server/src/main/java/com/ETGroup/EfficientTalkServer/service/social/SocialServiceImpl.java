package com.ETGroup.EfficientTalkServer.service.social;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.*;
import com.ETGroup.EfficientTalkServer.entity.PO.FriendGroupPO;
import com.ETGroup.EfficientTalkServer.entity.PO.FriendInvitationPO;
import com.ETGroup.EfficientTalkServer.entity.request.social.CreateFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.social.HandleFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.social.FriendInvitationListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.social.FriendListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.social.NewFriendsResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.SocialMapper;
import com.ETGroup.EfficientTalkServer.utils.UUIDUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        ArrayList<FriendGroupPO> userFriendGroups = socialMapper.getUserFriendGroups(userId);
        Map<String, FriendGroupListDTO> userFriendGroupsMap = new HashMap<>();
        // 初始化好友分组
        for (FriendGroupPO group : userFriendGroups) {
            userFriendGroupsMap.put(group.getGroupId(), new FriendGroupListDTO(group.getGroupId(), group.getGroupName(), new ArrayList<>()));
        }
        // 将好友进行分组并填充
        for (FriendListItemDTO item : friendList) {
            if (userFriendGroupsMap.containsKey(item.getGroupId())) {
                userFriendGroupsMap.get(item.getGroupId())
                                   .getFriendList()
                                   .add(item);
            }
            else {
                FriendGroupListDTO group = new FriendGroupListDTO(item.getGroupId(), item.getGroupName(), new ArrayList<>());
                group.getFriendList()
                     .add(item);
                userFriendGroupsMap.put(item.getGroupId(), group);
            }
        }
        // 转换为返回格式
        ArrayList<FriendGroupListDTO> groupList = new ArrayList<>(userFriendGroupsMap.values());
        
        FriendListResponseVO response = new FriendListResponseVO();
        response.setTotal(friendList.size());
        response.setGroupList(groupList);
        return response;
    }
    
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
    @Override
    public NewFriendsResponseVO searchNewFriend(String searchKey, String userId, Integer pageSize, Integer pageIndex) {
        NewFriendsResponseVO response = new NewFriendsResponseVO();
        response.setResultList(socialMapper.searchNewFriend(searchKey, userId, pageSize, pageIndex));
        return response;
    }
    
    /**
     * 获取好友申请列表
     *
     * @param userId 用户ID
     *
     * @return 申请列表
     */
    @Override
    public FriendInvitationListResponseVO getFriendInvitationList(String userId) {
        FriendInvitationListResponseVO response = new FriendInvitationListResponseVO();
        response.setInvitationList(socialMapper.getFriendInvitationList(userId));
        return response;
    }
    
    /**
     * 发送好友申请
     *
     * @param param 申请参数
     *
     * @return 申请结果
     */
    @Override
    public Boolean createFriendInvite(CreateFriendInviteRequestParam param) {
        // 检查好友申请记录是否已存在
        String relativeInvitationId = socialMapper.checkRelativeInvitation(param.getUserId(), param.getFriendId());
        if (relativeInvitationId != null) {
            // TODO 逻辑是否需要修改
            
            return true;
        }
        
        // 构造申请记录对象
        FriendInvitationPO invitation = new FriendInvitationPO();
        invitation.setId(UUIDUtils.generateUUID());
        invitation.setUserId(param.getUserId());
        invitation.setFriendId(param.getFriendId());
        invitation.setMessage(param.getMessage());
        invitation.setGroupId(param.getGroupId());
        invitation.setInviteTime(LocalDateTime.now());
        invitation.setState(0);
        
        return socialMapper.createFriendInvite(invitation) == 1;
    }
    
    /**
     * 处理好友申请
     *
     * @param param 处理参数
     *
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean handleFriendInvite(HandleFriendInviteRequestParam param) {
        try {
            // 检查申请记录状态,如果为已处理则直接返回
            if (socialMapper.getInvitationState(param.getInvitationId()) != 0) {
                return true;
            }
            
            // 更新申请记录
            LocalDateTime handleTime = LocalDateTime.now();
            socialMapper.handleFriendInvite(param.getInvitationId(), param.getAccept() ? 1 : 2, handleTime);
            String relativeInvitationId = socialMapper.checkRelativeInvitation(param.getFriendId(), param.getUserId());
            
            // 如果存在相对申请，则更新
            if (relativeInvitationId != null) {
                socialMapper.handleFriendInvite(relativeInvitationId, param.getAccept() ? 1 : 2, handleTime);
            }
            
            // 如果接受申请，则添加好友记录
            if (param.getAccept()) {
                // 添加好友记录
                AddFriendRecordDTO record = new AddFriendRecordDTO();
                record.setUserRecordId(UUIDUtils.generateUUID());
                // 如果是添加自己，那么只需要生成一条UUID即可
                if (!param.getUserId()
                          .equals(param.getFriendId())) {
                    record.setFriendRecordId(UUIDUtils.generateUUID());
                    record.setFriendGroupId(param.getFriendGroupId());
                }
                record.setUserId(param.getUserId());
                record.setFriendId(param.getFriendId());
                record.setUserGroupId(param.getUserGroupId());
                record.setCreateTime(handleTime);
                socialMapper.addFriendRecord(record);
            }
        }
        catch (Exception e) {
            log.error("处理好友申请失败", e);
            return false;
        }
        
        return true;
    }
    
    /**
     * 删除好友
     *
     * @param userId   用户ID
     * @param friendId 好友ID
     *
     * @return 删除结果
     */
    @Override
    public Boolean deleteFriend(String userId, String friendId) {
        return socialMapper.deleteFriend(userId, friendId) == 2;
    }
}
