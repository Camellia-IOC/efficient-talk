package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.response.app_store.AppStoreListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.service.app_store.AppStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "应用商店相关接口", description = "应用商店相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/appStore")
public class AppStoreController {
    @Resource
    private AppStoreService appStoreService;
    
    @Operation(summary = "获取应用商店应用列表")
    @GetMapping("/getAppList")
    public ResponseData<AppStoreListResponseVO> getAppList(@RequestParam String orgId) {
        AppStoreListResponseVO response = appStoreService.getAppList(orgId);
        if (response != null) {
            return ResponseData.success(response);
        }
        else {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
}
