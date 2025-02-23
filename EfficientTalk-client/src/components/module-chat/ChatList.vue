<template>
  <div class="chat-list">
    <div class="search-area">
      <a-input class="content-input"
               placeholder="搜索"
               v-model:value="searchInput"
               allow-clear
      ></a-input>
      <a-button class="search-btn">
        <SearchOutlined/>
      </a-button>
    </div>
    <div class="chat-list-area">
      <EmptyContainer description="暂无进行中的会话"
                      v-if="chatListFilter(chatList).length === 0"
      />
      <div v-for="(item,index) in chatListFilter(chatList)"
           :key="index"
           class="chat-list-item"
           :class="{'chat-list-item-active': curChatId === item.userId}"
           @click="handleSelectChat(item)"
           @contextmenu="handleContextMenu"
           v-else
      >
        <div class="item-avatar">
          <img class="avatar"
               v-if="item.userAvatar!==null"
               :src="item.userAvatar"
               alt=""
          />
          <a-avatar class="avatar"
                    v-else
          >{{ item.userName.substring(0, 2) }}
          </a-avatar>
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
        onBeforeUnmount,
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
    import EmptyContainer from "../empty-container/EmptyContainer.vue";
    import ChatApi from "../../api/modules/ChatApi.js";
    import { saveChatRecord } from "../../database/chat-history.js";

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

    // 根据消息类型转换消息内容
    const translateMessageContent = (type, message) => {
        // 根据消息类型修改消息内容
        switch (type) {
            case "image":
                return "[图片]";
            case "file":
                return "[文件]";
        }
        return message;
    };

    // 保存聊天列表
    const handleSaveChatList = async (chatList) => {
        const chatListJson = JSON.stringify(chatList);
        await saveChatList(curLoginUser.value.userId, chatListJson);
        await saveCloudChatList(curLoginUser.value.userId, chatListJson);
    };

    // 接收消息
    const handleMessageReceive = async (event) => {
        const message = event.detail;

        // 判断聊天列表中是否存在该用户
        let existFlag = false;

        // 遍历消息列表，修改相应的元素内容
        for (let i = 0; i < chatList.value.length; i++) {
            if (chatList.value[i].userId === message.sender) {
                chatList.value[i].lastMessage = translateMessageContent(message.type, message.content);
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
            }).then((response) => {
                const res = response.data;
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
            });

            chatList.value.unshift(newMessage);
        }

        // 保存聊天列表至本地和云端
        await handleSaveChatList(chatList.value);
    };

    // 发送消息
    const handleMessageSend = async (event) => {
        const message = event.detail;
        const receiver = message.receiver;

        // 遍历消息列表，修改相应的元素内容
        for (let i = 0; i < chatList.value.length; i++) {
            if (chatList.value[i].userId === receiver) {
                // 根据消息类型修改消息内容
                chatList.value[i].lastMessage = translateMessageContent(message.type, message.content);
                chatList.value[i].lastMessageTime = message.time;
                // 将对应元素提到数组第一个
                chatList.value.unshift(chatList.value.splice(i, 1)[0]);
                break;
            }
        }

        // 保存聊天列表至本地和云端
        await handleSaveChatList(chatList.value);
    };

    const emits = defineEmits(["setSelectedChat"]);

    // 右键菜单
    const handleContextMenu = () => {
        alert("context menu");
    };

    // 选择聊天
    const handleSelectChat = async (item) => {
        curChatId.value = item.userId;
        const chatInfo = {
            userId: item.userId,
            userName: item.userName,
            userAvatar: item.userAvatar
        };
        emits("setSelectedChat", chatInfo);

        // 广播聊天对象变化事件
        window.dispatchEvent(new CustomEvent("chatObjectChange", {
            detail: curChatId.value,
            bubbles: false
        }));

        // 如果有消息未读则清空未读消息数
        if (item.unreadCount !== 0) {
            item.unreadCount = 0;

            // 保存聊天列表至本地和云端
            await handleSaveChatList(chatList.value);
        }
    };

    // 聊天列表
    const chatList = ref([]);

    // 从服务器获取聊天列表
    const getCloudChatList = async (userId) => {
        // 从服务器获取聊天列表
        const response = await ChatApi.getChatList({
            userId: userId
        });

        const res = response.data;
        if (res.code === 0) {
            const data = res.data;
            if (data != null) {
                return JSON.parse(data.chatList);
            }
        }
        return null;
    };

    // 保存聊天列表到服务器
    const saveCloudChatList = async (userId, chatListJson) => {
        // 保存聊天列表到服务器
        const response = await ChatApi.saveChatList({
            userId: userId,
            chatListJson: chatListJson
        });

        const res = response.data;
        if (res.code === 0) {
            console.log("保存聊天列表到服务器成功");
        }
        else {
            console.error("保存聊天列表到服务器失败");
        }
    };

    // 获取缓存的聊天记录
    const getChatHistoryCache = async (userId) => {
        const response = await ChatApi.getChatHistoryCache({
            userId: userId
        });

        const res = response.data;
        if (res.code === 0) {
            let data = res.data.chatHistory;
            if (data.length === 0) {
                return;
            }

            for (let i = 0; i < data.length; i++) {
                // 保存聊天记录至本地
                const message = data[i];
                await saveChatRecord(message);

                let existFlag = false;
                for (let j = 0; j < chatList.value.length; j++) {
                    if (chatList.value[j].userId === message.sender) {
                        if (chatList.value[j].lastMessageTime < message.time) {
                            chatList.value[j].lastMessage = translateMessageContent(message.type, message.content);
                            chatList.value[j].lastMessageTime = message.time;

                            // 将对应元素提到数组第一个
                            chatList.value.unshift(chatList.value.splice(j, 1)[0]);
                        }
                        // 如果发送消息的用户不是当前聊天对象，则增加未读消息数
                        if (curChatId.value !== message.sender) {
                            chatList.value[j].unreadCount++;
                        }
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
                        lastMessage: translateMessageContent(message.type, message.content),
                        lastMessageTime: message.time,
                        unreadCount: 1
                    };

                    // 获取用户基本信息
                    await UserApi.getUserBasicInfo({
                        userId: message.sender
                    }).then((response) => {
                        const res = response.data;
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
                    });

                    chatList.value.unshift(newMessage);
                }
            }
        }

        // 保存聊天列表至本地和云端
        await handleSaveChatList(chatList.value);
    };

    onBeforeMount(async () => {
        // 订阅消息接收
        window.addEventListener("messageReceive", handleMessageReceive);
        // 订阅消息发送
        window.addEventListener("messageSend", handleMessageSend);

        // 初始化当前登录的用户信息
        await updateCurLoginUser();

        // 读取聊天列表
        chatList.value = await getCloudChatList(curLoginUser.value.userId);
        if (chatList.value === null) {
            chatList.value = await getChatList(curLoginUser.value.userId);
        }

        // 获取缓存的聊天记录，并更新至聊天列表
        await getChatHistoryCache(curLoginUser.value.userId);

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
            }

            // 保存对话列表
            await handleSaveChatList(chatList.value);

            curChatId.value = props.friendInfo.userId;
            const chatInfo = {
                userId: props.friendInfo.userId,
                userName: props.friendInfo.userName,
                userAvatar: props.friendInfo.userAvatar
            };
            emits("setSelectedChat", chatInfo);
            // 广播聊天对象变化事件
            window.dispatchEvent(new CustomEvent("chatObjectChange", {
                detail: curChatId.value,
                bubbles: false
            }));
        }
    });

    onBeforeUnmount(()=>{
        // 移除监听器防止事件多次触发
        window.removeEventListener("messageReceive", handleMessageReceive);
        window.removeEventListener("messageSend", handleMessageSend);
    })
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
</style>