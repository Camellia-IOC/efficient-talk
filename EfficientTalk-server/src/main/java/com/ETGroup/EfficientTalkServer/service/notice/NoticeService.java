package com.ETGroup.EfficientTalkServer.service.notice;

import com.ETGroup.EfficientTalkServer.entity.DTO.notice.NoticeListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.user.BasicUserDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.OrgNoticePO;
import com.ETGroup.EfficientTalkServer.entity.PO.OrgNoticeReadHistoryPO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface NoticeService {
    /**
     * 获取通知列表
     *
     * @param userId 用户ID
     * @param orgId  组织ID
     *
     * @return 通知列表
     */
    ArrayList<NoticeListItemDTO> getOrgNoticeList(String userId, String orgId);
    
    /**
     * 发布组织通知
     *
     * @param notice 通知
     *
     * @return 是否发布成功
     */
    boolean publishOrgNotice(OrgNoticePO notice);
    
    /**
     * 通知已读
     *
     * @param history 记录对象
     *
     * @return 是否成功
     */
    boolean readOrgNotice(OrgNoticeReadHistoryPO history);
    
    /**
     * 删除通知
     *
     * @param noticeId 通知ID
     *
     * @return 是否成功
     */
    boolean deleteOrgNotice(String noticeId);
    
    /**
     * 获取通知已读成员列表
     *
     * @param noticeId 通知ID
     *
     * @return 已读成员列表
     */
    ArrayList<BasicUserDTO> getOrgNoticeReadHistory(String noticeId);
}
