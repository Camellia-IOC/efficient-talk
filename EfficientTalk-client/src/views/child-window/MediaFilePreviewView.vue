<template>
  <div class="media-file-preview-view-container">
    <!--图片预览-->
    <div class="images"
         v-viewer="imagePreviewConfig"
         v-if="fileType === 'image'"
    >
      <img :src="imageUrl"
           alt="img"
           v-show="false"
      >
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import { useRoute } from "vue-router";
    import ChatApi from "../../api/modules/ChatApi.js";

    const route = useRoute();

    // 文件属性
    const isLoading = ref(true);
    const fileId = ref();
    const fileType = ref();
    const fileBlob = ref();
    const imageUrl = ref();
    // 图片预览配置
    const imagePreviewConfig = {
        inline: true,
        navbar: false,
        fullscreen: false,
        button: false
    };

    // 获取文件流
    const getFileBlob = async () => {
        const response = await ChatApi.getChatFileBlob({
            fileId: fileId.value,
            type: "media"
        });
        isLoading.value = false;

        // 获取文件Blob并转换为ArrayBuffer
        fileBlob.value = new Blob([response.data]);
        imageUrl.value = URL.createObjectURL(fileBlob.value);
    };

    onBeforeMount(async () => {
        const data = JSON.parse(route.query.data);
        fileId.value = data.fileId;
        fileType.value = data.fileType;
        await getFileBlob();
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .media-file-preview-view-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: global-variable.$background-color;
  }
</style>