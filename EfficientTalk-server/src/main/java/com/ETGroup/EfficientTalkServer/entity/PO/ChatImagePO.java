package com.ETGroup.EfficientTalkServer.entity.PO;

import lombok.Data;

@Data
public class ChatImagePO {
    private String imageId;
    private String imageName;
    private String imagePath;
    private String imageType;
    private Long imageSize;
    private String sender;
    private String receiver;
    private String createTime;
}
