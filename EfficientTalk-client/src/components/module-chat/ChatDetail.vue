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
        <div v-if="item.sender !== curLoginUser.userId"
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
              {{ formatMessageTime(item.time, "chat-detail") }}
            </div>
          </div>
        </div>
        <div v-else
             class="my-message"
        >
          <div class="message-info">
            <div class="user-name">
              {{ curLoginUser.userName }}
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
              {{ formatMessageTime(item.time, "chat-detail") }}
            </div>
          </div>
          <div class="message-avatar">
            <img class="avatar"
                 :src="curLoginUser.userAvatar"
                 alt="avatar"
            >
          </div>
        </div>
      </div>
    </div>
    <div class="chat-footer">
      <div class="operations-bar">
        <div class="left-bar">
          <a-popover title="表情包"
                     trigger="hover"
                     class="emoji-selector"
          >
            <template #content>
              <div style="width:400px;height: 400px;overflow-y:auto ">
                <!--<Vue3EmojiPicker style="width: 100%;height: 100%;border-radius: 0"-->
                <!--                 :native="true"-->
                <!--                 :disable-skin-tones="true"-->
                <!--                 :display-recent="true"-->
                <!--                 :disable-sticky-group-names="true"-->
                <!--                 :static-texts="{ placeholder: '搜索表情'}"-->
                <!--                 v-model="selectedEmoji"-->
                <!--                 @select="handleEmojiSelect"-->
                <!--                 :group-names="{-->
                <!--								 'recently-used':'最近使用',-->
                <!--							   'smileys_people': '表情 & 人',-->
                <!--								 'animals_nature': '动物 & 自然',-->
                <!--								 'food_drink': '食物 & 饮品',-->
                <!--								 'activities': '活动',-->
                <!--								 'travel_places': '旅行地点',-->
                <!--								 'objects': '物品',-->
                <!--								 'symbols': '标志',-->
                <!--								 'flags': '国旗'-->
                <!--							 }"-->
                <!--/>-->
              </div>
            </template>
            <a-button class="operation-btn">
              <SmileOutlined/>
            </a-button>
          </a-popover>
          <a-button class="operation-btn">
            <PictureOutlined/>
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
  <EmptyContainer v-else :description="'选择用户开始沟通吧'"/>
</template>

<script setup>
    import {
        nextTick,
        onBeforeMount,
        ref
    } from "vue";
    import { notification } from "ant-design-vue";
    import {
        FolderOutlined,
        HistoryOutlined,
        SmileOutlined,
        PictureOutlined
    } from "@ant-design/icons-vue";
    import dayjs from "dayjs";
    import { UUID } from "uuidjs";
    import { useWebSocketStore } from "../../store/WebSocketStore.js";
    import {
        getChatHistory,
        saveChatRecord
    } from "../../database/chat-history.js";
    import { getCurUserData } from "../../database/cur-user.js";
    import { formatMessageTime } from "../../utils/time-utils.js";
    import EmptyContainer from "../empty-container/EmptyContainer.vue";

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // WebSocket连接
    const websocketStore = useWebSocketStore();

    // 消息记录对话框元素
    const chatHistoryElement = ref(null);

    // 输入消息
    const chatInput = ref("");
    const selectedEmoji = ref();
    const handleEmojiSelect = (emoji) => {
        chatInput.value += emoji.i;
        selectedEmoji.value = "";
    };

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

        if (messageData.sender === props.chatInfo.userId) {
            // 如果发送者是当前聊天的对象,注入消息并滚动到底部
            chatHistory.value.push(messageData);
            scrollToBottom("smooth");
        }
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
                sender: curLoginUser.value.userId,
                receiver: props.chatInfo.userId,
                type: "text",
                fileId: null,
                content: chatInput.value,
                time: dayjs().format("YYYY-MM-DD HH:mm:ss")
            };
            websocketStore.socket.send(JSON.stringify(message));
            chatHistory.value.push(message);

            // 发送消息后滚动到底部
            scrollToBottom("smooth");

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
        chatHistory.value = await getChatHistory(friendId, curLoginUser.value.userId);

        // 发送消息后滚动到底部
        scrollToBottom("auto");
    };

    // 聊天记录
    const chatHistory = ref([]);

    onBeforeMount(async () => {
        // 注册消息监听事件
        window.addEventListener("messageReceive", handleMessageReceive);
        // 订阅聊天对象变化事件
        window.addEventListener("chatObjectChange", handleChatObjectChange);

        // 初始化当前登录的用户信息
        await updateCurLoginUser();
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .chat-detail {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;

    $header-height: 60px;
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
        $message-content-max-width: 80%;
        $message-content-horizontal-padding: 10px;
        $message-content-vertical-padding: 6px;

        .others-message {
          display: flex;
          justify-content: flex-start;
          width: 100%;
          height: fit-content;

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
                max-width: $message-content-max-width;
                padding: $message-content-vertical-padding $message-content-horizontal-padding;
                font-size: 14px;
                color: black;
                background-color: rgba(0, 0, 0, 0.06);
                border-radius: 5px;
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
              font-size: 10px;
              color: rgba(0, 0, 0, 0.35);
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
                max-width: $message-content-max-width;
                padding: $message-content-vertical-padding $message-content-horizontal-padding;
                font-size: 14px;
                color: white;
                background-color: global-variable.$theme-color;
                border-radius: 5px;
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
              font-size: 10px;
              color: rgba(0, 0, 0, 0.35);
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