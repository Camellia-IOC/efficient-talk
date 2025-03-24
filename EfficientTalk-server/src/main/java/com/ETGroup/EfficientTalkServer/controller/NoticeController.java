package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.DTO.notice.NoticeListItemDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.user.BasicUserDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.OrgNoticePO;
import com.ETGroup.EfficientTalkServer.entity.PO.OrgNoticeReadHistoryPO;
import com.ETGroup.EfficientTalkServer.entity.request.notice.ReadOrgNoticeRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.service.notice.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Tag(name = "通知相关接口", description = "通知相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;
    
    @Operation(summary = "发布通知")
    @PostMapping("/publishOrgNotice")
    public ResponseData<Boolean> publishOrgNotice(@RequestBody OrgNoticePO notice) {
        boolean response = noticeService.publishOrgNotice(notice);
        if (response) {
            return ResponseData.success(true);
        }
        else {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "获取通知列表")
    @GetMapping("/getOrgNoticeList")
    public ResponseData<ArrayList<NoticeListItemDTO>> getOrgNoticeList(@RequestParam String userId, @RequestParam String orgId) {
        ArrayList<NoticeListItemDTO> response = noticeService.getOrgNoticeList(userId, orgId);
        if (response != null) {
            return ResponseData.success(response);
        }
        else {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "已读通知")
    @PostMapping("/readOrgNotice")
    public ResponseData<Boolean> readOrgNotice(@RequestBody ReadOrgNoticeRequestParam param) {
        OrgNoticeReadHistoryPO history = new OrgNoticeReadHistoryPO();
        history.setUserId(param.getUserId());
        history.setNoticeId(param.getNoticeId());
        boolean response = noticeService.readOrgNotice(history);
        if (response) {
            return ResponseData.success(true);
        }
        else {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "删除通知")
    @DeleteMapping("/deleteOrgNotice")
    public ResponseData<Boolean> deleteOrgNotice(@RequestParam String noticeId) {
        boolean response = noticeService.deleteOrgNotice(noticeId);
        if (response) {
            return ResponseData.success(true);
        }
        else {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "获取通知已读成员列表")
    @GetMapping("/getOrgNoticeReadHistory")
    public ResponseData<ArrayList<BasicUserDTO>> getOrgNoticeReadHistory(@RequestParam String noticeId) {
        ArrayList<BasicUserDTO> response = noticeService.getOrgNoticeReadHistory(noticeId);
        if (response != null) {
            return ResponseData.success(response);
        }
        else {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
}
