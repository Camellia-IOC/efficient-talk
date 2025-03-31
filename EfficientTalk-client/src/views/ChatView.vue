<template>
  <div class="chat-view-container">
    <div class="chat-list-container">
      <ChatList ref="chatListRef"
                @set-selected-chat="setSelectedChat"
                :friend-info="friendInfo"
      />
    </div>
    <div class="chat-detail-container">
      <ChatDetail :chat-info="selectedChat"
                  @remove-chat-list-item="handleChatListRemove"
      />
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import ChatList from "../components/module-chat/ChatList.vue";
    import ChatDetail from "../components/module-chat/ChatDetail.vue";
    import { useRoute } from "vue-router";
    import { useChatDataStore } from "../store/ChatDataStore.js";

    const route = useRoute();

    const chatDataStore = useChatDataStore();

    // 组件Ref
    const chatListRef = ref();
    const handleChatListRemove = (userId) => {
        chatListRef.value.deleteFromChatList(userId);
    };

    // 选中的聊天
    const selectedChat = ref(chatDataStore.curSelectedChatInfo);
    const friendInfo = ref(null);

    // 设置选中的聊天
    const setSelectedChat = (chat) => {
        selectedChat.value = chat;
        chatDataStore.curSelectedChatInfo = selectedChat.value;
    };

    onBeforeMount(() => {
        if (route.query.friendInfo !== undefined && route.query.friendInfo !== null) {
            friendInfo.value = JSON.parse(route.query.friendInfo);
            if (friendInfo.value !== null && friendInfo.value !== undefined) {
                selectedChat.value = {
                    userId: friendInfo.value.userId,
                    userName: friendInfo.value.userName,
                    userAvatar: friendInfo.value.userAvatar,
                    isGroup: friendInfo.value.isGroup
                };
                chatDataStore.curSelectedChatInfo = selectedChat.value;
            }
        }
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .chat-view-container {
    display: flex;
    width: 100%;
    height: 100%;
    border-top: global-variable.$border-line-width solid global-variable.$border-line-color;
    background-color: global-variable.$background-color;

    $chat-list-container-width: 250px;

    .chat-list-container {
      width: $chat-list-container-width;
      height: 100%;
      border-right: global-variable.$border-line-width solid global-variable.$border-line-color;
      background-color: transparent;
    }

    .chat-detail-container {
      width: calc(100% - $chat-list-container-width);
      height: 100%;
      background-color: transparent;
    }
  }
</style>