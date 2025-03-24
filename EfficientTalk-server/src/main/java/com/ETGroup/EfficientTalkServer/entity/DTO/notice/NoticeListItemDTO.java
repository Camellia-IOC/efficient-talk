package com.ETGroup.EfficientTalkServer.entity.DTO.notice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeListItemDTO {
    private String id;
    private String title;
    private String content;
    private String orgId;
    private String orgName;
    private String DeptId;
    private String DeptName;
    private String publisher;
    private String publisherName;
    private LocalDateTime publishTime;
    private boolean hasRead;
}
