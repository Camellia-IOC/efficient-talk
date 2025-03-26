<template>
  <div class="org-tree-node-container">
    <a-collapse v-model:activeKey="activeKey"
                ghost
                style="width: 100%;padding: 0"
                @change="handleDeptOpen"
    >
      <a-collapse-panel v-for="(item,index) in orgTree.deptList"
                        :key="index"
      >
        <template #header>
          <div>
            {{ item.deptName }}
          </div>
        </template>
        <div v-if="item.isLoading||item.children === null||(item.children.deptList.length===0&&item.children.userList.length===0)"
             style="width: 100%;height: 50px;display: flex;justify-content: center;align-items: center"
        >
          <div v-if="item.isLoading">
            <a-spin tip="加载中"></a-spin>
          </div>
          <div v-else-if="item.children===null||(item.children.deptList.length===0&&item.children.userList.length===0)">
            暂无数据
          </div>
        </div>
        <OrgTreeNode v-model:selected="selectedUserList"
                     v-model:org-tree="item.children"
                     :org-id="orgId"
                     :cur-member-id-list="curMemberIdList"
                     ref="orgTreeNodeRef"
                     v-else
        />
      </a-collapse-panel>
    </a-collapse>
    <div class="user-list">
      <div v-for="(item,index) in orgTree.userList"
           :key="index"
           class="user-list-item"
           :class="{'active-user-list-item': item.isSelected}"
           @click="handleUserSelect(index)"
      >
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
        <div class="user-info">
          <div class="user-name">{{ item.userName }}</div>
          <div class="job-name">{{ item.jobName }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
    import { ref } from "vue";
    import SocialApi from "../../../../api/modules/SocialApi.js";

    // 组织树节点
    const orgTreeNodeRef = ref(null);

    const props = defineProps({
        orgId: {
            type: String,
            default: null
        },
        curMemberIdList: {
            type: Array,
            default: () => []
        }
    });

    const orgTree = defineModel("orgTree");
    const selectedUserList = defineModel("selected");

    // 处理打开下级组织
    const handleDeptOpen = (key) => {
        const index = key[key.length - 1];
        if (index === undefined) {
            return;
        }
        if (!orgTree.value.deptList[index].isLoaded) {
            getOrgTreeData(index);
            orgTree.value.deptList[index].isLoaded = true;
        }
    };

    // 处理用户选择
    const handleUserSelect = (index) => {
        if (orgTree.value.userList[index].isSelected) {
            if (!props.curMemberIdList.includes(orgTree.value.userList[index].userId)) {
                orgTree.value.userList[index].isSelected = false;
                selectedUserList.value.splice(
                    selectedUserList.value.findIndex((item) => item.userId === orgTree.value.userList[index].userId), 1);
            }
        }
        else {
            orgTree.value.userList[index].isSelected = true;
            selectedUserList.value.push(orgTree.value.userList[index]);
        }
    };

    // 获取组织树层级
    const getOrgTreeData = (index) => {
        orgTree.value.deptList[index].isLoading = true;
        SocialApi.getOrganizationTree({
            orgId: props.orgId,
            parentId: orgTree.value.deptList[index].deptId
        }).then((response) => {
            const res = response.data;
            if (res.code === 0) {
                orgTree.value.deptList[index].children = res.data;
                for (let i = 0; i < orgTree.value.deptList[index].children.deptList.length; i++) {
                    orgTree.value.deptList[index].children.deptList[i].isLoading = false;
                    orgTree.value.deptList[index].children.deptList[i].isLoaded = false;
                }
                for (let i = 0; i < orgTree.value.deptList[index].children.userList.length; i++) {
                    if (props.curMemberIdList.includes(orgTree.value.deptList[index].children.userList[i].userId)) {
                        console.error(1)
                        orgTree.value.deptList[index].children.userList[i].isSelected = true;
                    }
                    else {
                        for (let j = 0; j < selectedUserList.value.length; j++) {
                            orgTree.value.deptList[index].children.userList[i].isSelected = orgTree.value.deptList[index].children.userList[i].userId === selectedUserList.value[j].userId;
                        }
                    }
                }
            }
            orgTree.value.deptList[index].isLoading = false;
        }).catch((error) => {
            console.log(error);
            orgTree.value.deptList[index].isLoading = false;
        });
    };

    const activeKey = ref([]);

    // 重置组织树
    const resetOrgTree = () => {
        activeKey.value = [];
        if (orgTreeNodeRef.value !== null) {
            for (let i = 0; i < orgTreeNodeRef.value.length; i++) {
                orgTreeNodeRef.value[i].resetOrgTree();
            }
        }
    };

    defineExpose({
        resetOrgTree
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .org-tree-node-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;

    .user-list {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: fit-content;

      .active-user-list-item {
        background-color: rgba(global-variable.$theme-color, 0.1) !important;
      }

      .user-list-item {
        display: flex;
        align-items: center;
        width: 100%;
        min-height: 50px;
        cursor: pointer;
        padding-left: 10px;
        background-color: #FAFCFF;

        &:hover {
          background-color: global-variable.$hover-background-color;
        }

        .user-avatar {
          width: 40px;
          height: 40px;
          margin-right: 15px;

          .avatar {
            width: 100%;
            height: 100%;
            border-radius: 50%;
          }
        }

        .user-info {
          display: flex;
          flex-direction: column;
          justify-content: flex-start;
          align-items: center;
          width: calc(100% - 55px);

          .user-name {
            width: 100%;
            font-size: 14px;
            color: global-variable.$theme-color;
          }

          .job-name {
            width: 100%;
            font-size: 12px;
          }
        }
      }
    }
  }

  :deep(.ant-collapse-header) {
    border-radius: 0 !important;
    padding: 5px 5px !important;
    background-color: white;

    &:hover {
      background-color: global-variable.$hover-background-color;
    }
  }

  :deep(.ant-collapse-content-box) {
    padding: 0 0 0 8px !important;
    border-left: 1px dashed global-variable.$border-line-color;
  }
</style>