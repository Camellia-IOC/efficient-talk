package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "云盘相关接口", description = "云盘相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/cloudDisk")
public class CloudDiskController {
}
