package com.ETGroup.EfficientTalkServer.entity.response.app_store;

import com.ETGroup.EfficientTalkServer.entity.PO.ApplicationPO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class AppStoreListResponseVO {
    private ArrayList<ApplicationPO> systemAppList;
    private ArrayList<ApplicationPO> orgAppList;
}
