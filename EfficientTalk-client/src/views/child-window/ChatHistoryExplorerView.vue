<template>
  <div class="chat-history-explorer">
    <div class="search-area">
      <a-input placeholder="搜索"
               v-model:value="searchKey"
      />
    </div>
    <div class="result-area">
      <a-tabs v-model:activeKey="activeKey"
              class="tabs-container"
              @change="handleActiveKeyChange"
      >
        <a-tab-pane key="all"
                    tab="全部"
        >
          <div class="tab-content">
            <EmptyContainer v-if="chatHistoryAllList.length === 0"
                            :description="'暂无相关聊天记录'"
            />
            <div class="chat-history-record-item"
                 v-for="(item,index) in chatHistoryAllList"
                 :key="index"
                 v-else
            >
              <div class="message-item">
                <div class="message-avatar">
                  <img class="avatar"
                       :src="getUserAvatar(item.sender)"
                       alt="avatar"
                  >
                </div>
                <div class="message-info">
                  <div class="user-name">
                    {{ getUserName(item.sender) }}
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
                      >
                        <template #previewMask>
                          预览
                        </template>
                      </a-image>
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
            </div>
            <a-button @click="getChatHistory"
                      type="link"
                      v-if="chatHistoryAllList.length !== 0 && allLastCount === pageSize"
            >加载更多
            </a-button>
          </div>
        </a-tab-pane>
        <a-tab-pane key="image/video"
                    tab="图片/视频"
        >
          <div class="tab-content">
            <EmptyContainer v-if="chatHistoryImageList.length === 0"
                            :description="'暂无相关聊天记录'"
            />
            <div class="image-grid-container"
                 v-else
            >
              <a-image v-for="(item,index) in chatHistoryImageList"
                       :key="index"
                       :src="item.content"
              >
                <template #previewMask>
                  预览
                </template>
              </a-image>
            </div>
            <a-button @click="getChatHistory"
                      type="link"
                      v-if="chatHistoryImageList.length !== 0 && imageLastCount === pageSize"
            >加载更多
            </a-button>
          </div>
        </a-tab-pane>
        <a-tab-pane key="file"
                    tab="文件"
        >
          <div class="tab-content">
            <EmptyContainer v-if="chatHistoryFileList.length === 0"
                            :description="'暂无相关聊天记录'"
            />
            <div class="chat-history-file-item"
                 v-for="(item,index) in chatHistoryFileList"
                 :key="index"
                 v-else
            >
              <div class="file-info">
                <div class="file-icon">
                  <img class="icon"
                       :src="getFileIcon(item.fileType)"
                       alt="icon"
                  />
                </div>
                <div class="file-detail">
                  <div class="file-name">{{ item.fileName + "." + item.fileType }}</div>
                  <div class="user-info">
                    <img :src="getUserAvatar(item.sender)"
                         alt="avatar"
                         class="user-avatar"
                    />
                    <label class="user-name">{{ getUserName(item.sender) }}</label>
                    <label class="time">{{ formatDate(item.time) }}</label>
                  </div>
                </div>
              </div>
              <div class="operation-bar">
                <a-tag>{{ translateFileSize(item.fileSize) }}</a-tag>
                <a-button class="operation-btn"
                >
                  <DownloadOutlined/>
                </a-button>
              </div>
            </div>
            <a-button @click="getChatHistory"
                      type="link"
                      v-if="chatHistoryFileList.length !== 0 && fileLastCount === pageSize"
            >加载更多
            </a-button>
          </div>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref,
        watch
    } from "vue";
    import { useRoute } from "vue-router";
    import ChatApi from "../../api/modules/ChatApi.js";
    import UserApi from "../../api/modules/UserApi.js";
    import {
        formatMessageTime
    } from "../../utils/time-utils.js";
    import { getFileIcon } from "../../utils/file-utils.js";
    import { translateFileSize } from "../../utils/unit-utils.js";
    import { formatDate } from "../../utils/time-utils.js";
    import {
        DownloadOutlined
    } from "@ant-design/icons-vue";
    import EmptyContainer from "../../components/empty-container/EmptyContainer.vue";
    import { message } from "ant-design-vue";

    const route = useRoute();

    // 检索参数配置
    const user = ref({
        userId: null,
        userName: null,
        userAvatar: null
    });
    const friend = ref({
        friendId: null,
        friendName: null,
        friendAvatar: null
    });
    const searchKey = ref("");
    const activeKey = ref("all");
    const pageSize = 20;

    // 聊天记录列表
    const chatHistoryAllList = ref([]);
    const chatHistoryImageList = ref([]);
    const chatHistoryFileList = ref([]);
    const allLastCount = ref(0);
    const imageLastCount = ref(0);
    const fileLastCount = ref(0);

    const getUserAvatar = (userId) => {
        if (userId === user.value.userId) {
            return user.value.userAvatar;
        }
        else {
            return friend.value.friendAvatar;
        }
    };
    const getUserName = (userId) => {
        if (userId === user.value.userId) {
            return user.value.userName;
        }
        else {
            return friend.value.friendName;
        }
    };

    const handleActiveKeyChange = (key) => {
        activeKey.value = key;
        if (activeKey.value === "all") {
            if (chatHistoryAllList.value.length === 0) {
                getChatHistory();
            }
        }
        else if (activeKey.value === "image/video") {
            if (chatHistoryImageList.value.length === 0) {
                getChatHistory();
            }
        }
        else if (activeKey.value === "file") {
            if (chatHistoryFileList.value.length === 0) {
                getChatHistory();
            }
        }
    };

    // 获取历史记录
    const getChatHistory = async () => {
        let lastTime = null;
        if (activeKey.value === "all" && chatHistoryAllList.value.length > 0) {
            lastTime = chatHistoryAllList.value[chatHistoryAllList.value.length - 1].time;
        }
        else if (activeKey.value === "image/video" && chatHistoryImageList.value.length > 0) {
            lastTime = chatHistoryImageList.value[chatHistoryImageList.value.length - 1].time;
        }
        else if (activeKey.value === "file" && chatHistoryFileList.value.length > 0) {
            lastTime = chatHistoryFileList.value[chatHistoryFileList.value.length - 1].time;
        }

        const response = await ChatApi.getChatHistoryByType({
            userId: user.value.userId,
            friendId: friend.value.friendId,
            pageSize: pageSize,
            type: activeKey.value,
            searchKey: searchKey.value === "" ? null : searchKey.value,
            lastTime: lastTime
        });

        const res = response.data;
        if (res.code === 0) {
            if (res.data != null) {
                if (activeKey.value === "all") {
                    allLastCount.value = res.data.chatHistory.length;
                    chatHistoryAllList.value = chatHistoryAllList.value.concat(res.data.chatHistory);
                    if(res.data.chatHistory.length === 0 && chatHistoryAllList.value.length !== 0){
                        message.warning("没有更多消息了")
                    }
                }
                else if (activeKey.value === "image/video") {
                    imageLastCount.value = res.data.chatHistory.length;
                    chatHistoryImageList.value = chatHistoryImageList.value.concat(res.data.chatHistory);
                    if(res.data.chatHistory.length === 0 && chatHistoryImageList.value.length !== 0){
                        message.warning("没有更多消息了")
                    }
                }
                else if (activeKey.value === "file") {
                    fileLastCount.value = res.data.chatHistory.length;
                    chatHistoryFileList.value = chatHistoryFileList.value.concat(res.data.chatHistory);
                    if(res.data.chatHistory.length === 0 && chatHistoryFileList.value.length !== 0){
                        message.warning("没有更多消息了")
                    }
                }
            }
        }
    };

    // 更新相关用户信息
    const updateUserInfo = async (dataJson) => {
        const data = JSON.parse(dataJson);
        user.value.userId = data.userId;
        friend.value.friendId = data.friendId;

        let response;
        response = await UserApi.getUserBasicInfo({
            userId: user.value.userId
        });
        if (response.data.code === 0) {
            const data = response.data.data;
            if (data != null) {
                user.value.userName = data.userName;
                user.value.userAvatar = data.userAvatar;
            }
        }
        response = await UserApi.getUserBasicInfo({
            userId: friend.value.friendId
        });
        if (response.data.code === 0) {
            const data = response.data.data;
            if (data != null) {
                friend.value.friendName = data.userName;
                friend.value.friendAvatar = data.userAvatar;
            }
        }
    };

    watch(() => searchKey.value, () => {
        if (activeKey.value === "all") {
            chatHistoryAllList.value = [];
        }
        else if (activeKey.value === "image/video") {
            chatHistoryImageList.value = [];
        }
        else if (activeKey.value === "file") {
            chatHistoryFileList.value = [];
        }

        getChatHistory();
    });

    onBeforeMount(() => {
        updateUserInfo(route.query.data);

        // 获取历史记录
        getChatHistory();
    });
</script>

<style scoped
       lang="scss"
>
  @use "../../assets/style/global-variable";

  .chat-history-explorer {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    padding: 20px;

    $search-area-height: 40px;

    .search-area {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: $search-area-height;
    }

    .result-area {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: calc(100% - $search-area-height);

      .tabs-container {
        width: 99%;
        height: 100%;

        .tab-content {
          display: flex;
          flex-direction: column;
          align-items: center;
          width: 100%;
          height: 100%;
          overflow-y: auto;
          gap: 10px;

          .chat-history-record-item {
            display: flex;
            justify-content: flex-start;
            width: 99%;
            height: fit-content;
            padding: 10px 0;
            margin-right: 1%;
            border-bottom: global-variable.$border-line-width solid global-variable.$border-line-color;

            $avatar-container-width: 60px;
            $message-user-name-size: 12px;
            $message-element-gap: 8px;
            $message-content-max-width: 80%;
            $message-content-horizontal-padding: 10px;
            $message-content-vertical-padding: 6px;

            .message-item {
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
          }

          .image-grid-container {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
            grid-column-gap: 40px;
            grid-row-gap: 20px;
            width: 99%;
            margin-right: 1%;
          }

          .chat-history-file-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 99%;
            height: 90px;
            min-height: 90px;
            border-radius: 15px;
            background-color: #F5F7FA;
            margin-right: 1%;
            padding: 0 20px;

            .file-info {
              display: flex;
              justify-content: flex-start;
              align-items: center;
              width: 80%;
              height: 100%;
              gap: 20px;

              .file-icon {
                display: flex;
                justify-content: center;
                align-items: center;
                width: 65px;
                height: 100%;

                .icon {
                  width: 65px;
                  height: 65px;
                  background-color: white;
                  padding: 5px;
                  border-radius: 10px;
                }
              }

              .file-detail {
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 10px;
                width: calc(100% - 65px);

                .file-name {
                  display: flex;
                  justify-content: flex-start;
                  align-items: center;
                  font-size: 16px;
                  font-weight: bold;
                  text-align: left;
                  width: 100%;
                }

                .user-info {
                  display: flex;
                  justify-content: flex-start;
                  align-items: center;
                  width: 100%;
                  gap: 15px;

                  .user-avatar {
                    width: 20px;
                    height: 20px;
                    border-radius: 50%;
                  }

                  .user-name {
                    font-size: 14px;
                  }

                  .time {
                    color: gray;
                    font-size: 14px;
                  }
                }
              }
            }

            .operation-bar {
              display: flex;
              justify-content: flex-end;
              align-items: center;
              width: 20%;
              height: 100%;

              .operation-btn {
                display: flex;
                justify-content: center;
                align-items: center;
                width: 30px;
                height: 30px;
                border-radius: 50%;
                border: none;
              }
            }
          }
        }
      }
    }
  }

  :deep(.ant-tabs-nav) {
    width: 100%;
    height: 45px !important;
  }

  :deep(.ant-tabs-content-holder) {
    width: 100%;
    height: calc(100% - 45px) !important;
  }

  :deep(.ant-tabs-content) {
    width: 100%;
    height: 100% !important;
  }

  :deep(.ant-tabs-tabpane) {
    width: 100%;
    height: 100% !important;
    max-height: 100% !important;
  }
</style>