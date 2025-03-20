package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.request.social.CreateFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.social.HandleFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.entity.response.social.*;
import com.ETGroup.EfficientTalkServer.mapper.SocialMapper;
import com.ETGroup.EfficientTalkServer.service.social.SocialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
        UserFriendGroupsResponseVO response = new UserFriendGroupsResponseVO();
        response.setFriendGroups(socialMapper.getUserFriendGroups(userId));
        return ResponseData.success(response);
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
        OrganizationInfoResponseVO response = new OrganizationInfoResponseVO();
        response.setOrgInfo(socialMapper.getOrganizationInfo(orgId));
        return ResponseData.success(response);
    }
}
