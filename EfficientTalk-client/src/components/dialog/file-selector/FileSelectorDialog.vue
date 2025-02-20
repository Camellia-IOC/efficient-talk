<template>
  <a-modal v-model:open="dialogOpenFlag"
           :title="dialogTitle"
           centered
           :mask="false"
           width="700px"
           :footer="null"
           @cancel="dialogClose"
  >
    <div class="file-selector-container">
      <div class="file-selector">
        <a-upload-dragger
                v-model:fileList="fileList"
                name="file"
                :multiple="multi"
                :showUploadList="false"
                @drop="handleDrop"
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
          <div v-for="(file, index) in fileMessageList"
               :key="index"
               class="file-item"
          >
            <div class="file-info">
              <img :src="getFileIcon(file.fileType)"
                   alt="icon"
                   class="file-icon"
              >
              <div class="file-name">{{ file.fileName + "." + file.fileType }}</div>
            </div>
            <div class="file-size">{{ translateFileSize(file.fileSize) }}</div>
          </div>
          <EmptyContainer description="暂无已选择图片"
                          v-if="fileList.length === 0"
          />
        </div>
        <div class="selected-file-container"
             v-else-if="type==='file'"
        >
          <div v-for="(file, index) in fileMessageList"
               :key="index"
               class="file-item"
          >
            <div class="file-info">
              <img :src="getFileIcon(file.fileType)"
                   alt="icon"
                   class="file-icon"
              >
              <div class="file-name">{{ file.fileName + "." + file.fileType }}</div>
            </div>
            <div class="file-size">{{ translateFileSize(file.fileSize) }}</div>
          </div>
          <EmptyContainer description="暂无已选择文件"
                          v-if="fileList.length === 0"
          />
        </div>
      </div>
      <div class="operation-bar">
        <a-button type="primary"
                  @click="handleSend"
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
    import { InboxOutlined } from "@ant-design/icons-vue";
    import EmptyContainer from "../../empty-container/EmptyContainer.vue";
    import { UUID } from "uuidjs";
    import { translateFileSize } from "../../../utils/unit-utils.js";

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
        fileMessageList.value = [];
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

    // 获取文件图标
    const getFileIcon = (fileType) => {
        // TODO 有bug,不能正确判断
        console.error(fileController.checkIsExist("./public/file-icon/" + fileType + ".png"));
        if (fileController.checkIsExist("./public/file-icon/" + fileType + ".png")) {
            return "/public/file-icon/" + fileType + ".png";
        }
        else {
            return "/public/file-icon/other.png";
        }
    };

    // 文件列表
    const fileList = ref([]);
    const fileMessageList = ref([]);

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
        fileMessageList.value.push({
            type: props.type,
            fileId: UUID.generate(),
            fileName: fileName,
            fileType: fileType.toLowerCase(),
            fileSize: file.size,
            origin: file
        });
        console.error(fileMessageList.value);

        // 用于避免直接上传
        return false;
    };

    function handleDrop(e) {
        console.log(e);
    }

    const handleSend = () => {
        console.error(fileList.value);
        emit("sendSelectedFile", fileMessageList.value);
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

          .file-info {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 15px;

            .file-icon {
              width: 60px;
              height: 60px;
              padding: 5px;
              background-color: white;
              border-radius: 10px;
            }

            .file-name{
              font-size: 16px;
              font-weight: bold;
            }
          }

          .file-size {
            display: flex;
            justify-content: center;
            align-items: center;
            color: gray;
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

          .file-info {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 15px;

            .file-icon {
              width: 60px;
              height: 60px;
              padding: 5px;
              background-color: white;
              border-radius: 10px;
            }

            .file-name{
              font-size: 16px;
              font-weight: bold;
            }
          }

          .file-size {
            display: flex;
            justify-content: center;
            align-items: center;
            color: gray;
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
    }
  }
</style>