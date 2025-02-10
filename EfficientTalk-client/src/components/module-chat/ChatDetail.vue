<template>
  <div v-if="chatInfo!==null"
       class="chat-detail"
  >
    <div class="chat-header">
      <div class="user-info">
        <div class="user-avatar">
          <img class="avatar"
               :src="chatInfo.userAvatar"
               alt=""
          >
        </div>
        <div class="user-name">
          {{ chatInfo.userName }}
        </div>
      </div>
    </div>
    <div ref="chatHistoryElement"
         class="chat-history"
    >
      <div class="message-item"
           v-for="(item,index) in chatHistory"
           :key="index"
      >
        <div v-if="item.sender !== userDataStore.userId"
             class="others-message"
        >
          <div class="message-avatar">
            <img class="avatar"
                 :src="chatInfo.userAvatar"
                 alt="avatar"
            >
          </div>
          <div class="message-info">
            <div class="user-name">
              {{ chatInfo.userName }}
            </div>
            <div class="message-content-container">
              <div class="message-content">
                <div v-if="item.type === 'text'"
                     v-html="item.content"
                ></div>
                <img v-else-if="item.type === 'image'"
                     :src="item.content"
                     alt="image"
                     class="image-message"
                >
              </div>
            </div>
            <div class="message-time">
              {{ item.time }}
            </div>
          </div>
        </div>
        <div v-else
             class="my-message"
        >
          <div class="message-info">
            <div class="user-name">
              {{ userDataStore.userName }}
            </div>
            <div class="message-content-container">
              <div class="message-content">
                <div v-if="item.type === 'text'"
                     v-html="item.content"
                ></div>
                <img v-else-if="item.type === 'image'"
                     :src="item.content"
                     alt="image"
                     class="image-message"
                >
              </div>
            </div>
            <div class="message-time">
              {{ item.time }}
            </div>
          </div>
          <div class="message-avatar">
            <img class="avatar"
                 :src="userDataStore.userAvatar"
                 alt="avatar"
            >
          </div>
        </div>
      </div>
    </div>
    <div class="chat-footer">
      <div class="operations-bar">
        <div class="left-bar">
          <a-button class="operation-btn">
            <SmileOutlined/>
          </a-button>
          <a-button class="operation-btn">
            <FolderOutlined/>
          </a-button>
        </div>
        <div class="right-bar">
          <a-button class="operation-btn">
            <HistoryOutlined/>
          </a-button>
        </div>
      </div>
      <div class="input-area">
        <a-textarea class="input"
                    v-model:value="chatInput"
                    :bordered="false"
                    :autoSize="false"
                    @pressEnter="handleSend"
        ></a-textarea>
        <a-button type="primary"
                  class="btn-send"
                  @click="handleSend"
        >发送
        </a-button>
      </div>
    </div>
  </div>
  <div v-else
       class="chat-detail"
       style="justify-content: center"
  >
    <a-empty :image="simpleImage"
             :description="'选择用户开始沟通吧'"
    />
  </div>
</template>

<script setup>
    import {
        nextTick,
        onBeforeMount,
        ref
    } from "vue";
    import {
        Empty,
        notification
    } from "ant-design-vue";
    import {
        SmileOutlined,
        FolderOutlined,
        HistoryOutlined
    } from "@ant-design/icons-vue";
    import dayjs from "dayjs";
    import { UUID } from "uuidjs";
    import { useUserDataStore } from "../../store/UserDataStore.js";
    import { useWebSocketStore } from "../../store/WebSocketStore.js";
    import {
        saveChatRecord,
        getChatHistory
    } from "../../database/chat-history.js";

    const simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;

    // 用户数据
    const userDataStore = useUserDataStore();

    // WebSocket连接
    const websocketStore = useWebSocketStore();

    // 消息记录对话框元素
    const chatHistoryElement = ref(null);

    // 输入消息
    const chatInput = ref("");

    //传入参数
    const props = defineProps({
        chatInfo: {
            type: Object,
            default: () => {
                return {
                    userId: "",
                    userName: "",
                    userAvatar: ""
                };
            }
        }
    });

    // 对话框滚动到底部
    const scrollToBottom = (animation) => {
        nextTick(() => {
            if (chatHistoryElement.value) {
                chatHistoryElement.value.scrollTo({
                    top: chatHistoryElement.value.scrollHeight,
                    behavior: animation,
                });
            }
        });
    };

    // 保存聊天记录
    const handleSaveChatHistory = (record) => {
        saveChatRecord(record);
    };

    // 接收消息
    const handleMessageReceive = (message) => {
        const messageData = message.detail;
        console.log("收到服务器消息：", messageData);
        chatHistory.value.push(messageData);

        if (messageData.sender === props.chatInfo.userId) {
            // 如果发送者是当前聊天的对象，滚动到底部
            scrollToBottom("smooth")
        }

        // 保存聊天记录
        handleSaveChatHistory(messageData);
    };

    // 发送消息
    const handleSend = () => {
        if (chatInput.value === "") {
            notification.error({
                placement: "topRight",
                message: "消息不能为空",
                description: "请输入消息内容",
                duration: 2,
                top: 36
            });
        }
        else {
            const message = {
                id: UUID.generate(),
                sender: userDataStore.userId,
                receiver: props.chatInfo.userId,
                type: "text",
                content: chatInput.value,
                time: dayjs().format("YYYY-MM-DD HH:mm:ss")
            };
            websocketStore.socket.send(JSON.stringify(message));
            chatHistory.value.push(message);

            // 发送消息后滚动到底部
            scrollToBottom("smooth")

            // 广播消息
            window.dispatchEvent(new CustomEvent("messageSend", {
                detail: message
            }));

            // 保存聊天记录
            handleSaveChatHistory(message);
        }
        chatInput.value = "";
    };

    // 聊天对象变化
    const handleChatObjectChange = async (event) => {
        const friendId = event.detail;
        chatHistory.value = await getChatHistory(friendId, userDataStore.userId);

        // 发送消息后滚动到底部
        scrollToBottom("auto")
    };

    // 聊天记录
    const chatHistory = ref([
        {
            id: "1",
            sender: "1",
            receiver: "2",
            type: "text",
            content: "你好",
            time: "2023-05-05 12:00:00"
        },
        {
            id: "2",
            sender: "2",
            receiver: "1",
            type: "text",
            content: "你好",
            time: "2023-05-05 12:00:00",
        },
        {
            id: "3",
            sender: "1",
            receiver: "2",
            type: "image",
            content: "https://picsum.photos/200/300",
            time: "2023-05-05 12:00:00",
        }
    ]);

    onBeforeMount(() => {
        // 注册消息监听事件
        window.addEventListener("messageReceive", handleMessageReceive);
        // 订阅聊天对象变化事件
        window.addEventListener("chatObjectChange", handleChatObjectChange);
    });
</script>

<style scoped
       lang="scss"
>
  .chat-detail {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;

    $header-height: 50px;
    $footer-height: 210px;

    .chat-header {
      display: flex;
      align-items: center;
      width: 100%;
      height: $header-height;
      padding: 20px;
      border-bottom: 1px solid rgba(0, 0, 0, 0.1);

      .user-info {
        display: flex;
        align-items: center;
        width: 100%;
        height: 100%;

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

        .user-name {
          font-size: 16px;
          font-weight: bold;
        }
      }
    }

    .chat-history {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: calc(100% - #{$header-height + $footer-height});
      overflow-y: auto;
      padding-bottom: 20px;

      $message-item-gap: 15px;

      .message-item {
        display: flex;
        width: 95%;
        height: fit-content;
        margin-top: $message-item-gap;

        $avatar-container-width: 60px;
        $message-user-name-size: 12px;
        $message-element-gap: 8px;

        .others-message {
          display: flex;
          justify-content: flex-start;

          .message-avatar {
            display: flex;
            justify-content: center;
            width: $avatar-container-width;

            .avatar {
              width: 40px;
              height: 40px;
              border-radius: 50%;
            }
          }

          .message-info {
            display: flex;
            flex-direction: column;
            width: calc(100% - $avatar-container-width);
            gap: $message-element-gap;

            .user-name {
              font-size: $message-user-name-size;
              color: gray;
            }

            .message-content-container {
              display: flex;
              justify-content: flex-start;
              width: 100%;

              .message-content {
                display: flex;
                justify-content: flex-start;
                width: fit-content;
                max-width: 100%;
                padding: 5px;
                font-size: 14px;
                color: black;
                background-color: rgba(0, 0, 0, 0.1);
                border-radius: 5px;
                box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.1);
                word-wrap: break-word; /* 强制换行 */
                word-break: break-all; /* 在任意字符处换行 */
                white-space: normal; /* 默认行为，正常换行 */
                overflow-wrap: break-word; /* 在单词内部换行 */

                .image-message {
                  max-width: 100%;
                }
              }
            }

            .message-time {
              display: flex;
              justify-content: flex-start;
              align-items: center;
              font-size: 12px;
              color: gray;
            }
          }
        }

        .my-message {
          display: flex;
          justify-content: flex-end;
          width: 100%;

          .message-avatar {
            display: flex;
            justify-content: center;
            width: $avatar-container-width;

            .avatar {
              width: 40px;
              height: 40px;
              border-radius: 50%;
            }
          }

          .message-info {
            display: flex;
            flex-direction: column;
            justify-content: flex-end;
            width: calc(100% - $avatar-container-width);
            gap: $message-element-gap;

            .user-name {
              display: flex;
              justify-content: flex-end;
              align-items: center;
              font-size: $message-user-name-size;
              color: gray;
            }

            .message-content-container {
              display: flex;
              justify-content: flex-end;
              width: 100%;

              .message-content {
                display: flex;
                justify-content: flex-start;
                width: fit-content;
                max-width: 100%;
                padding: 5px;
                font-size: 14px;
                color: white;
                background-color: #1677FF;
                border-radius: 5px;
                box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.1);
                word-wrap: break-word; /* 强制换行 */
                word-break: break-all; /* 在任意字符处换行 */
                white-space: normal; /* 默认行为，正常换行 */
                overflow-wrap: break-word; /* 在单词内部换行 */

                .image-message {
                  max-width: 100%;
                }
              }
            }

            .message-time {
              display: flex;
              justify-content: flex-end;
              align-items: center;
              font-size: 12px;
              color: gray;
            }
          }
        }
      }
    }

    .chat-footer {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: $footer-height;

      $operations-bar-height: 40px;

      .operations-bar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        height: $operations-bar-height;
        padding: 0 20px;
        border-top: 1px solid rgba(0, 0, 0, 0.1);

        .left-bar {
          display: flex;
          justify-content: flex-start;
          align-items: center;
          gap: 10px;
        }

        .right-bar {
          display: flex;
          justify-content: flex-end;
          align-items: center;
          gap: 10px;
        }

        .operation-btn {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 30px;
          height: 30px;
          font-size: 20px;
          border-radius: 50%;
          border: none;
          box-shadow: none;
        }
      }

      .input-area {
        width: 100%;
        height: calc(100% - $operations-bar-height);
        padding: 0 12px;
        position: relative;

        .input {
          width: 100%;
          height: 100%;
          resize: none;
        }

        .btn-send {
          position: absolute;
          right: 15px;
          bottom: 15px;
        }
      }
    }
  }
</style>