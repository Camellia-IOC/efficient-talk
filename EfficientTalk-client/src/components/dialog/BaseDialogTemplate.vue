<template>
  <a-modal v-model:open="dialogOpenFlag"
           centered
           :mask="false"
           width="600px"
           :footer="null"
           @cancel="dialogClose"
  >
    <template #title>
      <div class="dialog-title">
        <div class="icon">
          <Logo :color="themeColor"
                :size="24"
          />
        </div>
        <label>{{ dialogTitle }}</label>
      </div>
    </template>
    <div class="dialog-body-container">
    </div>
  </a-modal>
</template>

<script setup>
    import { ref } from "vue";
    import Logo from "/src/components/logo/Logo.vue";
    import { themeColor } from "/src/config/config.js";

    // 对话框配置
    const dialogTitle = ref("用户选择");

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = () => {
        dialogOpenFlag.value = true;
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
    };

    defineExpose({
        dialogOpen,
        dialogClose
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable";

  .dialog-title {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 10px;
    font-size: 16px;
    width: 100%;
  }

  .dialog-body-container {
    display: flex;
    width: 100%;
    height: 400px;
  }
</style>