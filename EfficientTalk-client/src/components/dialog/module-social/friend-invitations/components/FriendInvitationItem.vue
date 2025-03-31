<template>
  <div class="invitation-item">
    <!--信息区-->
    <div class="invitation-info">
      <!--头像-->
      <div class="user-avatar">
        <img class="avatar"
             v-if="item.friendAvatar!==null"
             :src="item.friendAvatar"
             alt="avatar"
        />
        <a-avatar class="avatar"
                  v-else
        >{{ item.friendName.substring(0, 2) }}
        </a-avatar>
      </div>
      <!--信息-->
      <div class="info-detail">
        <div class="info">
          <label style="color: var(--theme-color)">{{ item.friendName }}</label>
          <label v-if="item.senderId === curLoginUserId"
                 style="color: black;margin: 0 10px"
          >等待对方处理</label>
          <label v-else
                 style="color: black;margin: 0 10px"
          >申请添加您为好友</label>
          <label style="color: gray">{{ formatMessageTime(item.handleTime) }}</label>
        </div>
        <div class="message single-line-ellipsis">
          留言： {{ item.message }}
        </div>
      </div>
    </div>
    <!--操作区-->
    <div class="invitation-handle">
      <div v-if="item.senderId === curLoginUserId">
        <label v-if="item.state===0"
               style="color: gray;"
        >等待处理</label>
        <label v-else-if="item.state===1"
               style="color: gray;"
        >已同意</label>
        <label v-else-if="item.state===2"
               style="color: gray;"
        >已拒绝</label>
      </div>
      <div v-else>
        <div v-if="item.state===0"
             style="display: flex;align-items: center;gap: 10px"
        >
          <a-select v-model:value="userGroupId"
                    style="width: 100px"
                    placeholder="好友分组"
                    :dropdown-match-select-width="false"
          >
            <a-select-option v-for="(group,index) in userFriendGroups"
                             :key="index"
                             :value="group.groupId"
            >{{ group.groupName }}
            </a-select-option>
          </a-select>
          <a-button type="primary"
                    style="background-color: #67C23A;color: white"
                    :class="{'disable-btn':userGroupId===null}"
                    @click="handleInvitation(item.invitationId,item.senderId,userGroupId,item.groupId,true)"
                    :disabled="userGroupId === null"
          >接受
          </a-button>
          <a-button type="primary"
                    danger
                    @click="handleInvitation(item.invitationId,item.senderId,false)"
          >拒绝
          </a-button>
        </div>
        <label v-else-if="item.state===1"
               style="color: gray;"
        >已同意</label>
        <label v-else-if="item.state===2"
               style="color: gray;"
        >已拒绝</label>
      </div>
    </div>
  </div>
</template>

<script setup>
    import { formatMessageTime } from "../../../../../utils/time-utils.js";
    import {
        ref,
        watch
    } from "vue";
    import { UUID } from "uuidjs";
    import dayjs from "dayjs";
    import { useWebSocketStore } from "../../../../../store/WebSocketStore.js";
    import { useCurLoginUserStore } from "../../../../../store/CurLoginUserStore.js";

    const emit = defineEmits(["handleInvitation"]);

    const props = defineProps({
        item: {
            type: Object,
            default: () => {
                return {
                    invitationId: "",
                    senderId: "",
                    friendId: "",
                    friendName: "",
                    friendAvatar: "",
                    message: "",
                    handleTime: "",
                    state: 0
                };
            }
        },
        curLoginUserId: {
            type: String,
            default: ""
        },
        userFriendGroups: {
            type: Array,
            default: () => {
                return [];
            }
        },
        dialogOpenFlag: {
            type: Boolean,
            default: false
        }
    });

    // 用户选择的分组ID
    const userGroupId = ref(null);

    const webSocketStore = useWebSocketStore();
    const curLoginUserStore = useCurLoginUserStore();

    // 处理好友邀请
    const handleInvitation = (invitationId, friendId, userGroupId, friendGroupId, accept) => {
        const handleMessage = {
            id: UUID.generate(),
            sender: curLoginUserStore.curLoginUser.userId,
            senderAvatar: curLoginUserStore.curLoginUser.userAvatar,
            senderName: curLoginUserStore.curLoginUser.userName,
            receiver: friendId,
            type: "text",
            fileId: null,
            content: "我已同意您的好友申请",
            time: dayjs().format("YYYY-MM-DD HH:mm:ss"),
            isGroup: false
        };
        webSocketStore.sendMessage(handleMessage);
        emit("handleInvitation", invitationId, friendId, userGroupId, friendGroupId, accept);
    };

    watch(() => props.dialogOpenFlag, () => {
        userGroupId.value = null;
    });
</script>

<style scoped
       lang="scss"
>
  .invitation-item {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 90px;
    min-height: 90px;
    background-color: rgba(0, 0, 0, 0.02);
    border-radius: 15px;
    margin: 10px 0;
    padding: 0 25px;

    .invitation-info {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      width: 70%;

      .user-avatar {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        width: 16%;
        height: 100%;

        .avatar {
          display: flex;
          justify-content: center;
          align-items: center;
          $avatar-size: 50px;
          width: $avatar-size;
          height: $avatar-size;
          border-radius: 50%;
        }
      }

      .info-detail {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 84%;
        height: 100%;
        gap: 5px;

        .info {
          display: flex;
          justify-content: flex-start;
          align-items: center;
          width: 100%;
          font-size: 14px;
        }

        .message {
          display: flex;
          justify-content: flex-start;
          align-items: center;
          width: 100%;
          font-size: 13px;
          color: gray;
        }
      }
    }

    .invitation-handle {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      width: 30%;
    }
  }

  .disable-btn {
    background-color: white !important;
    color: gray !important;
  }
</style>