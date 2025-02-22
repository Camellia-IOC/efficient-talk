<template>
  <div class="chat-view-container">
    <div class="chat-list-container">
      <ChatList @set-selected-chat="setSelectedChat"
                :friend-info="friendInfo"
      />
    </div>
    <div class="chat-detail-container">
      <ChatDetail :chat-info="selectedChat"/>
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

    const route = useRoute();

    // 选中的聊天
    const selectedChat = ref(null);
    const friendInfo = ref(null);

    // 设置选中的聊天
    const setSelectedChat = (chat) => {
        selectedChat.value = chat;
    };

    onBeforeMount(() => {
        if (route.query.friendInfo !== undefined && route.query.friendInfo !== null) {
            friendInfo.value = JSON.parse(route.query.friendInfo);
            if (friendInfo.value !== null && friendInfo.value !== undefined) {
                selectedChat.value = {
                    userId: friendInfo.value.userId,
                    userName: friendInfo.value.userName,
                    userAvatar: friendInfo.value.userAvatar
                };
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

    $chat-list-container-width: 250px;

    .chat-list-container {
      width: $chat-list-container-width;
      height: 100%;
      border-right: global-variable.$border-line-width solid global-variable.$border-line-color;
    }

    .chat-detail-container {
      width: calc(100% - $chat-list-container-width);
      height: 100%;
    }
  }
</style>