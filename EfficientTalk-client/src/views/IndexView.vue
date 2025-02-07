<template>
  <div class="index-view-container">
    <!--左侧导航栏-->
    <div class="nav-container">
      <AppNavigation/>
    </div>

    <!--模块显示区-->
    <div class="module-container">
      <RouterView/>
    </div>
  </div>
</template>

<script setup>
    import { RouterView } from "vue-router";
    import AppNavigation from "../components/app-nav/AppNavigation.vue";
</script>

<style scoped
       lang="scss"
>
  .index-view-container {
    display: flex;
    width: 100%;
    height: 100%;

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
</style>