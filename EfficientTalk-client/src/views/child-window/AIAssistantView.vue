<template>
  <div class="ai-assistant-view-container">
    <div class="ai-chat-container"
         ref="aiChatHistoryElement"
    >
      <div class="empty-list-container"
           v-if="chatMessageHistory.length === 1"
      >
        <div>
          <svg class="icon"
               viewBox="0 0 1105 1024"
               xmlns="http://www.w3.org/2000/svg"
               width="128"
               height="128"
          >
            <path d="M306.332 986.12c20.686 4.405 45.066-16.22 63.943-54.187s29.408-87.338 27.6-129.759-15.672-71.118-36.358-75.522-45.067 16.22-63.944 54.187-29.408 87.338-27.599 129.759 15.659 71.118 36.358 75.535z m412.572-117.532c9.856 46.352 29.944 86.827 52.852 106.19s44.916 14.661 57.917-12.326 14.91-72.154 4.99-118.53-29.944-86.814-52.851-106.178-44.917-14.66-57.918 12.314-14.972 72.166-4.99 118.53z"
                  fill="#DEE3F9"
            ></path>
            <path d="M547.734 363.75h15.296q204.858 0 204.858 204.845v248.664q0 204.845-204.858 204.845h-15.296q-204.833 0-204.845-204.845V568.595q0-204.832 204.845-204.845z"
                  fill="#D4E0F4"
            ></path>
            <path d="M513.896 523.603a36.707 36.707 0 1 0 36.707-36.706 36.707 36.707 0 0 0-36.707 36.706z"
                  fill="#00FFFF"
            ></path>
            <path d="M179.979 433.62c0 72.578-45.59 131.432-101.824 131.432S0 506.198 0 433.62s21.91-131.443 78.155-131.443S179.979 361.03 179.979 433.62z m743.62 0c0 72.578 45.59 131.432 101.823 131.432s78.155-58.854 78.155-131.432-21.897-131.443-78.155-131.443S923.6 361.03 923.6 433.62z"
                  fill="#ECEFFF"
            ></path>
            <path d="M97.095 385.535c0 212.93 203.572 385.534 454.694 385.534s454.681-172.617 454.681-385.534S802.898 0 551.789 0 97.095 172.605 97.095 385.535z"
                  fill="#ECEFFF"
            ></path>
            <path d="M551.789 657.405c-166.105 0-267.18-26.526-318.035-83.507-46.813-52.403-46.676-124.769-46.676-201.264v-8.884c0-94.013 22.645-160.016 71.218-207.715 59.04-57.83 154.925-85.94 293.493-85.94 136.684 0 232.07 28.11 291.659 85.916 49.146 47.649 73.039 115.647 73.039 207.74 0 9.469 0 18.951 0.187 28.247 0.724 72.528 1.435 140.988-46.963 189.91-50.98 51.504-152.018 75.497-317.922 75.497z"
            ></path>
            <path d="M551.789 649.92c-163.846 0-263.125-25.74-312.446-81-44.779-50.12-44.779-120.789-44.767-195.6v-9.57c0-91.854 21.91-156.16 68.96-202.374 57.568-56.383 151.856-83.795 288.215-83.795 134.625 0 228.327 27.45 286.444 83.807 47.624 46.164 70.768 112.367 70.768 202.362 0 9.358 0 19.027 0.188 28.41 0.71 70.88 1.385 137.82-44.805 184.483-49.446 49.994-148.774 73.276-312.557 73.276z"
                  fill="#331083"
            ></path>
            <path d="M352.858 354.28c0 30.456 10.156 58.641 26.638 73.838s36.807 15.234 53.288 0 26.639-43.382 26.639-73.838-10.144-58.641-26.639-73.838-36.806-15.234-53.288 0-26.638 43.382-26.638 73.838z m300.754 0c0 47.088 23.856 85.254 53.289 85.254s53.276-38.166 53.276-85.254-23.843-85.254-53.276-85.254-53.289 38.167-53.289 85.254z m-64.817 146.828a10.493 10.493 0 0 0-9.981 7.573 29.583 29.583 0 0 1-56.508 0 10.468 10.468 0 0 0-9.982-7.573h-0.686a10.318 10.318 0 0 0-9.981 13.138 50.88 50.88 0 0 0 97.656 0 10.318 10.318 0 0 0-9.981-13.138z"
                  fill="#00FFFF"
            ></path>
            <path d="M465.386 880.017a89.996 89.996 0 1 0 89.984-89.995 89.983 89.983 0 0 0-89.984 89.995z"
                  fill="#5415E8"
            ></path>
            <path d="M485.587 880.017a71.056 71.056 0 1 0 35.521-61.536 71.118 71.118 0 0 0-35.521 61.536z"
                  fill="#5415E8"
            ></path>
            <path d="M529.218 895.426l-3.743 8.098a9.22 9.22 0 0 1-8.335 5.29h-9.145l10.817-21.523c3.032-6.089 6.139-12.227 9.27-18.453s5.815-11.579 8.098-16.108a14.798 14.798 0 0 1 13.213-8.234h7.074l1.573 6.6 3.743 15.708c1.434 6.239 2.932 12.477 4.391 18.803s2.783 11.628 3.818 15.983a5.814 5.814 0 0 1-1.16 4.99 5.59 5.59 0 0 1-4.566 2.209h-7.674l-2.495-13.388z m17.83-39.04l-13.376 28.56h18.803z m30.78 52.528l8.272-58.504a6.613 6.613 0 0 1 2.37-4.292 6.8 6.8 0 0 1 4.567-1.66h7.786l-8.098 57.481a8.023 8.023 0 0 1-8.022 7.1z"
                  fill="#FFFFFF"
            ></path>
          </svg>
        </div>
        <label>你好，我是小易，有什么问题尽管问我</label>
      </div>
      <div class="chat-message-list-item"
           v-for="(item,index) in chatMessageHistory"
           :key="index"
           v-else
      >
        <div v-if="item.role==='assistant'"
             class="assistant-message-item"
        >
          <div class="message-avatar">
            <img class="avatar"
                 :src="'/logo.svg'"
                 alt="avatar"
            >
          </div>
          <div class="message-content markdown-body"
               v-html="marked(item.content)"
               v-highlight
          >
          </div>
        </div>
        <div v-else-if="item.role==='user'"
             class="user-message-item"
        >
          <div class="message-content markdown-body"
               v-html="marked(item.content)"
               v-highlight
          >
          </div>
          <div class="message-avatar">
            <img class="avatar"
                 :src="curLoginUserStore.curLoginUser.userAvatar"
                 alt="avatar"
            >
          </div>
        </div>
      </div>
    </div>
    <div class="question-input-container">
      <a-textarea class="question-input"
                  placeholder="输入你的问题，让小易帮你解答吧"
                  v-model:value="questionInput"
                  :auto-size="{ minRows: 3, maxRows: 3 }"
      />
      <div class="operation-bar">
        <a-button type="primary"
                  @click="sendQuestion"
        >发送
        </a-button>
      </div>
    </div>
  </div>
</template>

<script setup>
    import {
        nextTick,
        onBeforeMount,
        ref
    } from "vue";
    import {
        askAiQuestion
    } from "../../utils/ai-assistant.js";
    import { marked } from "marked";
    import { UUID } from "uuidjs";
    import { getCurrentTime } from "../../utils/time-utils.js";
    import AiApi from "../../api/modules/AiApi.js";
    import { useCurLoginUserStore } from "../../store/CurLoginUserStore.js";

    // 当前登录的用户信息
    const curLoginUserStore = useCurLoginUserStore();

    // 对话历史容器
    const aiChatHistoryElement = ref();

    const questionInput = ref("");
    const chatMessageHistory = ref([]);
    const curSessionId = ref("1");

    // 对话框滚动到底部
    const scrollToBottom = (animation) => {
        nextTick(() => {
            if (aiChatHistoryElement.value) {
                aiChatHistoryElement.value.scrollTo({
                    top: aiChatHistoryElement.value.scrollHeight,
                    behavior: animation,
                });
            }
        });
    };

    // 获取对话历史
    const getChatMessageHistory = async () => {
        const response = await AiApi.getAiChatHistory({
            sessionId: curSessionId.value
        });

        const res = response.data;
        if (res.code === 0) {
            chatMessageHistory.value = res.data.recordList;
            chatMessageHistory.value.unshift({
                role: "system",
                content: "你的名字是小易，请你以后都以小易自称"
            });
        }

        scrollToBottom("auto");
    };

    // 保存对话记录
    const saveChatMessageHistory = (message) => {
        AiApi.saveAiChatHistory(message).then(() => {
            console.log("保存对话记录成功");
        });
    };

    // 发送问题
    const sendQuestion = async () => {
        const message = {
            id: UUID.generate(),
            session: curSessionId.value,
            userId: curLoginUserStore.curLoginUser.userId,
            role: "user",
            content: questionInput.value,
            time: getCurrentTime()
        };
        chatMessageHistory.value.push(message);
        saveChatMessageHistory(message);
        questionInput.value = "";
        const response = await askAiQuestion(chatMessageHistory.value);
        const answer = {
            id: UUID.generate(),
            session: curSessionId.value,
            userId: null,
            role: "assistant",
            content: "",
            time: getCurrentTime()
        };
        chatMessageHistory.value.push(answer);
        for await (const data of response) {
            if (data.choices.length !== 0) {
                chatMessageHistory.value[chatMessageHistory.value.length - 1].content += data.choices[0].delta.content;
                scrollToBottom("smooth");
            }
        }
        saveChatMessageHistory(answer);
    };

    onBeforeMount(async () => {
        curSessionId.value = curLoginUserStore.curLoginUser.userId;
        await getChatMessageHistory();
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .ai-assistant-view-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: global-variable.$background-color;

    $question-input-container-height: 160px;

    .ai-chat-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: calc(100% - #{$question-input-container-height});
      overflow-y: auto;

      .empty-list-container {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 100%;
        gap: 30px;
      }

      .chat-message-list-item {
        display: flex;
        align-items: center;
        width: 100%;
        height: fit-content;
        padding: 0 20px;
        margin: 10px 0;

        $avatar-size: 50px;

        .assistant-message-item {
          display: flex;
          justify-content: flex-start;
          width: 100%;

          .message-avatar {
            display: flex;
            justify-content: flex-start;
            width: 65px;

            .avatar {
              width: $avatar-size;
              height: $avatar-size;
              border-radius: 50%;
            }
          }

          .message-content {
            display: flex;
            flex-direction: column;
            width: fit-content;
            max-width: calc(100% - 130px);
            height: fit-content;
            padding: 8px 16px;
            border-radius: 10px;
            background-color: white;
          }
        }

        .user-message-item {
          display: flex;
          justify-content: flex-end;
          width: 100%;

          .message-avatar {
            display: flex;
            justify-content: flex-end;
            width: 65px;

            .avatar {
              width: $avatar-size;
              height: $avatar-size;
              border-radius: 50%;
            }
          }

          .message-content {
            display: flex;
            justify-content: center;
            align-items: center;
            width: fit-content;
            max-width: calc(100% - 130px);
            height: fit-content;
            padding: 8px 15px;
            border-radius: 10px;
            background-color: global-variable.$theme-color;
            color: white;
          }
        }
      }
    }

    .question-input-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: calc(100% - 40px);
      height: $question-input-container-height - 30px;
      margin: 15px 20px;
      padding: 10px;
      justify-content: space-between;
      background-color: white;
      border-radius: 10px;

      .question-input {
        border: none;

        &:focus {
          border: none;
          box-shadow: none;
        }
      }

      .operation-bar {
        display: flex;
        justify-content: flex-end;
        align-items: center;
        width: 100%;
      }
    }
  }
</style>