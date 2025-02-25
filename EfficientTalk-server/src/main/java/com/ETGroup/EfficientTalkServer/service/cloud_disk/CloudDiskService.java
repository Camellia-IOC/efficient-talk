package com.ETGroup.EfficientTalkServer.service.cloud_disk;

import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskBasicInfoResponseVO;
import com.ETGroup.EfficientTalkServer.entity.response.cloud_disk.CloudDiskLevelContentResponseVO;
import org.springframework.stereotype.Service;

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
     * @param parentId  父目录ID
     * @param pageIndex 页码
     * @param pageSize  每页大小
     *
     * @return 该层级的云盘内容
     */
    CloudDiskLevelContentResponseVO getCloudDiskLevelContent(String parentId, Integer pageIndex, Integer pageSize);
}
