package com.ETGroup.EfficientTalkServer.service.app_store;

import com.ETGroup.EfficientTalkServer.entity.PO.ApplicationPO;
import com.ETGroup.EfficientTalkServer.entity.response.app_store.AppStoreListResponseVO;
import com.ETGroup.EfficientTalkServer.mapper.AppStoreMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class AppStoreServiceImpl implements AppStoreService {
    @Resource
    private AppStoreMapper appStoreMapper;
    
    /**
     * 获取应用商店应用列表
     *
     * @param orgId 组织ID
     *
     * @return 应用列表
     */
    @Override
    public AppStoreListResponseVO getAppList(String orgId) {
        ArrayList<ApplicationPO> appList = appStoreMapper.getAppList(orgId);
        if (appList != null) {
            AppStoreListResponseVO response = new AppStoreListResponseVO();
            ArrayList<ApplicationPO> systemAppList = new ArrayList<>();
            ArrayList<ApplicationPO> orgAppList = new ArrayList<>();
            for (ApplicationPO app : appList) {
                if (app.getType()
                       .equals("SYSTEM")) {
                    systemAppList.add(app);
                }
                else {
                    orgAppList.add(app);
                }
            }
            response.setSystemAppList(systemAppList);
            response.setOrgAppList(orgAppList);
            return response;
        }
        return null;
    }
}
