package com.ETGroup.EfficientTalkServer.service.cloud_disk;

import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFileDTO;
import com.ETGroup.EfficientTalkServer.entity.DTO.cloud_disk.CloudDiskFolderDTO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskLevelContentResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.CloudDiskMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            else if (folders.size() < pageSize && !folders.isEmpty()) {
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
}
