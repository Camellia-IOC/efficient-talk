<template>
  <div class="friend-list-container">
    <div class="search-area">
      <a-input class="content-input"
               placeholder="搜索"
      ></a-input>
      <a-button class="search-btn"
                @click="handleAddNewFriendDialogOpen"
      >
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
      <a-spin v-if="activeType === 'FRIEND'"
              :wrapper-class-name="'type-friend-list'"
              :spinning="isFriendListLoading"
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
                  <img v-if="friend.userAvatar!==null"
                       :src="friend.userAvatar"
                       alt="avatar"
                       class="avatar"
                  >
                  <a-avatar v-else
                            class="avatar"
                  >{{ friend.userName.substring(0, 2) }}
                  </a-avatar>
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
      </a-spin>
      <div v-else-if="activeType === 'ORG'"
           class="type-org-list"
      >
        <div class="empty-list"
             v-if="curLoginUser.orgId === null"
        >
          <EmptyContainer :description="'你还没有加入组织噢'"/>
        </div>
        <a-spin v-else
                :wrapper-class-name="'org-list'"
                :spinning="isOrgTreeLoading"
        >
          <div class="org-info-bar"
               v-if="openedNodeStack.length!==0"
          >
            <label class="org-info single-line-ellipsis">{{ curOrgNodeInfo.deptName }}</label>
            <a-button shape="round"
                      @click="preOrgTreeLevel"
                      size="small"
                      class="back-btn"
            >
              <LeftOutlined/>
            </a-button>
          </div>
          <div class="org-list-container"
               v-if="orgTree.deptList.length===0&&orgTree.userList.length===0"
          >
            <EmptyContainer :description="'空组织'"/>
          </div>
          <div class="org-list-container"
               v-else
          >
            <div class="group-label"
                 v-if="orgTree.deptList.length!==0&&openedNodeStack.length!==0"
            >
              <ApartmentOutlined/>
              <label>下级部门</label>
            </div>
            <div class="org-dept-list-item"
                 v-for="(item,index) in orgTree.deptList"
                 :key="index"
                 @click="nextOrgTreeLevel(item)"
            >
              <div class="org-info">
                <div class="org-icon">
                  <TeamOutlined/>
                </div>
                <label class="org-name">{{ item.deptName }}</label>
              </div>
              <div class="more-icon">
                <RightOutlined/>
              </div>
            </div>
            <div class="group-label"
                 v-if="orgTree.userList.length!==0&&openedNodeStack.length!==0"
            >
              <UserOutlined/>
              <label>部门成员</label>
            </div>
            <div class="org-user-list-item"
                 v-for="(item,index) in orgTree.userList"
                 :key="index"
                 @click="handleSelectFriend(item)"
            >
              <div class="user-avatar">
                <img v-if="item.userAvatar!==null"
                     :src="item.userAvatar"
                     alt="avatar"
                     class="avatar"
                >
                <a-avatar v-else
                          class="avatar"
                >{{ item.userName.substring(0, 2) }}
                </a-avatar>
              </div>
              <div class="user-info">
                <div class="user-name">{{ item.userName }}</div>
                <div class="user-dept"
                     v-if="item.jobName!==null && item.jobId!==null"
                >
                  <CrownTwoTone/>
                  {{ item.jobName }}
                </div>
              </div>
            </div>
          </div>
        </a-spin>
      </div>
    </div>
  </div>

  <!--好友通知对话框-->
  <FriendInvitationDialog ref="friendInvitationDialog"
                          :cur-login-user-id="curLoginUser.userId"
  />

  <!--添加好友对话框-->
  <AddNewFriendDialog ref="addNewFriendDialog"
                      :cur-login-user-id="curLoginUser.userId"
  />
</template>

<script setup>
    import {
        onBeforeMount,
        onBeforeUnmount,
        ref
    } from "vue";
    import {
        CrownTwoTone,
        RightOutlined,
        PlusOutlined,
        TeamOutlined,
        LeftOutlined,
        UserOutlined,
        ApartmentOutlined
    } from "@ant-design/icons-vue";
    import SocialApi from "../../api/modules/SocialApi";
    import { getCurUserData } from "../../database/cur-user.js";
    import FriendInvitationDialog from "../dialog/friend-invitations/FriendInvitationDialog.vue";
    import EmptyContainer from "../empty-container/EmptyContainer.vue";
    import AddNewFriendDialog from "../dialog/add-friend/AddNewFriendDialog.vue";
    import { message } from "ant-design-vue";

    const emits = defineEmits(["setSelectedFriend"]);

    // 好友邀请对话框控制
    const friendInvitationDialog = ref();
    const handleInvitationDialogOpen = () => {
        friendInvitationDialog.value.dialogOpen();
    };

    // 添加好友对话框控制
    const addNewFriendDialog = ref();
    const handleAddNewFriendDialogOpen = () => {
        addNewFriendDialog.value.dialogOpen();
    };

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // 加载标识符
    const isFriendListLoading = ref(true);
    const isOrgTreeLoading = ref(true);

    // 当前展开的好友分组
    const activeFriendGroupKey = ref([]);

    // 类型集合
    const typeSets = [{
        label: "好友",
        value: "FRIEND"
    }, {
        label: "组织",
        value: "ORG"
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
    const openedNodeStack = ref([]);
    const curOrgNodeInfo = ref(null);
    const orgTree = ref([]);

    // 获取好友列表
    const getFriendList = async () => {
        await SocialApi.getFriendList({
            userId: curLoginUser.value.userId,
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

    // 更新好友列表
    const handleUpdateFriendList = async () => {
        await getFriendList();
    };

    // 获取组织树
    const getOrgTree = async (parentId) => {
        const response = await SocialApi.getOrganizationTree({
            orgId: curLoginUser.value.orgId,
            parentId: parentId
        });

        const res = response.data;
        if (res.code === 0) {
            if (res.data != null) {
                orgTree.value = res.data;
            }
        }
        else {
            message.error("获取组织数据失败");
            orgTree.value = [];
        }
        isOrgTreeLoading.value = false;
    };

    // 处理组织树层级切换
    const nextOrgTreeLevel = async (item) => {
        isOrgTreeLoading.value = true;
        curOrgNodeInfo.value = item;
        openedNodeStack.value.push(curOrgNodeInfo.value);
        await getOrgTree(curOrgNodeInfo.value.deptId);
    };
    const preOrgTreeLevel = async () => {
        isOrgTreeLoading.value = true;
        curOrgNodeInfo.value = openedNodeStack.value.pop();
        if (openedNodeStack.value.length === 0) {
            curOrgNodeInfo.value = null;
            await getOrgTree(null);
        }
        else {
            await getOrgTree(curOrgNodeInfo.value.parentId);
            curOrgNodeInfo.value = openedNodeStack.value[openedNodeStack.value.length - 1];
        }
    };

    onBeforeMount(async () => {
        // 订阅好友列表更新事件
        window.addEventListener("updateFriendList", handleUpdateFriendList);

        await updateCurLoginUser();

        // 获取好友列表
        await getFriendList();

        if (curLoginUser.value.orgId != null) {
            // 获取组织树
            await getOrgTree(null);
        }
    });

    onBeforeUnmount(()=>{
        // 移除监听器防止事件多次触发
        window.removeEventListener("updateFriendList", handleUpdateFriendList);
    })
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .friend-list-container {
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
        background-color: global-variable.$hover-background-color;
      }
    }

    $type-select-bar-height: 50px;

    .type-select-bar {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 90%;
      height: $type-select-bar-height;
      border-top: global-variable.$border-line-width solid global-variable.$border-line-color;
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
                min-height: 80px;
                padding: 5px 15px;
                cursor: pointer;

                &:hover {
                  background-color: global-variable.$hover-background-color;
                }

                .user-avatar {
                  display: flex;
                  justify-content: center;
                  align-items: center;

                  .avatar {
                    display: flex;
                    justify-content: center;
                    align-items: center;
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

      .type-org-list {
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

        .org-list {
          display: flex;
          flex-direction: column;
          align-items: center;
          width: 100%;
          height: 100%;

          $org-info-bar-height: 50px;

          .org-info-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 90%;
            height: $org-info-bar-height;
            margin: 10px 5%;
            padding: 0 10px;
            background-color: global-variable.$theme-color;
            border-radius: 10px;

            .org-info {
              display: flex;
              align-items: center;
              width: calc(100% - 30px);
              font-size: 16px;
              gap: 10px;
              color: white;
            }

            .back-btn {
              display: flex;
              justify-content: center;
              align-items: center;
              width: 24px;
              border: none;
              color: global-variable.$theme-color;
            }
          }

          .org-list-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 100%;
            height: calc(100% - $org-info-bar-height - 20px);
            overflow-y: auto;

            .group-label {
              display: flex;
              justify-content: flex-start;
              align-items: center;
              width: 90%;
              margin: 10px 5% 5px 5%;
              color: gray;
              font-size: 12px;
              border-bottom: 1px solid global-variable.$border-line-color;

              label {
                margin-left: 5px;
              }
            }

            .org-dept-list-item {
              display: flex;
              justify-content: space-between;
              align-items: center;
              width: 100%;
              height: 40px;
              min-height: 40px;
              padding: 5px 18px;
              cursor: pointer;

              &:hover {
                background-color: global-variable.$hover-background-color;
              }

              .org-info {
                display: flex;
                justify-content: flex-start;
                align-items: center;
                height: 100%;
                cursor: pointer;

                .org-icon {
                  color: global-variable.$theme-color;
                  font-size: 20px;
                  margin-right: 10px;
                }

                .org-name {
                  font-size: 14px;
                  cursor: pointer;
                }
              }

              .more-icon {
                display: flex;
                justify-content: flex-end;
                align-items: center;
                height: 100%;
                font-size: 10px;
                color: gray;
              }
            }

            .org-user-list-item {
              display: flex;
              align-items: center;
              width: 100%;
              height: 80px;
              min-height: 80px;
              padding: 5px 15px;
              cursor: pointer;

              &:hover {
                background-color: global-variable.$hover-background-color;
              }

              .user-avatar {
                display: flex;
                justify-content: center;
                align-items: center;

                .avatar {
                  display: flex;
                  justify-content: center;
                  align-items: center;
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
  }

  // 设置选中项的字体颜色
  :deep(.ant-segmented-item-selected) {
    color: global-variable.$theme-color !important;
  }

  // 去除折叠面板内的边距
  :deep(.ant-collapse-content-box) {
    padding: 0 !important;
  }

  :deep(.ant-spin-container) {
    height: 100%;
    width: 100%;
  }
</style>