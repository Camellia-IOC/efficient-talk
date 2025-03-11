package com.ETGroup.EfficientTalkServer.service.cloud_disk;

import com.ETGroup.EfficientTalkServer.entity.request.cloud_disk.CreateCloudDiskFolderRequestParam;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskFileListResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskLevelContentResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CloudDiskService {
    /**
     * 获取云盘基础信息
     *
     * @param diskId 云盘ID
     *
     * @return 云盘基础信息
     */
    CloudDiskBasicInfoResponseVO getCloudDiskBasicInfo(String diskId);
    
    /**
     * 按层级获取云盘内容
     *
     * @param parentId   父目录ID
     * @param pageIndex  页码
     * @param pageSize   每页大小
     * @param orderByKey 排序字段
     * @param orderByType 排序方式
     *
     * @return 该层级的云盘内容
     */
    CloudDiskLevelContentResponseVO getCloudDiskLevelContent(String parentId,
                                                             Integer pageIndex,
                                                             Integer pageSize,
                                                             String orderByKey,
                                                             String orderByType);
    
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
    String uploadOrgCloudDiskFile(String fileId,
                                  String fileName,
                                  String folderId,
                                  String orgId,
                                  String diskId,
                                  String fileType,
                                  Long fileSize,
                                  String creatorId,
                                  MultipartFile file,
                                  String savePath);
    
    /**
     * 在组织云盘新建文件夹
     *
     * @param param 新建文件夹参数
     *
     * @return 是否成功
     */
    boolean createOrgCloudDiskFolder(CreateCloudDiskFolderRequestParam param);
    
    /**
     * 在组织云盘删除文件
     *
     * @param fileId 文件ID
     *
     * @return 是否成功
     */
    boolean deleteCloudDiskFile(String fileId);
    
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
    CloudDiskFileListResponseVO getRecentCloudDiskFiles(String orgId, String diskId, Integer pageIndex, Integer pageSize);
    
    /**
     * 删除组织云盘文件夹
     * @param folderId 文件夹ID
     * @return 是否成功
     */
    boolean deleteCloudDiskFolder(String folderId);
    
    /**
     * 重命名组织云盘文件夹
     * @param folderId 文件夹ID
     * @param newFolderName 文件夹名称
     * @return 是否成功
     */
    boolean renameOrgCloudDiskFolder(String folderId, String newFolderName);
    
    /**
     * 重命名组织云盘文件
     * @param fileId 文件ID
     * @param newFileName 文件名
     * @return 是否成功
     */
    boolean renameOrgCloudDiskFile(String fileId, String newFileName);
}
