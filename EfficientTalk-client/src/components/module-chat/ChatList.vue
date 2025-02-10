<template>
  <div class="chat-list">
    <div v-for="(item,index) in chatList"
         :key="index"
         class="chat-list-item"
         :class="{'chat-list-item-active': curChatId === item.userId}"
         @click="handleSelectChat(index)"
         @contextmenu="handleContextMenu"
    >
      <div class="item-avatar">
        <img class="avatar"
             :src="item.userAvatar"
             alt=""
        />
      </div>
      <div class="item-detail">
        <div class="detail-header">
          <div class="username">{{ item.userName }}</div>
          <div class="time">{{ item.lastMessageTime }}</div>
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
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import {
        saveChatList,
        getChatList
    } from "../../database/chat-list.js";
    import { getCurUserData } from "../../database/cur-user.js";

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // 当前聊天对象的ID
    const curChatId = ref("");

    // 保存聊天列表
    const handleSaveChatList = (chatList) => {
        const chatListJson = JSON.stringify(chatList);
        saveChatList(curLoginUser.value.userId, chatListJson);
    };

    // 接收消息
    const handleMessageReceive = (event) => {
        const message = event.detail;
        const sender = message.sender;

        // 遍历消息列表，修改相应的元素内容
        for (let i = 0; i < chatList.value.length; i++) {
            if (chatList.value[i].userId === sender) {
                switch (message.type) {
                    case "text":
                        chatList.value[i].lastMessage = message.content;
                        break;
                    case "image":
                        chatList.value[i].lastMessage = "[图片]";
                        break;
                    case "file":
                        chatList.value[i].lastMessage = "[文件]";
                        break;
                    default:
                        chatList.value[i].lastMessage = message.content;
                }
                chatList.value[i].lastMessageTime = message.time;

                // 如果发送消息的用户不是当前聊天对象，则增加未读消息数
                if (curChatId.value !== sender) {
                    chatList.value[i].unreadCount++;
                }
                break;
            }
        }

        // 保存聊天列表
        handleSaveChatList(chatList.value);
    };

    // 发送消息
    const handleMessageSend = (event) => {
        const message = event.detail;
        const receiver = message.receiver;

        // 遍历消息列表，修改相应的元素内容
        for (let i = 0; i < chatList.value.length; i++) {
            if (chatList.value[i].userId === receiver) {
                switch (message.type) {
                    case "text":
                        chatList.value[i].lastMessage = message.content;
                        break;
                    case "image":
                        chatList.value[i].lastMessage = "[图片]";
                        break;
                    case "file":
                        chatList.value[i].lastMessage = "[文件]";
                        break;
                    default:
                        chatList.value[i].lastMessage = message.content;
                }
                chatList.value[i].lastMessageTime = message.time;
                break;
            }
        }

        // 保存聊天列表
        handleSaveChatList(chatList.value);
    };

    const emits = defineEmits(["setSelectedChat"]);

    // 右键菜单
    const handleContextMenu = () => {
        alert("context menu");
    };

    // 选择聊天
    const handleSelectChat = (index) => {
        curChatId.value = chatList.value[index].userId;
        emits("setSelectedChat", chatList.value[index]);

        // 广播聊天对象变化事件
        window.dispatchEvent(new CustomEvent("chatObjectChange", {
            detail: curChatId.value
        }));

        // 如果有消息未读则清空未读消息数
        if (chatList.value[index].unreadCount !== 0) {
            chatList.value[index].unreadCount = 0;
            handleSaveChatList(chatList.value);
        }
    };

    // 聊天列表
    const chatList = ref([
        {
            userId: "1",
            userName: "测试1",
            userAvatar: "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png",
            lastMessage: "你好",
            lastMessageTime: "2023-05-01 12:00:00",
            unreadCount: 0
        },
        {
            userId: "2",
            userName: "测试2",
            userAvatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "你好",
            lastMessageTime: "2023-05-01 12:00:00",
            unreadCount: 0
        }
    ]);

    onBeforeMount(async () => {
        // 订阅消息接收
        window.addEventListener("messageReceive", handleMessageReceive);
        // 订阅消息发送
        window.addEventListener("messageSend", handleMessageSend);

        // 初始化当前登录的用户信息
        await updateCurLoginUser();

        // 读取聊天列表
        chatList.value = await getChatList(curLoginUser.value.userId);
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
    overflow-y: auto;

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
          width: 100%;
          height: 100%;
          border-radius: 50%;
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
        background-color: rgba(0, 0, 0, 0.05);
        cursor: pointer;
      }
    }
  }
</style>