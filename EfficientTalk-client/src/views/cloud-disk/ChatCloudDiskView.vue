<template>
  <div class="chat-cloud-disk-view-container"
       ref="cloudDiskViewRef"
  >
    <div class="cloud-disk-info">
      <div class="title">聊天文件</div>
    </div>
    <div class="result-table-container">
      <a-table :data-source="tableData"
               :columns="tableColumns"
               class="result-table"
               :scroll="{scrollToFirstRowOnChange: true, y: tableHeight}"
               :loading="isLoading"
               :pagination="{
                   current: paginationConfig.pageIndex,
                   pageSize: paginationConfig.pageSize,
                   total: paginationConfig.total,
                   showLessItems: paginationConfig.showLessItems,
                   showQuickJumper: paginationConfig.showQuickJumper,
                   showSizeChanger: paginationConfig.showSizeChanger,
                   onChange: paginationConfig.pageChange,
                   position: ['bottomCenter']
               }"
               :custom-row="handleRowOperation"
               :row-class-name="tableRowClassName"
      >
        <template #bodyCell="{column,record}">
          <template v-if="column.dataIndex === 'fileName'">
            <div style="display: flex;justify-content: center;align-items: center;width: 100%">
              <img :src="getFileIcon(record.fileType)"
                   alt="icon"
                   style="width: 30px;height: 30px;margin-right: 10px"
              />
              <label class="single-line-ellipsis"
                     style="min-width: calc(100% - 40px);text-align: left;font-weight: bold"
              >
                {{ record.fileName + "." + record.fileType }}
              </label>
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'uploadUser'">
            <div style="display: flex;justify-content: center;align-items: center;gap: 5px">
              <img :src="record.senderAvatar"
                   alt="avatar"
                   style="width: 20px;height: 20px;border-radius: 50%"
              >
              <div style="display: flex;justify-content: center;align-items: center">
                {{ record.senderName }}
              </div>
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'uploadTime'">
            <div style="display: flex;justify-content: center;align-items: center">
              {{ formatMessageTime(record.createTime) }}
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'fileSize'">
            <div style="display: flex;justify-content: center;align-items: center">
              {{ translateFileSize(record.fileSize) }}
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'operation'">
            <div style="display: flex;justify-content: center;align-items: center">
              <a-button type="primary"
                        @click="handleDownloadFile(record.fileId)"
                        shape="circle"
                        style="display:flex;justify-content:center;align-items:center;font-size: 18px;font-weight: bold"
              >
                <DownloadOutlined/>
              </a-button>
            </div>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        onMounted,
        onUnmounted,
        ref
    } from "vue";
    import ChatApi from "../../api/modules/ChatApi.js";
    import { getCurUserData } from "../../database/cur-user.js";
    import { translateFileSize } from "../../utils/unit-utils.js";
    import { formatMessageTime } from "../../utils/time-utils.js";
    import { DownloadOutlined } from "@ant-design/icons-vue";
    import { getFileIcon } from "../../utils/file-utils.js";
    import { openFilePreviewChildWindow } from "../../window-controller/controller/ChildWindowController.js";

    // 分页器配置
    const paginationConfig = ref({
        pageIndex: 1,
        pageSize: 20,
        total: 0,
        showLessItems: true,
        showQuickJumper: false,
        showSizeChanger: false,
        pageChange: (pageIndex, pageSize) => {
            isLoading.value = true;
            paginationConfig.value.pageIndex = pageIndex;
            paginationConfig.value.pageSize = pageSize;
            getTableData();
        }
    });

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // 加载标志
    const isLoading = ref(true);

    // 文件预览
    const handleFilePreview = (fileId, fileType) => {
        const data = {
            fileId: fileId,
            fileType: fileType,
            module: "CHAT"
        };
        openFilePreviewChildWindow(data);
    };

    // 表格行类名
    const tableRowClassName = (record, index) => {
        return "cloud-disk-table-row";
    };

    // 处理表格行点击事件
    const handleRowOperation = (record) => {
        return {
            onClick: () => {
                console.error(record);
                handleFilePreview(record.fileId, record.fileType);
            }
        };
    };

    // 计算表格高度
    let observer = null;
    const cloudDiskViewRef = ref();
    const tableHeight = ref(0);
    const observeResize = () => {
        if (cloudDiskViewRef.value) {
            const observer = new ResizeObserver((entries) => {
                entries.forEach((entry) => {
                    tableHeight.value = entry.target.offsetHeight - 245;
                });
            });
            observer.observe(cloudDiskViewRef.value);
        }
    };

    // 聊天文件列表
    const tableData = ref([]);

    // 表格列配置
    const tableColumns = ref([
        {
            title: "文件名称",
            dataIndex: "fileName",
            key: "fileName",
            width: "30%",
            align: "center"
        },
        {
            title: "上传用户",
            dataIndex: "uploadUser",
            key: "uploadUser",
            width: "20%",
            align: "center"
        },
        {
            title: "上传时间",
            dataIndex: "uploadTime",
            key: "uploadTime",
            width: "15%",
            align: "center"
        },
        {
            title: "文件大小",
            dataIndex: "fileSize",
            key: "fileSize",
            width: "15%",
            align: "center"
        },
        {
            title: "操作",
            dataIndex: "operation",
            key: "operation",
            width: "10%",
            align: "center"
        }
    ]);

    // 获取结果数据
    const getTableData = async () => {
        const response = await ChatApi.getChatFileList({
            userId: curLoginUser.value.userId,
            pageIndex: paginationConfig.value.pageIndex,
            pageSize: paginationConfig.value.pageSize
        });

        const res = response.data;
        if (res.code === 0) {
            tableData.value = res.data.chatFileList;
            paginationConfig.value.total = res.data.total;
        }
        else {
            tableData.value = [];
        }

        isLoading.value = false;
    };

    // 下载文件
    const handleDownloadFile = (fileId) => {
        console.error(fileId);
        // TODO 下载请求
    };

    onBeforeMount(async () => {
        // 初始化当前登录的用户信息
        await updateCurLoginUser();

        await getTableData();
    });

    onMounted(() => {
        observeResize();
    });

    onUnmounted(() => {
        if (observer) {
            observer.disconnect();
        }
    });
</script>

<style scoped
       lang="scss"
>
  .chat-cloud-disk-view-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    padding: 20px 30px;

    $cloud-disk-info-height: 50px;

    .cloud-disk-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      height: $cloud-disk-info-height;

      .title {
        font-size: 20px;
        font-weight: bold;
        color: black;
      }
    }

    .result-table-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: calc(100% - #{$cloud-disk-info-height});

      .result-table {
        width: 100%;
        height: 100%;
      }
    }
  }

  :deep(.ant-table-wrapper) {
    width: 100% !important;
    height: 100% !important;
  }

  :deep(.ant-spin-nested-loading) {
    width: 100% !important;
    height: 100% !important;
  }

  :deep(.ant-spin-container) {
    width: 100% !important;
    height: 100% !important;
  }

  :deep(.ant-table) {
    width: 100%;
    height: 90%;
  }

  :deep(.ant-pagination) {
    margin-bottom: 0 !important;
  }

  // 表格行
  :deep(.cloud-disk-table-row) {
    td {
      cursor: pointer;

      label {
        cursor: pointer;
      }
    }
  }
</style>