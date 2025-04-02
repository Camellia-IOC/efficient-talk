<template>
  <div class="app-container">
    <RouterView/>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import { useCurLoginUserStore } from "./store/CurLoginUserStore.js";
    import { getCurUserData } from "./database/cur-user.js";
    import { RouterView } from "vue-router";

    const curLoginUserStore = useCurLoginUserStore();

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    onBeforeMount(async () => {
        await updateCurLoginUser();
        curLoginUserStore.initUserData(curLoginUser.value);
    });
</script>

<style scoped
       lang="scss"
>
  .app-container {
    width: 100%;
    height: 100%;
    background-color: transparent;
  }
</style>
