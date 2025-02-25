<template>
  <div class="ai-assistant-view-container">
    <div class="ai-chat-container">

    </div>
    <div class="question-input-container">
      <a-textarea class="question-input"
                  placeholder="请输入问题"
                  v-model:value="questionInput"
                  :auto-size="{ minRows: 4, maxRows: 4 }"
      />
      <div class="operation-bar">
        <a-button type="primary">发送</a-button>
      </div>
    </div>
  </div>
</template>

<script setup>
    import { ref } from "vue";

    const questionInput = ref("");
</script>

<style scoped
       lang="scss"
>
  @use "../../assets/style/global-variable.scss";

  .ai-assistant-view-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: global-variable.$background-color;

    $question-input-container-height: 160px;

    .ai-chat-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: calc(100% - #{$question-input-container-height});
    }

    .question-input-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: calc(100% - 20px);
      height: $question-input-container-height - 20px;
      margin:10px;
      padding: 10px;
      justify-content: space-between;
      background-color: white;

      .operation-bar {
        display: flex;
        justify-content: flex-end;
        align-items: center;
        width: 100%;
      }
    }
  }
</style>