<template>
  <div class="friend-list">
    <div class="search-area">
      <a-input class="content-input"
               placeholder="搜索"
      ></a-input>
      <a-button class="search-btn">
        <PlusOutlined/>
      </a-button>
    </div>
    <div class="notification-item"
         @click="handleInvitationDialogOpen"
    >
      <div class="item-content">
        <span>好友通知</span>
        <RightOutlined/>
      </div>
    </div>
    <div class="type-select-bar">
      <a-segmented v-model:value="activeType"
                   :options="typeSets"
                   :block="true"
                   style="width: 100%"
      />
    </div>
    <div class="list-container">
      <div v-if="activeType === 'FRIEND'"
           class="type-friend-list"
      >
        <div class="empty-list"
             v-if="friendList.total === 0"
        >
          <EmptyContainer :description="'暂无好友'"/>
        </div>
        <a-collapse v-model:activeKey="activeFriendGroupKey"
                    ghost
                    class="group-list"
                    v-else
        >
          <a-collapse-panel v-for="(group,index) in friendList.groupList"
                            :key="index"
                            :header="group.groupName"
                            class="group-list-item"
          >
            <template #extra><label style="color: gray;font-size: 12px">{{ group.friendList.length }}</label></template>
            <div class="friend-list">
              <div v-for="(friend,index) in group.friendList"
                   :key="index"
                   class="friend-list-item"
                   :class="{'friend-list-item-active': curFriendId === friend.userId}"
                   @click="handleSelectFriend(friend)"
              >
                <div class="user-avatar">
                  <img :src="friend.userAvatar"
                       alt="avatar"
                       class="avatar"
                  >
                </div>
                <div class="user-info">
                  <div class="user-name">{{ friend.userName }}</div>
                  <div class="user-dept"
                       v-if="friend.deptName!==null && friend.deptId!==null"
                  >
                    <CrownTwoTone/>
                    {{ friend.deptName }}
                  </div>
                </div>
              </div>
            </div>
          </a-collapse-panel>
        </a-collapse>
      </div>
      <div v-else-if="activeType === 'GROUP'"
           class="type-group-list"
      >
        <div class="empty-list"
             v-if="groupList.length === 0"
        >
          <EmptyContainer :description="'你还没有加入组织噢'"/>
        </div>
      </div>
    </div>
  </div>

  <!--好友通知对话框-->
  <FriendInvitationDialog ref="friendInvitationDialog"
                          :cur-login-user-id="curLoginUser.userId"
  />
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import {
        CrownTwoTone,
        RightOutlined,
        PlusOutlined
    } from "@ant-design/icons-vue";
    import SocialApi from "../../api/modules/SocialApi";
    import { getCurUserData } from "../../database/cur-user.js";
    import FriendInvitationDialog from "../dialog/friend-invitations/FriendInvitationDialog.vue";
    import EmptyContainer from "../empty-container/EmptyContainer.vue";

    const emits = defineEmits(["setSelectedFriend"]);

    // 好友邀请对话框控制
    const friendInvitationDialog = ref();
    const handleInvitationDialogOpen = () => {
        friendInvitationDialog.value.dialogOpen();
    };

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // 当前展开的好友分组
    const activeFriendGroupKey = ref([]);

    // 类型集合
    const typeSets = [{
        label: "好友",
        value: "FRIEND"
    }, {
        label: "组织",
        value: "GROUP"
    }];

    // 当前选择的类型
    const activeType = ref("FRIEND");

    // 选择朋友
    const curFriendId = ref(null);
    const handleSelectFriend = (friend) => {
        curFriendId.value = friend.userId;
        emits("setSelectedFriend", curFriendId.value);
    };

    // 好友列表
    const friendList = ref({});

    // 组织列表
    const groupList = ref([]);

    // 获取好友列表
    const getFriendList = async () => {
        await SocialApi.getFriendList({
            userId: curLoginUser.value.userId,
        }).then((res) => {
            if (res.code === 0) {
                const data = res.data;
                if (data != null) {
                    friendList.value = data;
                }
            }
        });
    };

    // 更新好友列表
    const handleUpdateFriendList = async () => {
        await getFriendList();
    };

    onBeforeMount(async () => {
        // 订阅好友列表更新事件
        window.addEventListener("updateFriendList", handleUpdateFriendList);

        await updateCurLoginUser();

        // 获取好友列表
        await getFriendList();
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .friend-list {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;

    $search-area-height: 60px;

    .search-area {
      display: flex;
      justify-content: space-around;
      align-items: center;
      width: 100%;
      padding: 0 10px;
      height: $search-area-height;

      .content-input {
        color: gray;
        width: 75%;
        height: 60%;
        border: none;
        background-color: rgba(0, 0, 0, 0.04);
      }

      .search-btn {
        display: flex;
        justify-content: center;
        align-items: center;
        width: calc($search-area-height * 0.6);
        height: 60%;
        color: gray;
        border: none;
        border-radius: 8px;
        background-color: rgba(0, 0, 0, 0.04);

        &:hover {
          background-color: rgba(0, 0, 0, 0.08);
        }
      }
    }

    $notification-item-height: 50px;

    .notification-item {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: $notification-item-height;
      padding: 2px 6%;

      .item-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        height: 100%;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.65);
      }

      &:hover {
        cursor: pointer;
        background-color: rgba(0, 0, 0, 0.05);
      }
    }

    $type-select-bar-height: 50px;

    .type-select-bar {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 90%;
      height: $type-select-bar-height;
      border-top: 1px solid rgba(0, 0, 0, 0.05);
    }

    .list-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: calc(100% - #{$type-select-bar-height} - $notification-item-height - $search-area-height);

      .type-friend-list {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: 100%;
        overflow-y: auto;

        .empty-list {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 100%;
          height: 100%;
          font-size: 14px;
          color: gray;
        }

        .group-list {
          width: 100%;

          .group-list-item {
            width: 100%;

            .friend-list {
              display: flex;
              flex-direction: column;
              align-items: center;
              width: 100%;

              .friend-list-item-active {
                background-color: rgba(global-variable.$theme-color, 0.1);

                &:hover {
                  background-color: rgba(global-variable.$theme-color, 0.1) !important;
                }
              }

              .friend-list-item {
                display: flex;
                align-items: center;
                width: 100%;
                height: 80px;
                padding: 5px 15px;
                cursor: pointer;

                &:hover {
                  background-color: rgba(0, 0, 0, 0.05);
                }

                .user-avatar {
                  display: flex;
                  justify-content: center;
                  align-items: center;

                  .avatar {
                    width: 50px;
                    height: 50px;
                    border-radius: 50%;
                  }
                }

                .user-info {
                  display: flex;
                  flex-direction: column;
                  justify-content: center;
                  align-items: flex-start;
                  width: 100%;
                  height: 65%;
                  padding-left: 15px;
                  gap: 5px;

                  .user-name {
                    font-size: 14px;
                  }

                  .user-dept {
                    display: flex;
                    align-items: center;
                    font-size: 12px;
                    color: gray;
                    gap: 5px;
                  }
                }
              }
            }
          }
        }
      }

      .type-group-list {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: 100%;

        .empty-list {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 100%;
          height: 100%;
          font-size: 14px;
          color: gray;
        }
      }
    }
  }

  // 设置选中项的字体颜色
  :deep(.ant-segmented-item-selected) {
    color: global-variable.$theme-color !important;
  }

  // 去除折叠面板内的边距
  :deep(.ant-collapse-content-box) {
    padding: 0 !important;
  }
</style>