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
    import { useWebSocketStore} from "../store/WebSocketStore.js";
    import {
        onBeforeMount
    } from "vue";

    const websocketStore = useWebSocketStore();
    onBeforeMount(()=>{
        websocketStore.initSocket();
    })
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