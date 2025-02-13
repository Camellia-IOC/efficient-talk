<template>
  <div class="friend-detail">
    <div class="default-content"
         v-if="friendId === null"
    >
      <img style="width: 200px;height: 200px"
           src="../../assets/logo.png"
           alt="logo"
      >
    </div>
    <div class="user-detail-container"
         v-else
    >
      <div class="user-info">
        <div class="user-avatar">
          <a-image :src="friendInfo.userAvatar"
                   :preview-mask="false"
                   style="width: 100px;height: 100px;cursor: pointer;border-radius: 50%"
          />
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
        <a-button v-if="friendInfo.userId===curLoginUser.userId"
                  class="operation-btn"
        >编辑资料
        </a-button>
        <a-button type="primary"
                  danger
                  v-if="friendInfo.isFriend === true && friendInfo.userId !== curLoginUser.userId"
                  class="operation-btn"
                  @click="handleDeleteFriend"
        >删除好友
        </a-button>
        <a-button @click="handleCreateFriendInvite"
                  class="operation-btn"
                  v-else-if="friendInfo.isFriend === false && friendInfo.userId !== curLoginUser.userId"
        >添加好友
        </a-button>
        <a-button type="primary"
                  @click="handleGoChat"
                  class="operation-btn"
                  v-if="friendInfo.isFriend === true && friendInfo.userId !== curLoginUser.userId"
        > 发送消息
        </a-button>
      </div>
    </div>
  </div>
</template>

<script setup>
    import {
        ref,
        watch
    } from "vue";
    import { useRouter } from "vue-router";
    import UserApi from "../../api/modules/UserApi.js";
    import { getCurUserData } from "../../database/cur-user.js";
    import SocialApi from "../../api/modules/SocialApi.js";
    import { message } from "ant-design-vue";

    const router = useRouter();

    const props = defineProps({
        friendId: {
            type: String,
            default: null
        }
    });

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
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
    const getUserDetail = async (userId) => {
        await UserApi.getUserDetail({
            curLoginUserId: curLoginUser.value.userId,
            userId: userId
        }).then((res) => {
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
                        isFriend: data.isFriend
                    };
                }
            }
        });
    };

    // 监听传入参数变化
    watch(() => props.friendId, async (newValue, oldValue) => {
        // 初始化当前登录的用户信息
        await updateCurLoginUser();

        // 获取用户信息
        await getUserDetail(newValue);
    });

    // 删除好友
    const handleDeleteFriend = async () => {
        await SocialApi.deleteFriend({
            userId: curLoginUser.value.userId,
            friendId: props.friendId
        }).then((res) => {
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

    // 添加好友
    const handleCreateFriendInvite = async () => {
        await SocialApi.createFriendInvite({
            userId: curLoginUser.value.userId,
            friendId: props.friendId
        }).then((res) => {
            if (res.code === 0) {
                message.success("已发送好友邀请");
            }
            else {
                message.error("发送好友邀请失败");
            }
        }).catch((err) => {
            console.error(err);
            message.error("发送好友邀请失败");
        });
    };
</script>

<style scoped
       lang="scss"
>
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