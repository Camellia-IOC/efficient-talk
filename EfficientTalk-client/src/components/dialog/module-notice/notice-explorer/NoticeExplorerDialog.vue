<template>
  <a-modal v-model:open="dialogOpenFlag"
           centered
           :mask="false"
           width="500px"
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
    <div class="dialog-body-container"
         ref="dialogBodyContainerRef"
    >
      <div class="notice-header">
        <div style="display:flex;justify-content: center;align-items:center;font-weight: bold;font-size: 24px;text-align: center">
          {{ noticeItem.title }}
        </div>
        <div style="display: flex;justify-content: center;align-items:center;width: 100%; gap:80px">
          <div style="color: gray;font-size: 14px">{{ formatDate(noticeItem.publishTime) }}</div>
          <div style="color: gray;font-size: 14px">{{ noticeItem.deptName }}</div>
        </div>
      </div>
      <div class="notice-body">
        <div class="paragraph"
             v-for="(item,index) in noticeItem.paragraphs"
             :key="index"
        >
          {{ item }}
        </div>
      </div>
      <div class="notice-footer">
        <div class="operation-bar"
             v-if="noticeItem.publisher===curLoginUser.userId"
        >
          <a-button type="primary"
                    @click="handleEdit"
                    class="operation-btn"
          >编辑
          </a-button>
          <a-button type="primary"
                    danger
                    class="operation-btn"
                    @click="handleDelete"
          >删除
          </a-button>
        </div>
        <div class="reader-list">
          <div class="reader-list-title">
            <CheckCircleOutlined/>
            已读成员
          </div>
          <div class="reader-list-item"
               v-for="(item,index) in noticeReaderList"
               :key="index"
               v-if="noticeReaderList.length!==0"
          >
            <a-tooltip>
              <template #title>
                <div>{{ item.userName }}</div>
              </template>
              <img v-if="item.userAvatar!==null"
                   :src="item.userAvatar"
                   class="avatar"
                   alt="avatar"
              />
              <a-avatar v-else
                        class="avatar"
                        style="display:flex;justify-content:center;align-items:center;font-size: 24px"
              >{{ item.userName.substring(0, 2) }}
              </a-avatar>
            </a-tooltip>
          </div>
          <div v-else
               style="width: 100%;height: 80px"
          >
            <EmptyContainer :description="'暂无已读成员'"/>
          </div>
        </div>
      </div>
    </div>
  </a-modal>

  <!--通知编辑对话框-->
  <NoticeCreatorDialog ref="noticeCreatorDialogRef"
                       @edit-old-notice="editOldNotice"
  />
</template>

<script setup>
    import { themeColor } from "../../../../config/config.js";
    import Logo from "../../../logo/Logo.vue";
    import {
        nextTick,
        ref
    } from "vue";
    import { formatDate } from "../../../../utils/time-utils";
    import NoticeCreatorDialog from "../notice-creator/NoticeCreatorDialog.vue";
    import { useCurLoginUserStore } from "../../../../store/CurLoginUserStore.js";
    import NoticeApi from "../../../../api/modules/NoticeApi.js";
    import { message } from "ant-design-vue";
    import EmptyContainer from "../../../empty-container/EmptyContainer.vue";
    import {
        CheckCircleOutlined
    } from "@ant-design/icons-vue";

    const emit = defineEmits(["updateOldNotice"]);

    const curLoginUser = useCurLoginUserStore().curLoginUser;

    const dialogBodyContainerRef = ref();
    const noticeCreatorDialogRef = ref();
    const handleEdit = () => {
        noticeCreatorDialogRef.value.dialogOpen(true, noticeItem.value);
    };
    const handleDelete = () => {
        NoticeApi.deleteOrgNotice({
            noticeId: noticeItem.value.id
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                message.success("删除成功").then();
                dialogClose();
            }
            else {
                message.error("删除失败").then();
            }
        });
    };

    const dialogTitle = ref("通知详情");
    const noticeItem = ref(null);
    const noticeReaderList = ref([]);
    const loadNoticeReaderList = () => {
        NoticeApi.getOrgNoticeReadHistory({
            noticeId: noticeItem.value.id
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                noticeReaderList.value = res.data;
            }
        });
    };

    const editOldNotice = (notice) => {
        noticeItem.value = notice;
        noticeItem.value.paragraphs = notice.content.split("\n");
        emit("updateOldNotice", notice);
    };

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = (notice) => {
        noticeItem.value = notice;
        noticeItem.value.paragraphs = notice.content.split("\n");
        loadNoticeReaderList();
        dialogOpenFlag.value = true;
        nextTick(() => {
            dialogBodyContainerRef.value.scrollTop = 0;
        });
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
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
    width: 100%;
    height: 600px;
    overflow: auto;
    gap: 10px;

    .notice-header {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      gap: 10px;
      margin-bottom: 15px;
    }

    .notice-body {
      display: flex;
      flex-direction: column;
      width: 100%;
      min-height: 460px;
      gap: 8px;

      .paragraph {
        white-space: pre-wrap;
        font-size: 16px;
        text-indent: 2em;
      }
    }

    .notice-footer {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      margin-top: 15px;

      .operation-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        gap: 20px;
        margin-bottom: 15px;

        .operation-btn {
          width: 45%;
        }
      }

      .reader-list {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        flex-wrap: wrap;
        width: 100%;
        background-color: global-variable.$background-color;
        border-radius: 10px;
        padding: 10px 20px 10px 30px;
        gap: 10px;

        .reader-list-title {
          width: 100%;
          font-size: 18px;
          font-weight: bold;
          margin-bottom: 5px;
        }

        .reader-list-item {
          display: flex;
          align-items: center;
          width: 40px;
          height: 40px;
          cursor: pointer;

          .avatar {
            width: 40px;
            height: 40px;
            border-radius: 8px;
          }
        }
      }
    }
  }
</style>