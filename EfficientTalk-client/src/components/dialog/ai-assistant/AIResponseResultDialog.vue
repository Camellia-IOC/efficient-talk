<template>
  <a-modal v-model:open="dialogOpenFlag"
           centered
           :mask="false"
           width="450px"
           :footer="null"
           @cancel="dialogClose"
  >
    <template #title>
      <div class="dialog-title">
        <div class="icon">
          <AiAssistantIcon :color="themeColor"
                           :size="24"
          />
        </div>
        <label>{{ dialogTitle }}</label>
      </div>
    </template>
    <div class="empty-container"
         v-if="content===''"
    >
      <a-spin :tip="'小易正在努力思考'"></a-spin>
    </div>
    <div class="dialog-body-container"
         v-html="marked(content)"
         v-else
    >
    </div>
  </a-modal>
</template>

<script setup>
    import { themeColor } from "../../../config/config.js";
    import { ref } from "vue";
    import AiAssistantIcon from "../../icon/AiAssistantIcon.vue";
    import { marked } from "marked";

    // 对话框配置
    const dialogTitle = ref("小易");

    const props = defineProps({
        content: {
            type: String
        }
    });

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = (title) => {
        dialogTitle.value = title;
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
    flex-direction: column;
    width: 100%;
    height: 250px;
    overflow-y: auto;
  }

  .empty-container{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 250px;
  }
</style>