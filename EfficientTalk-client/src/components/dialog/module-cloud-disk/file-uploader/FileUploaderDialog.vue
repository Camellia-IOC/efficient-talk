<template>
  <a-modal v-model:open="dialogOpenFlag"
           centered
           :mask="false"
           width="700px"
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
    <div class="file-uploader-container">
      <div class="file-uploader">
        <a-upload-dragger
                v-model:fileList="fileList"
                name="file"
                :multiple="multi"
                :showUploadList="false"
                style="width: 100%"
                :customRequest="()=>{}"
                :beforeUpload="handleFileSelect"
                :accept="getAcceptFile()"
        >
          <p class="ant-upload-drag-icon">
            <InboxOutlined/>
          </p>
          <p class="ant-upload-text">单击或拖动文件到此区域以上传</p>
        </a-upload-dragger>
      </div>
      <div class="file-list-container">
        <div class="selected-file-container">
          <div v-for="(file, index) in fileSelectedList"
               :key="index"
               class="file-item"
          >
            <div class="file-info-container">
              <div class="file-info">
                <img :src="getFileIcon(file.fileType)"
                     alt="icon"
                     class="file-icon"
                >
                <div class="file-name single-line-ellipsis">{{ file.fileName + "." + file.fileType }}</div>
              </div>
              <div class="file-size">{{ translateFileSize(file.fileSize) }}</div>
            </div>
            <div class="operation-bar">
              <a-button type="primary"
                        danger
                        @click="deleteFileFromList(index)"
              >
                <DeleteOutlined/>
              </a-button>
            </div>
          </div>
          <EmptyContainer description="暂无已选择文件"
                          v-if="fileList.length === 0"
          />
        </div>
      </div>
      <div class="operation-bar">
        <a-button type="primary"
                  @click="handleUpload"
                  class="operation-btn"
        >上传
        </a-button>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
    import {
        ref
    } from "vue";
    import {
        InboxOutlined,
        DeleteOutlined
    } from "@ant-design/icons-vue";
    import EmptyContainer from "../../../empty-container/EmptyContainer.vue";
    import { UUID } from "uuidjs";
    import { translateFileSize } from "../../../../utils/unit-utils.js";
    import { getFileIcon } from "../../../../utils/file-utils.js";
    import { themeColor } from "../../../../config/config.js";
    import Logo from "../../../logo/Logo.vue";

    const emit = defineEmits(["uploadSelectedFile"]);

    const props = defineProps({
        curLoginUserId: {
            type: String,
            default: ""
        },
        dialogTitle: {
            type: String,
            default: "选择文件"
        },
        multi: {
            type: Boolean,
            default: false
        }
    });

    // 上传信息配置
    const uploaderConfig = ref({
        folderId: null,
        userId: null,
        orgId: null,
        diskId: null
    });

    // 更新上传器配置
    const setUploaderConfig = (folderId, userId, orgId, diskId) => {
        uploaderConfig.value.folderId = folderId;
        uploaderConfig.value.userId = userId;
        uploaderConfig.value.orgId = orgId;
        uploaderConfig.value.diskId = diskId;
    };

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = (folderId, userId, orgId, diskId) => {
        dialogOpenFlag.value = true;
        setUploaderConfig(folderId, userId, orgId, diskId);
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;

        // 重置数据
        fileList.value = [];
        fileSelectedList.value = [];
        setUploaderConfig(null, null, null, null);
    };

    // 获取准入文件类型
    const getAcceptFile = () => {
        return "*";
    };

    // 文件列表
    const fileList = ref([]);
    const fileSelectedList = ref([]);

    // 删除文件列表中的文件
    const deleteFileFromList = (index) => {
        fileList.value.splice(index, 1);
        fileSelectedList.value.splice(index, 1);
    };

    // 选择文件回调
    const handleFileSelect = (file) => {
        // 获取文件名和后缀
        let fileName = "";
        let fileType = "";
        const nameStr = file.name.split(".");
        nameStr.forEach((item, index) => {
            if (index !== nameStr.length - 1) {
                fileName += item;
            }
            else {
                fileType = item;
            }
        });

        // 装填文件信息
        fileSelectedList.value.push({
            fileId: UUID.generate(),
            fileName: fileName,
            folderId: uploaderConfig.value.folderId,
            orgId:uploaderConfig.value.orgId,
            diskId: uploaderConfig.value.diskId,
            fileType: fileType.toLowerCase(),
            fileSize: file.size,
            creatorId: uploaderConfig.value.userId,
            origin: file
        });

        // 用于避免直接上传
        return false;
    };

    // 发送文件
    const handleUpload = () => {
        console.error(fileList.value);
        emit("uploadSelectedFile", fileSelectedList.value);
        dialogClose();
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

  .file-uploader-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 500px;

    .file-uploader {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 30%;
    }

    .file-list-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: 60%;
      overflow-y: auto;

      .selected-file-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: 100%;
        overflow-y: auto;
        gap: 10px;

        .file-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 100%;
          height: 80px;
          min-height: 80px;
          padding: 0 20px;
          background-color: #F5F7FA;
          border-radius: 15px;

          .file-info-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 90%;
            height: 100%;

            .file-info {
              display: flex;
              justify-content: flex-start;
              align-items: center;
              width: 85%;
              gap: 15px;

              .file-icon {
                width: 60px;
                height: 60px;
                padding: 5px;
                background-color: white;
                border-radius: 10px;
              }

              .file-name {
                font-size: 16px;
              }
            }

            .file-size {
              display: flex;
              justify-content: center;
              align-items: center;
              width: 15%;
              color: gray;
            }
          }

          .operation-bar {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            width: 10%;
            height: 100%;
          }
        }
      }
    }

    .operation-bar {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 10%;

      .operation-btn {
        background-color: global-variable.$theme-color;
      }
    }
  }
</style>