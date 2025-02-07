<template>
  <div class="chat-view-container">
    <div class="chat-list-container">
      <ChatList/>
    </div>
    <div class="chat-detail-container">
      <ChatDeail/>
    </div>
  </div>
</template>

<script setup>

import ChatList from "../components/module-chat/ChatList.vue";
import ChatDeail from "../components/module-chat/ChatDeail.vue";
</script>

<style scoped
       lang="scss"
>
  .chat-view-container {
    display: flex;
    width: 100%;
    height: 100%;

    $chat-list-container-width: 250px;

    .chat-list-container{
      width: $chat-list-container-width;
      height: 100%;
    }

    .chat-detail-container{
      width: calc(100% - $chat-list-container-width);
      height: 100%;
    }
  }
</style>