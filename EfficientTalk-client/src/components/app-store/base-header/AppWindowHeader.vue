<template>
  <div class="app-window-header-container draggable">
    <div class="title-container">
      <div class="logo">
        <Logo :color="themeColor"
              :size="25"
        />
      </div>
      <label class="title">{{ config.title }}</label>
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
    import { ref } from "vue";
    import {
        MinusOutlined,
        CompressOutlined,
        ExpandOutlined,
        CloseOutlined
    } from "@ant-design/icons-vue";
    import Logo from "../../logo/Logo.vue";
    import { themeColor } from "../../../config/config.js";
    import AppWindowController from "../../../window-controller/app-window-controller.js";

    const props = defineProps({
        appId: {
            type: String,
            default: ""
        },
        config: {
            type: Object,
            default: {
                title: "易飞讯",
                data: {}
            }
        }
    });

    // 窗口最大化状态
    const isMaximized = ref(false);

    // 关闭窗口
    const windowClose = () => {
        AppWindowController.closeAppWindow(props.appId);
    };

    // 最小化窗口
    const windowMinimize = () => {
        AppWindowController.minimizeAppWindow(props.appId);
    };

    // 最大化窗口
    const windowMaximize = () => {
        if (!isMaximized.value) {
            AppWindowController.maximizeAppWindow(props.appId);
            isMaximized.value = true;
        }
        else {
            AppWindowController.recoverAppWindow(props.appId);
            isMaximized.value = false;
        }
    };
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .app-window-header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: global-variable.$background-color;

    $title-container-width: 80%;

    .title-container {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      width: $title-container-width;
      height: 100%;
      padding-left: 20px;

      .logo {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 5px;
      }

      .title{
        font-size: 14px;
        font-weight: bold;
      }
    }

    .window-controller {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      width: calc(100% - $title-container-width);
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