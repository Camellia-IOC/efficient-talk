<template>
  <div class="chat-list">
    <div class="search-area">
      <a-input class="content-input"
               placeholder="搜索"
      ></a-input>
      <a-button class="search-btn">
        <SearchOutlined/>
      </a-button>
    </div>
    <div class="chat-list-area">
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
    import { SearchOutlined } from "@ant-design/icons-vue";
    import { formatMessageTime } from "../../utils/time-utils.js";
    import UserApi from "../../api/modules/UserApi.js";

    const props = defineProps({
        friendInfo: {
            type: Object,
            default: null
        }
    });

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
    const handleMessageReceive = async (event) => {
        const message = event.detail;

        // 根据消息类型修改消息内容
        switch (message.type) {
            case "image":
                message.content = "[图片]";
                break;
            case "file":
                message.content = "[文件]";
                break;
        }

        // 判断聊天列表中是否存在该用户
        let existFlag = false;

        // 遍历消息列表，修改相应的元素内容
        for (let i = 0; i < chatList.value.length; i++) {
            if (chatList.value[i].userId === message.sender) {
                chatList.value[i].lastMessage = message.content;
                chatList.value[i].lastMessageTime = message.time;

                // 如果发送消息的用户不是当前聊天对象，则增加未读消息数
                if (curChatId.value !== message.sender) {
                    chatList.value[i].unreadCount++;
                }

                // 将对应元素提到数组第一个
                chatList.value.unshift(chatList.value.splice(i, 1)[0]);
                existFlag = true;
                break;
            }
        }

        // 如果不存在该用户，则添加到聊天列表中
        if (!existFlag) {
            let newMessage = {
                userId: message.sender,
                userName: null,
                userAvatar: null,
                lastMessage: message.content,
                lastMessageTime: message.time,
                unreadCount: 1
            };

            // 获取用户基本信息
            await UserApi.getUserBasicInfo({
                userId: message.sender
            }).then((res) => {
                if (res.code === 0) {
                    const data = res.data;
                    if (data != null) {
                        newMessage.userName = data.userName;
                        newMessage.userAvatar = data.userAvatar;
                    }
                }
            }).catch(() => {
                console.error("获取用户基本信息失败");
                newMessage.userName = "未知用户";
                // TODO 默认头像需要替换
                newMessage.userAvatar = "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";
            }).finally(() => {
                chatList.value.unshift(newMessage);
            });
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
                // 将对应元素提到数组第一个
                chatList.value.unshift(chatList.value.splice(i, 1)[0]);
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
        const chatInfo = {
            userId: chatList.value[index].userId,
            userName: chatList.value[index].userName,
            userAvatar: chatList.value[index].userAvatar
        };
        emits("setSelectedChat", chatInfo);

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
    const chatList = ref([]);

    onBeforeMount(async () => {
        // 订阅消息接收
        window.addEventListener("messageReceive", handleMessageReceive);
        // 订阅消息发送
        window.addEventListener("messageSend", handleMessageSend);

        // 初始化当前登录的用户信息
        await updateCurLoginUser();

        // 读取聊天列表
        chatList.value = await getChatList(curLoginUser.value.userId);

        // 如果传入的好友ID不为null,则将聊天对象设置为该好友
        if (props.friendInfo !== null) {
            let flag = false;
            for (let i = 0; i < chatList.value.length; i++) {
                if (chatList.value[i].userId === props.friendInfo.userId) {
                    flag = true;
                    // 更新聊天对象的名称和头像
                    chatList.value[i].userName = props.friendInfo.userName;
                    chatList.value[i].userAvatar = props.friendInfo.userAvatar;
                    // 清空未读消息数
                    chatList.value[i].unreadCount = 0;
                    handleSaveChatList(chatList.value);
                    break;
                }
            }

            if (!flag) {
                // 在聊天列表的头部添加该好友
                chatList.value.unshift({
                    userId: props.friendInfo.userId,
                    userName: props.friendInfo.userName,
                    userAvatar: props.friendInfo.userAvatar,
                    lastMessage: "",
                    lastMessageTime: "",
                    unreadCount: 0
                });
                handleSaveChatList(chatList.value);
            }

            curChatId.value = props.friendInfo.userId;
            const chatInfo = {
                userId: props.friendInfo.userId,
                userName: props.friendInfo.userName,
                userAvatar: props.friendInfo.userAvatar
            };
            emits("setSelectedChat", chatInfo);
            // 广播聊天对象变化事件
            window.dispatchEvent(new CustomEvent("chatObjectChange", {
                detail: curChatId.value
            }));
        }
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

    .chat-list-area {
      display: flex;
      flex-direction: column;
      width: 100%;
      height: calc(100% - $search-area-height);

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
  }
</style>