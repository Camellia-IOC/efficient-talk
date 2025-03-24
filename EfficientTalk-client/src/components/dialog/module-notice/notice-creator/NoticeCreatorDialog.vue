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
    <div class="dialog-body-container">
      <div class="item-label">通知主题</div>
      <a-input placeholder="请输入通知主题"
               v-model:value="noticeObject.title"
      ></a-input>
      <div class="item-label">通知内容</div>
      <a-textarea placeholder="请输入通知内容"
                  v-model:value="noticeObject.content"
                  :auto-size="{ minRows: 18, maxRows: 18 }"
      ></a-textarea>
      <div style="margin-top: 20px">
        <a-button type="primary"
                  @click="handlePublishNotice"
                  block
                  :disabled="noticeObject.title === ''||noticeObject.content === ''"
        >发布
        </a-button>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
    import { themeColor } from "../../../../config/config.js";
    import Logo from "../../../logo/Logo.vue";
    import { ref } from "vue";
    import { useCurLoginUserStore } from "../../../../store/CurLoginUserStore.js";
    import { message } from "ant-design-vue";
    import NoticeApi from "../../../../api/modules/NoticeApi.js";
    import { getCurrentTime } from "../../../../utils/time-utils.js";

    const curLoginUserStore = useCurLoginUserStore();

    const dialogTitle = ref("通知编辑");
    const emit = defineEmits(["editOldNotice"]);

    const isEditMode = ref(false);
    const noticeObject = ref({
        id: null,
        title: "",
        content: "",
        orgId: curLoginUserStore.curLoginUser.orgId,
        deptId: curLoginUserStore.curLoginUser.deptId,
        publisher: curLoginUserStore.curLoginUser.userId,
        publishTime: ""
    });
    const handlePublishNotice = () => {
        noticeObject.value.publishTime = getCurrentTime();
        if (isEditMode.value) {
            emit("editOldNotice", noticeObject.value);
        }

        NoticeApi.publishOrgNotice(noticeObject.value).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                message.success("发布成功").then();
                dialogClose();
            }
            else {
                message.error("发布失败").then();
            }
        });
    };

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = (isEdit = false, notice = null) => {
        isEditMode.value = isEdit;
        if (notice !== null) {
            noticeObject.value = notice;
        }
        dialogOpenFlag.value = true;
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
        noticeObject.value = {
            id: null,
            title: "",
            content: "",
            orgId: curLoginUserStore.curLoginUser.orgId,
            deptId: curLoginUserStore.curLoginUser.deptId,
            publisher: curLoginUserStore.curLoginUser.userId,
            publishTime: ""
        };
        isEditMode.value = false;
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
    justify-content: center;
    width: 100%;
    height: 600px;
    overflow: auto;
    gap: 10px;

    .item-label {
      font-size: 16px;
      font-weight: bold;

      &:before {
        content: "*";
        color: red;
      }
    }
  }
</style>