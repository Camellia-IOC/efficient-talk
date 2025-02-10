<template>
  <div class="app-header-container theme-background-color draggable">
    <div class="logo-container">
      <DingdingOutlined style="font-size: 30px"/>
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
        CloseOutlined,
        DingdingOutlined,
    } from "@ant-design/icons-vue";
    import { Modal } from "ant-design-vue";

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
</script>

<style scoped
       lang="scss"
>
  .app-header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: 100%;

    $logo-container-width: 70px;

    .logo-container {
      display: flex;
      justify-content: center;
      align-items: center;
      width: $logo-container-width;
      height: 100%;
      color: white;
    }

    .window-controller {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      width: calc(100% - $logo-container-width);
      height: 100%;
      background-color: white;
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