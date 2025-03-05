<template>
  <div class="child-window-container-view">
    <div class="child-window-header">
      <ChildWindowHeader :window-name="childWindowName"
                         :config="childWindowConfig"
      />
    </div>
    <div class="child-window-content">
      <RouterView/>
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import ChildWindowHeader from "../components/child-window/base-header/ChildWindowHeader.vue";
    import {
        useRoute,
        useRouter
    } from "vue-router";
    import ChildWindowController from "../window-controller/child-window-controller.js";

    const route = useRoute();
    const router = useRouter();

    // 子窗口配置
    const childWindowName = ref(null);
    const childWindowUrl = ref(null);
    const childWindowConfig = ref({
        title: "易飞讯",
        data: {}
    });

    onBeforeMount(async () => {
        childWindowName.value = route.query.windowName;
        childWindowUrl.value = route.query.url;
        childWindowConfig.value = await ChildWindowController.getChildWindowConfig(childWindowName.value);
        await router.push({
            name: childWindowUrl.value,
            query: {
                data: JSON.stringify(childWindowConfig.value.data)
            }
        });
    });
</script>

<style scoped
       lang="scss"
>
  .child-window-container-view {
    width: 100%;
    height: 100%;

    $child-window-header-height: 40px;

    .child-window-header {
      width: 100%;
      height: $child-window-header-height;
    }

    .child-window-content {
      width: 100%;
      height: calc(100% - $child-window-header-height);
    }
  }
</style>