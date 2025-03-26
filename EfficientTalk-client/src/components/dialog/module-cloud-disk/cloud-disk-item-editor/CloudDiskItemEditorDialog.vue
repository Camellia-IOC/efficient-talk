<template>
  <a-modal v-model:open="dialogOpenFlag"
           centered
           :mask="false"
           width="350px"
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
      <div class="form-area">
        <a-form style="width: 90%">
          <a-form-item label="文件名称">
            <a-input v-model:value="fileInfo.name"
                     placeholder="请输入文件名称"
            />
          </a-form-item>
          <a-form-item label="上传用户">
            <a-input v-model:value="fileInfo.creatorName"
                     placeholder="请输入创建人名称"
                     disabled
            />
          </a-form-item>
          <a-form-item label="上传时间">
            <a-input v-model:value="fileInfo.updateTime"
                     placeholder="请输入创建时间"
                     disabled
            />
          </a-form-item>
        </a-form>
      </div>
      <div class="operation-bar">
        <a-button type="primary"
                  @click="handleSave"
        >保存
        </a-button>
        <a-button danger
                  @click="handleDelete"
        >删除
        </a-button>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
    import { ref } from "vue";
    import { themeColor } from "../../../../config/config.js";
    import Logo from "../../../logo/Logo.vue";
    import CloudDiskApi from "../../../../api/modules/CloudDiskApi.js";
    import { message } from "ant-design-vue";

    // 对话框配置
    const dialogTitle = ref("编辑");
    const fileInfo = ref({
        name: "",
        creatorName: "",
        updateTime: ""
    });

    const emit = defineEmits(["refresh"]);

    // 重命名操作
    const renameFolder = async () => {
        const response = await CloudDiskApi.renameOrgCloudDiskFolder({
            folderId: fileInfo.value.folderId,
            newFolderName: fileInfo.value.name
        });

        const res = response.data;
        if (res.code === 0) {
            emit("refresh");
            dialogClose();
            message.success("保存成功");
        }
        else {
            message.error("保存失败");
        }
    };
    const renameFile = async () => {
        const response = await CloudDiskApi.renameOrgCloudDiskFile({
            fileId: fileInfo.value.fileId,
            newFileName: fileInfo.value.name
        });

        const res = response.data;
        if (res.code === 0) {
            emit("refresh");
            dialogClose();
            message.success("保存成功");
        }
        else {
            message.error("保存失败");
        }
    };
    const handleSave = () => {
        if (fileInfo.value.type === "file") {
            renameFile();
        }
        else if (fileInfo.value.type === "folder") {
            renameFolder();
        }
    };

    // 删除操作
    const deleteFolder = async () => {
        const response = await CloudDiskApi.deleteOrgCloudDiskFolder({
            folderId: fileInfo.value.folderId
        });

        const res = response.data;
        if (res.code === 0) {
            emit("refresh");
            dialogClose();
            message.success("删除成功");
        }
        else {
            message.error("删除失败");
        }
    };
    const deleteFile = async () => {
        const response = await CloudDiskApi.deleteOrgCloudDiskFile({
            diskId: fileInfo.value.diskId,
            fileId: fileInfo.value.fileId
        });

        const res = response.data;
        if (res.code === 0) {
            emit("refresh");
            dialogClose();
            message.success("删除成功");
        }
        else {
            message.error("删除失败");
        }
    };
    const handleDelete = () => {
        if (fileInfo.value.type === "file") {
            deleteFile();
        }
        else if (fileInfo.value.type === "folder") {
            deleteFolder();
        }
    };

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = (file) => {
        fileInfo.value = file;
        fileInfo.value.name = fileInfo.value.name.split(".")[0];
        console.error(file);
        dialogOpenFlag.value = true;
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
        fileInfo.value = {
            name: "",
            creatorName: "",
            updateTime: ""
        };
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
    align-items: center;
    width: 100%;
    height: 250px;
    overflow-y: auto;

    .form-area {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 85%;
    }

    .operation-bar {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 15%;
      gap: 20px
    }
  }
</style>