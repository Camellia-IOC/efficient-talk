package com.ETGroup.EfficientTalkServer.entity.request.notice;

import lombok.Data;

@Data
public class ReadOrgNoticeRequestParam {
    private String userId;
    private String noticeId;
}
