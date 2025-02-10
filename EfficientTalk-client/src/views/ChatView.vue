<template>
  <div class="chat-view-container">
    <div class="chat-list-container">
      <ChatList @set-selected-chat="setSelectedChat"/>
    </div>
    <div class="chat-detail-container">
      <ChatDetail :chat-info="selectedChat"/>
    </div>
  </div>
</template>

<script setup>
    import { ref } from "vue";
    import ChatList from "../components/module-chat/ChatList.vue";
    import ChatDetail from "../components/module-chat/ChatDetail.vue";

    // 选中的聊天
    const selectedChat = ref(null);

    // 设置选中的聊天
    const setSelectedChat = (chat) => {
      selectedChat.value = chat;
    };
</script>

<style scoped
       lang="scss"
>
  .chat-view-container {
    display: flex;
    width: 100%;
    height: 100%;

    $chat-list-container-width: 250px;

    .chat-list-container {
      width: $chat-list-container-width;
      height: 100%;
      border-right: 1px solid rgba(0, 0, 0, 0.1);
    }

    .chat-detail-container {
      width: calc(100% - $chat-list-container-width);
      height: 100%;
    }
  }
</style>