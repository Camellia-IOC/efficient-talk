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
    <div class="file-selector-container">
      <div class="file-selector">
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
        <div class="selected-picture-container"
             v-if="type==='image'"
        >
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
          <EmptyContainer description="暂无已选择图片"
                          v-if="fileList.length === 0"
          />
        </div>
        <div class="selected-file-container"
             v-else-if="type==='file'"
        >
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
                  @click="handleSend"
                  class="operation-btn"
        >发送
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

    const emit = defineEmits(["sendSelectedFile"]);

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
        },
        type: {
            type: String,
            default: "file"
        }
    });

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = () => {
        dialogOpenFlag.value = true;
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
        fileList.value = [];
        fileSelectedList.value = [];
    };

    // 获取准入文件类型
    const getAcceptFile = () => {
        if (props.type === "image") {
            return "image/*";
        }
        else {
            return "*";
        }
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
            type: props.type,
            fileId: UUID.generate(),
            fileName: fileName,
            fileType: fileType.toLowerCase(),
            fileSize: file.size,
            origin: file
        });

        // 用于避免直接上传
        return false;
    };

    // 发送文件
    const handleSend = () => {
        console.error(fileList.value);
        emit("sendSelectedFile", fileSelectedList.value);
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

  .file-selector-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 500px;

    .file-selector {
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

      .selected-picture-container {
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
          padding: 0 20px;
          background-color: #F5F7FA;
          border-radius: 15px;

          .file-info-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 85%;
            height: 100%;

            .file-info {
              display: flex;
              justify-content: flex-start;
              align-items: center;
              width: 90%;
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
                font-weight: bold;
              }
            }

            .file-size {
              display: flex;
              justify-content: center;
              align-items: center;
              width: 10%;
              color: gray;
            }
          }

          .operation-bar {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            width: 15%;
            height: 100%;
          }
        }
      }

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