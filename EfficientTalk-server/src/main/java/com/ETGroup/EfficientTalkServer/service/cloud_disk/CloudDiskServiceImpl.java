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
import com.ETGroup.EfficientTalkServer.utils.MinIOUtils;
import com.ETGroup.EfficientTalkServer.utils.UUIDUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
public class CloudDiskServiceImpl implements CloudDiskService {
    @Resource
    private CloudDiskMapper cloudDiskMapper;
    
    @Resource
    private MinIOUtils minIOUtils;
    
    /**
     * 获取云盘基础信息
     *
     * @param diskId 云盘ID
     *
     * @return 云盘基础信息
     */
    @Override
    public CloudDiskBasicInfoResponseVO getCloudDiskBasicInfo(String diskId) {
        Long usedCapacity = cloudDiskMapper.getCloudDiskUsedCapacity(diskId);
        CloudDiskBasicInfoResponseVO result = cloudDiskMapper.getCloudDiskBasicInfo(diskId);
        result.setDiskSize(usedCapacity);
        return result;
    }
    
    /**
     * 按层级获取云盘内容
     *
     * @param parentId    父目录ID
     * @param pageIndex   页码
     * @param pageSize    每页大小
     * @param orderByKey  排序字段
     * @param orderByType 排序方式
     *
     * @return 该层级的云盘内容
     */
    @Override
    public CloudDiskLevelContentResponseVO getCloudDiskLevelContent(String parentId,
                                                                    Integer pageIndex,
                                                                    Integer pageSize,
                                                                    String orderByKey,
                                                                    String orderByType) {
        try {
            CloudDiskLevelContentResponseVO response = new CloudDiskLevelContentResponseVO();
            Integer folderCount = cloudDiskMapper.getCurLevelFolderCount(parentId);
            Integer fileCount = cloudDiskMapper.getCurLevelFileCount(parentId);
            ArrayList<CloudDiskFolderDTO> folders = cloudDiskMapper.getCurLevelFolders(parentId, pageIndex, pageSize, orderByKey, orderByType);
            ArrayList<CloudDiskFileDTO> files;
            
            if (folders.size() == pageSize) {
                files = new ArrayList<>();
            }
            else if (folders.isEmpty()) {
                files = cloudDiskMapper.getCurLevelFiles(parentId, pageIndex, pageSize, 0, orderByKey, orderByType);
            }
            else if (folders.size() < pageSize) {
                files = cloudDiskMapper.getCurLevelFiles(parentId, 1, (pageSize - folders.size()), 0, orderByKey, orderByType);
            }
            else {
                int startPageIndex = (folderCount - 1) / pageSize + 1;
                int rest = folderCount % pageSize;
                int offset = 0;
                if (rest != 0) {
                    offset = pageSize - rest;
                }
                files = cloudDiskMapper.getCurLevelFiles(parentId, pageIndex - startPageIndex, pageSize, offset, orderByKey, orderByType);
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
            if (minIOUtils.isBucketExist(diskId)) {
                String filePath = minIOUtils.getObjectUrl(diskId, minIOUtils.upload(diskId, fileId, file));
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
            }
            
            return null;
        }
        catch (Exception e) {
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
     * @param diskId 云盘ID
     * @param fileId 文件ID
     *
     * @return 是否成功
     */
    @Override
    public boolean deleteCloudDiskFile(String diskId, String fileId) {
        if (minIOUtils.isBucketExist(diskId)) {
            if (cloudDiskMapper.deleteCloudDiskFile(fileId) == 1) {
                return minIOUtils.remove(diskId, fileId);
            }
        }
        return false;
    }
    
    /**
     * 获取组织云盘最近文件
     *
     * @param orgId    组织ID
     * @param diskId   云盘ID
     * @param pageSize 页面大小
     *
     * @return 最近文件列表
     */
    @Override
    public CloudDiskFileListResponseVO getRecentCloudDiskFiles(String orgId, String diskId, Integer pageSize) {
        CloudDiskFileListResponseVO response = new CloudDiskFileListResponseVO();
        ArrayList<CloudDiskFileDTO> fileList = cloudDiskMapper.getRecentCloudDiskFiles(orgId, diskId, pageSize);
        response.setFileList(fileList);
        return response;
    }
    
    /**
     * 删除组织云盘文件夹
     *
     * @param folderId 文件夹ID
     *
     * @return 是否成功
     */
    @Override
    public boolean deleteCloudDiskFolder(String folderId) {
        return cloudDiskMapper.deleteCloudDiskFolder(folderId) == 1;
    }
    
    /**
     * 重命名组织云盘文件夹
     *
     * @param folderId      文件夹ID
     * @param newFolderName 文件夹名称
     *
     * @return 是否成功
     */
    @Override
    public boolean renameOrgCloudDiskFolder(String folderId, String newFolderName) {
        return cloudDiskMapper.renameOrgCloudDiskFolder(folderId, newFolderName) == 1;
    }
    
    /**
     * 重命名组织云盘文件
     *
     * @param fileId      文件ID
     * @param newFileName 文件名
     *
     * @return 是否成功
     */
    @Override
    public boolean renameOrgCloudDiskFile(String fileId, String newFileName) {
        return cloudDiskMapper.renameOrgCloudDiskFile(fileId, newFileName) == 1;
    }
    
    /**
     * 获取组织云盘我的文件
     *
     * @param orgId     组织ID
     * @param diskId    云盘ID
     * @param userId    用户ID
     * @param pageIndex 页面索引
     * @param pageSize  页面大小
     *
     * @return 文件列表
     */
    @Override
    public CloudDiskFileListResponseVO getMyCloudDiskFiles(String orgId, String diskId, String userId, Integer pageIndex, Integer pageSize) {
        CloudDiskFileListResponseVO response = new CloudDiskFileListResponseVO();
        Integer total = cloudDiskMapper.getMyCloudDiskFilesCount(orgId, diskId, userId);
        ArrayList<CloudDiskFileDTO> fileList = cloudDiskMapper.getMyCloudDiskFiles(orgId, diskId, userId, pageIndex, pageSize);
        response.setTotal(total);
        response.setFileList(fileList);
        return response;
    }
}
