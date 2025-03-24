<template>
  <div class="notice-and-todo-view-container">
    <div class="notice-area-container">
      <div class="area-header">
        <div class="title">
          <NotificationOutlined class="icon"/>
          通知中心
          <a-button shape="circle"
                    class="refresh-btn"
                    @click="getNoticeList"
          >
            <ReloadOutlined/>
          </a-button>
        </div>
        <div style="display: flex;align-items: center;gap: 10px">
          <a-button type="primary"
                    @click="handleNoticeCreatorDialogOpen"
          >
            <PlusOutlined/>
          </a-button>
          <a-segmented v-model:value="noticeActiveType"
                       :options="noticeTypeSets"
                       style="width: 100%"
          />
        </div>
      </div>
      <div class="area-body">
        <div v-if="isNoticeListLoading||noticeList.length === 0"
             style="display: flex;justify-content: center;align-items: center;width: 100%;height: 100%"
        >
          <EmptyContainer :description="'暂无通知'"
                          v-if="!isNoticeListLoading"
          />
          <a-spin :tip="'通知列表加载中'"
                  v-else
          ></a-spin>
        </div>
        <div v-else
             v-for="(item,index) in noticeList"
             :key="index"
             class="notice-item"
             v-show="noticeActiveType==='ALL'||(noticeActiveType==='READ'&&item.hasRead===true)||(noticeActiveType==='UNREAD'&&item.hasRead===false)"
             @click="handleNoticeExplorerDialogOpen(item)"
        >
          <div class="notice-header">
            <div style="display: flex;align-items: center;gap: 10px;width: 70%">
              <ReadOutlined style="background-color: var(--theme-color);color: white;padding: 8px;font-size: 20px;border-radius: 50%;"/>
              <div class="single-line-ellipsis"
                   style="color: var(--theme-color)"
              >{{ item.title }}
              </div>
            </div>
            <div style="color: gray;font-size: 14px;width: 30%;text-align: end"
                 class="single-line-ellipsis"
            >
              {{ item.deptName }}
            </div>
          </div>
          <div class="notice-body">
            {{ item.content }}
          </div>
          <div class="notice-footer">
            <div style="color: gray;font-size: 14px">
              {{ formatDate(item.publishTime) }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="todo-area-container">
      <div class="area-header">
        <div class="title">
          <CarryOutOutlined class="icon"/>
          待办中心
          <a-button shape="circle"
                    class="refresh-btn"
                    @click="getTodoList"
          >
            <ReloadOutlined/>
          </a-button>
        </div>
      </div>
      <div class="area-body">
        <EmptyContainer :description="'暂无待办'"/>
      </div>
    </div>
  </div>

  <!--通知内容浏览器-->
  <NoticeExplorerDialog ref="noticeExplorerDialogRef"
                        @update-old-notice="updateOldNotice"
  />

  <!--通知发布对话框-->
  <NoticeCreatorDialog ref="noticeCreatorDialogRef"/>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import {
        NotificationOutlined,
        CarryOutOutlined,
        ReloadOutlined,
        ReadOutlined,
        PlusOutlined
    } from "@ant-design/icons-vue";
    import EmptyContainer from "../components/empty-container/EmptyContainer.vue";
    import { useCurLoginUserStore } from "../store/CurLoginUserStore.js";
    import NoticeApi from "../api/modules/NoticeApi.js";
    import NoticeExplorerDialog from "../components/dialog/module-notice/notice-explorer/NoticeExplorerDialog.vue";
    import { formatDate } from "../utils/time-utils.js";
    import NoticeCreatorDialog from "../components/dialog/module-notice/notice-creator/NoticeCreatorDialog.vue";

    const curLoginUserStore = useCurLoginUserStore();

    const updateOldNotice = (notice) => {
        const index = noticeList.value.findIndex((item) => item.id === notice.id);
        if (index !== -1) {
            noticeList.value[index] = notice;
        }
    };

    // 通知内容浏览器控制
    const noticeExplorerDialogRef = ref();
    const handleNoticeExplorerDialogOpen = (notice) => {
        noticeExplorerDialogRef.value.dialogOpen(notice);
    };

    // 通知发布对话框控制
    const noticeCreatorDialogRef = ref();
    const handleNoticeCreatorDialogOpen = () => {
        noticeCreatorDialogRef.value.dialogOpen();
    };

    const noticeActiveType = ref("ALL");
    const noticeTypeSets = ref([
        {
            label: "全部",
            value: "ALL"
        },
        {
            label: "已读",
            value: "READ"
        },
        {
            label: "未读",
            value: "UNREAD"
        }
    ]);

    // 通知列表
    const isNoticeListLoading = ref(true);
    const noticeList = ref([]);
    const getNoticeList = () => {
        isNoticeListLoading.value = true;
        NoticeApi.getOrgNoticeList({
            userId: curLoginUserStore.curLoginUser.userId,
            orgId: curLoginUserStore.curLoginUser.orgId
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                noticeList.value = res.data;
            }
            isNoticeListLoading.value = false;
        });
    };

    // 待办列表
    const isTodoListLoading = ref(true);
    const todoList = ref([]);
    const getTodoList = () => {

    };

    onBeforeMount(() => {
        getNoticeList();
        getTodoList();
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .notice-and-todo-view-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: 100%;
    padding: 25px;
    background-color: global-variable.$background-color;
    border-top: global-variable.$border-line-width solid global-variable.$border-line-color;

    .notice-area-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 48%;
      height: 100%;
      padding: 15px 20px;
      background-color: white;
      border-radius: 15px;

      .area-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        width: 100%;
        height: 40px;
        border-bottom: global-variable.$border-line-width solid global-variable.$border-line-color;

        .title {
          font-size: 20px;

          .icon {
            color: global-variable.$theme-color;
            margin-right: 5px;
          }

          .refresh-btn {
            border: none;
            box-shadow: none;
            color: gray;
            font-size: 16px;

            &:hover {
              color: global-variable.$theme-color;
            }
          }
        }
      }

      .area-body {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: calc(100% - 40px);
        padding: 10px 10px 10px 0;
        overflow-y: auto;
        gap: 15px;

        .notice-item {
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          align-items: center;
          width: 100%;
          height: 180px;
          min-height: 180px;
          padding: 10px 15px;
          background-color: global-variable.$background-color;
          border-radius: 10px;
          cursor: pointer;

          &:hover {
            opacity: 0.75;
          }

          .notice-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 100%;
            height: 40px;
          }

          .notice-body {
            display: flex;
            width: 100%;
            height: 100px;
            padding: 6px 0;
            font-size: 15px;
            overflow: hidden;
            text-overflow: ellipsis;
            -webkit-line-clamp: 4;
            display: -webkit-box;
            -webkit-box-orient: vertical;
          }

          .notice-footer {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            width: 100%;
            height: 20px;
          }
        }
      }
    }

    .todo-area-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 48%;
      height: 100%;
      padding: 15px 20px;
      background-color: white;
      border-radius: 15px;

      .area-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        width: 100%;
        height: 40px;
        border-bottom: global-variable.$border-line-width solid global-variable.$border-line-color;
      }

      .title {
        font-size: 20px;

        .icon {
          color: global-variable.$theme-color;
          margin-right: 5px;
        }

        .refresh-btn {
          border: none;
          box-shadow: none;
          color: gray;
          font-size: 16px;

          &:hover {
            color: global-variable.$theme-color;
          }
        }
      }
    }

    .area-body {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: calc(100% - 40px);
      padding: 10px 10px 10px 0;
      overflow-y: auto;
    }
  }
</style>