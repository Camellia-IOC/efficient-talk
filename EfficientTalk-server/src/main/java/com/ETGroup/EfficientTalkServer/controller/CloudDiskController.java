package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.request.cloud_disk.CreateCloudDiskFolderRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.cloud_disk.RenameFileRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.cloud_disk.RenameFolderRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskFileListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskLevelContentResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.UploadCloudDiskFileResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.mapper.CloudDiskMapper;
import com.ETGroup.EfficientTalkServer.service.cloud_disk.CloudDiskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "云盘相关接口", description = "云盘相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/cloudDisk")
public class CloudDiskController {
    @Resource
    private CloudDiskService cloudDiskService;
    
    @Resource
    private CloudDiskMapper cloudDiskMapper;
    
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
                                                                                  @RequestParam Integer pageSize,
                                                                                  @RequestParam(required = false) String orderByKey,
                                                                                  @RequestParam(required = false) String orderByType) {
        CloudDiskLevelContentResponseVO response = cloudDiskService.getCloudDiskLevelContent(parentId, pageIndex, pageSize, orderByKey, orderByType);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "在组织云盘上传文件")
    @PostMapping("/uploadOrgCloudDiskFile")
    public ResponseData<UploadCloudDiskFileResponseVO> uploadOrgCloudDiskFile(@RequestParam String fileId,
                                                                              @RequestParam String fileName,
                                                                              @RequestParam String folderId,
                                                                              @RequestParam String orgId,
                                                                              @RequestParam String diskId,
                                                                              @RequestParam String fileType,
                                                                              @RequestParam Long fileSize,
                                                                              @RequestParam String creatorId,
                                                                              @RequestParam MultipartFile file,
                                                                              @RequestParam String savePath) {
        String filePath = cloudDiskService.uploadOrgCloudDiskFile(fileId, fileName, folderId, orgId, diskId, fileType, fileSize, creatorId, file,
                                                                  savePath);
        if (filePath != null) {
            UploadCloudDiskFileResponseVO response = new UploadCloudDiskFileResponseVO();
            response.setFilePath(filePath);
            return ResponseData.success(response);
        }
        else {
            return ResponseData.error(ResponseConfig.RESOURCE_UPLOAD_FAILED);
        }
    }
    
    @Operation(summary = "在组织云盘新建文件夹")
    @PostMapping("/createOrgCloudDiskFolder")
    public ResponseData<Void> createOrgCloudDiskFolder(@RequestBody CreateCloudDiskFolderRequestParam param) {
        if (cloudDiskMapper.checkSameFolderName(param.getFolderName(), param.getParentId(), param.getOrgId(), param.getDiskId()) == 0) {
            // TODO 验证名称是否合法
            if (false) {
                return ResponseData.error(ResponseConfig.RESOURCE_NAME_INVALID_ERROR);
            }
            else {
                boolean result = cloudDiskService.createOrgCloudDiskFolder(param);
                if (result) {
                    return ResponseData.success();
                }
                else {
                    return ResponseData.error(ResponseConfig.RESOURCE_UPLOAD_FAILED);
                }
            }
        }
        else {
            return ResponseData.error(ResponseConfig.RESOURCE_NAME_SAME_ERROR);
        }
    }
    
    @Operation(summary = "重命名组织云盘文件夹")
    @PutMapping("/renameOrgCloudDiskFolder")
    public ResponseData<Void> renameOrgCloudDiskFolder(@RequestBody RenameFolderRequestParam param) {
        boolean result = cloudDiskService.renameOrgCloudDiskFolder(param.getFolderId(), param.getNewFolderName());
        if (result) {
            return ResponseData.success();
        }
        else {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "在组织云盘删除文件夹")
    @DeleteMapping("/deleteOrgCloudDiskFolder")
    public ResponseData<Void> deleteOrgCloudDiskFolder(@RequestParam String folderId) {
        boolean result = cloudDiskService.deleteCloudDiskFolder(folderId);
        if (result) {
            return ResponseData.success();
        }
        else {
            return ResponseData.error(ResponseConfig.RESOURCE_DELETE_FAILED);
        }
    }
    
    @Operation(summary = "重命名组织云盘文件")
    @PutMapping("/renameOrgCloudDiskFile")
    public ResponseData<Void> renameOrgCloudDisFile(@RequestBody RenameFileRequestParam param) {
        boolean result = cloudDiskService.renameOrgCloudDiskFile(param.getFileId(), param.getNewFileName());
        if (result) {
            return ResponseData.success();
        }
        else {
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
    
    @Operation(summary = "在组织云盘删除文件")
    @DeleteMapping("/deleteOrgCloudDiskFile")
    public ResponseData<Void> deleteOrgCloudDiskFile(@RequestParam String fileId) {
        boolean result = cloudDiskService.deleteCloudDiskFile(fileId);
        if (result) {
            return ResponseData.success();
        }
        else {
            return ResponseData.error(ResponseConfig.RESOURCE_DELETE_FAILED);
        }
    }
    
    @Operation(summary = "获取组织云盘最近文件")
    @GetMapping("/getRecentCloudDiskFiles")
    public ResponseData<CloudDiskFileListResponseVO> getRecentCloudDiskFiles(@RequestParam String orgId,
                                                                             @RequestParam String diskId,
                                                                             @RequestParam Integer pageIndex,
                                                                             @RequestParam Integer pageSize) {
        CloudDiskFileListResponseVO response = cloudDiskService.getRecentCloudDiskFiles(orgId, diskId, pageIndex, pageSize);
        return ResponseData.success(response);
    }
}
