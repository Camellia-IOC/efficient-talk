<template>
  <div class="app-header-container draggable">
    <div class="logo-container">
      <Logo :color="'#FFFFFF'"
            :size="35"
      />
    </div>
    <div class="toolbar-container">
      <div class="org-info">
        <img :src="orgInfo.orgLogo"
             alt="org-logo"
             class="org-logo"
        />
        <label class="org-name">{{ orgInfo.orgName }}</label>
        <a-tag color="blue">组织归属</a-tag>
      </div>
      <div class="toolbar">
        <a-button class="toolbar-btn no-drag toolbar-btn-ai"
                  @click="openAiAssistantWindow"
        >
          <SlackOutlined/>
          <label>小易</label>
        </a-button>
      </div>
    </div>
    <div class="window-controller">
      <a-button class="control-btn no-drag"
                @click="windowMinimize"
      >
        <MinusOutlined/>
      </a-button>
      <a-button class="control-btn no-drag"
                @click="windowMaximize"
      >
        <CompressOutlined v-if="isMaximized"/>
        <ExpandOutlined v-else/>
      </a-button>
      <a-button class="control-btn no-drag"
                @click="windowClose"
      >
        <CloseOutlined/>
      </a-button>
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import {
        MinusOutlined,
        CompressOutlined,
        ExpandOutlined,
        CloseOutlined,
        SlackOutlined
    } from "@ant-design/icons-vue";
    import { Modal } from "ant-design-vue";
    import Logo from "../logo/Logo.vue";
    import SocialApi from "../../api/modules/SocialApi.js";
    import { getCurUserData } from "../../database/cur-user.js";

    // 窗口最大化状态
    const isMaximized = ref(false);

    // 关闭窗口
    const windowClose = () => {
        // TODO:改成弹出子窗口
        Modal.confirm({
            title: "退出",
            content: "您确定要退出吗？",
            onOk() {
                windowController.close();
            },
            okText: "确定",
            okType: "danger",
            onCancel() {
            },
            cancelText: "取消",
            centered: true,
        });
    };

    // 最小化窗口
    const windowMinimize = () => {
        windowController.minimize();
    };

    // 最大化窗口
    const windowMaximize = () => {
        if (!isMaximized.value) {
            windowController.maximize();
            isMaximized.value = true;
        }
        else {
            windowController.recover();
            isMaximized.value = false;
        }
    };

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // 获取组织信息
    const orgInfo = ref({
        orgId: null,
        orgName: null,
        orgLogo: null,
        diskId: null
    });
    const getOrgInfo = async () => {
        const response = await SocialApi.getOrganizationInfo({
            orgId: curLoginUser.value.orgId
        });

        const res = response.data;
        if (res.code === 0) {
            if (res.data != null) {
                orgInfo.value = res.data.orgInfo;
            }
        }
    };

    // 打开AI助手窗口
    const openAiAssistantWindow = () => {
        const windowName = "aiAssistantWindow";
        const routerUrl = "child-window-ai-assistant";
        childWindowController.open({
            windowName: windowName,
            width: 400,
            height: 600,
            isChild: false,
            url: `/child-window?windowName=${windowName}&url=${routerUrl}`,
            config: {
                title: "小易",
                data: {
                    userId: curLoginUser.value.userId
                }
            }
        });
    };

    onBeforeMount(async () => {
        // 初始化当前登录的用户信息
        await updateCurLoginUser();

        await getOrgInfo();
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .app-header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: 100%;

    $logo-container-width: 70px;
    $window-controller-width: 100px;

    .logo-container {
      display: flex;
      justify-content: center;
      align-items: center;
      width: $logo-container-width;
      height: 100%;
      color: white;
      background-color: global-variable.$theme-color;
    }

    .toolbar-container {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: calc(100% - #{$logo-container-width} - #{$window-controller-width});
      height: 100%;
      padding: 15px;
      background-color: global-variable.$background-color;

      .org-info {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 10px;

        .org-logo {
          width: 30px;
          height: 30px;
          border-radius: 50%;
        }

        .org-name {
          text-align: center;
          font-weight: bold;
        }
      }

      .toolbar {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 10px;

        .toolbar-btn {
          display: flex;
          justify-content: center;
          align-items: center;
          color: black;
          font-size: 20px;
          border-radius: 12px;
          border: 0;
          box-shadow: none;
          background-color: transparent;
          gap: 5px;

          label {
            cursor: pointer;
            font-size: 16px;
          }

          &:hover {
            background-color: rgba(160, 160, 160, 0.1);
          }
        }

        .toolbar-btn-ai {
          color: white;
          background-color: global-variable.$theme-color;

          &:hover {
            background-color: rgba(global-variable.$theme-color, 0.8);
          }
        }
      }
    }

    .window-controller {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      width: $window-controller-width;
      height: 100%;
      background-color: global-variable.$background-color;
      padding-right: 10px;

      .control-btn {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 30px;
        height: 30px;
        color: black;
        border-radius: 50%;
        border: 0;
        box-shadow: none;
        background-color: transparent;

        &:hover {
          background-color: rgba(160, 160, 160, 0.1);
        }
      }
    }
  }
</style>