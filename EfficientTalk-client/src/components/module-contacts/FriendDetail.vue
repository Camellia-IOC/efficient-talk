<template>
  <div>
    <a-button @click="handleGoChat"></a-button>
  </div>
</template>

<script setup>
    import { ref } from "vue";
    import { useRouter } from "vue-router";

    const router = useRouter();

    const props = defineProps({
        friendId: {
            type: String,
            default: "3"
        }
    });

    // 好友信息
    const friendInfo = ref({
        userId: props.friendId,
        userName: "测试3",
        userAvatar: ""
    });

    const handleGoChat = () => {
        window.dispatchEvent(new CustomEvent("navForceChange", {
            detail: "chat"
        }));
        router.push({
            name: "chat",
            query: {
                friendInfo: JSON.stringify(friendInfo.value)
            }
        });
    };
</script>

<style scoped
       lang="scss"
>

</style>