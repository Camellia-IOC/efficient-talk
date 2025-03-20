<template>
  <div class="org-cloud-disk-view-container"
       ref="cloudDiskViewRef"
  >
    <div class="cloud-disk-info">
      <div class="title">组织文件</div>
      <a-breadcrumb class="opened-folder-nav">
        <template #separator>
          <RightOutlined/>
        </template>
        <a-breadcrumb-item v-for="(item,index) in openedFolder"
                           :key="index"
                           @click="openFolder(item.folderId, null, index)"
                           style="cursor: pointer;"
        >
          <a-tag :bordered="false">{{ item.folderName }}</a-tag>
        </a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <div class="folder-nav">
      <div class="operation-bar-left">
        <a-button @click="exitCurFolder"
                  :disabled="openedFolder.length===1"
        >
          <ArrowLeftOutlined/>
        </a-button>
        <a-button type="primary"
                  @click="handleFolderCreatorDialogOpen"
        >
          <PlusOutlined/>
          新建
        </a-button>
        <a-button type="primary"
                  @click="handleFileUploaderDialogOpen"
        >
          <UploadOutlined/>
          上传
        </a-button>
        <a-button shape="circle"
                  @click="refreshCurrentFolder"
        >
          <ReloadOutlined/>
        </a-button>
      </div>
      <div class="operation-bar-right">
        <!--<a-select></a-select>-->
      </div>
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
        <template #emptyText>
          <EmptyContainer :height="tableHeight"
                          :description="'暂无数据'"
          />
        </template>
        <template #bodyCell="{column,record}">
          <template v-if="column.dataIndex === 'fileName'">
            <div style="display: flex;justify-content: center;align-items: center;width: 100%">
              <img :src="getFileIcon(record.fileType)"
                   alt="icon"
                   style="width: 30px;height: 30px;margin-right: 10px"
                   v-if="record.type==='file'"
              />
              <img src="/folder-icon/folder.png"
                   alt="icon"
                   style="width: 30px;height: 30px;margin-right: 10px"
                   v-else-if="record.type==='folder'"
              />
              <label class="single-line-ellipsis"
                     style="min-width: calc(100% - 40px);text-align: left;font-weight: bold"
              >
                {{ record.name }}
              </label>
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'uploadUser'">
            <div style="display: flex;justify-content: center;align-items: center;gap: 5px">
              <img :src="record.creatorAvatar"
                   alt="avatar"
                   style="width: 20px;height: 20px;border-radius: 50%"
              >
              <div style="display: flex;justify-content: center;align-items: center">
                {{ record.creatorName }}
              </div>
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'uploadTime'">
            <div style="display: flex;justify-content: center;align-items: center">
              {{ formatMessageTime(record.updateTime) }}
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'fileSize'">
            <div style="display: flex;justify-content: center;align-items: center"
                 v-if="record.type==='file'"
            >
              {{ translateFileSize(record.fileSize) }}
            </div>
            <div style="display: flex;justify-content: center;align-items: center"
                 v-else-if="record.type==='folder'"
            >
              --
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'operation'">
            <div style="display: flex;justify-content: center;align-items: center">
              <a-button @click.stop="handleCloudDiskItemEditorDialogOpen(record)"
                        shape="circle"
                        style="display:flex;justify-content:center;align-items:center;font-size: 18px;"
                        v-show="record.creatorId===curLoginUser.userId"
              >
                <FormOutlined/>
              </a-button>
            </div>
          </template>
        </template>
      </a-table>
    </div>
  </div>

  <!--文件夹创建对话框-->
  <FolderCreatorDialog ref="folderCreatorDialogRef"
                       @refresh-folder="refreshCurrentFolder"
  />

  <!--文件上传对话框-->
  <FileUploaderDialog ref="fileUploaderDialogRef"
                      :multi="true"
                      @upload-selected-file="uploadFileToCloudDisk"
  />

  <!--编辑文件/文件夹信息-->
  <CloudDiskItemEditorDialog ref="cloudDiskItemEditorDialogRef"
                             @refresh="refreshCurrentFolder"
  />
</template>

<script setup>
    import {
        onBeforeMount,
        onMounted,
        onUnmounted,
        ref
    } from "vue";
    import CloudDiskApi from "../../api/modules/CloudDiskApi.js";
    import { message } from "ant-design-vue";
    import { getFileIcon } from "../../utils/file-utils.js";
    import {
        formatMessageTime
    } from "../../utils/time-utils.js";
    import { translateFileSize } from "../../utils/unit-utils.js";
    import {
        FormOutlined,
        ReloadOutlined,
        RightOutlined,
        UploadOutlined,
        PlusOutlined,
        ArrowLeftOutlined
    } from "@ant-design/icons-vue";
    import { useRoute } from "vue-router";
    import EmptyContainer from "../../components/empty-container/EmptyContainer.vue";
    import FileUploaderDialog from "../../components/dialog/module-cloud-disk/file-uploader/FileUploaderDialog.vue";
    import { getCurUserData } from "../../database/cur-user.js";
    import FolderCreatorDialog from "../../components/dialog/module-cloud-disk/folder-creator/FolderCreatorDialog.vue";
    import CloudDiskItemEditorDialog
        from "../../components/dialog/module-cloud-disk/cloud-disk-item-editor/CloudDiskItemEditorDialog.vue";
    import { openFilePreviewChildWindow } from "../../window-controller/controller/ChildWindowController.js";

    const route = useRoute();

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // 文件夹创建对话框
    const folderCreatorDialogRef = ref();
    const handleFolderCreatorDialogOpen = () => {
        folderCreatorDialogRef.value.dialogOpen(openedFolder.value[openedFolder.value.length - 1].folderId, curLoginUser.value.userId, route.query.orgId, route.query.diskId);
    };

    // 文件上传对话框
    const fileUploaderDialogRef = ref();
    const handleFileUploaderDialogOpen = () => {
        fileUploaderDialogRef.value.dialogOpen(openedFolder.value[openedFolder.value.length - 1].folderId, curLoginUser.value.userId, route.query.orgId, route.query.diskId);
    };

    // 编辑对话框
    const cloudDiskItemEditorDialogRef = ref();
    const handleCloudDiskItemEditorDialogOpen = (file) => {
        cloudDiskItemEditorDialogRef.value.dialogOpen(file);
    };

    // 加载标志
    const isLoading = ref(true);

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
            getCloudDiskData();
        }
    });

    // 文件预览
    const handleFilePreview = (fileId, fileType) => {
        const data = {
            fileId: fileId,
            fileType: fileType,
            module: "CLOUD_DISK"
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
                if (record.type === "folder") {
                    openFolder(record.folderId, record.name, null);
                }
                else if (record.type === "file") {
                    handleFilePreview(record.fileId, record.fileType);
                }
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
                    tableHeight.value = entry.target.offsetHeight - 275;
                });
            });
            observer.observe(cloudDiskViewRef.value);
        }
    };

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

    // 云盘数据
    const openedFolder = ref([]);
    const cloudDiskFolders = ref([]);
    const cloudDiskFiles = ref([]);
    const tableData = ref([]);

    // 获取打开文件夹的路径串
    const getFileSavePath = () => {
        let path = "";
        for (let i = 0; i < openedFolder.value.length; i++) {
            path += openedFolder.value[i].folderId;
            path += "\\";
        }
        return path;
    };

    // 获取云盘数据
    const getCloudDiskData = async (parentId = null) => {
        isLoading.value = true;

        const response = await CloudDiskApi.getCloudDiskLevelContent({
            parentId: parentId === null ? openedFolder.value[openedFolder.value.length - 1].folderId : parentId,
            pageIndex: paginationConfig.value.pageIndex,
            pageSize: paginationConfig.value.pageSize
        });

        const res = response.data;
        if (res.code === 0) {
            // 清空表格数据
            tableData.value = [];

            if (res.data !== null) {
                cloudDiskFolders.value = res.data.folders;
                cloudDiskFiles.value = res.data.files;
                paginationConfig.value.total = res.data.folderCount + res.data.fileCount;

                // 导入文件夹数据
                for (let i = 0; i < cloudDiskFolders.value.length; i++) {
                    const item = cloudDiskFolders.value[i];
                    const folder = {
                        type: "folder",
                        folderId: item.folderId,
                        name: item.folderName,
                        parentId: item.parentId,
                        orgId: item.orgId,
                        diskId: item.diskId,
                        creatorId: item.creatorId,
                        creatorName: item.creatorName,
                        creatorAvatar: item.creatorAvatar,
                        updateTime: item.updateTime
                    };
                    tableData.value.push(folder);
                }

                // 导入文件数据
                for (let i = 0; i < cloudDiskFiles.value.length; i++) {
                    const item = cloudDiskFiles.value[i];
                    const file = {
                        type: "file",
                        fileId: item.fileId,
                        name: item.fileName + "." + item.fileType,
                        fileSize: item.fileSize,
                        fileType: item.fileType,
                        parentId: item.folderId,
                        orgId: item.orgId,
                        diskId: item.diskId,
                        creatorId: item.creatorId,
                        creatorName: item.creatorName,
                        creatorAvatar: item.creatorAvatar,
                        updateTime: item.updateTime
                    };
                    tableData.value.push(file);
                }
            }

            isLoading.value = false;
        }
        else {
            message.error("获取云盘数据失败");
        }
    };

    // 退出当前文件夹
    const exitCurFolder = () => {
        const folder = openedFolder.value.pop();
        paginationConfig.value.pageIndex = folder.prePageIndex;
        getCloudDiskData(openedFolder.value.at(-1).folderId);
    };

    // 打开文件夹
    const openFolder = (folderId, folderName, index) => {
        if (index === null) {
            openedFolder.value.push({
                folderId: folderId,
                folderName: folderName,
                prePageIndex: paginationConfig.value.pageIndex
            });
            paginationConfig.value.pageIndex = 1;
        }
        else {
            const pageIndex = openedFolder.value[index + 1].prePageIndex;
            openedFolder.value.splice(index + 1, openedFolder.value.length - index - 1);
            paginationConfig.value.pageIndex = pageIndex;
        }
        getCloudDiskData(folderId);
    };

    // 刷新当前打开的文件
    const refreshCurrentFolder = () => {
        getCloudDiskData();
    };

    // 上传文件
    const uploadProgressList = ref([]);
    const uploadFileToCloudDisk = async (fileList) => {
        const savePath = getFileSavePath();

        for (let i = 0; i < fileList.length; i++) {
            let formData = new FormData();
            formData.set("fileId", fileList[i].fileId);
            formData.set("fileName", fileList[i].fileName);
            formData.set("folderId", fileList[i].folderId);
            formData.set("orgId", fileList[i].orgId);
            formData.set("diskId", fileList[i].diskId);
            formData.set("fileType", fileList[i].fileType);
            formData.set("fileSize", fileList[i].fileSize);
            formData.set("creatorId", fileList[i].creatorId);
            formData.set("file", fileList[i].origin);
            formData.set("savePath", savePath);

            const response = await CloudDiskApi.uploadOrgCloudDiskFile(formData);
            const res = response.data;
        }

        refreshCurrentFolder();
    };

    onBeforeMount(async () => {
        // 初始化当前登录的用户信息
        await updateCurLoginUser();

        // 接收云盘ID
        openedFolder.value.push({
            folderId: route.query.diskId,
            folderName: "根目录",
            prePageIndex: 1
        });

        // 获取云盘数据
        await getCloudDiskData();
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
  @use '/src/assets/style/global-variable';

  .org-cloud-disk-view-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    padding: 20px 30px;

    $cloud-disk-info-height: 50px;
    $folder-nav-height: 50px;

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
        width: 100px;
      }

      .opened-folder-nav {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        width: calc(100% - 100px);
        background-color: white;
        padding: 8px 12px;
        border-radius: 15px;
        overflow-x: auto;

        :deep(.ant-tag) {
          margin: 0;
        }
      }
    }

    .folder-nav {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      height: $folder-nav-height;

      .operation-bar-left {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        width: 50%;
        gap: 10px;
      }

      .operation-bar-right {
        display: flex;
        justify-content: flex-end;
        align-items: center;
        width: 50%;
        gap: 10px;
      }
    }

    .result-table-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: calc(100% - #{$cloud-disk-info-height + $folder-nav-height});

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