<template>
  <div class="chat-list">
    <div v-for="(item,index) in chatList"
         :key="index"
         class="chat-list-item"
    >
      <div class="item-avatar">
        <img class="avatar"
             :src="item.avatar"
             alt=""
        />
      </div>
      <div class="item-detail">
        <div class="detail-header">
          <div class="username">{{ item.name }}</div>
          <div class="time">{{ item.lastMessageTime }}</div>
        </div>
        <div class="detail-content">
          <div class="detail-message"
               :class="{ 'detail-message-unread': item.unreadCount!==0 }"
          >{{ item.lastMessage }}
          </div>
          <div class="unread-count"
               v-if="item.unreadCount!==0"
          >
            <div class="unread-count-badge"
                 style="font-size: 7px"
                 v-if="item.unreadCount>99"
            >
              99+
            </div>
            <div class="unread-count-badge"
                 v-else
            >
              {{ item.unreadCount }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
    import { ref } from "vue";

    const chatList = ref([
        {
            name: "Chat 1",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "Hello, how are you?",
            lastMessageTime: "2023-07-01",
            unreadCount: 2,
        },
        {
            name: "Chat 2",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "I'm good, thanks!",
            lastMessageTime: "2023-07-01",
            unreadCount: 100,
        },
        {
            name: "Chat 3",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "What's up?",
            lastMessageTime: "2023-07-01",
            unreadCount: 99,
        },
        {
            name: "Chat 4",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "How are you doing?",
            lastMessageTime: "2023-07-01",
            unreadCount: 0,
        },
        {
            name: "Chat 5",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "Hey!",
            lastMessageTime: "2023-07-01",
            unreadCount: 0,
        },
        {
            name: "Chat 6",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "Good morning!",
            lastMessageTime: "2023-07-01",
            unreadCount: 0,
        },
        {
            name: "Chat 7",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "What's new?",
            lastMessageTime: "2023-07-01",
            unreadCount: 0,
        },
        {
            name: "Chat 8",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "Hello there!",
            lastMessageTime: "2023-07-01",
            unreadCount: 0,
        },
        {
            name: "Chat 9",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "Hi!",
            lastMessageTime: "2023-07-01",
            unreadCount: 0,
        },
        {
            name: "Chat 10",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "Good afternoon!",
            lastMessageTime: "2023-07-01",
            unreadCount: 999,
        },
        {
            name: "Chat 11",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "Good evening!",
            lastMessageTime: "2023-07-01",
            unreadCount: 0,
        },
        {
            name: "Chat 12",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "How's it going?",
            lastMessageTime: "2023-07-01",
            unreadCount: 0,
        },
        {
            name: "Chat 13",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "What's up?",
            lastMessageTime: "2023-07-01",
            unreadCount: 0,
        },
        {
            name: "Chat 14",
            avatar: "https://avatars.githubusercontent.com/u/123456789?v=4",
            lastMessage: "Good night!",
            lastMessageTime: "2023-07-01",
            unreadCount: 0,
        },
    ]);
</script>

<style scoped
       lang="scss"
>
  .chat-list {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    overflow-y: auto;

    .chat-list-item {
      display: flex;
      align-items: center;
      width: 100%;
      min-height: 75px;
      padding: 0 15px;

      $item-avatar-size: 40px;

      .item-avatar {
        display: flex;
        justify-content: center;
        align-items: center;
        width: $item-avatar-size;
        height: $item-avatar-size;
        margin-right: 15px;

        .avatar {
          width: 100%;
          height: 100%;
          border-radius: 50%;
        }
      }

      .item-detail {
        display: flex;
        flex-direction: column;
        width: calc(100% - #{$item-avatar-size} - 15px);

        .detail-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 100%;

          .username {
            font-size: 14px;
          }

          .time {
            font-size: 10px;
            color: gray;
          }
        }

        .detail-content {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 100%;

          .detail-message {
            font-size: 12px;
            color: gray;
            width: 100%;
          }

          .detail-message-unread {
            width: 90%;
          }

          .unread-count {
            width: 10%;

            .unread-count-badge {
              display: flex;
              justify-content: center;
              align-items: center;
              width: 16px;
              height: 16px;
              border-radius: 50%;
              background-color: red;
              font-size: 9px;
              color: white;
            }
          }
        }
      }

      &:hover {
        background-color: rgba(0, 0, 0, 0.1);
        cursor: pointer;
      }
    }
  }
</style>