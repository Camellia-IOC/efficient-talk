<template>
  <a-modal v-model:open="dialogOpenFlag"
           title="发起好友申请"
           centered
           :mask="false"
           width="400px"
           :footer="null"
           @cancel="dialogClose"
  >
    <div class="edit-invitation-container">
      <div class="invitation-form">
        <div class="form-item">
          <label>好友分组</label>
          <a-select v-model:value="friendGroup"
                    placeholder="好友分组"
          >
            <a-select-option v-for="(group,index) in userFriendGroups"
                             :key="index"
                             :value="group.groupId"
            >{{ group.groupName }}
            </a-select-option>
          </a-select>
        </div>
        <div class="form-item">
          <label>添加理由</label>
          <a-textarea placeholder="请输入添加理由"
                      v-model:value="inviteMessage"
                      :auto-size="{ minRows: 3, maxRows: 3 }"
          ></a-textarea>
        </div>
      </div>
      <div class="operation-bar">
        <a-button type="primary"
                  @click="handleAddNewFriend"
        >加好友
        </a-button>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
    import { ref } from "vue";
    import { message } from "ant-design-vue";
    import SocialApi from "../../../api/modules/SocialApi.js";

    // 当前登录的用户ID
    const curLoginUserId = ref(null);
    const selectedFriendId = ref(null);

    // 申请信息
    const friendGroup = ref();
    const inviteMessage = ref("");

    // 用户的好友分组
    const userFriendGroups = ref([]);
    const getUserFriendGroups = async () => {
        const response = await SocialApi.getUserFriendGroups({
            userId: curLoginUserId.value
        });

        const res = response.data;
        if (res.code === 0) {
            if (res.data !== null) {
                userFriendGroups.value = res.data.friendGroups;
            }
        }
        else {
            message.error("获取好友分组失败");
        }
    };

    // 添加好友
    const handleAddNewFriend = async () => {
        const response = await SocialApi.createFriendInvite({
            userId: curLoginUserId.value,
            friendId: selectedFriendId.value,
            groupId: friendGroup.value,
            message: inviteMessage.value
        });

        const res = response.data;
        if (res.code === 0) {
            message.success("好友申请已发送");
            dialogClose();
        }
        else {
            message.error("好友申请发送失败");
        }
    };

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = (userId, friendId) => {
        curLoginUserId.value = userId;
        selectedFriendId.value = friendId;

        // 获取用户的好友分组
        getUserFriendGroups();

        dialogOpenFlag.value = true;
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
        curLoginUserId.value = null;
        selectedFriendId.value = null;
        friendGroup.value = null;
        inviteMessage.value = "";
    };

    defineExpose({
        dialogOpen,
        dialogClose
    });
</script>

<style scoped
       lang="scss"
>
  .edit-invitation-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 240px;

    .invitation-form {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 85%;
      gap: 20px;

      .form-item {
        display: flex;
        flex-direction: column;
        justify-content: center;
        width: 100%;
        gap: 5px;
      }
    }

    .operation-bar {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 15%;
    }
  }
</style>