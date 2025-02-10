<template>
  <div class="friend-list">
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
        <div class="friend-list-item"
             :class="{'friend-list-item-active': curFriendId === item.userId}"
             v-for="(item,index) in friendList"
             :key="index"
             @click="handleSelectFriend(item)"
        >
          <div class="user-avatar">
            <img :src="item.userAvatar"
                 alt="avatar"
                 class="avatar"
            >
          </div>
          <div class="user-info">
            <div class="user-name">{{ item.userName }}</div>
            <div class="user-dept"
                 v-if="item.dept!==null"
            >
              <CrownTwoTone/>
              {{ item.dept }}
            </div>
          </div>
        </div>
      </div>
      <div v-else-if="activeType === 'GROUP'"
           class="type-group-list"
      >

      </div>
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import {
        CrownTwoTone
    } from "@ant-design/icons-vue";
    import SocialApi from "../../api/modules/SocialApi";

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
    };

    // 好友列表
    const friendList = ref([
        {
            userId: "1",
            userName: "测试1",
            userAvatar: "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png",
            dept: "部门1"
        },
        {
            userId: "2",
            userName: "测试2",
            userAvatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            dept: null
        }
    ]);

    // 组织列表
    const groupList = ref([
        {
            groupId: "1",
            groupName: "测试1",
            groupAvatar: "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png",
            dept: "部门1"
        },
        {
            groupId: "2",
            groupName: "测试2",
            groupAvatar: "https"
        }
    ]);

    onBeforeMount(() => {
        SocialApi.getFriendList({
            userId: "1"
        }).then(res => {
            friendList.value = res.data;
        });
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

    $type-select-bar-height: 50px;

    .type-select-bar {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 90%;
      height: $type-select-bar-height;
    }

    .list-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: calc(100% - #{$type-select-bar-height});

      .type-friend-list {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: 100%;
        overflow-y: auto;

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

      .type-group-list {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: 100%;
      }
    }
  }

  // 设置选中项的字体颜色
  :deep(.ant-segmented-item-selected) {
    color: global-variable.$theme-color !important;
  }
</style>