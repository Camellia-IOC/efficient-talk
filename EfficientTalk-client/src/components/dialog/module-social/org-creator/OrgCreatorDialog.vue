<template>
  <a-modal v-model:open="dialogOpenFlag"
           centered
           :mask="false"
           width="400px"
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
      <div class="org-create-container"
           v-if="curMode === 'CREATE'"
      >
        <div class="input-area">
          <label class="tip-label">组织名称</label>
          <a-input v-model:value="orgName"
                   placeholder="请输入组织名称"
                   allow-clear
          />
          <label class="tip-label">组织ID</label>
          <a-input v-model:value="orgId"
                   placeholder="请输入组织ID"
                   allow-clear
          />
          <label class="tip-label">组织Logo</label>
          <a-upload
                  v-model:file-list="orgLogo"
                  name="avatar"
                  list-type="picture-card"
                  class="avatar-uploader"
                  :show-upload-list="false"
                  action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
                  :before-upload="beforeUpload"
                  @change="handleChange"
          >
            <img v-if="imageUrl"
                 :src="imageUrl"
                 alt="avatar"
            />
            <div v-else>
              <loading-outlined v-if="loading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
              <div class="ant-upload-text">上传</div>
            </div>
          </a-upload>
        </div>
        <a-button type="primary"
                  :disabled="orgName === '' && orgId === ''"
                  @click="handleCreate"
        >创建
        </a-button>
      </div>
      <div class="org-join-container"
           v-else
      >
        <div class="input-area">
          <label class="tip-label">组织ID</label>
          <a-input v-model:value="inviteCode"
                   placeholder="请输入组织ID"
                   allow-clear
          />
        </div>
        <a-button type="primary"
                  :disabled="inviteCode === ''"
                  @click="handleJoin"
        >申请加入
        </a-button>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
    import { ref } from "vue";
    import Logo from "../../../logo/Logo.vue";
    import { themeColor } from "../../../../config/config.js";
    import {
        PlusOutlined,
        LoadingOutlined
    } from "@ant-design/icons-vue";
    import { useCurLoginUserStore } from "../../../../store/CurLoginUserStore.js";
    import SocialApi from "../../../../api/modules/SocialApi.js";
    import { message } from "ant-design-vue";

    // 当前登录用户数据
    const curLoginUserStore = useCurLoginUserStore();

    // 对话框配置
    const curMode = ref("CREATE");
    const dialogTitle = ref("创建/加入组织");
    const orgName = ref("");
    const orgId = ref("");
    const orgLogo = ref();
    const inviteCode = ref("");
    const resetData = () => {
        inviteCode.value = "";
    };

    // 处理创建组织
    const handleCreate = () => {
        const formData = new FormData();
        formData.append("orgId", orgId.value);
        formData.append("orgName", orgName.value);
        if (orgLogo.value !== undefined) {
            formData.append("orgLogo", orgLogo.value);
        }
        formData.append("owner", curLoginUserStore.curLoginUser.userId);

        SocialApi.createOrganization(formData).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                curLoginUserStore.curLoginUser.orgId = orgId.value;
                message.success("创建成功");
                dialogClose();
            }
            else {
                message.error("创建失败");
            }
        });
    };

    // 处理加入组织
    const handleJoin = () => {
        dialogClose();
    };

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = (mode) => {
        curMode.value = mode;
        switch (mode) {
            case "CREATE":
                dialogTitle.value = "创建组织";
                break;
            case "JOIN":
                dialogTitle.value = "加入组织";
                break;
            default:
                dialogTitle.value = "创建/加入组织";
                break;
        }
        dialogOpenFlag.value = true;
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
        resetData();
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
    width: 100%;
    height: fit-content;

    .org-create-container {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: center;
      gap: 20px;
      width: 100%;
      margin: 20px 0;
      height: fit-content;

      .input-area {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: flex-start;
        width: 100%;
        gap: 10px;

        .tip-label {
          &:before {
            content: "*";
            color: red;
          }
        }
      }
    }

    .org-join-container {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: center;
      gap: 20px;
      width: 100%;
      margin: 20px 0;
      height: fit-content;

      .input-area {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: flex-start;
        width: 100%;
        gap: 10px;

        .tip-label {
          &:before {
            content: "*";
            color: red;
          }
        }
      }
    }
  }
</style>