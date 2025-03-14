<template>
  <div>
    <a-button @click="openNotification">111</a-button>
  </div>
</template>

<script setup>
    import {
        notification
    } from "ant-design-vue";
    import {
        onBeforeMount,
        onMounted,
        ref
    } from "vue";
    import { remote } from "electron";

    // 系统通知项ID
    const notificationKey = "systemNotification";

    // 通知内容
    const notificationContent = ref();

    const openNotification = (placement) => {
        notification.open({
            notificationKey,
            message: "Notification Title",
            description:
                "This is the content of the notification. This is the content of the notification. This is the content of the notification.",
            onClick: () => {
                console.log("Notification Clicked!");
            },
            placement: placement,
            style: {
                height: "150px",
            }
        });
    };

    const openSystemNotification = (placement = "bottomRight", notification) => {
        openSystemNotification(placement);

        setTimeout(() => {
            openSystemNotification(placement);
        }, 1000);
    };
</script>

<style scoped
       lang="scss"
>
  .clickable-element {
    pointer-events: auto;
  }
</style>