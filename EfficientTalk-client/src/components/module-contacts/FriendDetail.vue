<template>
  <div class="friend-detail">
    <div class="default-content"
         v-if="friendId === null"
    >
      <Logo :color="themeColor"
            :size="256"
      />
    </div>
    <div class="user-detail-container"
         v-else
    >
      <div class="user-info">
        <div class="user-avatar">
          <a-image v-if="friendInfo.userAvatar!==null"
                   :src="friendInfo.userAvatar"
                   :preview-mask="false"
                   style="width: 100px;height: 100px;cursor: pointer;border-radius: 50%"
          />
          <a-avatar v-else
                    style="display:flex;justify-content:center;align-items:center;width: 100px;height: 100px;cursor: pointer;border-radius: 50%;font-size: 24px"
          >{{ friendInfo.userName.substring(0, 2) }}
          </a-avatar>
        </div>
        <div class="user-base-info">
          <div class="user-name">{{ friendInfo.userName }}</div>
          <div class="user-id">ID:<label style="margin-left: 10px">{{ friendInfo.userId }}</label></div>
        </div>
      </div>
      <div class="dept-info">
        <a-descriptions class="dept-info-content"
                        bordered
                        :column="1"
        >
          <a-descriptions-item label="工号">{{ friendInfo.employeeId }}</a-descriptions-item>
          <a-descriptions-item label="部门">{{ friendInfo.deptName }}</a-descriptions-item>
          <a-descriptions-item label="职位">{{ friendInfo.jobName }}</a-descriptions-item>
        </a-descriptions>
      </div>
      <div class="operation-bar">
        <a-button v-if="friendInfo.userId===curLoginUserStore.curLoginUser.userId"
                  class="operation-btn"
        >编辑资料
        </a-button>
        <a-button type="primary"
                  danger
                  v-if="friendInfo.isFriend === true && friendInfo.userId !== curLoginUserStore.curLoginUser.userId"
                  class="operation-btn"
                  @click="handleDeleteFriend"
        >删除好友
        </a-button>
        <a-button class="operation-btn"
                  v-else-if="friendInfo.isFriend === false"
                  @click="handleEditInvitationDialogOpen"
        >添加好友
        </a-button>
        <a-button type="primary"
                  @click="handleGoChat"
                  class="operation-btn"
        > 发送消息
        </a-button>
      </div>
    </div>
  </div>

  <!--编辑添加信息对话框-->
  <EditInvitationDialog ref="editInvitationDialog"/>
</template>

<script setup>
    import {
        ref,
        watch
    } from "vue";
    import { useRouter } from "vue-router";
    import UserApi from "../../api/modules/UserApi.js";
    import SocialApi from "../../api/modules/SocialApi.js";
    import { message } from "ant-design-vue";
    import Logo from "../logo/Logo.vue";
    import { themeColor } from "../../config/config.js";
    import EditInvitationDialog from "../dialog/module-social/add-friend/EditInvitationDialog.vue";
    import { useCurLoginUserStore } from "../../store/CurLoginUserStore.js";

    const router = useRouter();

    const props = defineProps({
        friendId: {
            type: String,
            default: null
        }
    });

    // 当前登录的用户信息
    const curLoginUserStore = useCurLoginUserStore()

    // 编辑添加信息对话框
    const editInvitationDialog = ref();
    const handleEditInvitationDialogOpen = () => {
        editInvitationDialog.value.dialogOpen(curLoginUserStore.curLoginUser.userId, props.friendId);
    };

    // 好友信息
    const friendInfo = ref({});

    // 处理跳转到聊天界面
    const handleGoChat = () => {
        window.dispatchEvent(new CustomEvent("navForceChange", {
            detail: "chat"
        }));
        router.push({
            name: "chat",
            query: {
                friendInfo: JSON.stringify(friendInfo.value)
            }
        });
    };

    // 获取好友信息
    const getUserDetail = (userId) => {
        UserApi.getUserDetail({
            curLoginUserId: curLoginUserStore.curLoginUser.userId,
            userId: userId
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                const data = res.data;
                if (data != null) {
                    friendInfo.value = {
                        userId: data.userId,
                        userName: data.userName,
                        userAvatar: data.userAvatar,
                        employeeId: data.employeeId,
                        deptId: data.deptId,
                        deptName: data.deptName,
                        jobId: data.jobId,
                        jobName: data.jobName,
                        isFriend: data.isFriend,
                        isGroup: false
                    };
                }
            }
        });
    };

    // 监听传入参数变化
    watch(() => props.friendId, async (newValue, oldValue) => {
        // 获取用户信息
        getUserDetail(newValue);
    });

    // 删除好友
    const handleDeleteFriend = () => {
        SocialApi.deleteFriend({
            userId: curLoginUserStore.curLoginUser.userId,
            friendId: props.friendId
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                message.success("删除成功");
                friendInfo.value.isFriend = false;
            }
            else {
                message.error("删除失败");
            }
        }).catch((err) => {
            console.error(err);
            message.error("删除失败");
        });
    };
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .friend-detail {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;

    .user-detail-container {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100%;
      gap: 80px;

      .user-info {
        display: flex;
        align-items: center;
        width: 60%;
        height: fit-content;

        .user-avatar {
          display: flex;
          justify-content: center;
          align-items: center;
        }

        .user-base-info {
          display: flex;
          flex-direction: column;
          margin-left: 20px;
          gap: 12px;

          .user-name {
            font-size: 27px;
            font-weight: bold;
          }

          .user-id {
            color: gray;
            font-size: 18px;
          }
        }
      }

      .dept-info {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 60%;
        height: fit-content;

        .dept-info-content {
          width: 100%;
        }
      }

      .operation-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 60%;
        height: fit-content;
        gap: 30px;

        .operation-btn {
          width: 100px;
        }
      }
    }
  }
</style>