<template>
  <div class="child-window-header-container draggable">
    <div class="title-container">
      <div class="logo">
        <Logo :color="themeColor"
              :size="25"
        />
      </div>
      <label class="title">{{ config.title }}</label>
    </div>
    <div class="window-controller">
      <a-button class="window-header-btn no-drag"
                @click="windowMinimize"
      >
        <MinusOutlined/>
      </a-button>
      <a-button class="window-header-btn no-drag"
                @click="windowMaximize"
      >
        <CompressOutlined v-if="isMaximized"/>
        <ExpandOutlined v-else/>
      </a-button>
      <a-button class="window-header-btn no-drag"
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
    import ChildWindowController from "../../../window-controller/child-window-controller.js";

    const props = defineProps({
        windowName: {
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
        ChildWindowController.closeChildWindow(props.windowName);
    };

    // 最小化窗口
    const windowMinimize = () => {
        ChildWindowController.minimizeChildWindow(props.windowName);
    };

    // 最大化窗口
    const windowMaximize = () => {
        if (!isMaximized.value) {
            ChildWindowController.maximizeChildWindow(props.windowName);
            isMaximized.value = true;
        }
        else {
            ChildWindowController.recoverChildWindow(props.windowName);
            isMaximized.value = false;
        }
    };
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .child-window-header-container {
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

      .title {
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
    }
  }
</style>