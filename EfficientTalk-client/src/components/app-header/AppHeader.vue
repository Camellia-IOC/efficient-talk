<template>
  <div class="app-header-container draggable">
    <div class="logo-container">
      <Logo :color="'#FFFFFF'"
            :size="35"
      />
    </div>
    <div class="toolbar-container">
      <div class="org-info"
           v-if="curLoginUserStore.curLoginUser.orgId!==null"
      >
        <img :src="orgInfo.orgLogo"
             alt="logo"
             class="org-logo"
             v-if="orgInfo.orgLogo!==null&&orgInfo.orgLogo!==undefined"
        >
        <div v-else
             class="org-logo"
             style="display: flex;justify-content: center;align-items: center;background-color: #1677FF"
        >
          <HomeOutlined style="font-size: 18px;color: white"/>
        </div>
        <label class="org-name">{{ orgInfo.orgName }}</label>
        <a-tag color="blue">组织归属</a-tag>
      </div>
      <div class="org-info"
           v-else
      >
        <a-tag>暂无归属组织</a-tag>
      </div>
      <div class="toolbar">
        <a-button class="toolbar-btn no-drag toolbar-btn-ai"
                  @click="openAiAssistantWindow"
        >
          <div>
            <AiAssistantIcon :color="'#FFFFFF'"
                             :size="20"
            />
          </div>
          <label>小易</label>
        </a-button>
      </div>
    </div>
    <div class="window-controller">
      <a-button class="control-btn window-header-btn no-drag"
                @click="windowMinimize"
      >
        <MinusOutlined/>
      </a-button>
      <a-button class="control-btn window-header-btn no-drag"
                @click="windowMaximize"
      >
        <CompressOutlined v-if="isMaximized"/>
        <ExpandOutlined v-else/>
      </a-button>
      <a-button class="control-btn window-header-btn no-drag"
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
        ref,
        watch
    } from "vue";
    import {
        MinusOutlined,
        CompressOutlined,
        ExpandOutlined,
        CloseOutlined,
        HomeOutlined,
    } from "@ant-design/icons-vue";
    import { Modal } from "ant-design-vue";
    import Logo from "../logo/Logo.vue";
    import SocialApi from "../../api/modules/SocialApi.js";
    import { openAiAssistantChildWindow } from "../../window-controller/controller/ChildWindowController.js";
    import MainWindowController from "../../window-controller/main-window-controller.js";
    import AiAssistantIcon from "../icon/AiAssistantIcon.vue";
    import { useCurLoginUserStore } from "../../store/CurLoginUserStore.js";

    // 窗口最大化状态
    const isMaximized = ref(false);

    // 关闭窗口
    const windowClose = () => {
        Modal.confirm({
            title: "退出",
            content: "您确定要退出吗？",
            onOk() {
                MainWindowController.closeMainWindow();
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
        MainWindowController.minimizeMainWindow();
    };

    // 最大化窗口
    const windowMaximize = () => {
        if (!isMaximized.value) {
            MainWindowController.maximizeMainWindow();
            isMaximized.value = true;
        }
        else {
            MainWindowController.recoverMainWindow();
            isMaximized.value = false;
        }
    };

    // 当前登录的用户信息
    const curLoginUserStore = useCurLoginUserStore();
    watch(() => curLoginUserStore.curLoginUser.orgId, () => {
        getOrgInfo();
    });

    // 获取组织信息
    const orgInfo = ref({
        orgId: null,
        orgName: null,
        orgLogo: null,
        diskId: null
    });
    const getOrgInfo = async () => {
        if (curLoginUserStore.curLoginUser.orgId !== null) {
            const response = await SocialApi.getOrganizationInfo({
                orgId: curLoginUserStore.curLoginUser.orgId
            });

            const res = response.data;
            if (res.code === 0) {
                if (res.data != null) {
                    orgInfo.value = res.data.orgInfo;
                }
            }
        }
    };

    // 打开AI助手窗口
    const openAiAssistantWindow = () => {
        const data = {
            userId: curLoginUserStore.curLoginUser.userId
        };
        openAiAssistantChildWindow(data);
    };

    onBeforeMount(async () => {
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
          border-radius: 8px;
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
      }
    }
  }
</style>