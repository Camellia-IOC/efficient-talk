package com.ETGroup.EfficientTalkServer.service.notice;

import com.ETGroup.EfficientTalkServer.entity.DTO.notice.NoticeListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.user.BasicUserDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.OrgNoticePO;
import com.ETGroup.EfficientTalkServer.entity.PO.OrgNoticeReadHistoryPO;
import com.ETGroup.EfficientTalkServer.mapper.NoticeMapper;
import com.ETGroup.EfficientTalkServer.utils.UUIDUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {
    @Resource
    private NoticeMapper noticeMapper;
    
    /**
     * 获取通知列表
     *
     * @param userId 用户ID
     * @param orgId  组织ID
     *
     * @return 通知列表
     */
    @Override
    public ArrayList<NoticeListItemDTO> getOrgNoticeList(String userId, String orgId) {
        try {
            ArrayList<NoticeListItemDTO> noticeList = noticeMapper.getOrgNoticeList(orgId);
            for (NoticeListItemDTO notice : noticeList) {
                notice.setHasRead(noticeMapper.checkNoticeIsRead(userId, notice.getId()));
            }
            return noticeList;
        }
        catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
    
    /**
     * 发布组织通知
     *
     * @param notice 通知
     *
     * @return 是否发布成功
     */
    @Override
    public boolean publishOrgNotice(OrgNoticePO notice) {
        if (notice.getId() == null) {
            notice.setId(UUIDUtils.generateUUID());
            return noticeMapper.publishOrgNotice(notice) == 1;
        }
        else {
            return noticeMapper.updateOrgNotice(notice) == 1;
        }
    }
    
    /**
     * 通知已读
     *
     * @param history 记录对象
     *
     * @return 是否成功
     */
    @Override
    public boolean readOrgNotice(OrgNoticeReadHistoryPO history) {
        try {
            history.setId(UUIDUtils.generateUUID());
            return noticeMapper.readOrgNotice(history) == 1;
        }
        catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }
    
    /**
     * 删除通知
     *
     * @param noticeId 通知ID
     *
     * @return 是否成功
     */
    @Override
    public boolean deleteOrgNotice(String noticeId) {
        return noticeMapper.deleteOrgNotice(noticeId) == 1 && noticeMapper.deleteOrgNoticeReadHistory(noticeId) >= 0;
    }
    
    /**
     * 获取通知已读成员列表
     *
     * @param noticeId 通知ID
     *
     * @return 已读成员列表
     */
    @Override
    public ArrayList<BasicUserDTO> getOrgNoticeReadHistory(String noticeId) {
        return noticeMapper.getOrgNoticeReadHistory(noticeId);
    }
}
