<template>
  <div class="app-container">
    <!--标题栏操作区-->
    <div class="app-header">
      <AppHeader/>
    </div>
    <!--内容主体-->
    <div class="app-body">
      <RouterView/>
    </div>
  </div>
</template>

<script setup>
    import { RouterView } from "vue-router";
    import AppHeader from "./components/app-header/AppHeader.vue";
</script>

<style scoped
       lang="scss"
>
  .app-container {
    width: 100%;
    height: 100%;

    // 标题栏高度
    $app-header-height: 50px;

    .app-header {
      width: 100%;
      height: $app-header-height;
    }

    .app-body {
      width: 100%;
      height: calc(100% - $app-header-height);
    }
  }
</style>
