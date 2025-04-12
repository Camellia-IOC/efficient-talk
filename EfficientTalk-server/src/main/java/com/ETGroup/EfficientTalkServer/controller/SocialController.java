package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.ChatGroupListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.social.ChatGroupMemberListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.social.FriendListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.social.OrgTreeUserNodeDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatGroupMemberPO;
import com.ETGroup.EfficientTalkServer.entity.PO.ChatGroupPO;
import com.ETGroup.EfficientTalkServer.entity.PO.FriendGroupPO;
import com.ETGroup.EfficientTalkServer.entity.request.social.*;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.entity.response.social.*;
import com.ETGroup.EfficientTalkServer.mapper.SocialMapper;
import com.ETGroup.EfficientTalkServer.service.social.SocialService;
import com.ETGroup.EfficientTalkServer.utils.RedisUtils;
import com.ETGroup.EfficientTalkServer.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@Tag(name = "社交相关接口", description = "社交相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/social")
public class SocialController {
    @Resource
    private SocialService socialService;
    
    @Resource
    private SocialMapper socialMapper;
    
    @Resource
    private RedisUtils redisUtils;
    
    @Operation(summary = "获取好友列表")
    @GetMapping("/getFriendList")
    public ResponseData<FriendListResponseVO> getFriendList(@RequestParam String userId) {
        FriendListResponseVO response = socialService.getFriendList(userId);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "获取好友申请列表")
    @GetMapping("/getFriendInvitationList")
    public ResponseData<FriendInvitationListResponseVO> getFriendInvitationList(@RequestParam String userId) {
        FriendInvitationListResponseVO response = socialService.getFriendInvitationList(userId);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "搜索新好友")
    @GetMapping("/searchNewFriend")
    public ResponseData<NewFriendsResponseVO> searchNewFriend(@RequestParam String searchKey,
                                                              @RequestParam String userId,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam Integer pageIndex) {
        NewFriendsResponseVO response = socialService.searchNewFriend(searchKey, userId, pageSize, pageIndex);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "发送好友申请")
    @PostMapping("/createFriendInvite")
    public ResponseData<String> createNewFriendInvite(@RequestBody CreateFriendInviteRequestParam param) {
        if (socialService.createFriendInvite(param)) {
            return ResponseData.success("发送好友申请成功");
        }
        return ResponseData.error(ResponseConfig.INVITE_FRIEND_FAILED);
    }
    
    @Operation(summary = "处理好友申请")
    @PutMapping("/handleFriendInvite")
    public ResponseData<String> handleFriendInvite(@RequestBody HandleFriendInviteRequestParam param) {
        if (socialService.handleFriendInvite(param)) {
            return ResponseData.success("处理好友申请成功");
        }
        return ResponseData.error(ResponseConfig.HANDLE_INVITATION_FAILED);
    }
    
    @Operation(summary = "删除好友")
    @DeleteMapping("/deleteFriend")
    public ResponseData<String> deleteFriend(@RequestParam String userId, @RequestParam String friendId) {
        if (socialService.deleteFriend(userId, friendId)) {
            return ResponseData.success("删除好友成功");
        }
        return ResponseData.error(ResponseConfig.DELETE_FRIEND_FAILED);
    }
    
    @Operation(summary = "获取用户的好友分组")
    @GetMapping("/getUserFriendGroups")
    public ResponseData<UserFriendGroupsResponseVO> getUserFriendGroups(@RequestParam String userId) {
        try {
            UserFriendGroupsResponseVO response = new UserFriendGroupsResponseVO();
            response.setFriendGroups(socialMapper.getUserFriendGroups(userId));
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取用户的好友分组失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "获取组织层级")
    @GetMapping("/getOrganizationTree")
    public ResponseData<OrgTreeResponseVO> getOrganizationTree(@RequestParam String orgId, @RequestParam(required = false) String parentId) {
        OrgTreeResponseVO response = socialService.getOrganizationTree(orgId, parentId);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "获取组织信息")
    @GetMapping("/getOrganizationInfo")
    public ResponseData<OrganizationInfoResponseVO> getOrganizationInfo(@RequestParam String orgId) {
        try {
            OrganizationInfoResponseVO response = new OrganizationInfoResponseVO();
            response.setOrgInfo(socialMapper.getOrganizationInfo(orgId));
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取组织信息失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "获取群聊列表")
    @GetMapping("/getChatGroupList")
    public ResponseData<ArrayList<ChatGroupListItemDTO>> getChatGroupList(@RequestParam String userId) {
        try {
            ArrayList<ChatGroupListItemDTO> response = socialMapper.getChatGroupList(userId);
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取群聊列表失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "获取群聊基本信息")
    @GetMapping("/getChatGroupBasicInfo")
    public ResponseData<ChatGroupPO> getChatGroupBasicInfo(@RequestParam String groupId) {
        try {
            ChatGroupPO response = socialMapper.getChatGroupBasicInfo(groupId);
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取群聊基本信息失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "获取群聊成员列表")
    @GetMapping("/getChatGroupMemberList")
    public ResponseData<ArrayList<ChatGroupMemberListItemDTO>> getChatGroupMemberList(@RequestParam String groupId) {
        try {
            ArrayList<ChatGroupMemberListItemDTO> response = socialMapper.getChatGroupMemberList(groupId);
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取群聊成员列表失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "获取群聊成员ID列表")
    @GetMapping("/getChatGroupMemberIdList")
    public ResponseData<ArrayList<String>> getChatGroupMemberIdList(@RequestParam String groupId) {
        try {
            ArrayList<String> response;
            Set<Object> cache = redisUtils.setMembers("chat_group:member:" + groupId);
            if (!cache.isEmpty()) {
                response = new ArrayList<>();
                for (Object id : cache) {
                    response.add((String) id);
                }
            }
            else {
                response = socialMapper.getChatGroupMemberIdList(groupId);
            }
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取群聊成员ID列表失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "退出群聊")
    @DeleteMapping("/quitChatGroup")
    public ResponseData<Boolean> quitChatGroup(@RequestParam String userId, @RequestParam String groupId) {
        try {
            if (socialMapper.removeChatGroupMember(userId, groupId) == 1) {
                redisUtils.setRemove("chat_group:member:" + groupId, userId);
                return ResponseData.success(true);
            }
            return ResponseData.error(ResponseConfig.ERROR);
        }
        catch (Exception e) {
            log.error("退出群聊失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "移除群聊成员")
    @DeleteMapping("/removeChatGroupMember")
    public ResponseData<Boolean> removeChatGroupMember(@RequestParam String userId, @RequestParam String groupId) {
        try {
            if (socialMapper.removeChatGroupMember(userId, groupId) == 1) {
                redisUtils.setRemove("chat_group:member:" + groupId, userId);
                return ResponseData.success(true);
            }
            return ResponseData.error(ResponseConfig.ERROR);
        }
        catch (Exception e) {
            log.error("移除群聊成员失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "根据名称获取组织成员列表")
    @GetMapping("/getOrgMemberListByName")
    public ResponseData<ArrayList<OrgTreeUserNodeDTO>> getOrgMemberListByName(@RequestParam String orgId, @RequestParam String searchKey) {
        try {
            ArrayList<OrgTreeUserNodeDTO> response = socialMapper.getOrgMemberListByName(orgId, searchKey);
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取组织成员列表失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "邀请新的群聊成员")
    @PostMapping("/inviteChatGroupMember")
    public ResponseData<String> inviteChatGroupMember(@RequestBody InviteChatGroupMemberRequestParam param) {
        ArrayList<String> idList = param.getIdList();
        try {
            for (String id : idList) {
                ChatGroupMemberPO member = new ChatGroupMemberPO();
                member.setId(UUIDUtils.generateUUID());
                member.setGroupId(param.getGroupId());
                member.setUserId(id);
                member.setCreateTime(LocalDateTime.now());
                socialMapper.addChatGroupMember(member);
                redisUtils.setAdd("chat_group:member:" + param.getGroupId(), id);
            }
            return ResponseData.success("邀请成功");
        }
        catch (Exception e) {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "创建组织")
    @PostMapping("/createOrganization")
    public ResponseData<Void> createOrganization(@RequestParam String orgId,
                                                 @RequestParam String orgName,
                                                 @RequestParam(required = false) MultipartFile logo,
                                                 @RequestParam String owner) {
        try {
            socialService.createOrganization(orgId, orgName, logo, owner);
            return ResponseData.success();
        }
        catch (Exception e) {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "新增好友分组")
    @PostMapping("/addFriendGroup")
    public ResponseData<String> addFriendGroup(@RequestBody AddFriendGroupRequestParam param) {
        try {
            FriendGroupPO group = new FriendGroupPO();
            group.setGroupId(UUIDUtils.generateUUID());
            group.setGroupName(param.getGroupName());
            group.setUserId(param.getUserId());
            socialMapper.addFriendGroup(group);
            return ResponseData.success("新增成功");
        }
        catch (Exception e) {
            log.error("新增好友分组失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "修改好友分组")
    @PutMapping("/updateFriendGroup")
    public ResponseData<String> updateFriendGroup(@RequestBody UpdateFriendGroupRequestParam param) {
        try {
            socialMapper.updateFriendGroup(param);
            return ResponseData.success("修改成功");
        }
        catch (Exception e) {
            log.error("修改好友分组失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "根据分组获取好友列表")
    @GetMapping("/getFriendListByGroup")
    public ResponseData<ArrayList<FriendListItemDTO>> getFriendListByGroup(@RequestParam String userId, @RequestParam String groupId) {
        try {
            ArrayList<FriendListItemDTO> response = socialMapper.getFriendListByGroup(userId, groupId);
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取好友列表失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "获取所有好友")
    @GetMapping("/getAllFriendList")
    public ResponseData<ArrayList<FriendListItemDTO>> getAllFriendList(@RequestParam String userId) {
        try {
            ArrayList<FriendListItemDTO> response = socialMapper.getAllFriendList(userId);
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取好友列表失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "删除好友分组")
    @DeleteMapping("/deleteFriendGroup")
    public ResponseData<String> deleteFriendGroup(@RequestParam String userId, @RequestParam String groupId) {
        try {
            socialMapper.deleteFriendGroup(userId, groupId);
            return ResponseData.success("删除成功");
        }
        catch (Exception e) {
            log.error("删除好友分组失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
}
