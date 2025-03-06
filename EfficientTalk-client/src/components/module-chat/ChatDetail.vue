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
      <div v-if="historyPageConfig.lastCount === historyPageConfig.pageSize"
           class="load-btn-container"
      >
        <a-button type="link"
                  class="load-btn"
                  @click="handleLoadMoreHistory"
        >
          <CloudSyncOutlined/>
          <label>加载更多</label>
        </a-button>
      </div>
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
                         style="border-radius: 15px"
                         fallback="data:image/png;base64,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"
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
                  <div class="operations-bar">
                    <a-button class="operation-btn">下载</a-button>
                    <a-button class="operation-btn"
                              @click="handleFilePreview"
                    >预览
                    </a-button>
                  </div>
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
                         style="border-radius: 15px"
                         fallback="data:image/png;base64,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"
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
                  <div class="operations-bar">
                    <a-button class="operation-btn">下载</a-button>
                    <a-button class="operation-btn"
                              @click="handleFilePreview"
                    >预览
                    </a-button>
                  </div>
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
          <a-button class="operation-btn"
                    @click="handleOpenChatHistoryWindow"
          >
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
        onBeforeUnmount,
        ref
    } from "vue";
    import {
        message,
        notification
    } from "ant-design-vue";
    import {
        FolderOutlined,
        HistoryOutlined,
        SmileOutlined,
        PictureOutlined,
        CloudSyncOutlined
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
    import FileSelectorDialog from "../dialog/module-chat/file-selector/FileSelectorDialog.vue";
    import ChatApi from "../../api/modules/ChatApi.js";
    import { translateFileSize } from "../../utils/unit-utils.js";
    import { getFileIcon } from "../../utils/file-utils.js";
    import {
        openChatHistoryChildWindow,
        openFilePreviewChildWindow
    } from "../../window-controller/controller/ChildWindowController.js";
    import ChildWindowController from "../../window-controller/child-window-controller.js";

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

    // 聊天记录分页配置
    const historyPageConfig = ref({
        // 每页大小
        pageSize: 20,
        // 最后一次获取的聊天记录数
        lastCount: 0,
        resetPage: () => {
            historyPageConfig.value.lastCount = 0;
        }
    });

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
        else if (websocketStore.onlineState === "OUTLINE") {
            message.error("当前为离线状态，发送失败");
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

    // 上传聊天图片
    const uploadChatImage = async (image) => {
        const formData = new FormData();
        formData.append("imageId", image.fileId);
        formData.append("imageName", image.fileName);
        formData.append("imageType", image.fileType);
        formData.append("imageSize", image.fileSize);
        formData.append("image", image.origin);
        formData.append("sender", curLoginUser.value.userId);
        formData.append("receiver", props.chatInfo.userId);

        const response = await ChatApi.uploadChatImage(formData);
        const res = response.data;
        if (res.code === 0) {
            return res.data.filePath;
        }
        return null;
    };

    // 发送文件消息
    const handleSendFileMessage = async (messageList) => {
        if (websocketStore.onlineState !== "OUTLINE") {
            for (let i = 0; i < messageList.length; i++) {
                const messageItem = messageList[i];

                // 保存文件
                let filePath = null;
                if (messageItem.type === "image") {
                    // 上传图片
                    filePath = await uploadChatImage(messageItem);
                }
                else if (messageItem.type === "file") {
                    // 上传文件
                    filePath = await uploadChatFile(messageItem);
                }

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
        }
        else {
            message.error("当前为离线状态，发送失败");
        }
    };

    // 聊天记录
    const chatHistory = ref([]);

    /**
     * 获取云端聊天记录
     * @param friendId 好友ID
     * @param lastTime 最早一条记录的时间
     * @returns {Promise<any>}
     */
    const getCloudChatHistory = async (friendId, lastTime) => {
        const response = await ChatApi.getChatHistory({
            userId: curLoginUser.value.userId,
            friendId: friendId,
            pageSize: historyPageConfig.value.pageSize,
            lastTime: lastTime
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

    // 加载更多聊天记录
    const handleLoadMoreHistory = async () => {
        const oldContainerSize = chatHistoryElement.value.scrollHeight;
        const friendId = props.chatInfo.userId;
        const historyPage = await getCloudChatHistory(friendId, chatHistory.value[0].time);
        historyPageConfig.value.lastCount = historyPage.length;
        chatHistory.value = historyPage.reverse().concat(chatHistory.value);

        // 保持浏览位置在原处
        await nextTick(() => {
            const newContainerSize = chatHistoryElement.value.scrollHeight;
            if (chatHistoryElement.value) {
                chatHistoryElement.value.scrollTo({
                    top: (newContainerSize - oldContainerSize),
                    behavior: "auto",
                });
            }
        });
    };

    // 打开聊天记录浏览窗口
    const handleOpenChatHistoryWindow = () => {
        const data = {
            userId: curLoginUser.value.userId,
            friendId: props.chatInfo.userId,
        };
        openChatHistoryChildWindow(data);
    };

    // 聊天对象变化
    const handleChatObjectChange = async (event) => {
        // 重置分页
        historyPageConfig.value.resetPage();

        chatHistory.value = [];
        const friendId = event.detail;
        const historyPage = await getCloudChatHistory(friendId, null);
        historyPageConfig.value.lastCount = historyPage.length;
        chatHistory.value = chatHistory.value.concat(historyPage.reverse());
        if (chatHistory.value.length === 0) {
            chatHistory.value = await getChatHistory(friendId, curLoginUser.value.userId);
        }

        // 发送消息后滚动到底部
        scrollToBottom("auto");
    };

    // 处理文件预览
    const handleFilePreview = () => {
        const data = {};
        openFilePreviewChildWindow(data);
    };

    onBeforeMount(async () => {
        // 注册消息监听事件
        window.addEventListener("messageReceive", handleMessageReceive);
        // 订阅聊天对象变化事件
        window.addEventListener("chatObjectChange", handleChatObjectChange);

        // 初始化当前登录的用户信息
        await updateCurLoginUser();
    });

    onBeforeUnmount(() => {
        // 注销消息监听事件
        window.removeEventListener("messageReceive", handleMessageReceive);
        // 注销聊天对象变化事件
        window.removeEventListener("chatObjectChange", handleChatObjectChange);
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
      background-color: global-variable.$background-color;

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
      background-color: global-variable.$background-color;

      .load-btn-container {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;

        .load-btn {
          display: flex;
          justify-content: center;
          align-items: center;
          color: gray;
          gap: 5px;
          cursor: pointer;

          &:hover {
            color: global-variable.$theme-color;
          }

          label {
            cursor: pointer;
          }
        }
      }

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
                background-color: white;
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
                width: 260px;
                padding: 10px;
                background-color: white;
                border-radius: 10px;
                cursor: pointer;
                gap: 15px;

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
                  justify-content: space-between;
                  align-items: center;
                  width: 100%;
                  cursor: pointer;

                  .operations-bar {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    gap: 10px;

                    .operation-btn {
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      width: 75px;
                      height: 25px;

                      &:hover {
                        border: none;
                      }
                    }
                  }

                  .file-size {
                    text-align: center;
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
                width: 260px;
                padding: 10px;
                background-color: white;
                border-radius: 10px;
                cursor: pointer;
                gap: 15px;

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
                  justify-content: space-between;
                  align-items: center;
                  width: 100%;
                  cursor: pointer;

                  .operations-bar {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    gap: 10px;

                    .operation-btn {
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      width: 75px;
                      height: 25px;

                      &:hover {
                        border: none;
                      }
                    }
                  }

                  .file-size {
                    text-align: center;
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
      background-color: global-variable.$background-color;

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
          background-color: global-variable.$background-color;
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
          background-color: global-variable.$theme-color;
        }
      }
    }
  }
</style>