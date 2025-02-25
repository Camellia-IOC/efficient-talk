package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskLevelContentResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.service.cloud_disk.CloudDiskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "云盘相关接口", description = "云盘相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/cloudDisk")
public class CloudDiskController {
    @Resource
    private CloudDiskService cloudDiskService;
    
    @Operation(summary = "获取云盘基础信息")
    @GetMapping("/getCloudDiskBasicInfo")
    public ResponseData<CloudDiskBasicInfoResponseVO> getCloudDiskBasicInfo(@RequestParam String diskId) {
        CloudDiskBasicInfoResponseVO response = cloudDiskService.getCloudDiskBasicInfo(diskId);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "按层级获取云盘内容")
    @GetMapping("/getCloudDiskLevelContent")
    public ResponseData<CloudDiskLevelContentResponseVO> getCloudDiskLevelContent(@RequestParam String parentId,
                                                                                  @RequestParam Integer pageIndex,
                                                                                  @RequestParam Integer pageSize) {
        CloudDiskLevelContentResponseVO response = cloudDiskService.getCloudDiskLevelContent(parentId, pageIndex, pageSize);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "在组织云盘上传文件")
    @PostMapping("/uploadOrgCloudDiskFile")
    public ResponseData<Object> uploadOrgCloudDiskFile() {
        return ResponseData.success();
    }
    
    @Operation(summary = "在组织云盘新建文件夹")
    @PostMapping("/createOrgCloudDiskFolder")
    public ResponseData<Object> createOrgCloudDiskFolder() {
        return ResponseData.success();
    }
}
