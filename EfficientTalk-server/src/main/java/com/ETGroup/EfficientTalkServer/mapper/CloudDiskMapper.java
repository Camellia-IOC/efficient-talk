package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFileDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFolderDTO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskBasicInfoResponseVO;
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
                                                     @Param("pageSize") Integer pageSize);
    
    // 获取云盘内当前层级的文件数
    Integer getCurLevelFileCount(@Param("parentId") String parentId);
    
    // 获取云盘内当前层级的文件
    ArrayList<CloudDiskFileDTO> getCurLevelFiles(@Param("parentId") String parentId,
                                                 @Param("pageIndex") Integer pageIndex,
                                                 @Param("pageSize") Integer pageSize,
                                                 @Param("offset") Integer offset);
}
