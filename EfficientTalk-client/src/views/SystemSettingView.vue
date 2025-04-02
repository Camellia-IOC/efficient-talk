<template>
  <div class="system-setting-view-container">
    <div class="setting-block-container">
      <label>系统通知</label>
      <div class="setting-area">
        <div class="setting-item">
          <label class="item-label">开启系统通知</label>
          <a-switch v-model:checked="systemSettingConfig.moduleNotice.enable"
                    @change="(check)=>{updateSystemSettingConfig('moduleNotice','enable',check)}"
          ></a-switch>
        </div>
      </div>
    </div>
    <div class="setting-block-container">
      <label>存储空间</label>
      <div class="setting-area">
        <div class="setting-item">
          <label class="item-label">启用本地存储</label>
          <a-switch v-model:checked="systemSettingConfig.moduleCache.localCache"
                    @change="(check)=>{updateSystemSettingConfig('moduleCache','localCache',check)}"
          ></a-switch>
        </div>
        <a-divider class="divider"/>
        <div class="setting-item">
          <label class="item-label">清理本地存储</label>
          <a-button size="small">清理</a-button>
        </div>
      </div>
    </div>
    <div class="setting-block-container">
      <label>下载管理</label>
      <div class="setting-area">
        <div class="setting-item">
          <div style="display: flex;align-items: center;gap: 20px;width: 85%">
            <label class="item-label">文件保存路径</label>
            <a-tooltip :color="themeColor"
                       placement="topLeft"
                       :overlay-inner-style="{width: 'fit-content'}"
            >
              <template #title>
                <div style="white-space: nowrap">{{ systemSettingConfig.moduleFile.downloadPath }}</div>
              </template>
              <label style="font-size: 14px;width: 75%;color: gray"
                     class="single-line-ellipsis"
              >{{ systemSettingConfig.moduleFile.downloadPath }}</label>
            </a-tooltip>
          </div>
          <a-button @click="handleDownloadPathChange"
                    size="small"
          >更改
          </a-button>
        </div>
      </div>
    </div>
    <div class="setting-block-container">
      <label>用户管理</label>
      <div class="setting-area">
        <div class="setting-item">
          <label class="item-label">自动登录</label>
          <a-switch v-model:checked="systemSettingConfig.moduleAuth.autoLogin"
                    @change="(check)=>{updateSystemSettingConfig('moduleAuth','autoLogin',check)}"
          ></a-switch>
        </div>
        <a-divider class="divider"/>
        <div class="setting-item">
          <label class="item-label">同意使用协议</label>
          <a-switch v-model:checked="systemSettingConfig.moduleAuth.acceptLicense"
                    @change="(check)=>{updateSystemSettingConfig('moduleAuth','acceptLicense',check)}"
          ></a-switch>
        </div>
        <a-divider class="divider"/>
        <div class="setting-item setting-list-item">
          <label class="item-label">历史登录用户</label>
          <div class="list-item"
               v-for="(item,index) in userList"
               :key="index"
          >
            <div style="display: flex;align-items: center;gap: 10px;">
              <img v-if="item.avatar!==null&&item.avatar!==undefined"
                   class="avatar"
                   :src="item.avatar"
                   alt="avatar"
              />
              <a-avatar v-else
                        class="avatar"
                        style="display:flex;justify-content:center;align-items:center;font-size: 24px"
              >{{ item.username.substring(0, 2) }}
              </a-avatar>
              <label style="font-size: 14px">{{ item.username }}</label>
            </div>
            <a-button danger
                      size="small"
            >删除
            </a-button>
          </div>
        </div>
      </div>
    </div>
    <div class="setting-block-container">
      <label>关于我们</label>
      <div class="setting-area">
        <div class="setting-item">
          <label class="item-label"
                 style="display: flex;align-items: center;gap: 10px;"
          >
            <CodeFilled style="color: #1677FF;"/>
            项目名称
          </label>
          <a-button type="link">易飞讯（EfficientTalk）</a-button>
        </div>
        <a-divider class="divider"/>
        <div class="setting-item">
          <label class="item-label"
                 style="display: flex;align-items: center;gap: 10px;"
          >
            <GithubOutlined/>
            GitHub仓库地址
          </label>
          <a-button type="link"
                    @click="MainWindowController.openExternalLink('https://github.com/Camellia-IOC/efficient-talk')"
          >https://github.com/Camellia-IOC/efficient-talk
          </a-button>
        </div>
        <a-divider class="divider"/>
        <div class="setting-item">
          <label class="item-label"
                 style="display: flex;align-items: center;gap: 10px;"
          >
            <HeartFilled style="color: red"/>
            给小易打个分吧
          </label>
          <a-rate v-model:value="appRateStar"
                  allow-half
          />
        </div>
        <a-divider class="divider"/>
        <div class="setting-item">
          <label class="item-label"
                 style="display: flex;align-items: center;gap: 10px;"
          >
            <QuestionCircleFilled style="color: gray"/>
            在使用中遇到问题
          </label>
          <a-button size="small"
                    @click="MainWindowController.openExternalLink('https://github.com/Camellia-IOC/efficient-talk/issues/new')"
          >反馈
          </a-button>
        </div>
        <a-divider class="divider"/>
        <div class="setting-item">
          <label class="item-label"
                 style="display: flex;align-items: center;gap: 10px;"
          >
            <CopyrightCircleOutlined style="color: black"/>
            版权所有
          </label>
          <label class="item-label" style="color: gray;">
            Copyright © 2021-2025 LittleQ. All Rights Reserved.
          </label>
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
    import MainWindowController from "../window-controller/main-window-controller.js";
    import { themeColor } from "../config/config.js";
    import UserApi from "../api/modules/UserApi.js";
    import { useCurLoginUserStore } from "../store/CurLoginUserStore.js";
    import { getUserHistoryList } from "../database/user-list.js";
    import {
        GithubOutlined,
        HeartFilled,
        CodeFilled,
        QuestionCircleFilled,
        CopyrightCircleOutlined
    } from "@ant-design/icons-vue";

    // 系统设置对象
    const systemSettingConfig = ref();
    const userList = ref([]);
    const appRateStar = ref(0);

    const curLoginUserStore = useCurLoginUserStore();

    // 更新系统设置
    const updateSystemSettingConfig = (module, key, value) => {
        const config = {
            userId: curLoginUserStore.curLoginUser.userId,
            module: module,
            key: key,
            value: value
        };
        MainWindowController.setSystemSettingConfig(config);
        UserApi.saveUserSystemSetting({
            userId: curLoginUserStore.curLoginUser.userId,
            config: JSON.stringify(systemSettingConfig.value)
        });
    };

    // 更改下载路径
    const handleDownloadPathChange = () => {
        MainWindowController.selectSystemPath().then((path) => {
            if (path) {
                const savePath = path[0];
                systemSettingConfig.value.moduleFile.downloadPath = savePath;
                updateSystemSettingConfig("moduleFile", "downloadPath", savePath);
            }
        });
    };

    onBeforeMount(async () => {
        systemSettingConfig.value = await MainWindowController.getSystemSettingConfig(curLoginUserStore.curLoginUser.userId);
        userList.value = await getUserHistoryList();
        console.log(systemSettingConfig.value);
        console.log(userList.value);
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .system-setting-view-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    border-top: global-variable.$border-line-width solid global-variable.$border-line-color;
    background-color: global-variable.$background-color;
    padding: 20px;
    gap: 30px;
    overflow-y: auto;

    .setting-block-container {
      display: flex;
      flex-direction: column;
      width: 70%;
      height: fit-content;
      gap: 10px;

      .setting-area {
        display: flex;
        flex-direction: column;
        width: 100%;
        height: fit-content;
        padding: 10px 15px;
        background-color: white;
        border-radius: 10px;

        .setting-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 100%;

          .item-label {
            font-size: 15px;
          }
        }

        .setting-list-item {
          flex-direction: column;
          justify-content: flex-start;
          align-items: flex-start;

          .list-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 100%;
            height: fit-content;
            padding: 8px 0;
          }

          .avatar {
            width: 36px;
            height: 36px;
            border-radius: 8px;
          }
        }
      }
    }
  }

  .divider {
    margin: 10px 0;
  }
</style>