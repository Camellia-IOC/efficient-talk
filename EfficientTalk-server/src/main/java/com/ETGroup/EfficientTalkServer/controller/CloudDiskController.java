package com.ETGroup.EfficientTalkServer.controller;

import com.ETGroup.EfficientTalkServer.entity.request.cloud_disk.CreateCloudDiskFolderRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.cloud_disk.RenameFileRequestParam;
import com.ETGroup.EfficientTalkServer.entity.request.cloud_disk.RenameFolderRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.*;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseConfig;
import com.ETGroup.EfficientTalkServer.entity.response.common.ResponseData;
import com.ETGroup.EfficientTalkServer.mapper.CloudDiskMapper;
import com.ETGroup.EfficientTalkServer.service.cloud_disk.CloudDiskService;
import com.ETGroup.EfficientTalkServer.utils.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
    
    @Resource
    private FileUtils fileUtils;
    
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
    public ResponseData<Void> deleteOrgCloudDiskFile(@RequestParam String diskId, @RequestParam String fileId) {
        boolean result = cloudDiskService.deleteCloudDiskFile(diskId, fileId);
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
                                                                             @RequestParam Integer pageSize) {
        CloudDiskFileListResponseVO response = cloudDiskService.getRecentCloudDiskFiles(orgId, diskId, pageSize);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "获取组织云盘我的文件")
    @GetMapping("/getMyCloudDiskFiles")
    public ResponseData<CloudDiskFileListResponseVO> getMyCloudDiskFiles(@RequestParam String orgId,
                                                                         @RequestParam String diskId,
                                                                         @RequestParam String userId,
                                                                         @RequestParam Integer pageIndex,
                                                                         @RequestParam Integer pageSize) {
        CloudDiskFileListResponseVO response = cloudDiskService.getMyCloudDiskFiles(orgId, diskId, userId, pageIndex, pageSize);
        return ResponseData.success(response);
    }
    
    @Operation(summary = "获取云盘文件Blob")
    @GetMapping("/getCloudDiskFileBlob")
    public ResponseEntity<byte[]> getCloudDiskFileBlob(@RequestParam String diskId, @RequestParam String fileId) {
        try {
            Map<String, String> fileInfo = cloudDiskMapper.getFileName(diskId, fileId);
            String fileName = fileId + "." + fileInfo.get("fileType");
            return fileUtils.getOSSFileBlob(diskId, fileName, fileInfo.get("fileName"));
        }
        catch (Exception e) {
            log.error("获取云盘文件Blob失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(summary = "获取云盘文件下载地址")
    @GetMapping("/getCloudDiskFileDownloadUrl")
    public ResponseData<FileDownloadInfoResponseVO> getCloudDiskFileDownloadUrl(@RequestParam String diskId, @RequestParam String fileId) {
        try {
            FileDownloadInfoResponseVO response = cloudDiskMapper.getFileDownloadInfo(diskId, fileId);
            return ResponseData.success(response);
        }
        catch (Exception e) {
            log.error("获取云盘文件下载地址失败", e);
            return ResponseData.error(ResponseConfig.ERROR);
        }
    }
}
