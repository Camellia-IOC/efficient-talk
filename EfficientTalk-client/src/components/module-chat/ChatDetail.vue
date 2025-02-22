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
              <div class="text-message"
                   v-if="item.type === 'text'"
              >
                <div>{{ item.content }}</div>
              </div>
              <div class="image-message"
                   v-else-if="item.type === 'image'"
              >
                <a-image :src="item.content"
                         alt="image"
                />
              </div>
              <div class="file-message"
                   v-else-if="item.type === 'file'"
              >
                <div class="file-detail">
                  <img :src="getFileIcon(item.fileType)"
                       alt="image"
                       class="file-icon"
                  >
                  <label class="file-name">{{ item.fileName + "." + item.fileType }}</label>
                </div>
                <div class="file-info">
                  <label class="file-size">{{ translateFileSize(item.fileSize) }}</label>
                </div>
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
              <div class="text-message"
                   v-if="item.type === 'text'"
              >
                <div>{{ item.content }}</div>
              </div>
              <div class="image-message"
                   v-else-if="item.type === 'image'"
              >
                <a-image :src="item.content"
                         alt="image"
                />
              </div>
              <div class="file-message"
                   v-else-if="item.type === 'file'"
              >
                <div class="file-detail">
                  <img :src="getFileIcon(item.fileType)"
                       alt="image"
                       class="file-icon"
                  >
                  <label class="file-name">{{ item.fileName + "." + item.fileType }}</label>
                </div>
                <div class="file-info">
                  <label class="file-size">{{ translateFileSize(item.fileSize) }}</label>
                </div>
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
          <a-button class="operation-btn"
                    @click="handlePictureSelectorDialogOpen"
          >
            <PictureOutlined/>
          </a-button>
          <a-button class="operation-btn"
                    @click="handleFileSelectorDialogOpen"
          >
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
  <EmptyContainer v-else
                  :description="'选择用户开始沟通吧'"
  />

  <!--图片上传-->
  <FileSelectorDialog ref="pictureSelectorDialog"
                      :dialog-title="'选择图片'"
                      :multi="true"
                      :type="'image'"
                      @send-selected-file="handleSendFileMessage"
  />

  <!--文件上传-->
  <FileSelectorDialog ref="fileSelectorDialog"
                      :dialog-title="'选择文件'"
                      :multi="true"
                      :type="'file'"
                      @send-selected-file="handleSendFileMessage"
  />
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
    import FileSelectorDialog from "../dialog/file-selector/FileSelectorDialog.vue";
    import ChatApi from "../../api/modules/ChatApi.js";
    import { translateFileSize } from "../../utils/unit-utils.js";
    import { getFileIcon } from "../../utils/file-utils.js";

    // 图片上传对话框控制
    const pictureSelectorDialog = ref();
    const handlePictureSelectorDialogOpen = () => {
        pictureSelectorDialog.value.dialogOpen();
    };

    // 文件上传对话框控制
    const fileSelectorDialog = ref();
    const handleFileSelectorDialogOpen = () => {
        fileSelectorDialog.value.dialogOpen();
    };

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

    // 上传聊天文件
    const uploadChatFile = async (file) => {
        const formData = new FormData();
        formData.append("fileId", file.fileId);
        formData.append("fileName", file.fileName);
        formData.append("fileType", file.fileType);
        formData.append("fileSize", file.fileSize);
        formData.append("file", file.origin);
        formData.append("sender", curLoginUser.value.userId);
        formData.append("receiver", props.chatInfo.userId);

        const response = await ChatApi.uploadChatFile(formData);
        const res = response.data;
        if (res.code === 0) {
            return res.data.filePath;
        }
        return null;
    };

    // 发送文件消息
    const handleSendFileMessage = async (messageList) => {
        for (let i = 0; i < messageList.length; i++) {
            const messageItem = messageList[i];
            // 保存文件
            const filePath = await uploadChatFile(messageItem);

            // 发送消息
            const message = {
                id: UUID.generate(),
                sender: curLoginUser.value.userId,
                receiver: props.chatInfo.userId,
                type: messageItem.type,
                fileId: messageItem.fileId,
                content: filePath,
                time: dayjs().format("YYYY-MM-DD HH:mm:ss")
            };
            websocketStore.socket.send(JSON.stringify(message));

            // 添加文件信息至本地消息记录
            message.fileName = messageItem.fileName;
            message.fileType = messageItem.fileType;
            message.fileSize = messageItem.fileSize;
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
    };

    // 聊天记录
    const chatHistory = ref([]);

    /**
     * 获取云端聊天记录
     * @param friendId 好友ID
     * @returns {Promise<any>}
     */
    const getCloudChatHistory = async (friendId) => {
        // TODO 添加分页获取聊天记录
        const response = await ChatApi.getChatHistory({
            userId: curLoginUser.value.userId,
            friendId: friendId,
            pageSize: 0,
            pageIndex: 0
        });

        const res = response.data;
        if (res.code === 0) {
            if (res.data != null) {
                return new Promise((resolve) => {
                    resolve(res.data.chatHistory);
                });
            }
        }
        return new Promise((resolve) => {
            resolve([]);
        });
    };

    // 聊天对象变化
    const handleChatObjectChange = async (event) => {
        // TODO 存在随着使用次数的增加，请求次数会越来越多的bug
        const friendId = event.detail;
        chatHistory.value = await getCloudChatHistory(friendId);
        if (chatHistory.value.length === 0) {
            chatHistory.value = await getChatHistory(friendId, curLoginUser.value.userId);
        }

        // 发送消息后滚动到底部
        scrollToBottom("auto");
    };

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
      border-bottom: global-variable.$border-line-width solid global-variable.$border-line-color;

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

              .text-message {
                display: flex;
                justify-content: flex-start;
                width: fit-content;
                max-width: $message-content-max-width;
                padding: $message-content-vertical-padding $message-content-horizontal-padding;
                font-size: 14px;
                color: black;
                background-color: #F0F2F5;
                border-radius: 5px;
                word-wrap: break-word; /* 强制换行 */
                word-break: break-all; /* 在任意字符处换行 */
                white-space: normal; /* 默认行为，正常换行 */
                overflow-wrap: break-word; /* 在单词内部换行 */
              }

              .image-message {
                max-width: 40%;
              }

              .file-message {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                align-items: center;
                width: 40%;
                height: 90px;
                padding: 10px;
                background-color: #F5F7FA;
                border-radius: 10px;
                cursor: pointer;

                &:hover {
                  background-color: #EBEDF0;
                }

                .file-detail {
                  display: flex;
                  align-items: center;
                  width: 100%;
                  cursor: pointer;

                  .file-icon {
                    width: 50px;
                    height: 45px;
                    border-radius: 5px;
                    margin-right: 15px;
                    cursor: pointer;
                  }

                  .file-name {
                    font-size: 14px;
                    cursor: pointer;
                  }
                }

                .file-info {
                  display: flex;
                  justify-content: flex-end;
                  align-items: center;
                  width: 100%;
                  cursor: pointer;

                  .file-size {
                    font-size: 12px;
                    color: gray;
                    cursor: pointer;
                  }
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

              .text-message {
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
              }

              .image-message {
                max-width: 40%;
              }

              .file-message {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                align-items: center;
                width: 40%;
                height: 90px;
                padding: 10px;
                background-color: #F5F7FA;
                border-radius: 10px;
                cursor: pointer;

                &:hover {
                  background-color: #EBEDF0;
                }

                .file-detail {
                  display: flex;
                  align-items: center;
                  width: 100%;
                  cursor: pointer;

                  .file-icon {
                    width: 50px;
                    height: 45px;
                    border-radius: 5px;
                    margin-right: 15px;
                    cursor: pointer;
                  }

                  .file-name {
                    font-size: 14px;
                    cursor: pointer;
                  }
                }

                .file-info {
                  display: flex;
                  justify-content: flex-end;
                  align-items: center;
                  width: 100%;
                  cursor: pointer;

                  .file-size {
                    font-size: 12px;
                    color: gray;
                    cursor: pointer;
                  }
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
        border-top: global-variable.$border-line-width solid global-variable.$border-line-color;

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