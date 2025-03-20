<template>
  <a-modal v-model:open="dialogOpenFlag"
           centered
           :mask="false"
           width="600px"
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
      <div class="user-list-container">
        <div class="search-area">
          <a-input style="width: 100%"
                   placeholder="请输入"
          />
        </div>
        <div class="select-user-area">
          <div class="empty-list"
               v-if="orgTreeData.deptList.length === 0 && orgTreeData.userList.length === 0"
          >
            <EmptyContainer :description="'暂无可选用户'"
                            v-show="!isLoading"
            />
            <a-spin :tip="'组织架构加载中'"
                    v-show="isLoading"
            ></a-spin>
          </div>
          <div class="org-tree"
               v-else
          >
            <OrgTreeNode v-model:selected="selectedUserList"
                         v-model:org-tree="orgTreeData"
                         :org-id="curOrgId"
                         ref="orgTreeNodeRef"
            />
          </div>
        </div>
      </div>
      <div class="selected-list-container">
        <div class="user-selected-area">
          <EmptyContainer :description="'暂未选择用户'"
                          v-show="selectedUserList.length===0"
          />
          <div class="user-item"
               v-for="(item,index) in selectedUserList"
               :key="index"
               v-show="selectedUserList.length!==0"
          >
            <div class="user-info">
              <div class="user-avatar">
                <img v-if="item.userAvatar!==null"
                     :src="item.userAvatar"
                     class="avatar"
                     alt="avatar"
                />
                <a-avatar v-else
                          class="avatar"
                          style="display:flex;justify-content:center;align-items:center;font-size: 24px"
                >{{ item.userName.substring(0, 2) }}
                </a-avatar>
              </div>
              <label style="cursor: default">{{ item.userName }}</label>
            </div>
            <CloseOutlined class="delete-icon"
                           @click="handleDeleteUser(item.userId)"
            />
          </div>
        </div>
        <div class="operation-bar">
          <a-button type="primary"
                    @click="handleConfirm"
          >确定
          </a-button>
          <a-button @click="dialogClose">取消</a-button>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
    import { ref } from "vue";
    import { themeColor } from "../../../../config/config.js";
    import Logo from "../../../logo/Logo.vue";
    import { message } from "ant-design-vue";
    import {
        CloseOutlined,
        UserOutlined
    } from "@ant-design/icons-vue";
    import EmptyContainer from "../../../empty-container/EmptyContainer.vue";
    import OrgTreeNode from "./OrgTreeNode.vue";
    import SocialApi from "../../../../api/modules/SocialApi.js";

    const emit = defineEmits(["handleUserSelect"]);

    // 组织树节点
    const orgTreeNodeRef = ref(null);

    // 对话框配置
    const isLoading = ref(false);
    const dialogTitle = ref("用户选择");
    const curOrgId = ref(null);
    const curMessageList = ref([]);
    const orgTreeData = ref({
        deptList: [],
        userList: []
    });
    const selectedUserList = ref([]);

    // 获取组织树层级
    const getOrgTreeData = () => {
        isLoading.value = true;
        SocialApi.getOrganizationTree({
            orgId: curOrgId.value,
            parentId: null
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                orgTreeData.value = res.data;
                for (let i = 0; i < orgTreeData.value.deptList.length; i++) {
                    orgTreeData.value.deptList[i].isLoading = false;
                    orgTreeData.value.deptList[i].isLoaded = false;
                }
                for (let i = 0; i < orgTreeData.value.userList.length; i++) {
                    orgTreeData.value.userList[i].isSelected = false;
                }
            }
            isLoading.value = false;
        });
    };

    // 处理选择确认
    const handleConfirm = () => {
        if (selectedUserList.value.length !== 0) {
            let idList = [];
            for (let i = 0; i < selectedUserList.value.length; i++) {
                idList.push(selectedUserList.value[i].userId);
            }
            emit("handleUserSelect", idList, curMessageList.value);
            dialogClose();
        }
        else {
            message.error("未选择用户");
        }
    };
    // 删除已选的用户
    const handleDeleteUser = (userId) => {
        selectedUserList.value = selectedUserList.value.filter(item => item.userId !== userId);
    };

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = (orgId, messageList) => {
        curOrgId.value = orgId;
        curMessageList.value = messageList;
        dialogOpenFlag.value = true;
        getOrgTreeData();
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
        curOrgId.value = null;
        curMessageList.value = [];
        selectedUserList.value = [];
        orgTreeData.value = {
            deptList: [],
            userList: []
        };
        if (orgTreeNodeRef.value !== null) {
            for (let i = 0; i < orgTreeNodeRef.value.length; i++) {
                orgTreeNodeRef.value[i].resetOrgTree();
            }
        }
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
    height: 400px;

    .user-list-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 50%;
      height: 100%;
      padding-right: 20px;
      border-right: global-variable.$border-line-width solid global-variable.$border-line-color;

      .search-area {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 10%;
      }

      .select-user-area {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: 90%;
        overflow-y: auto;

        .empty-list {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 100%;
          height: 100%;
        }

        .org-tree {
          display: flex;
          flex-direction: column;
          align-items: center;
          width: 100%;
          height: 100%;
          overflow-y: auto;
        }
      }
    }

    .selected-list-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 50%;
      height: 100%;
      padding-left: 20px;

      .user-selected-area {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: 90%;
        overflow-y: auto;

        .user-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 100%;
          min-height: 40px;
          padding: 5px 10px;

          .user-info {
            display: flex;
            justify-content: flex-start;
            align-items: center;
            color: black;

            .user-avatar {
              width: 30px;
              height: 30px;
              margin-right: 15px;

              .avatar {
                width: 100%;
                height: 100%;
                border-radius: 50%;
              }
            }
          }

          .delete-icon {
            color: white;
          }

          &:hover {
            background-color: global-variable.$hover-background-color;

            .delete-icon {
              color: gray;
              cursor: pointer;

              &:hover {
                color: black;
              }
            }
          }
        }
      }

      .operation-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 20px;
        width: 100%;
        height: 10%;
      }
    }
  }
</style>