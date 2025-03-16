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
           :id="item.id"
           :class="{'selected-message':item.isSelected}"
      >
        <a-checkbox v-model:checked="item.isSelected"
                    v-show="isSelectMessageMode"
                    @change="checkedValue=>handleMessageSelect(checkedValue.target.checked,item.id)"
        ></a-checkbox>
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
              <a-dropdown :trigger="['contextmenu']"
                          v-if="item.type === 'text'"
              >
                <div class="text-message">
                  <div>{{ item.content }}</div>
                </div>
                <template #overlay>
                  <div class="context-menu-container">
                    <div class="context-menu-item"
                         @click="contextMenuOperations.copyToClipboard(item.content)"
                    >
                      <div class="icon">
                        <CopyOutlined/>
                      </div>
                      复制
                    </div>
                    <div class="context-menu-item"
                         @click="contextMenuOperations.transmitMessage(item)"
                    >
                      <div class="icon">
                        <ExportOutlined/>
                      </div>
                      转发
                    </div>
                    <div class="context-menu-item"
                         @click="contextMenuOperations.deleteFromChatHistory(item.id)"
                    >
                      <div class="icon">
                        <DeleteOutlined/>
                      </div>
                      删除
                    </div>
                    <div class="context-menu-item"
                         @click="contextMenuOperations.switchSelectMessageMode"
                    >
                      <div class="icon">
                        <CheckSquareOutlined/>
                      </div>
                      多选
                    </div>
                    <div class="context-menu-item"
                         @click="contextMenuOperations.assistantHelperReadText(item.content)"
                    >
                      <div class="icon">
                        <AiAssistantIcon :size="16"/>
                      </div>
                      小易帮阅
                    </div>
                    <div class="context-menu-item"
                         @click="contextMenuOperations.assistantHelperReply(item.content)"
                    >
                      <div class="icon">
                        <AiAssistantIcon :size="16"/>
                      </div>
                      快捷回复
                    </div>
                  </div>
                </template>
              </a-dropdown>
              <a-dropdown :trigger="['contextmenu']"
                          v-else-if="item.type === 'image'"
              >
                <div class="image-message">
                  <img :src="item.content"
                       alt="image"
                       class="image"
                       @click="handleMediaFilePreview(item.fileId, 'image')"
                  />
                </div>
                <template #overlay>
                  <div class="context-menu-container">
                    <div class="context-menu-item"
                         @click="contextMenuOperations.transmitMessage(item)"
                    >
                      <div class="icon">
                        <ExportOutlined/>
                      </div>
                      转发
                    </div>
                    <div class="context-menu-item"
                         @click="contextMenuOperations.deleteFromChatHistory(item.id)"
                    >
                      <div class="icon">
                        <DeleteOutlined/>
                      </div>
                      删除
                    </div>
                    <div class="context-menu-item"
                         @click="contextMenuOperations.assistantHelperReadImage(item.content)"
                    >
                      <div class="icon">
                        <AiAssistantIcon :size="16"/>
                      </div>
                      小易识图
                    </div>
                  </div>
                </template>
              </a-dropdown>
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
                              @click="handleFilePreview(item.fileId,item.fileType)"
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
              <a-dropdown :trigger="['contextmenu']"
                          v-if="item.type === 'text'"
              >
                <div class="text-message disable-context-menu">
                  <div>{{ item.content }}</div>
                </div>
                <template #overlay>
                  <div class="context-menu-container">
                    <div class="context-menu-item"
                         @click="contextMenuOperations.copyToClipboard(item.content)"
                    >
                      <div class="icon">
                        <CopyOutlined/>
                      </div>
                      复制
                    </div>
                    <div class="context-menu-item"
                         @click="contextMenuOperations.transmitMessage(item)"
                    >
                      <div class="icon">
                        <ExportOutlined/>
                      </div>
                      转发
                    </div>
                    <div class="context-menu-item"
                         @click="contextMenuOperations.switchSelectMessageMode"
                    >
                      <div class="icon">
                        <CheckSquareOutlined/>
                      </div>
                      多选
                    </div>
                    <div class="context-menu-item"
                         @click="contextMenuOperations.deleteFromChatHistory(item.id)"
                    >
                      <div class="icon">
                        <DeleteOutlined/>
                      </div>
                      删除
                    </div>
                  </div>
                </template>
              </a-dropdown>
              <div class="image-message"
                   v-else-if="item.type === 'image'"
              >
                <img :src="item.content"
                     alt="image"
                     class="image"
                     @click="handleMediaFilePreview(item.fileId, 'image')"
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
                              @click="handleFilePreview(item.fileId, item.fileType)"
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
    <div class="chat-footer"
         v-show="isSelectMessageMode"
    >
      <div class="operations-bar"
           style="justify-content: flex-end"
      >
        <a-button class="operation-btn"
                  style="font-size: 14px"
                  @click="contextMenuOperations.switchSelectMessageMode()"
        >
          <CloseOutlined/>
        </a-button>
      </div>
      <div class="message-operations">
        <div class="operation-btn">
          <CopyOutlined/>
          <label>复制</label>
        </div>
        <div class="operation-btn">
          <ExportOutlined/>
          <label>转发</label>
        </div>
        <div class="operation-btn"
             @click="contextMenuOperations.deleteFromChatHistory(getSelectedMessageIdListStr())"
        >
          <DeleteOutlined/>
          <label>删除</label>
        </div>
      </div>
    </div>
    <div class="chat-footer"
         v-show="!isSelectMessageMode"
    >
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
        <a-dropdown :trigger="['contextmenu']">
          <a-textarea class="input"
                      v-model:value="chatInput"
                      :bordered="false"
                      :autoSize="false"
                      @pressEnter.prevent="handleSend"
                      ref="chatInputRef"
          ></a-textarea>
          <template #overlay>
            <div class="context-menu-container">
              <div class="context-menu-item"
                   @click="contextMenuOperations.cutChatInput"
              >
                <ScissorOutlined/>
                剪切
              </div>
              <div class="context-menu-item"
                   @click="contextMenuOperations.copyToClipboard(getSelectedContent())"
              >
                <CopyOutlined/>
                复制
              </div>
              <div class="context-menu-item"
                   @click="contextMenuOperations.pasteToChatInput"
              >
                <ReconciliationOutlined/>
                粘贴
              </div>
              <div class="context-menu-item"
                   @click="contextMenuOperations.selectAllInputContent"
              >
                <CheckCircleOutlined/>
                全选
              </div>
            </div>
          </template>
        </a-dropdown>
        <div class="operation-bar">
          <label class="tip-label">按下<code>Enter</code>键以发送</label>
          <div class="btn-container">
            <label class="tip-label"
                   v-show="isAiGenerating"
            >
              <LoadingOutlined style="margin-right: 10px"/>
              小易创作中</label>
            <a-popover v-model:open="writerHelperOpen"
                       placement="topRight"
                       trigger="click"
                       @open-change="aiWriterInput=''"
            >
              <template #title>
                <div style="display: flex;justify-content:space-between;align-items: center">
                  <div style="display: flex;align-items: center;gap: 5px">
                  <span>
                    <AiAssistantIcon :size="20"/>
                  </span>
                    小易帮写
                  </div>
                  <div>
                    <a-button style="border: none;box-shadow: none"
                              shape="circle"
                              @click="closeWriterHelper"
                    >
                      <CloseOutlined/>
                    </a-button>
                  </div>
                </div>
              </template>
              <template #content>
                <div class="ai-writer-container">
                  <a-textarea v-model:value="aiWriterInput"
                              placeholder="提出你的需求，让小易帮你解决吧"
                              :auto-size="{ minRows: 4, maxRows: 4 }"
                  ></a-textarea>
                  <div style="display: flex;justify-content: space-between;align-items: center;width: 100%">
                    <label style="font-size: 12px;color: gray">
                      <WarningOutlined style="margin-right: 2px;color: orange"/>
                      内容由AI生成，仅供参考</label>
                    <a-button type="primary"
                              @click="writerAssistantWork"
                    >开始写作
                    </a-button>
                  </div>
                </div>
              </template>
              <a-button class="btn-ai-writer gradient-background"
                        @click="openWriterHelper"
              >
                <AiWriterIcon :size="18"
                              :color="'#FFFFFF'"
                />
              </a-button>
            </a-popover>
            <a-button type="primary"
                      class="btn-send"
                      @click="handleSend"
            >发送
            </a-button>
          </div>
        </div>
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

  <!--AI助手对话框-->
  <AIResponseResultDialog ref="aiResponseResultDialog"
                          :content="aiResponseResultDialogContent"
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
        CloudSyncOutlined,
        CopyOutlined,
        ExportOutlined,
        DeleteOutlined,
        ScissorOutlined,
        ReconciliationOutlined,
        CheckCircleOutlined,
        CloseOutlined,
        WarningOutlined,
        CheckSquareOutlined,
        LoadingOutlined
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
        openFilePreviewChildWindow,
        openMediaFilePreviewChildWindow
    } from "../../window-controller/controller/ChildWindowController.js";
    import {
        copyToClipboard,
        getSelectedContent,
        readFromClipboard
    } from "../../utils/system-utils.js";
    import { chatInfoObject } from "../../type/type.js";
    import AiWriterIcon from "../icon/AiWriterIcon.vue";
    import AiAssistantIcon from "../icon/AiAssistantIcon.vue";
    import {
        replyHelperWork,
        textReaderHelperWork,
        writerHelperWork
    } from "../../utils/ai-assistant.js";
    import AIResponseResultDialog from "../dialog/ai-assistant/AIResponseResultDialog.vue";

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

    // AI助手对话框控制
    const aiResponseResultDialog = ref();
    const handleAiResponseResultDialogOpen = (title) => {
        aiResponseResultDialog.value.dialogOpen(title);
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
    const chatInputRef = ref(null);
    const selectedEmoji = ref();
    const handleEmojiSelect = (emoji) => {
        chatInput.value += emoji.i;
        selectedEmoji.value = "";
    };
    const isAiGenerating = ref(false);
    const aiWriterInput = ref("");
    const aiResponseResultDialogContent = ref("");

    // 打开小易帮写弹窗
    const writerHelperOpen = ref(false);
    const openWriterHelper = () => {
        writerHelperOpen.value = true;
    };
    const closeWriterHelper = () => {
        writerHelperOpen.value = false;
    };

    // 小易帮写
    const writerAssistantWork = async () => {
        isAiGenerating.value = true;
        writerHelperOpen.value = false;
        const response = await writerHelperWork(aiWriterInput.value);
        aiWriterInput.value = "";
        for await (const data of response) {
            if (data.choices.length !== 0) {
                chatInput.value += data.choices[0].delta.content;
            }
            else {
                message.success("内容生成完成");
                isAiGenerating.value = false;
            }
        }
    };

    // 消息选择
    const isSelectMessageMode = ref(false);
    const selectedChatHistoryRecords = ref([]);
    const getSelectedMessageIdListStr = () => {
        return selectedChatHistoryRecords.value.join(",");
    };
    const handleMessageSelect = (isSelected, recordId) => {
        if (isSelected) {
            selectedChatHistoryRecords.value.push(recordId);
        }
        else {
            for (let i = 0; i < selectedChatHistoryRecords.value.length; i++) {
                if (selectedChatHistoryRecords.value[i] === recordId) {
                    selectedChatHistoryRecords.value.splice(i, 1);
                }
            }
        }
    };

    // 右键菜单操作
    const contextMenuOperations = {
        // 复制内容
        copyToClipboard: (content) => {
            copyToClipboard(content);
        },
        // 粘贴内容
        pasteToChatInput: async () => {
            const content = await readFromClipboard();
            chatInput.value += content;
        },
        // 剪切内容
        cutChatInput: () => {
            const inputElement = document.querySelector("textarea.input");
            const startIndex = inputElement.selectionStart;
            const endIndex = inputElement.selectionEnd;
            const content = chatInput.value.substring(startIndex, endIndex);
            copyToClipboard(content);
            chatInput.value = chatInput.value.substring(0, startIndex) + chatInput.value.substring(endIndex);
        },
        // 全选内容
        selectAllInputContent: () => {
            const inputElement = document.querySelector("textarea.input");
            inputElement.select();
        },
        // 删除选中的消息
        deleteFromChatHistory: async (idListStr) => {
            const idList = idListStr.split(",");
            ChatApi.deleteChatHistory({
                idList: idListStr
            }).then((response) => {
                const res = response.data;
                if (res.code === 0) {
                    for (let i = 0; i < chatHistory.value.length; i++) {
                        if (idList.includes(chatHistory.value[i].id)) {
                            chatHistory.value.splice(i, 1);
                            message.success("删除成功");
                            break;
                        }
                    }
                }
                else {
                    message.error("删除失败");
                }
            });
            // 如果当前处于选择消息模式，从选中列表中删除当前消息
            if (isSelectMessageMode.value && idList.length === 1) {
                for (let i = 0; i < selectedChatHistoryRecords.value.length; i++) {
                    if (selectedChatHistoryRecords.value[i] === idListStr) {
                        selectedChatHistoryRecords.value.splice(i, 1);
                    }
                }
            }
        },
        // 转发消息
        transmitMessage: (originMessage) => {
            const newMessage = {
                id: UUID.generate(),
                sender: curLoginUser.value.userId,
                receiver: "2",
                type: originMessage.type,
                fileId: originMessage.fileId,
                content: originMessage.content,
                time: dayjs().format("YYYY-MM-DD HH:mm:ss")
            };

            websocketStore.sendMessage(newMessage);
            window.dispatchEvent(new CustomEvent("messageSend", {
                detail: newMessage
            }));
            handleSaveChatHistory(newMessage);
            message.success("消息已转发");

            if (newMessage.receiver === props.chatInfo.userId) {
                chatHistory.value.push(newMessage);
                scrollToBottom("smooth");
            }
        },
        // 小易帮阅
        assistantHelperReadText: async (originMessage) => {
            aiResponseResultDialogContent.value = "";
            handleAiResponseResultDialogOpen("小易帮阅");
            const response = await textReaderHelperWork(originMessage);
            for await (const data of response) {
                if (data.choices.length !== 0) {
                    aiResponseResultDialogContent.value += data.choices[0].delta.content;
                }
            }
        },
        // 小易识图
        assistantHelperReadImage: async (originMessage) => {
            aiResponseResultDialogContent.value = "";
            handleAiResponseResultDialogOpen("小易帮阅");
            const response = await textReaderHelperWork(originMessage);
            for await (const data of response) {
                if (data.choices.length !== 0) {
                    aiResponseResultDialogContent.value += data.choices[0].delta.content;
                }
            }
        },
        // 快捷回复
        assistantHelperReply: async (originMessage) => {
            aiResponseResultDialogContent.value = "";
            handleAiResponseResultDialogOpen("快捷回复");
            const response = await replyHelperWork(originMessage);
            for await (const data of response) {
                if (data.choices.length !== 0) {
                    aiResponseResultDialogContent.value += data.choices[0].delta.content;
                }
            }
        },
        // 切换选择消息模式
        switchSelectMessageMode: () => {
            selectedChatHistoryRecords.value = [];
            for (let i = 0; i < chatHistory.value.length; i++) {
                if (chatHistory.value[i].isSelected === true) {
                    chatHistory.value[i].isSelected = false;
                }
            }
            isSelectMessageMode.value = !isSelectMessageMode.value;
        }
    };

    //传入参数
    const props = defineProps({
        chatInfo: {
            type: chatInfoObject
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
            websocketStore.sendMessage(message);
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
                websocketStore.sendMessage(message);

                // 添加文件信息至本地消息记录
                message.fileName = messageItem.fileName;
                message.fileType = messageItem.fileType;
                message.fileSize = messageItem.fileSize;
                chatHistory.value.push(message);

                if (message.type === "image") {
                    await afterImageSendCallBack(message.id);
                }
                else {
                    // 发送消息后滚动到底部
                    scrollToBottom("smooth");
                }

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

    // 图片发送后回调
    const afterImageSendCallBack = async (id) => {
        await nextTick(() => {
            const elementTag = `#${id} img.image`;
            let image;
            try {
                image = document.querySelector(elementTag);
            } catch (error) {
                console.error(error);
            }
            console.error(image);
            if (image) {
                if (image.complete) {
                    scrollToBottom("smooth");
                    return;
                }
                image.onload = () => {
                    scrollToBottom("smooth");
                };
            }
            else {
                setTimeout(() => {
                    afterImageSendCallBack(id);
                }, 300);
            }
        });
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

        await nextTick(() => {
            const images = document.querySelectorAll(".image-message img");
            let loadedCount = 0;
            const totalImages = images.length;

            if (totalImages === 0) {
                scrollToBottom("auto");
            }
            else {
                images.forEach(img => {
                    img.onload = () => {
                        loadedCount++;
                        if (loadedCount === totalImages) {
                            scrollToBottom("auto");
                        }
                    };
                });
            }
        });
    };

    // 处理文件预览
    const handleFilePreview = (fileId, fileType) => {
        const data = {
            fileId: fileId,
            fileType: fileType
        };
        openFilePreviewChildWindow(data);
    };

    // 处理图片预览
    const handleMediaFilePreview = (fileId, fileType) => {
        const data = {
            fileId: fileId,
            fileType: fileType
        };
        openMediaFilePreviewChildWindow(data);
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

      .selected-message {
        background-color: rgba(global-variable.$theme-color, 0.1);
      }

      .message-item {
        display: flex;
        width: 100%;
        height: fit-content;
        margin-top: $message-item-gap;
        padding: 2%;

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

                .image {
                  max-width: 400px;
                  max-height: 400px;
                  border-radius: 15px;
                  cursor: pointer;

                  &:hover {
                    opacity: 0.8;
                  }
                }
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
                max-width: fit-content;

                .image {
                  max-width: 400px;
                  max-height: 400px;
                  border-radius: 15px;
                  cursor: pointer;

                  &:hover {
                    opacity: 0.8;
                  }
                }
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
      border-top: global-variable.$border-line-width solid global-variable.$border-line-color;

      $operations-bar-height: 40px;

      .message-operations {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        gap: 50px;
        height: calc(100% - $operations-bar-height);

        .operation-btn {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          gap: 5px;
          background-color: white;
          width: 70px;
          height: 70px;
          border-radius: 15px;
          cursor: pointer;
          font-size: 14px;
          box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.1);
          transition: all 0.3s ease;

          label {
            cursor: pointer;
          }

          &:hover {
            background-color: global-variable.$theme-color;
            color: white;
          }
        }
      }

      .operations-bar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        height: $operations-bar-height;
        padding: 0 20px;

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
          height: 75%;
          resize: none;
        }

        .operation-bar {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 100%;
          height: 25%;
          padding: 0 8px;

          .tip-label {
            color: gray;
            font-size: 12px;
          }

          .btn-container {
            display: flex;
            align-items: center;
            gap: 10px;

            .btn-ai-writer {
              display: flex;
              justify-content: center;
              align-items: center;
              width: 32px;
              height: 32px;
              padding: 0;

              &:hover {
                border: none;
              }
            }

            .btn-send {
              background-color: global-variable.$theme-color;
            }
          }
        }
      }
    }
  }

  .gradient-background {
    width: 100%;
    height: 100%;
    background: linear-gradient(45deg, #14143a, #7b61ff, #5ce6ff, #5ce6c0);
    background-size: 800% 800%;
    animation: gradientFlow 5s ease infinite;
  }

  @keyframes gradientFlow {
    0% {
      background-position: 0 50%;
    }
    50% {
      background-position: 100% 50%;
    }
    100% {
      background-position: 0 50%;
    }
  }

  .ai-writer-container {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
    width: 300px;
    height: 140px;
  }
</style>