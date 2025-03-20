<template>
  <div class="file-preview-view-container">
    <!--加载容器-->
    <div class="loading-container"
         v-show="isLoading"
    >
      <a-spin :spinning="isLoading"
              size="large"
              :tip="'文件加载中...'"
      ></a-spin>
    </div>
    <vue-office-pdf v-if="fileType === 'pdf'"
                    style="width: 100%;height: 100%;overflow-y: auto"
                    :src="fileData"
                    @rendered="renderHandler"
                    @error="errorHandler"
    ></vue-office-pdf>
    <!--Excel预览-->
    <vue-office-excel v-else-if="fileType === 'xlsx'"
                      style="width: 100%;height: 100vh;overflow-y: auto"
                      :src="fileData"
                      @rendered="renderHandler"
                      @error="errorHandler"
    ></vue-office-excel>
    <!--Word预览-->
    <vue-office-docx v-else-if="fileType === 'docx'"
                     style="width: 100%;height: 100%;overflow-y: auto"
                     :src="fileData"
                     @rendered="renderHandler"
                     @error="errorHandler"
    ></vue-office-docx>
    <!--PPT预览-->
    <vue-office-pptx v-else-if="fileType === 'pptx'"
                     style="width: 100%;height: 100%;overflow-y: auto"
                     :src="fileData"
                     @rendered="renderHandler"
                     @error="errorHandler"
    >
    </vue-office-pptx>
    <!--图片预览-->
    <div v-viewer="imagePreviewConfig"
         v-else-if="imageAccept.includes(fileType)"
    >
      <img :src="imageUrl"
           alt="img"
           v-show="false"
      >
    </div>
    <EmptyContainer v-else
                    :description="'暂不支持该文件类型预览'"
    />
  </div>
</template>

<script setup>
    import VueOfficePdf from "@vue-office/pdf";
    import VueOfficeDocx from "@vue-office/docx";
    import VueOfficeExcel from "@vue-office/excel";
    import VueOfficePptx from "@vue-office/pptx";
    import "@vue-office/docx/lib/index.css";
    import "@vue-office/excel/lib/index.css";
    import { useRoute } from "vue-router";
    import "viewerjs/dist/viewer.css";
    import {
        nextTick,
        onBeforeMount,
        onMounted,
        ref
    } from "vue";
    import EmptyContainer from "../../components/empty-container/EmptyContainer.vue";
    import ChatApi from "../../api/modules/ChatApi.js";
    import CloudDiskApi from "../../api/modules/CloudDiskApi.js";

    const route = useRoute();

    // 当前模块
    const module = ref(null);

    // 文件属性
    const isLoading = ref(true);
    const fileId = ref();
    const fileType = ref();
    const fileBlob = ref();
    const fileData = ref();
    const imageUrl = ref();
    // 图片准入类型
    const imageAccept = [
        "jpg",
        "jpeg",
        "png",
        "gif",
        "bmp",
        "webp",
        "svg",
    ];
    // 图片预览配置
    const imagePreviewConfig = {
        inline: true,
        navbar: false,
        fullscreen: false,
        button: false,
        title: false
    };

    // 文档渲染回调
    const renderHandler = () => {
        console.log("文档渲染完成");
        isLoading.value = false;
    };
    const errorHandler = () => {
        console.error("文档渲染失败");
    };

    // 获取文件流
    const getFileBlob = async () => {
        let response;
        if (module.value === "CHAT") {
            response = await ChatApi.getChatFileBlob({
                fileId: fileId.value,
                type: "file"
            });
        }
        else if (module.value === "CLOUD_DISK") {
            response = await CloudDiskApi.getCloudDiskFileBlob({
                fileId: fileId.value
            });
        }
        console.error(response)

        // 获取文件Blob并转换为ArrayBuffer
        fileBlob.value = new Blob([response.data]);
        if (imageAccept.includes(fileType.value)) {
            imageUrl.value = URL.createObjectURL(fileBlob.value);
        }
        fileData.value = await response.data.arrayBuffer();

        isLoading.value = false;
    };

    onBeforeMount(() => {
        debugger
        const data = JSON.parse(route.query.data);
        fileId.value = data.fileId;
        fileType.value = data.fileType;
        module.value = data.module;
    });

    onMounted(() => {
        getFileBlob();
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .file-preview-view-container {
    width: 100%;
    height: 100%;
    background-color: global-variable.$background-color;

    .loading-container {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100%;
    }

    .preview-container {
      width: 100%;
      height: 100%;
    }
  }
</style>