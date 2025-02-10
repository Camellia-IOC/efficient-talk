<template>
  <div class="friend-view-container">
    <div class="friend-list-container">
      <FriendList @set-selected-friend="setSelectedFriend"/>
    </div>
    <div class="friend-detail-container">
      <FriendDetail :friend-info="selectedFriend"/>
    </div>
  </div>
</template>

<script setup>
    import { ref } from "vue";
    import FriendList from "../components/module-contacts/FriendList.vue";
    import FriendDetail from "../components/module-contacts/FriendDetail.vue";

    // 选中的朋友
    const selectedFriend = ref(null);

    // 设置选中的朋友
    const setSelectedFriend = (chat) => {
        selectedFriend.value = chat;
    };
</script>

<style scoped
       lang="scss"
>
  .friend-view-container {
    display: flex;
    width: 100%;
    height: 100%;

    $friend-list-container-width: 250px;

    .friend-list-container {
      width: $friend-list-container-width;
      height: 100%;
      border-right: 1px solid rgba(0, 0, 0, 0.1);
    }

    .friend-detail-container {
      width: calc(100% - $friend-list-container-width);
      height: 100%;
    }
  }
</style>