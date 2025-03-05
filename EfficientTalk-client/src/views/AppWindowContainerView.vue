<template>
  <div class="app-window-container-view">
    <div class="app-window-header">
      <AppWindowHeader :app-id="appId"
                       :config="appWindowConfig"
      />
    </div>
    <div class="app-window-content"
         v-show="isAppLoading"
    >
      <a-spin size="large" tip="应用加载中，请耐心等待~"/>
    </div>
    <div id="app-container"
         class="app-window-content"
         v-show="!isAppLoading"
    ></div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import AppWindowHeader from "../components/app-store/base-header/AppWindowHeader.vue";
    import {
        useRoute
    } from "vue-router";
    import AppWindowController from "../window-controller/app-window-controller.js";

    const route = useRoute();

    // 子窗口配置
    const isAppLoading = ref(true);
    const appId = ref(null);
    const appWindowConfig = ref({
        title: "易飞讯",
        url: "",
        data: {}
    });

    // 处理应用加载
    const handleAppLoad = () => {
        isAppLoading.value = false;
    };

    onBeforeMount(async () => {
        appId.value = route.query.appId;
        appWindowConfig.value = await AppWindowController.getAppWindowConfig(appId.value);

        // 创建并挂载iframe对象
        const appFrame = document.createElement("iframe");
        appFrame.src = appWindowConfig.value.url;
        appFrame.style.display = "block";
        appFrame.style.border = "none";
        appFrame.style.width = "100%";
        appFrame.style.height = "100%";
        appFrame.onload = handleAppLoad;
        document.getElementById("app-container").appendChild(appFrame);
    });
</script>

<style scoped
       lang="scss"
>
 @use "/src/assets/style/global-variable.scss";

  .app-window-container-view {
    width: 100%;
    height: 100%;

    $app-window-header-height: 40px;

    .app-window-header {
      width: 100%;
      height: $app-window-header-height;
    }

    .app-window-content {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: calc(100% - $app-window-header-height);
      background-color: global-variable.$background-color;
    }
  }
</style>