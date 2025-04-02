package com.ETGroup.EfficientTalkServer.service.social;

import com.ETGroup.EfficientTalkServer.entity.DTO.social.*;
import com.ETGroup.EfficientTalkServer.entity.PO.DepartmentPO;
import com.ETGroup.EfficientTalkServer.entity.PO.FriendGroupPO;
import com.ETGroup.EfficientTalkServer.entity.PO.FriendInvitationPO;
import com.ETGroup.EfficientTalkServer.entity.PO.OrganizationPO;
import com.ETGroup.EfficientTalkServer.entity.request.social.CreateFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.social.HandleFriendInviteRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.social.FriendInvitationListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.social.FriendListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.social.NewFriendsResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.social.OrgTreeResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.CloudDiskMapper;
import com.ETGroup.EfficientTalkServer.mapper.SocialMapper;
import com.ETGroup.EfficientTalkServer.mapper.UserMapper;
import com.ETGroup.EfficientTalkServer.utils.OSSUtils;
import com.ETGroup.EfficientTalkServer.utils.UUIDUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SocialServiceImpl implements SocialService {
    @Resource
    private SocialMapper socialMapper;
    
    @Resource
    private OSSUtils ossUtils;
    
    @Value("${system-config.default-org-cloud-disk-capacity}")
    private Long defaultCloudDiskCapacity;
    
    @Autowired
    private CloudDiskMapper cloudDiskMapper;
    
    @Autowired
    private UserMapper userMapper;
    
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
            String relativeInvitationId = socialMapper.checkRelativeInvitation(param.getUserId(), param.getFriendId());
            
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
    
    /**
     * 获取组织树
     *
     * @param orgId    组织ID
     * @param parentId 父节点ID
     *
     * @return 好友分组列表
     */
    @Override
    public OrgTreeResponseVO getOrganizationTree(String orgId, String parentId) {
        OrgTreeResponseVO response = new OrgTreeResponseVO();
        response.setDeptList(socialMapper.getOrgTreeDeptNodeList(orgId, parentId));
        response.setUserList(socialMapper.getOrgTreeUserNodeList(orgId, parentId));
        return response;
    }
    
    /**
     * 获取组织信息
     *
     * @param orgId 组织ID
     *
     * @return 组织信息
     */
    @Override
    public OrganizationPO getOrganizationInfo(String orgId) {
        return socialMapper.getOrganizationInfo(orgId);
    }
    
    /**
     * 创建组织
     *
     * @param orgId   组织ID
     * @param orgName 组织名称
     * @param logo    组织LOGO
     * @param owner   组织创建者
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrganization(String orgId, String orgName, MultipartFile logo, String owner) {
        try {
            OrganizationPO organization = new OrganizationPO();
            if (socialMapper.checkOrgIdExist(orgId.toLowerCase()) == 1) {
                throw new RuntimeException("组织ID已存在");
            }
            organization.setOrgId(orgId);
            organization.setOrgName(orgName);
            organization.setOwner(owner);
            // 保存Logo
            if (logo != null) {
                String logoName = ossUtils.upload(ossUtils.getSystemOrgLogoBucketName(), orgId, logo);
                String logoPath = ossUtils.getObjectUrl(ossUtils.getSystemOrgLogoBucketName(), logoName);
                organization.setOrgLogo(logoPath);
            }
            // 创建云盘
            String diskId = "org-cloud-disk-" + orgId.toLowerCase();
            ossUtils.createBucket(diskId);
            organization.setDiskId(diskId);
            socialMapper.createOrganization(organization);
            DepartmentPO department = new DepartmentPO();
            department.setDeptId(UUIDUtils.generateUUID());
            department.setId(orgId + '-' + department.getDeptId());
            department.setDeptName(orgName);
            department.setOrgId(orgId);
            department.setLevel((byte) 0);
            department.setAdmin(owner);
            socialMapper.createDepartment(department);
            cloudDiskMapper.createOrgCloudDisk(diskId, orgId, defaultCloudDiskCapacity);
            userMapper.updateUserOrgInfo(owner, orgId, department.getDeptId(), 100000);
        }
        catch (Exception e) {
            log.error("创建组织失败", e);
            throw e;
        }
    }
}
