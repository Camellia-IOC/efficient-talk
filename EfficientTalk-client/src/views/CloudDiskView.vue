<template>
  <div class="cloud-disk-view-container">
    <div class="nav-bar">

    </div>
    <div class="content-container">
      <RouterView/>
    </div>
  </div>
</template>

<script setup>
    import {ref} from "vue";

    const navItemList = [
        {
            index: 0,
            name: "我的文件",
            path: "/app/cloud-disk/my-files"
        },
        {
            index: 1,
            name: "组织文件",
            path: "/app/cloud-disk/org-files"
        },
        {
            index: 2,
            name: "聊天文件",
            path: "/app/cloud-disk/chat-files"
        }
    ]
</script>

<style scoped
       lang="scss"
>
    .cloud-disk-view-container{
      display: flex;
      align-items: center;
      width: 100%;
      height: 100%;

      .nav-bar{
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 250px;
        height: 100%;
      }

      .content-container{
        display: flex;
        align-items: center;
        width: calc(100% - 250px);
        height: 100%;
      }
    }
</style>