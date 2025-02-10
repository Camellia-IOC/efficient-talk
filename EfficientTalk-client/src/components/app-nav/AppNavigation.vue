<template>
  <div class="app-nav-container theme-background-color draggable">
    <div class="nav-main">
      <div class="user-info-container no-drag">
        <img class="user-avatar"
             :src="curLoginUser.userAvatar"
             alt="avatar"
        />
      </div>
      <div v-for="(item,index) in navConfig"
           :key="index"
           class="nav-item no-drag"
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
      <div class="nav-item no-drag"
      >
        <div class="nav-item-icon">
          <QuestionCircleOutlined/>
        </div>
      </div>
      <div class="nav-item no-drag"
      >
        <div class="nav-item-icon">
          <BarsOutlined/>
        </div>
      </div>
      <div class="nav-item no-drag"
           @click="handleLogout()"
      >
        <div class="nav-item-icon">
          <PoweroffOutlined/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import {
        MessageOutlined,
        MessageFilled,
        SmileOutlined,
        SmileFilled,
        CloudFilled,
        CloudOutlined,
        AppstoreOutlined,
        AppstoreFilled,
        QuestionCircleOutlined,
        PoweroffOutlined,
        BarsOutlined
    } from "@ant-design/icons-vue";
    import { useRouter } from "vue-router";
    import { getCurUserData } from "../../database/cur-user.js";

    const router = useRouter();

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // 处理退出登录
    const handleLogout = () => {
        // TODO 使用electron运行时记得取消相应注释
        // windowController.hide();
        // appController.logout();
        router.push("/auth");
        // windowController.show();
    };

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
            path: "/app/cloud-disk"
        },
        {
            name: "工作台",
            index: 3,
            icon: AppstoreOutlined,
            iconActive: AppstoreFilled,
            path: "/app/app-store"
        }
    ];

    // 选中的导航索引
    const selectedNavIndex = ref(0);

    // 处理导航切换
    const handleNavChange = (index, path) => {
        selectedNavIndex.value = index;
        router.push(path);
    };

    onBeforeMount(async () => {
        // 初始化当前登录的用户信息
        await updateCurLoginUser();
    })
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
      flex-direction: column;
      justify-content: flex-end;
      align-items: center;
      width: 100%;
      height: 50%;

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
    }
  }
</style>