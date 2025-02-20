<template>
  <div class="new-friend-item-container">
    <!--信息区-->
    <div class="new-friend-info">
      <!--头像-->
      <div class="user-avatar">
        <img class="avatar"
             v-if="userInfo.userAvatar!==null"
             :src="userInfo.userAvatar"
             alt="avatar"
        />
        <a-avatar class="avatar"
                  v-else
        >{{ userInfo.userName.substring(0, 2) }}
        </a-avatar>
      </div>
      <!--信息-->
      <div class="info-detail">
        <div class="info">
          <label style="color: var(--theme-color)">{{ userInfo.userName }}</label>
          <label style="color: gray;margin-left: 10px">ID: {{ userInfo.userId }}</label>
        </div>
        <div class="role">
          <label class="role-item">
            <CrownTwoTone/>
            {{ userInfo.deptName }}</label>
          <label class="role-item">{{ userInfo.jobName }}</label>
        </div>
      </div>
    </div>
    <!--操作区-->
    <div class="add-handle">
      <div v-if="userInfo.isFriend"
           style="color: gray;width: 80px;display: flex;justify-content: center;align-items: center"
      >已添加
      </div>
      <a-button v-else
                type="primary"
                @click="handleEditInvitation"
                style="width: 80px"
      >加好友
      </a-button>
    </div>
  </div>
</template>

<script setup>
    import { CrownTwoTone } from "@ant-design/icons-vue";

    const props = defineProps({
        userInfo: {
            type: Object
        }
    });

    const emit = defineEmits(["openInviteDialog"]);

    const handleEditInvitation = () => {
        emit("openInviteDialog", props.userInfo.userId);
    };
</script>

<style scoped
       lang="scss"
>
  .new-friend-item-container {
    display: flex;
    align-items: center;
    width: 100%;
    height: 75px;
    min-height: 75px;
    padding: 10px;
    border-radius: 10px;

    &:hover {
      background-color: rgba(0, 0, 0, 0.03);
    }

    .new-friend-info {
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

        .role {
          display: flex;
          justify-content: flex-start;
          align-items: center;
          width: 100%;
          font-size: 13px;
          color: gray;
          gap: 10px;

          .role-item {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 3px
          }
        }
      }
    }

    .add-handle {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      width: 30%;
    }
  }
</style>