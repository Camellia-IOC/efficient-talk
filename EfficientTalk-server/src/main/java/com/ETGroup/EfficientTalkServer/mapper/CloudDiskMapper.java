package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFileDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFolderDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.CloudDiskFilePO;
import com.ETGroup.EfficientTalkServer.entity.PO.CloudDiskFolderPO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.FileDownloadInfoResponseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface CloudDiskMapper {
    // 获取云盘基础信息
    CloudDiskBasicInfoResponseVO getCloudDiskBasicInfo(String diskId);
    
    // 获取云盘内当前层级的目录数
    Integer getCurLevelFolderCount(@Param("parentId") String parentId);
    
    // 获取云盘内当前层级的目录
    ArrayList<CloudDiskFolderDTO> getCurLevelFolders(@Param("parentId") String parentId,
                                                     @Param("pageIndex") Integer pageIndex,
                                                     @Param("pageSize") Integer pageSize,
                                                     @Param("orderByKey") String orderByKey,
                                                     @Param("orderByType") String orderByType);
    
    // 获取云盘内当前层级的文件数
    Integer getCurLevelFileCount(@Param("parentId") String parentId);
    
    // 获取云盘内当前层级的文件
    ArrayList<CloudDiskFileDTO> getCurLevelFiles(@Param("parentId") String parentId,
                                                 @Param("pageIndex") Integer pageIndex,
                                                 @Param("pageSize") Integer pageSize,
                                                 @Param("offset") Integer offset,
                                                 @Param("orderByKey") String orderByKey,
                                                 @Param("orderByType") String orderByType);
    
    // 在组织云盘上传文件
    Integer uploadOrgCloudDiskFile(CloudDiskFilePO cloudDiskFile);
    
    // 根据ID获取文件夹路径
    String getFolderPath(@Param("folderId") String folderId);
    
    // 根据ID获取云盘路径
    String getDiskPath(@Param("diskId") String diskId);
    
    // 在组织云盘新建文件夹
    Integer createOrgCloudDiskFolder(CloudDiskFolderPO cloudDiskFolder);
    
    // 检查文件夹名称是否重复
    Integer checkSameFolderName(@Param("folderName") String folderName,
                                @Param("parentId") String parentId,
                                @Param("orgId") String orgId,
                                @Param("diskId") String diskId);
    
    // 删除云盘文件
    Integer deleteCloudDiskFile(@Param("fileId") String fileId);
    
    // 获取组织云盘最近文件
    ArrayList<CloudDiskFileDTO> getRecentCloudDiskFiles(@Param("orgId") String orgId,
                                                        @Param("diskId") String diskId,
                                                        @Param("pageSize") Integer pageSize);
    
    // 删除云盘文件夹
    Integer deleteCloudDiskFolder(@Param("folderId") String folderId);
    
    // 重命名组织云盘文件夹
    Integer renameOrgCloudDiskFolder(@Param("folderId") String folderId, @Param("newFolderName") String newFolderName);
    
    // 重命名组织云盘文件
    Integer renameOrgCloudDiskFile(@Param("fileId") String fileId, @Param("newFileName") String newFileName);
    
    /**
     * 根据文件ID获取文件路径
     *
     * @param fileId 文件ID
     *
     * @return 文件路径
     */
    String getFilePath(@Param("fileId") String fileId);
    
    /**
     * 获取组织云盘我的文件
     *
     * @param orgId     组织ID
     * @param diskId    云盘ID
     * @param userId    用户ID
     * @param pageIndex 页码
     * @param pageSize  每页数量
     *
     * @return 文件列表
     */
    ArrayList<CloudDiskFileDTO> getMyCloudDiskFiles(@Param("orgId") String orgId,
                                                    @Param("diskId") String diskId,
                                                    @Param("userId") String userId,
                                                    @Param("pageIndex") Integer pageIndex,
                                                    @Param("pageSize") Integer pageSize);
    
    /**
     * 获取组织云盘我的文件数量
     *
     * @param orgId  组织ID
     * @param diskId 云盘ID
     * @param userId 用户ID
     *
     * @return 文件数量
     */
    Integer getMyCloudDiskFilesCount(@Param("orgId") String orgId, @Param("diskId") String diskId, @Param("userId") String userId);
    
    /**
     * 获取组织云盘已使用容量
     *
     * @param diskId 云盘ID
     *
     * @return 已使用容量
     */
    Long getCloudDiskUsedCapacity(@Param("diskId") String diskId);
    
    /**
     * 获取组织云盘文件名称
     *
     * @param diskId 云盘ID
     * @param fileId 文件ID
     *
     * @return 组织云盘文件名称
     */
    Map<String, String> getFileName(@Param("diskId") String diskId, @Param("fileId") String fileId);
    
    /**
     * 获取组织云盘文件下载信息
     *
     * @param diskId 云盘ID
     * @param fileId 文件ID
     *
     * @return 文件信息
     */
    FileDownloadInfoResponseVO getFileDownloadInfo(@Param("diskId") String diskId, @Param("fileId") String fileId);
    
    // 创建组织云盘
    void createOrgCloudDisk(@Param("diskId") String diskId, @Param("orgId") String orgId, @Param("capacity") Long capacity);
}
