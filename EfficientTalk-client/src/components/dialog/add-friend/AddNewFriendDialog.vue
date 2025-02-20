<template>
  <a-modal v-model:open="dialogOpenFlag"
           title="添加好友"
           centered
           :mask="false"
           width="700px"
           :footer="null"
           @cancel="dialogClose"
  >
    <div class="add-new-friend-container">
      <div class="search-area">
        <a-input v-model:value="searchKey"
                 placeholder="请输入用户名或用户ID"
        ></a-input>
        <div class="operation-bar">
          <a-button type="primary"
                    @click="searchNewFriend"
          >搜索
          </a-button>
          <a-button @click="resetSearch">重置</a-button>
        </div>
      </div>
      <EmptyContainer v-if="searchResultList === null"
                      :description="'寻找新的朋友吧'"
      />
      <EmptyContainer v-else-if="searchResultList.length === 0"
                      :description="'没有符合要求的用户'"
      />
      <div v-else
           class="search-result-list"
      >
        <NewFriendItem v-for="(item,index) in searchResultList"
                       :key="index"
                       :userInfo="item"
                       @open-invite-dialog="handleEditInvitationDialogOpen"
        />
      </div>
    </div>
  </a-modal>

  <!--编辑添加信息对话框-->
  <EditInvitationDialog ref="editInvitationDialog"/>
</template>

<script setup>
    import { ref } from "vue";
    import EmptyContainer from "../../empty-container/EmptyContainer.vue";
    import SocialApi from "../../../api/modules/SocialApi.js";
    import { message } from "ant-design-vue";
    import NewFriendItem from "./components/NewFriendItem.vue";
    import EditInvitationDialog from "./EditInvitationDialog.vue";

    const props = defineProps({
        curLoginUserId: {
            type: String,
            default: ""
        }
    });

    // 编辑添加信息对话框
    const editInvitationDialog = ref();
    const handleEditInvitationDialogOpen = (friendId) => {
        editInvitationDialog.value.dialogOpen(props.curLoginUserId, friendId);
    };

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = () => {
        dialogOpenFlag.value = true;
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
        resetSearch();
    };

    // 搜索关键字
    const searchKey = ref("");
    const resetSearch = () => {
        searchKey.value = "";
        searchResultList.value = null;
    };
    const searchNewFriend = async () => {
        if (searchKey.value !== "") {
            // TODO 添加分页
            const response = await SocialApi.searchNewFriend({
                searchKey: searchKey.value,
                userId: props.curLoginUserId,
                pageSize: 100,
                pageIndex: 1
            });

            const res = response.data;
            if (res.code === 0) {
                if (res.data !== null) {
                    searchResultList.value = res.data.resultList;
                }
            }
            else {
                message.error("搜索失败");
            }
        }
        else {
            message.error("搜索内容为空");
        }
    };

    // 搜索结果列表
    const searchResultList = ref(null);

    defineExpose({
        dialogOpen,
        dialogClose
    });
</script>

<style scoped
       lang="scss"
>
  .add-new-friend-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 500px;

    .search-area {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 10%;
      gap: 20px;

      .operation-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 10px;
      }
    }

    .search-result-list {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: 90%;
      overflow-y: auto;
    }
  }
</style>