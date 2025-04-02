<template>
  <div class="chat-list">
    <div class="search-area">
      <a-input class="content-input"
               placeholder="搜索"
               v-model:value="searchInput"
               allow-clear
      ></a-input>
      <a-button class="search-btn"
                @click="handleOpenGroupCreatorDialog"
      >
        <PlusOutlined/>
      </a-button>
    </div>
    <a-spin :wrapper-class-name="'chat-list-area'"
            :spinning="isFriendListLoading"
    >
      <EmptyContainer description="暂无进行中的会话"
                      v-show="chatListFilter(chatDataStore.chatList.commonList).length===0&&chatListFilter(chatDataStore.chatList.vipList).length===0"
      />
      <div class="chat-list-item-container"
           v-show="chatListFilter(chatDataStore.chatList.vipList).length!==0"
      >
        <a-dropdown :trigger="['contextmenu']"
                    v-for="(item,index) in chatListFilter(chatDataStore.chatList.vipList) "
                    :key="index"
        >
          <div class="chat-list-item chat-list-item-top"
               :class="{'chat-list-item-active': chatDataStore.curChatId === item.userId}"
               @click="handleSelectChat(item)"
          >
            <div class="item-avatar"
                 v-if="!item.isGroup"
            >
              <img class="avatar"
                   v-if="item.userAvatar!==null"
                   :src="item.userAvatar"
                   alt="avatar"
              />
              <a-avatar class="avatar"
                        v-else
              >{{ item.userName.substring(0, 2) }}
              </a-avatar>
            </div>
            <div class="item-avatar"
                 v-else
            >
              <div class="group-avatar">
                <ChatGroupIcon :size="32"
                               :color="'#FFFFFF'"
                />
              </div>
            </div>
            <div class="item-detail">
              <div class="detail-header">
                <div class="username">{{ item.userName }}</div>
                <div class="time">{{ formatMessageTime(item.lastMessageTime, "chat-list") }}</div>
              </div>
              <div class="detail-content">
                <div class="detail-message single-line-ellipsis"
                     :class="{ 'detail-message-unread': item.unreadCount!==0 }"
                >{{ item.lastMessage }}
                </div>
                <div class="unread-count"
                     v-if="item.unreadCount!==0"
                >
                  <div class="unread-count-badge"
                       style="font-size: 7px"
                       v-if="item.unreadCount>99"
                  >
                    99+
                  </div>
                  <div class="unread-count-badge"
                       v-else
                  >
                    {{ item.unreadCount }}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <template #overlay>
            <div class="context-menu-container">
              <div class="context-menu-item"
                   @click="setUserMessageToCommon(item.userId)"
              >
                <CloseCircleOutlined/>
                取消置顶
              </div>
              <div class="context-menu-item"
                   v-if="item.unreadCount===0"
                   @click="setMessageReadState(item.userId,false)"
              >
                <EyeInvisibleOutlined/>
                标记未读
              </div>
              <div class="context-menu-item"
                   v-if="item.unreadCount!==0"
                   @click="setMessageReadState(item.userId,true)"
              >
                <EyeOutlined/>
                标记已读
              </div>
              <div class="context-menu-item"
                   @click="deleteFromChatList(item.userId)"
              >
                <DeleteOutlined/>
                从聊天列表中移除
              </div>
            </div>
          </template>
        </a-dropdown>
      </div>
      <div class="chat-list-item-container"
           v-show="chatListFilter(chatDataStore.chatList.commonList).length!==0"
      >
        <a-dropdown :trigger="['contextmenu']"
                    v-for="(item,index) in chatListFilter(chatDataStore.chatList.commonList) "
                    :key="index"
        >
          <div class="chat-list-item"
               :class="{'chat-list-item-active': chatDataStore.curChatId === item.userId}"
               @click="handleSelectChat(item)"
          >
            <div class="item-avatar"
                 v-if="!item.isGroup"
            >
              <img class="avatar"
                   v-if="item.userAvatar!==null"
                   :src="item.userAvatar"
                   alt="avatar"
              />
              <a-avatar class="avatar"
                        v-else
              >{{ item.userName.substring(0, 2) }}
              </a-avatar>
            </div>
            <div class="item-avatar"
                 v-else
            >
              <div class="group-avatar">
                <ChatGroupIcon :size="32"
                               :color="'#FFFFFF'"
                />
              </div>
            </div>
            <div class="item-detail">
              <div class="detail-header">
                <div class="username">{{ item.userName }}</div>
                <div class="time">{{ formatMessageTime(item.lastMessageTime, "chat-list") }}</div>
              </div>
              <div class="detail-content">
                <div class="detail-message single-line-ellipsis"
                     :class="{ 'detail-message-unread': item.unreadCount!==0 }"
                >{{ item.lastMessage }}
                </div>
                <div class="unread-count"
                     v-if="item.unreadCount!==0"
                >
                  <div class="unread-count-badge"
                       style="font-size: 7px"
                       v-if="item.unreadCount>99"
                  >
                    99+
                  </div>
                  <div class="unread-count-badge"
                       v-else
                  >
                    {{ item.unreadCount }}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <template #overlay>
            <div class="context-menu-container">
              <div class="context-menu-item"
                   @click="setUserMessageToTop(item.userId)"
              >
                <VerticalAlignTopOutlined/>
                置顶
              </div>
              <div class="context-menu-item"
                   v-if="item.unreadCount===0"
                   @click="setMessageReadState(item.userId,false)"
              >
                <EyeInvisibleOutlined/>
                标记未读
              </div>
              <div class="context-menu-item"
                   v-if="item.unreadCount!==0"
                   @click="setMessageReadState(item.userId,true)"
              >
                <EyeOutlined/>
                标记已读
              </div>
              <div class="context-menu-item"
                   @click="deleteFromChatList(item.userId)"
              >
                <DeleteOutlined/>
                从聊天列表中移除
              </div>
            </div>
          </template>
        </a-dropdown>
      </div>
    </a-spin>
  </div>

  <!--创建群聊对话框-->
  <GroupCreatorDialog ref="groupCreatorDialogRef"/>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import {
        getChatList
    } from "../../database/chat-list.js";
    import { getCurUserData } from "../../database/cur-user.js";
    import {
        VerticalAlignTopOutlined,
        EyeOutlined,
        EyeInvisibleOutlined,
        DeleteOutlined,
        PlusOutlined,
        CloseCircleOutlined
    } from "@ant-design/icons-vue";
    import { formatMessageTime } from "../../utils/time-utils.js";
    import EmptyContainer from "../empty-container/EmptyContainer.vue";
    import ChatApi from "../../api/modules/ChatApi.js";
    import ChatGroupIcon from "../icon/ChatGroupIcon.vue";
    import GroupCreatorDialog from "../dialog/module-chat/group-creator/GroupCreatorDialog.vue";
    import { useChatDataStore } from "../../store/ChatDataStore.js";
    import { ChatListObject } from "../../type/type.js";
    import { useCurLoginUserStore } from "../../store/CurLoginUserStore.js";

    const props = defineProps({
        friendInfo: {
            type: ChatListObject,
            default: null
        }
    });

    // 聊天数据
    const chatDataStore = useChatDataStore();

    // 当前登录的用户信息
    const curLoginUserStore = useCurLoginUserStore()

    // 创建群聊对话框控制
    const groupCreatorDialogRef = ref();
    const handleOpenGroupCreatorDialog = () => {
        groupCreatorDialogRef.value.dialogOpen(curLoginUserStore.curLoginUser.orgId, curLoginUserStore.curLoginUser.userId);
    };

    // 聊天列表加载标识符
    const isFriendListLoading = ref(false);

    // 搜索内容
    const searchInput = ref("");
    const chatListFilter = (chatList) => {
        if (searchInput.value === "") {
            return chatList;
        }
        else {
            return chatList.filter((item) => {
                return item.userName.includes(searchInput.value);
            });
        }
    };

    /**
     * 将消息设为置顶
     * @param {string} userId 用户ID
     */
    const setUserMessageToTop = (userId) => {
        for (let i = 0; i < chatDataStore.chatList.commonList.length; i++) {
            if (chatDataStore.chatList.commonList[i].userId === userId) {
                chatDataStore.chatList.vipList.unshift(chatDataStore.chatList.commonList[i]);
                chatDataStore.chatList.commonList.splice(i, 1);
            }
        }
        chatDataStore.handleSaveChatList();
    };

    /**
     * 将消息取消置顶
     * @param {string} userId 用户ID
     */
    const setUserMessageToCommon = (userId) => {
        for (let i = 0; i < chatDataStore.chatList.vipList.length; i++) {
            if (chatDataStore.chatList.vipList[i].userId === userId) {
                chatDataStore.chatList.commonList.unshift(chatDataStore.chatList.vipList[i]);
                chatDataStore.chatList.vipList.splice(i, 1);
            }
        }
        chatDataStore.handleSaveChatList();
    };

    /**
     * 设置消息已读状态
     * @param {string} userId 用户ID
     * @param {boolean} state 已读状态
     */
    const setMessageReadState = (userId, state) => {
        for (let i = 0; i < chatDataStore.chatList.vipList.length; i++) {
            if (chatDataStore.chatList.vipList[i].userId === userId) {
                const unreadCount = chatDataStore.chatList.vipList[i].unreadCount === 0 ? 1 : chatDataStore.chatList.vipList[i].unreadCount;
                chatDataStore.chatList.vipList[i].unreadCount = state ? 0 : unreadCount;
                chatDataStore.handleSaveChatList();
                return;
            }
        }
        for (let i = 0; i < chatDataStore.chatList.commonList.length; i++) {
            if (chatDataStore.chatList.commonList[i].userId === userId) {
                const unreadCount = chatDataStore.chatList.commonList[i].unreadCount === 0 ? 1 : chatDataStore.chatList.commonList[i].unreadCount;
                chatDataStore.chatList.commonList[i].unreadCount = state ? 0 : unreadCount;
                chatDataStore.handleSaveChatList();
                return;
            }
        }
    };

    /**
     * 从聊天列表中删除
     * @param {string} userId 用户ID
     */
    const deleteFromChatList = (userId) => {
        for (let i = 0; i < chatDataStore.chatList.vipList.length; i++) {
            if (chatDataStore.chatList.vipList[i].userId === userId) {
                chatDataStore.chatList.vipList.splice(i, 1);
                chatDataStore.handleSaveChatList();
                emits("setSelectedChat", null);
                return;
            }
        }
        for (let i = 0; i < chatDataStore.chatList.commonList.length; i++) {
            if (chatDataStore.chatList.commonList[i].userId === userId) {
                chatDataStore.chatList.commonList.splice(i, 1);
                chatDataStore.handleSaveChatList();
                emits("setSelectedChat", null);
                return;
            }
        }
    };

    const emits = defineEmits(["setSelectedChat"]);

    // 选择聊天
    const handleSelectChat = async (item) => {
        if (chatDataStore.curChatId !== item.userId) {
            chatDataStore.curChatId = item.userId;
            const chatInfo = {
                userId: item.userId,
                userName: item.userName,
                userAvatar: item.userAvatar,
                creator: item.creator,
                isGroup: item.isGroup
            };
            emits("setSelectedChat", chatInfo);

            // 广播聊天对象变化事件
            window.dispatchEvent(new CustomEvent("chatObjectChange", {
                detail: {
                    userId: chatDataStore.curChatId,
                    isGroup: item.isGroup
                },
                bubbles: false
            }));

            // 如果有消息未读则清空未读消息数
            if (item.unreadCount !== 0) {
                item.unreadCount = 0;

                // 保存聊天列表至本地和云端
                chatDataStore.handleSaveChatList();
            }
        }
    };

    // 从服务器获取聊天列表
    const getCloudChatList = async (userId) => {
        isFriendListLoading.value = true;

        // 从服务器获取聊天列表
        const response = await ChatApi.getChatList({
            userId: userId
        });

        const res = response.data;
        if (res.code === 0) {
            const data = res.data;
            if (data != null) {
                isFriendListLoading.value = false;
                return JSON.parse(data.chatList);
            }
        }

        isFriendListLoading.value = false;
        return null;
    };

    onBeforeMount(async () => {
        // 读取聊天列表
        if (!chatDataStore.isChatListUpdated) {
            isFriendListLoading.value = true;
            // 判断是否使用本地存储
            if (await chatDataStore.checkIsUseLocalCache()) {
                const localChatList = await getChatList(curLoginUserStore.curLoginUser.userId);
                if (localChatList !== null) {
                    chatDataStore.chatList = localChatList;
                }
                else {
                    chatDataStore.chatList = await getCloudChatList(curLoginUserStore.curLoginUser.userId);
                }
            }
            else {
                chatDataStore.chatList = await getCloudChatList(curLoginUserStore.curLoginUser.userId);
            }

            chatDataStore.isChatListUpdated = true;
            isFriendListLoading.value = false;
        }

        // 如果传入的好友ID不为null,则将聊天对象设置为该好友
        if (props.friendInfo !== null) {
            let flag = false;
            // 遍历置顶列表
            for (let i = 0; i < chatDataStore.chatList.vipList.length; i++) {
                if (chatDataStore.chatList.vipList[i].userId === props.friendInfo.userId) {
                    flag = true;
                    // 更新聊天对象的名称和头像
                    chatDataStore.chatList.vipList[i].userName = props.friendInfo.userName;
                    chatDataStore.chatList.vipList[i].userAvatar = props.friendInfo.userAvatar;
                    // 清空未读消息数
                    chatDataStore.chatList.vipList[i].unreadCount = 0;
                    break;
                }
            }

            if (!flag) {
                // 如果在置顶列表没有找到，遍历普通列表
                for (let i = 0; i < chatDataStore.chatList.commonList.length; i++) {
                    if (chatDataStore.chatList.commonList[i].userId === props.friendInfo.userId) {
                        flag = true;
                        // 更新聊天对象的名称和头像
                        chatDataStore.chatList.commonList[i].userName = props.friendInfo.userName;
                        chatDataStore.chatList.commonList[i].userAvatar = props.friendInfo.userAvatar;
                        // 清空未读消息数
                        chatDataStore.chatList.commonList[i].unreadCount = 0;
                        break;
                    }
                }
            }

            if (!flag) {
                const chatInfo = {
                    userId: props.friendInfo.userId,
                    userName: props.friendInfo.userName,
                    userAvatar: props.friendInfo.userAvatar,
                    lastMessage: "",
                    lastMessageTime: "",
                    unreadCount: 0,
                    isGroup: props.friendInfo.isGroup
                };
                if (chatInfo.isGroup) {
                    chatInfo.creator = props.friendInfo.creator;
                }
                // 在聊天列表的头部添加该好友
                chatDataStore.chatList.commonList.unshift(chatInfo);
            }

            // 保存对话列表
            chatDataStore.handleSaveChatList();

            chatDataStore.curChatId = props.friendInfo.userId;
            const chatInfo = {
                userId: props.friendInfo.userId,
                userName: props.friendInfo.userName,
                userAvatar: props.friendInfo.userAvatar,
                isGroup: props.friendInfo.isGroup
            };
            if (chatInfo.isGroup) {
                chatInfo.creator = props.friendInfo.creator;
            }
            emits("setSelectedChat", chatInfo);
            // 广播聊天对象变化事件
            window.dispatchEvent(new CustomEvent("chatObjectChange", {
                detail: {
                    userId: chatDataStore.curChatId,
                    isGroup: props.friendInfo.isGroup
                },
                bubbles: false
            }));
        }
    });

    defineExpose({
        deleteFromChatList
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .chat-list {
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

        :deep(input) {
          background-color: transparent !important;
        }
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

    .chat-list-area {
      display: flex;
      flex-direction: column;
      width: 100%;
      height: calc(100% - $search-area-height);
      overflow-y: auto;

      .chat-list-item-container {
        width: 100%;
        height: fit-content;

        .chat-list-item-top {
          position: relative;
          background-color: global-variable.$hover-background-color;

          &:after {
            content: '';
            position: absolute;
            top: 0;
            right: 0;
            width: 0;
            height: 0;
            border-top: 8px solid global-variable.$theme-color;
            border-left: 8px solid transparent;
          }
        }

        .chat-list-item-active {
          background-color: rgba(global-variable.$theme-color, 0.1);

          &:hover {
            background-color: rgba(global-variable.$theme-color, 0.1) !important;
          }
        }

        .chat-list-item {
          display: flex;
          align-items: center;
          width: 100%;
          min-height: 75px;
          padding: 0 15px;

          $item-avatar-size: 40px;

          .item-avatar {
            display: flex;
            justify-content: center;
            align-items: center;
            width: $item-avatar-size;
            height: $item-avatar-size;
            margin-right: 15px;

            .avatar {
              display: flex;
              justify-content: center;
              align-items: center;
              width: 100%;
              height: 100%;
              border-radius: 50%;
            }

            .group-avatar {
              display: flex;
              justify-content: center;
              align-items: center;
              width: 100%;
              height: 100%;
              border-radius: 50%;
              background-color: global-variable.$theme-color;
            }
          }

          .item-detail {
            display: flex;
            flex-direction: column;
            width: calc(100% - #{$item-avatar-size} - 15px);

            .detail-header {
              display: flex;
              justify-content: space-between;
              align-items: center;
              width: 100%;

              .username {
                font-size: 14px;
              }

              .time {
                font-size: 10px;
                color: gray;
              }
            }

            .detail-content {
              display: flex;
              justify-content: space-between;
              align-items: center;
              width: 100%;

              .detail-message {
                font-size: 12px;
                color: gray;
                width: 100%;
              }

              .detail-message-unread {
                width: 90%;
              }

              .unread-count {
                width: 10%;

                .unread-count-badge {
                  display: flex;
                  justify-content: center;
                  align-items: center;
                  width: 16px;
                  height: 16px;
                  border-radius: 50%;
                  background-color: red;
                  font-size: 9px;
                  color: white;
                }
              }
            }
          }

          &:hover {
            background-color: global-variable.$hover-background-color;
            cursor: pointer;
          }
        }
      }
    }
  }

  :deep(.ant-spin-container) {
    height: 100%;
    width: 100%;
  }
</style>