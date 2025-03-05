<template>
  <div class="index-view-container">
    <!--标题栏操作区-->
    <div class="app-header">
      <AppHeader/>
    </div>
    <!--内容主体-->
    <div class="app-body">
      <!--左侧导航栏-->
      <div class="nav-container">
        <AppNavigation/>
      </div>

      <!--模块显示区-->
      <div class="module-container">
        <RouterView/>
      </div>
    </div>
  </div>
</template>

<script setup>
    import { RouterView } from "vue-router";
    import AppNavigation from "../components/app-nav/AppNavigation.vue";
    import AppHeader from "../components/app-header/AppHeader.vue";
    import { useWebSocketStore } from "../store/WebSocketStore.js";
    import {
        onBeforeMount,
        onUnmounted,
        ref
    } from "vue";
    import { getCurUserData } from "../database/cur-user.js";

    const websocketStore = useWebSocketStore();

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    onBeforeMount(async () => {
        await updateCurLoginUser();
        if (curLoginUser.value === null) {
            // 如果当前登录用户信息为空则退出到登录页面
            // TODO 使用electron运行时记得取消相应注释
            windowController.hide();
            systemController.logout();
            windowController.show();
        }

        // 打开WebSocket连接
        websocketStore.initSocket(curLoginUser.value.userId);
    });

    onUnmounted(() => {
        // 退出时关闭WebSocket连接
        websocketStore.closeSocket();
    });
</script>

<style scoped
       lang="scss"
>
  .index-view-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;

    // 标题栏高度
    $app-header-height: 50px;

    .app-header {
      width: 100%;
      height: $app-header-height;
    }

    .app-body {
      display: flex;
      width: 100%;
      height: calc(100% - $app-header-height);

      $nav-container-width: 70px;

      .nav-container {
        width: $nav-container-width;
        height: 100%;
      }

      .module-container {
        width: calc(100% - $nav-container-width);
        height: 100%;
      }
    }
  }
</style>