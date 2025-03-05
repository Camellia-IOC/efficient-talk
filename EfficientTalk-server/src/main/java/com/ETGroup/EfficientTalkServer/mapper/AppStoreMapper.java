package com.ETGroup.EfficientTalkServer.mapper;

import com.ETGroup.EfficientTalkServer.entity.PO.ApplicationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface AppStoreMapper {
    /**
     * 获取应用商店应用列表
     * @param orgId 组织ID
     * @return 应用列表
     */
    ArrayList<ApplicationPO> getAppList(@Param("orgId") String orgId);
}
