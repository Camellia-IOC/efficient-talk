<template>
  <div class="app-store-view-container">
    <div class="loading-container"
         v-show="isAppListLoading"
    >
      <a-spin :spinning="isAppListLoading"
              size="large"
              :tip="'应用列表加载中...'"
      ></a-spin>
    </div>
    <div class="app-list-container"
         v-show="!isAppListLoading"
    >
      <div class="app-list-block">
        <div class="block-title">
          <NumberOutlined class="block-icon"/>
          <label class="block-label">系统应用</label>
        </div>
        <div class="app-list">
          <div class="app-list-item"
               v-for="(item,index) in appList.systemApp"
               :key="index"
               @click="handleAppOpen(item.id,item.appName,item.appUrl)"
          >
            <img :src="item.iconUrl"
                 alt="icon"
                 class="app-icon"
            >
            <label class="app-name single-line-ellipsis">{{ item.appName }}</label>
          </div>
        </div>
      </div>
      <div class="app-list-block">
        <div class="block-title">
          <NumberOutlined class="block-icon"/>
          <label class="block-label">组织应用</label>
        </div>
        <div class="app-list">
          <div class="app-list-item"
               v-for="(item,index) in appList.orgApp"
               :key="index"
               @click="handleAppOpen(item.id,item.appName,item.appUrl)"
          >
            <img :src="item.iconUrl"
                 alt="icon"
                 class="app-icon"
            >
            <label class="app-name single-line-ellipsis">{{ item.appName }}</label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import AppWindowController from "../window-controller/app-window-controller.js";
    import AppStoreApi from "../api/modules/AppStoreApi.js";
    import { getCurUserData } from "../database/cur-user.js";
    import { NumberOutlined } from "@ant-design/icons-vue";

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // 应用列表
    const isAppListLoading = ref(true);
    const appList = ref({
        systemApp: [],
        orgApp: []
    });

    // 启动应用
    const handleAppOpen = (appId, title, url) => {
        const config = {
            title: title,
            url: url,
            data: {}
        };
        AppWindowController.openAppWindow(appId, config);
    };

    // 获取应用商店列表
    const getAppList = async () => {
        isAppListLoading.value = true;
        const response = await AppStoreApi.getAppList({
            orgId: curLoginUser.value.orgId,
        });

        const res = response.data;
        if (res.code === 0) {
            appList.value.systemApp = res.data.systemAppList;
            appList.value.orgApp = res.data.orgAppList;
        }
        isAppListLoading.value = false;
    };

    onBeforeMount(async () => {
        await updateCurLoginUser();
        await getAppList();
    });
</script>

<style scoped
       lang="scss"
>
  @use '/src/assets/style/global-variable.scss';

  .app-store-view-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    border-top: global-variable.$border-line-width solid global-variable.$border-line-color;
    background-color: global-variable.$background-color;
    padding: 20px 30px;
    overflow-y: auto;

    .loading-container {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100%;
    }

    .app-list-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: 100%;
      gap: 20px;

      .app-list-block {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        gap: 10px;

        .block-title {
          display: flex;
          align-items: center;
          width: 100%;
          gap: 10px;

          .block-icon {
            font-size: 24px;
            color: global-variable.$theme-color;
          }

          .block-label {
            font-size: 24px;
          }
        }

        .app-list {
          display: grid;
          width: 100%;
          grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr;
          grid-template-rows: repeat(auto-fit, minmax(100px, 1fr));
          grid-column-gap: 10px;
          grid-row-gap: 10px;
          background-color: white;
          padding: 20px 10px;
          border-radius: 15px;
          place-items: center;

          .app-list-item {
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 90px;
            height: 90px;
            cursor: pointer;
            gap: 5px;
            //background-color: black;

            .app-icon {
              width: 60px;
              height: 60px;
              border-radius: 10px;
            }

            .app-name {
              font-size: 14px;
              cursor: pointer;
            }
          }
        }
      }
    }
  }
</style>