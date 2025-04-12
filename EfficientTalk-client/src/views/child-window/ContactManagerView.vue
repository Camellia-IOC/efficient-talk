<template>
  <div class="contact-manager-view-container">
    <div class="select-area">
      <div class="header-container">
        <a-button class="group-btn"
                  @click="getAllFriendList"
        >全部好友
        </a-button>
      </div>
      <div class="group-selector-container">
        <a-button v-for="(item,index) in groupList"
                  :key="index"
                  type="primary"
                  class="group-btn"
                  @click="handleSelectGroup(item)"
        >
          {{ item.groupName }}
        </a-button>
      </div>
      <div class="footer-container">
        <a-button class="group-btn"
                  @click="handleOpenCreateNewGroupDialog"
        >新建分组
        </a-button>
      </div>
    </div>
    <div class="friend-list-area">
      <div class="friend-group-info">
        <label style="font-size: 18px">{{ curSelectedGroup.groupName }}</label>
        <div class="operation-bar">
          <a-input placeholder="请输入用户名"
                   allow-clear
                   v-model:value="filterKey"
          >
            <template #prefix>
              <SearchOutlined/>
            </template>
          </a-input>
          <a-button danger
                    @click="handleDeleteFriendGroup"
          >删除
          </a-button>
        </div>
      </div>
      <a-spin wrapper-class-name="friend-list"
              :spinning="isFriendListLoading"
      >
        <EmptyContainer v-if="friendList.length === 0"
                        description="暂无好友"
        />
        <div class="list-item"
             v-for="(item,index) in friendList.filter((item) => {
                 return item.userName.includes(filterKey);
             })"
             :key="index"
             v-else
        >
          <div style="display: flex;align-items: center;gap: 10px;width: 28%">
            <img style="width: 30px;height: 30px;border-radius: 50%"
                 :src="item.userAvatar"
                 alt="avatar"
            >
            <div style="font-size: 14px">{{ item.userName }}</div>
          </div>
          <div style="display: flex;justify-content:center;align-items: center;width: 32%">
            <label style="font-size: 14px;color: var(--theme-color)">
              <CrownOutlined/>
              {{ item.orgName }}</label>
          </div>
          <div style="display: flex;align-items: center;width: 25%">
            <a-select style="width: 100%"
                      v-model:value="item.groupId"
                      @change="(groupId) => handleFriendGroupChange(item.userId,groupId)"
            >
              <a-select-option v-for="(option,index) in groupList"
                               :key="index"
                               :value="option.groupId"
              >{{ option.groupName }}
              </a-select-option>
            </a-select>
          </div>
          <div style="display: flex;justify-content: flex-end;align-items: center;width: 15%">
            <a-button type="primary"
                      danger
                      size="small"
                      @click="handleDeleteFriend(item.userId)"
            >删除
            </a-button>
          </div>
        </div>
      </a-spin>
    </div>
  </div>

  <a-modal v-model:open="CreateNewGroupDialogOpenFlag"
           title="新建好友分组"
           :mask="false"
           width="320px"
           :footer="null"
           @cancel="handleCloseCreateNewGroupDialog"
           centered
  >
    <div style="display: flex;flex-direction: column;align-items: center">
      <div style="display: flex;justify-content: space-between;align-items: center;margin: 10px 0">
        <label style="width: 80px">分组名称</label>
        <a-input placeholder="请输入分组名称"
                 allow-clear
                 v-model:value="newGroupName"
        />
      </div>
      <a-button type="primary"
                style="margin-top: 10px"
                @click="handleCreateNewGroup"
      >新建分组
      </a-button>
    </div>
  </a-modal>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import { useRoute } from "vue-router";
    import SocialApi from "../../api/modules/SocialApi.js";
    import {
        SearchOutlined,
        CrownOutlined
    } from "@ant-design/icons-vue";
    import { message } from "ant-design-vue";
    import EmptyContainer from "../../components/empty-container/EmptyContainer.vue";

    const route = useRoute();

    // 当前登录的用户ID
    const curLoginUserId = ref();

    // 获取好友列表
    const filterKey = ref("");
    const isFriendListLoading = ref(false);
    const curSelectedGroup = ref();
    const groupList = ref([]);
    const getUserFriendGroups = async () => {
        await SocialApi.getUserFriendGroups({
            userId: curLoginUserId.value,
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                const data = res.data;
                if (data != null) {
                    groupList.value = data.friendGroups;
                }
            }
        });
    };
    const friendList = ref([]);
    const getAllFriendList = async () => {
        isFriendListLoading.value = true;
        curSelectedGroup.value = {
            groupId: "DEFAULT_GROUP",
            groupName: "全部好友"
        };
        await SocialApi.getAllFriendList({
            userId: curLoginUserId.value,
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                const data = res.data;
                if (data != null) {
                    friendList.value = data;
                }
            }
        });
        isFriendListLoading.value = false;
    };

    // 根据分组获取好友列表
    const getFriendListByGroup = async () => {
        isFriendListLoading.value = true;
        await SocialApi.getFriendListByGroup({
            userId: curLoginUserId.value,
            groupId: curSelectedGroup.value.groupId,
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                const data = res.data;
                if (data != null) {
                    friendList.value = data;
                }
            }
        });
        isFriendListLoading.value = false;
    };

    // 选择分组
    const handleSelectGroup = (item) => {
        curSelectedGroup.value = item;
        getFriendListByGroup();
    };
    const handleFriendGroupChange = async (userId, groupId) => {
        await SocialApi.updateFriendGroup({
            userId: curLoginUserId.value,
            friendId: userId,
            groupId: groupId,
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                message.success("修改成功");
                friendList.value = friendList.value.filter((item) => {
                    return item.userId !== userId;
                });
            }
        });
    };
    const handleDeleteFriend = async (friendId) => {
        await SocialApi.deleteFriend({
            userId: curLoginUserId.value,
            friendId: friendId,
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                message.success("删除成功");
                friendList.value = friendList.value.filter((item) => {
                    return item.userId !== friendId;
                });
            }
        });
    };

    // 删除好友分组
    const handleDeleteFriendGroup = () => {
        if (curSelectedGroup.value.groupId === "DEFAULT_GROUP") {
            message.error("默认分组无法删除");
        }
        else {
            if (friendList.value.length === 0) {
                SocialApi.deleteFriendGroup({
                    userId: curLoginUserId.value,
                    groupId: curSelectedGroup.value.groupId
                }).then((response) => {
                    const res = response.data;
                    if (res.code === 0) {
                        message.success("删除成功");
                        groupList.value = groupList.value.filter((item) => {
                            return item.groupId !== curSelectedGroup.value.groupId;
                        });
                        curSelectedGroup.value = {
                            groupId: "DEFAULT_GROUP",
                            groupName: "全部好友"
                        };
                        getAllFriendList();
                    }
                });
            }
            else {
                message.error("好友列表不为空，无法删除");
            }
        }
    };

    // 创建新的好友分组
    const CreateNewGroupDialogOpenFlag = ref(false);
    const handleOpenCreateNewGroupDialog = () => {
        CreateNewGroupDialogOpenFlag.value = true;
    };
    const handleCloseCreateNewGroupDialog = () => {
        CreateNewGroupDialogOpenFlag.value = false;
        newGroupName.value = "";
    };
    const newGroupName = ref("");
    const handleCreateNewGroup = () => {
        if (newGroupName.value === "") {
            message.error("分组名称不能为空");
        }
        else {
            SocialApi.addFriendGroup({
                userId: curLoginUserId.value,
                groupName: newGroupName.value
            }).then((response) => {
                const res = response.data;
                if (res.code === 0) {
                    message.success("创建成功");
                    getUserFriendGroups();
                    CreateNewGroupDialogOpenFlag.value = false;
                    newGroupName.value = "";
                }
            });
        }
    };

    onBeforeMount(() => {
        const data = JSON.parse(route.query.data);
        curLoginUserId.value = data.userId;
        getUserFriendGroups();
        getAllFriendList();
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .contact-manager-view-container {
    display: flex;
    align-items: center;
    width: 100%;
    height: 100%;

    .select-area {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 20%;
      height: 100%;

      .group-btn {
        min-width: 90px;
        max-width: 90px;
      }

      .header-container {
        display: flex;
        justify-content: center;
        align-items: center;
        width: fit-content;
        height: 50px;
        border-bottom: global-variable.$border-line-width solid global-variable.$border-line-color;
      }

      .group-selector-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: fit-content;
        height: calc(100% - 86px);
        margin: 8px 0;
        overflow-y: auto;
        gap: 10px;
      }

      .footer-container {
        display: flex;
        justify-content: center;
        align-items: center;
        width: fit-content;
        height: 50px;
        border-top: global-variable.$border-line-width solid global-variable.$border-line-color;
      }
    }

    .friend-list-area {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: calc(80% - 15px);
      height: 100%;
      background-color: white;
      margin-right: 15px;
      border-radius: 12px 12px 0 0;
      padding: 10px 20px;

      .friend-group-info {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        height: 50px;

        .operation-bar {
          display: flex;
          justify-content: flex-end;
          align-items: center;
          gap: 10px;
        }
      }

      .friend-list {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: calc(100% - 50px);
        overflow-y: auto;

        .list-item {
          display: flex;
          align-items: center;
          width: 100%;
          height: 50px;
          min-width: 50px;
          padding-right: 10px;
        }
      }
    }
  }

  :deep(.ant-spin-container) {
    height: 100%;
    width: 100%;
  }
</style>