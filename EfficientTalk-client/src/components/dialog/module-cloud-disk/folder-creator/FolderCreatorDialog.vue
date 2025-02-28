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
        <div class="logo">
          <Logo :color="themeColor"
                :size="30"
          />
        </div>
        <label>{{ dialogTitle }}</label>
      </div>
    </template>
    <div class="folder-creator-container">
      <div class="folder-creator">
        <label>文件夹名称</label>
        <a-input v-model:value="folderData.folderName"
                 placeholder="请输入文件夹名称"
                 :status="formStatus.folderName"
        ></a-input>
      </div>
      <div class="operation-bar">
        <a-button type="primary"
                  @click="handleCreate"
                  class="operation-btn"
        >新建
        </a-button>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
    import { ref } from "vue";
    import { themeColor } from "../../../../config/config.js";
    import Logo from "../../../logo/Logo.vue";
    import { message } from "ant-design-vue";
    import CloudDiskApi from "../../../../api/modules/CloudDiskApi.js";

    const emit = defineEmits(["refreshFolder"]);

    const props = defineProps({
        dialogTitle: {
            type: String,
            default: "新建文件夹"
        }
    });

    // 表单内容
    const folderData = ref({
        folderName: "",
        parentId: null,
        orgId: null,
        diskId: null,
        creator: null
    });
    const resetFolderData = () => {
        folderData.value = {
            folderName: "",
            parentId: null,
            orgId: null,
            diskId: null,
            creator: null
        };
    };

    // 表单状态
    const formStatus = ref({
        folderName: ""
    });

    // 创建文件夹
    const handleCreate = async () => {
        // TODO 替换成正则表达式判断名称是否合法,并检查文件夹名称是否重复
        if (folderData.value.folderName === "") {
            formStatus.value.folderName = "error";
            message.error("文件夹名称不能为空");
        }
        else {
            const folderName = folderData.value.folderName;
            const response = await CloudDiskApi.createOrgCloudDiskFolder({
                folderName: folderName,
                parentId: folderData.value.parentId,
                orgId: folderData.value.orgId,
                diskId: folderData.value.diskId,
                creator: folderData.value.creator,
            });
            const res = response.data;
            if (res.code === 0) {
                message.success("创建成功");
                emit("refreshFolder");
                dialogClose();
            }
            else {
                formStatus.value.folderName = "error";
                message.error(res.message);
            }
        }
    };

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = (parentId, creatorId, orgId, diskId) => {
        folderData.value.parentId = parentId;
        folderData.value.creator = creatorId;
        folderData.value.orgId = orgId;
        folderData.value.diskId = diskId;
        dialogOpenFlag.value = true;
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
        resetFolderData();
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
    font-size: 18px;
    width: 100%;
  }

  .folder-creator-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 150px;

    .folder-creator {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 90%;
      gap: 10px;

      label {
        font-size: 16px;
        font-weight: bold;
      }
    }

    .operation-bar {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 10%;

      .operation-btn {
        //background-color: global-variable.$theme-color;
      }
    }
  }
</style>