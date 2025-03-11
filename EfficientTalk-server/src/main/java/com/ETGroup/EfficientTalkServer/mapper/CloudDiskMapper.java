package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFileDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFolderDTO;
import com.ETGroup.EfficientTalkServer.entity.PO.CloudDiskFilePO;
import com.ETGroup.EfficientTalkServer.entity.PO.CloudDiskFolderPO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskFileListResponseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

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
                                                        @Param("pageIndex") Integer pageIndex,
                                                        @Param("pageSize") Integer pageSize);
    
    // 删除云盘文件夹
    Integer deleteCloudDiskFolder(@Param("folderId") String folderId);
    
    // 重命名组织云盘文件夹
    Integer renameOrgCloudDiskFolder(@Param("folderId") String folderId, @Param("newFolderName") String newFolderName);
    
    // 重命名组织云盘文件
    Integer renameOrgCloudDiskFile(@Param("fileId") String fileId, @Param("newFileName") String newFileName);
}
