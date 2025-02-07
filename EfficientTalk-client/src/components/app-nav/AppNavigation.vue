<template>
  <div class="app-nav-container theme-background-color">
    <div class="nav-main">
      <div class="user-info-container">
        <div class="user-avatar"></div>
      </div>
      <div v-for="(item,index) in navConfig"
           :key="index"
           class="nav-item"
           :class="{ 'nav-item-active': selectedNavIndex === item.index }"
           @click="handleNavChange(item.index, item.path)"
      >
        <div class="nav-item-icon">
          <component :is="item.iconActive"
                     v-if="selectedNavIndex === item.index"
          />
          <component :is="item.icon"
                     v-else
          />
        </div>
      </div>
    </div>
    <div class="nav-footer">

    </div>
  </div>
</template>

<script setup>
    import { ref } from "vue";
    import {
        MessageOutlined,
        MessageFilled,
        SmileOutlined,
        SmileFilled,
        CloudFilled,
        CloudOutlined,
        AppstoreOutlined,
        AppstoreFilled
    } from "@ant-design/icons-vue";
    import { useRouter } from "vue-router";

    const router = useRouter();

    // 导航配置
    const navConfig = [
        {
            name: "消息",
            index: 0,
            icon: MessageOutlined,
            iconActive: MessageFilled,
            path: "/app/chat"
        },
        {
            name: "通讯录",
            index: 1,
            icon: SmileOutlined,
            iconActive: SmileFilled,
            path: "/app/contacts"
        },
        {
            name: "云盘",
            index: 2,
            icon: CloudOutlined,
            iconActive: CloudFilled,
            path: "/"
        },
        {
            name: "工作台",
            index: 3,
            icon: AppstoreOutlined,
            iconActive: AppstoreFilled,
            path: "/"
        }
    ];

    // 选中的导航索引
    const selectedNavIndex = ref(0);

    // 处理导航切换
    const handleNavChange = (index, path) => {
        selectedNavIndex.value = index;
        router.push(path)
    };
</script>

<style scoped
       lang="scss"
>
  .app-nav-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;

    .nav-main {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: 50%;

      .user-info-container {
        display: flex;
        justify-content: center;
        align-items: center;

        .user-avatar {
          width: 40px;
          height: 40px;
          background-color: white;
          border-radius: 50%;
          margin: 10px;
        }
      }

      .nav-item {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 40px;
        height: 40px;
        border-radius: 10px;
        margin: 10px;
        transition: all 0.3s ease;

        &:hover {
          background-color: rgba(255, 255, 255, 0.2);
        }

        .nav-item-icon {
          font-size: 20px;
          color: white;
        }
      }

      .nav-item-active {
        background-color: rgba(255, 255, 255, 0.2);
      }
    }

    .nav-footer {
      display: flex;
      justify-content: flex-end;
      align-items: center;
    }
  }
</style>