package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.notice.NoticeListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.user.BasicUserDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.OrgNoticePO;
import com.ETGroup.EfficientTalkServer.entity.PO.OrgNoticeReadHistoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface NoticeMapper {
    /**
     * 获取组织通知列表
     *
     * @param orgId 组织ID
     *
     * @return 通知列表
     */
    ArrayList<NoticeListItemDTO> getOrgNoticeList(@Param("orgId") String orgId);
    
    /**
     * 检查通知是否已读
     *
     * @param userId   用户ID
     * @param noticeId 通知ID
     *
     * @return 是否已读
     */
    boolean checkNoticeIsRead(@Param("userId") String userId, @Param("noticeId") String noticeId);
    
    /**
     * 发布组织通知
     *
     * @param notice 通知对象
     *
     * @return 发布结果
     */
    int publishOrgNotice(OrgNoticePO notice);
    
    /**
     * 已读通知
     *
     * @param history 已读记录
     *
     * @return 已读结果
     */
    int readOrgNotice(OrgNoticeReadHistoryPO history);
    
    /**
     * 更新通知
     *
     * @param notice 通知对象
     *
     * @return 更新结果
     */
    int updateOrgNotice(OrgNoticePO notice);
    
    /**
     * 删除通知
     *
     * @param noticeId 通知ID
     *
     * @return 删除结果
     */
    int deleteOrgNotice(@Param("noticeId") String noticeId);
    
    /**
     * 删除通知已读记录
     * @param noticeId 通知ID
     * @return 删除结果
     */
    int deleteOrgNoticeReadHistory(@Param("noticeId") String noticeId);
    
    /**
     * 获取通知已读成员列表
     *
     * @param noticeId 通知ID
     *
     * @return 已读成员列表
     */
    ArrayList<BasicUserDTO> getOrgNoticeReadHistory(@Param("noticeId") String noticeId);
}
