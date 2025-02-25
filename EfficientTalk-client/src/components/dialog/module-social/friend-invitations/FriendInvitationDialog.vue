<template>
  <a-modal v-model:open="dialogOpenFlag"
           centered
           :mask="false"
           width="700px"
           :footer="null"
           @cancel="dialogClose"
  >
    <template #title>
      <div class="dialog-title">
        <div class="logo">
          <Logo :color="themeColor" :size="30"/>
        </div>
        <label>好友通知</label>
      </div>
    </template>
    <div class="invitations-container">
      <div v-if="invitationList.length !== 0"
           class="invitations-list"
      >
        <FriendInvitationItem v-for="(item,index) in invitationList"
                              :key="index"
                              :item="item"
                              :cur-login-user-id="curLoginUserId"
                              :user-friend-groups="userFriendGroups"
                              @handle-invitation="handleInvitation"
                              :dialog-open-flag="dialogOpenFlag"
        />
      </div>
      <EmptyContainer v-else
                      :description="'暂无好友通知'"
      />
    </div>
  </a-modal>
</template>

<script setup>
    import {
        ref
    } from "vue";
    import EmptyContainer from "../../../empty-container/EmptyContainer.vue";
    import SocialApi from "../../../../api/modules/SocialApi.js";
    import { message } from "ant-design-vue";
    import FriendInvitationItem from "./components/FriendInvitationItem.vue";
    import Logo from "../../../logo/Logo.vue";
    import { themeColor } from "../../../../config/config.js";

    const props = defineProps({
        curLoginUserId: {
            type: String,
            default: ""
        }
    });

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = () => {
        // 获取好友通知列表
        getFriendInvitationList();
        // 获取好友分组列表
        getUserFriendGroups();
        dialogOpenFlag.value = true;
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
    };

    // 好友通知列表
    const invitationList = ref([]);
    const getFriendInvitationList = () => {
        SocialApi.getFriendInvitationList({
            userId: props.curLoginUserId,
        }).then((response) => {
            const res = response.data
            if (res.code === 0) {
                const data = res.data;
                if (data != null) {
                    invitationList.value = data.invitationList;
                }
            }
        }).catch((err) => {
            // 获取好友通知列表失败, 清空列表
            console.error(err);
            invitationList.value = [];
        });
    };

    // 用户的好友分组
    const userFriendGroups = ref([]);
    const getUserFriendGroups = () => {
        SocialApi.getUserFriendGroups({
            userId: props.curLoginUserId
        }).then((response) => {
            const res = response.data
            if (res.code === 0) {
                const data = res.data;
                if (data != null) {
                    userFriendGroups.value = data.friendGroups;
                }
            }
        }).catch((err) => {
            // 获取好友分组失败, 清空列表
            console.error(err);
        });
    };

    // 处理好友邀请
    const handleInvitation = (invitationId, friendId, userGroupId, friendGroupId, accept) => {
        SocialApi.handleFriendInvite({
            invitationId: invitationId,
            userId: props.curLoginUserId,
            friendId: friendId,
            userGroupId: userGroupId,
            friendGroupId: friendGroupId,
            accept: accept
        }).then((response) => {
            const res = response.data
            if (res.code === 0) {
                const result = accept ? 1 : 2;
                for (let i = 0; i < invitationList.value.length; i++) {
                    if ((invitationList.value[i].senderId === friendId && invitationList.value[i].receiverId === props.curLoginUserId) || (invitationList.value[i].senderId === props.curLoginUserId && invitationList.value[i].receiverId === friendId)) {
                        invitationList.value[i].state = result;
                    }
                }
                if (accept) {
                    message.success("已同意");
                    window.dispatchEvent(new Event("updateFriendList"));
                }
                else {
                    message.success("已拒绝");
                }
            }
        }).catch((err) => {
            console.error(err);
            message.error("处理失败");
        });
    };

    defineExpose({
        dialogOpen,
        dialogClose
    });
</script>

<style scoped
       lang="scss"
>
  .dialog-title {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 10px;
    font-size: 18px;
    width: 100%;
  }

  .invitations-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 500px;

    .invitations-list {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: 100%;
      overflow-y: auto;
    }
  }
</style>