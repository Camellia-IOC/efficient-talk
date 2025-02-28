package com.ETGroup.EfficientTalkServer.service.cloud_disk;

import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFileDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFolderDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.CloudDiskFilePO;
import com.ETGroup.EfficientTalkServer.entity.PO.CloudDiskFolderPO;
import com.ETGroup.EfficientTalkServer.entity.request.cloud_disk.CreateCloudDiskFolderRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskFileListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskLevelContentResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.CloudDiskMapper;
import com.ETGroup.EfficientTalkServer.utils.UUIDUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
public class CloudDiskServiceImpl implements CloudDiskService {
    @Resource
    private CloudDiskMapper cloudDiskMapper;
    
    /**
     * 获取云盘基础信息
     *
     * @param diskId 云盘ID
     *
     * @return 云盘基础信息
     */
    @Override
    public CloudDiskBasicInfoResponseVO getCloudDiskBasicInfo(String diskId) {
        return cloudDiskMapper.getCloudDiskBasicInfo(diskId);
    }
    
    /**
     * 按层级获取云盘内容
     *
     * @param parentId  父目录ID
     * @param pageIndex 页码
     * @param pageSize  每页大小
     *
     * @return 该层级的云盘内容
     */
    @Override
    public CloudDiskLevelContentResponseVO getCloudDiskLevelContent(String parentId, Integer pageIndex, Integer pageSize) {
        try {
            CloudDiskLevelContentResponseVO response = new CloudDiskLevelContentResponseVO();
            Integer folderCount = cloudDiskMapper.getCurLevelFolderCount(parentId);
            Integer fileCount = cloudDiskMapper.getCurLevelFileCount(parentId);
            ArrayList<CloudDiskFolderDTO> folders = cloudDiskMapper.getCurLevelFolders(parentId, pageIndex, pageSize);
            ArrayList<CloudDiskFileDTO> files;
            
            if (folders.size() == pageSize) {
                files = new ArrayList<>();
            }
            else if (folders.isEmpty()) {
                files = cloudDiskMapper.getCurLevelFiles(parentId, pageIndex, pageSize, 0);
            }
            else if (folders.size() < pageSize) {
                files = cloudDiskMapper.getCurLevelFiles(parentId, 1, (pageSize - folders.size()), 0);
            }
            else {
                int startPageIndex = (folderCount - 1) / pageSize + 1;
                int rest = folderCount % pageSize;
                int offset = 0;
                if (rest != 0) {
                    offset = pageSize - rest;
                }
                files = cloudDiskMapper.getCurLevelFiles(parentId, pageIndex - startPageIndex, pageSize, offset);
            }
            
            response.setFolderCount(folderCount);
            response.setFileCount(fileCount);
            response.setFolders(folders);
            response.setFiles(files);
            return response;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 在组织云盘上传文件
     *
     * @param fileId    文件ID
     * @param fileName  文件名
     * @param folderId  文件夹ID
     * @param orgId     组织ID
     * @param diskId    云盘ID
     * @param fileType  文件类型
     * @param fileSize  文件大小
     * @param creatorId 创建者ID
     * @param file      文件
     *
     * @return 文件路径
     */
    @Override
    public String uploadOrgCloudDiskFile(String fileId,
                                         String fileName,
                                         String folderId,
                                         String orgId,
                                         String diskId,
                                         String fileType,
                                         Long fileSize,
                                         String creatorId,
                                         MultipartFile file,
                                         String savePath) {
        try {
            String targetFolder = System.getProperty("user.dir") + "\\resources\\cloud_disk\\" + savePath;
            File saveFile = new File(targetFolder + file.getOriginalFilename());
            file.transferTo(saveFile);
            String filePath = targetFolder + file.getOriginalFilename();
            LocalDateTime updateTime = LocalDateTime.now();
            
            CloudDiskFilePO cloudDiskFile = new CloudDiskFilePO();
            cloudDiskFile.setFileId(fileId);
            cloudDiskFile.setFileName(fileName);
            cloudDiskFile.setFolderId(folderId);
            cloudDiskFile.setOrgId(orgId);
            cloudDiskFile.setDiskId(diskId);
            cloudDiskFile.setFileType(fileType);
            cloudDiskFile.setFileSize(fileSize);
            cloudDiskFile.setFilePath(filePath);
            cloudDiskFile.setCreator(creatorId);
            cloudDiskFile.setUpdateTime(updateTime);
            
            if (cloudDiskMapper.uploadOrgCloudDiskFile(cloudDiskFile) == 1) {
                return filePath;
            }
            return null;
        }
        catch (IOException e) {
            log.error("上传文件至组织云盘失败:{}", e.toString());
            return null;
        }
    }
    
    /**
     * 在组织云盘新建文件夹
     *
     * @param param 新建文件夹参数
     *
     * @return 是否成功
     */
    @Override
    public boolean createOrgCloudDiskFolder(CreateCloudDiskFolderRequestParam param) {
        String folderId = UUIDUtils.generateUUID();
        String parentPath;
        if (param.getParentId()
                 .equals(param.getDiskId())) {
            parentPath = cloudDiskMapper.getDiskPath(param.getDiskId());
        }
        else {
            parentPath = cloudDiskMapper.getFolderPath(param.getParentId());
        }
        String folderPath = parentPath + "\\" + folderId;
        File folder = new File(folderPath);
        boolean isCreated = folder.mkdir();
        if (isCreated) {
            CloudDiskFolderPO cloudDiskFolder = new CloudDiskFolderPO();
            cloudDiskFolder.setFolderId(folderId);
            cloudDiskFolder.setFolderName(param.getFolderName());
            cloudDiskFolder.setParentId(param.getParentId());
            cloudDiskFolder.setOrgId(param.getOrgId());
            cloudDiskFolder.setDiskId(param.getDiskId());
            cloudDiskFolder.setCreator(param.getCreator());
            cloudDiskFolder.setUpdateTime(LocalDateTime.now());
            cloudDiskFolder.setFolderPath(folderPath);
            return cloudDiskMapper.createOrgCloudDiskFolder(cloudDiskFolder) == 1;
        }
        return false;
    }
    
    /**
     * 在组织云盘删除文件
     *
     * @param fileId 文件ID
     *
     * @return 是否成功
     */
    @Override
    public boolean deleteCloudDiskFile(String fileId) {
        return cloudDiskMapper.deleteCloudDiskFile(fileId) == 1;
    }
    
    /**
     * 获取组织云盘最近文件
     *
     * @param orgId     组织ID
     * @param diskId    云盘ID
     * @param pageIndex 页码
     * @param pageSize  页面大小
     *
     * @return 最近文件列表
     */
    @Override
    public CloudDiskFileListResponseVO getRecentCloudDiskFiles(String orgId, String diskId, Integer pageIndex, Integer pageSize) {
        CloudDiskFileListResponseVO response = new CloudDiskFileListResponseVO();
        ArrayList<CloudDiskFileDTO> fileList = cloudDiskMapper.getRecentCloudDiskFiles(orgId, diskId, pageIndex, pageSize);
        response.setFileList(fileList);
        return response;
    }
}
