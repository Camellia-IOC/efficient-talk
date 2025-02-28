<template>
  <div class="cloud-disk-view-container">
    <!--导航栏-->
    <div class="nav-bar">
      <!--组织信息-->
      <div class="org-info">
        <div class="org-logo">
          <img :src="orgInfo.orgLogo"
               alt="logo"
               class="logo"
          >
        </div>
        <div class="org-detail">
          <div class="org-name">{{ orgInfo.orgName }}</div>
          <div class="disk-tag">
            <a-tag>组织云盘</a-tag>
          </div>
        </div>
      </div>

      <!--导航列表-->
      <div class="nav-list-container">
        <div v-for="(item,index) in navItemList"
             :key="index"
             class="nav-item"
             :class="{'nav-item-active': curSelectedNavIndex === item.index}"
             @click="handleSelectNavItem(item.index, item.path)"
        >
          {{ item.name }}
        </div>
      </div>

      <div class="disk-info">
        <div class="info-label">
          <label>已使用：{{ translateFileSize(capacityConfig.usedCapacity) }}</label>
          <label>总容量：{{ translateFileSize(capacityConfig.totalCapacity) }}</label>
        </div>
        <div class="info-chart">
          <a-progress :percent="capacityConfig.percent"
                      size="small"
                      :show-info="false"
                      :stroke-color="capacityConfig.getRestCapacityColor()"
          />
        </div>
      </div>
    </div>

    <!--内容显示区域-->
    <div class="content-container">
      <RouterView/>
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import { useRouter } from "vue-router";
    import { message } from "ant-design-vue";
    import SocialApi from "../api/modules/SocialApi.js";
    import { getCurUserData } from "../database/cur-user.js";
    import CloudDiskApi from "../api/modules/CloudDiskApi.js";
    import { translateFileSize } from "../utils/unit-utils.js";

    const router = useRouter();

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // 容量进度条配置
    const capacityConfig = ref({
        // 已使用空间
        usedCapacity: 0,
        // 总容量
        totalCapacity: 0,
        // 容量百分比
        percent: 0,
        // 容量百分比对应的颜色
        getRestCapacityColor: () => {
            if (capacityConfig.value.percent < 60) {
                return "#52c41a";
            }
            else if (capacityConfig.value.percent < 80) {
                return "#faad14";
            }
            else {
                return "#f5222d";
            }
        }
    });

    // 导航项配置
    const navItemList = [
        {
            index: 1,
            name: "最近文件",
            path: "/app/cloud-disk/recent-files"
        },
        {
            index: 2,
            name: "我的文件",
            path: "/app/cloud-disk/my-files"
        },
        {
            index: 3,
            name: "组织文件",
            path: "/app/cloud-disk/org-files"
        },
        {
            index: 4,
            name: "聊天文件",
            path: "/app/cloud-disk/chat-files"
        }
    ];

    // 选择导航
    const curSelectedNavIndex = ref(1);
    const handleSelectNavItem = (index, path) => {
        curSelectedNavIndex.value = index;
        router.push({
            path: path,
            query: {
                orgId: orgInfo.value.orgId,
                diskId: orgInfo.value.diskId
            }
        });
    };

    // 获取组织信息
    const orgInfo = ref({});
    const getOrganizationInfo = async () => {
        const response = await SocialApi.getOrganizationInfo({
            orgId: curLoginUser.value.orgId
        });

        const res = response.data;
        if (res.code === 0) {
            if (res.data != null) {
                orgInfo.value = res.data.orgInfo;
            }
        }
        else {
            message.error("获取组织数据失败");
            orgInfo.value = {};
        }
    };

    //获取云盘信息
    const getCloudDiskBasicInfo = async () => {
        const response = await CloudDiskApi.getCloudDiskBasicInfo({
            diskId: orgInfo.value.diskId
        });

        const res = response.data;
        if (res.code === 0) {
            if (res.data != null) {
                capacityConfig.value.usedCapacity = res.data.diskSize;
                capacityConfig.value.totalCapacity = res.data.diskCapacity;
                capacityConfig.value.percent = (res.data.diskSize / res.data.diskCapacity) * 100;
            }
        }
        else {
            message.error("获取云盘数据失败");
        }
    };

    onBeforeMount(async () => {
        // 初始化当前登录的用户信息
        await updateCurLoginUser();

        // 获取组织信息
        await getOrganizationInfo();

        await getCloudDiskBasicInfo();
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .cloud-disk-view-container {
    display: flex;
    align-items: center;
    width: 100%;
    height: 100%;
    border-top: global-variable.$border-line-width solid global-variable.$border-line-color;
    background-color: global-variable.$background-color;

    .nav-bar {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 250px;
      height: 100%;
      border-right: global-variable.$border-line-width solid global-variable.$border-line-color;
      background-color: transparent;

      $org-info-container-height: 120px;
      $disk-nfo-container-height: 100px;

      .org-info {
        display: flex;
        align-items: center;
        width: 100%;
        height: 120px;
        padding: 20px;
        gap: 15px;

        .org-logo {
          display: flex;
          justify-content: center;
          align-items: center;

          .logo {
            $logo-size: 80px;
            width: $logo-size;
            height: $logo-size;
            border-radius: 50%;
          }
        }

        .org-detail {
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 15px;

          .org-name {
            display: flex;
            justify-content: flex-start;
            align-items: center;
            font-weight: bold;
            font-size: 20px;
          }

          .disk-tag {
            display: flex;
            justify-content: flex-start;
            align-items: center;
          }
        }
      }

      .nav-list-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: calc(100% - #{$org-info-container-height} - #{$disk-nfo-container-height});
        padding: 20px;
        gap: 10px;

        .nav-item {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 100%;
          height: 40px;
          background-color: #F2F3F5;
          border-radius: 10px;
          font-size: 14px;
          cursor: pointer;
          transition: all 0.3s ease;
        }

        .nav-item-active {
          background-color: global-variable.$theme-color;
          color: white;
        }
      }

      .disk-info {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: $disk-nfo-container-height;
        padding: 20px;

        .info-label {
          display: flex;
          flex-direction: column;
          align-items: center;
          width: 100%;

          label {
            color: gray;
            font-size: 14px;
            width: 100%;
          }
        }

        .info-chart {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 100%;
        }
      }
    }

    .content-container {
      display: flex;
      align-items: center;
      width: calc(100% - 250px);
      height: 100%;
      background-color: transparent;
    }
  }

  :deep(.ant-progress) {
    margin: 0;
  }
</style>