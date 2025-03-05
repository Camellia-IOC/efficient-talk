package com.ETGroup.EfficientTalkServer.service.app_store;

import com.ETGroup.EfficientTalkServer.entity.response.app_store.AppStoreListResponseVO;
import org.springframework.stereotype.Service;

@Service
public interface AppStoreService {
    /**
     * 获取应用商店应用列表
     * @param orgId 组织ID
     * @return 应用列表
     */
    AppStoreListResponseVO getAppList(String orgId);
}
