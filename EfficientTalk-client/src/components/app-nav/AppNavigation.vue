<template>
  <div class="app-nav-container draggable">
    <div class="nav-main">
      <div class="user-info-container no-drag">
        <a-popover placement="rightTop"
                   trigger="click"
        >
          <template #content>
            <!--TODO 封装名片组件-->
            <div style="width: 300px;height: 300px">

            </div>
          </template>
          <img class="user-avatar"
               v-if="curLoginUserStore.curLoginUser.userAvatar!==null"
               :src="curLoginUserStore.curLoginUser.userAvatar"
               alt="avatar"
          />
          <a-avatar class="user-avatar"
                    v-else
          >{{ curLoginUserStore.curLoginUser.userName.substring(0, 2) }}
          </a-avatar>
        </a-popover>
      </div>
      <div class="online-switcher-container no-drag"
           @click="handleOpenOnlineStateSwitcherDialog"
      >
        <div class="state"
             v-if="websocketStore.onlineState==='ONLINE'"
        >
          <div class="state-icon">
            <SmileFilled class="online"/>
          </div>
          <label class="state-label">在线</label>
        </div>
        <div class="state outline"
             v-else-if="websocketStore.onlineState==='OUTLINE'"
        >
          <div class="state-icon">
            <FrownFilled class="outline"/>
          </div>
          <label class="state-label">离线</label>
        </div>
        <div class="state leave"
             v-else-if="websocketStore.onlineState==='HIDE'"
        >
          <div class="state-icon">
            <EyeInvisibleFilled class="hide"/>
          </div>
          <label class="state-label">隐身</label>
        </div>
      </div>
      <div v-for="(item) in navConfig"
           :key="item.index"
           class="nav-item no-drag"
           :class="{ 'nav-item-active': selectedNavIndex === item.index }"
           @click="handleNavChange(item.index, item.path)"
      >
        <a-badge :count="chatDataStore.totalUnreadCount"
                 v-if="item.index === 0"
        >
          <div class="nav-item-icon">
            <component :is="item.iconActive"
                       v-if="selectedNavIndex === item.index"
            />
            <component :is="item.icon"
                       v-else
            />
          </div>
        </a-badge>
        <div class="nav-item-icon"
             v-else
        >
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
           :class="{ 'nav-item-active': selectedNavIndex === item.index }"
           v-for="(item) in footerNavConfig"
           :key="item.index"
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
      <div class="nav-item no-drag"
           @click="handleLogout()"
      >
        <div class="nav-item-icon">
          <PoweroffOutlined/>
        </div>
      </div>
    </div>
  </div>

  <!--用户状态切换器-->
  <OnlineStateSwitcherDialog ref="onlineStateSwitcherDialogRef"/>
</template>

<script setup>
    import {
        onBeforeMount,
        onBeforeUnmount,
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
        PoweroffOutlined,
        SettingOutlined,
        FrownFilled,
        EyeInvisibleFilled,
        BellOutlined,
        BellFilled,
        SettingFilled
    } from "@ant-design/icons-vue";
    import { useRouter } from "vue-router";
    import { useWebSocketStore } from "../../store/WebSocketStore.js";
    import OnlineStateSwitcherDialog from "../dialog/module-chat/online-state-switcher/OnlineStateSwitcherDialog.vue";
    import MainWindowController from "../../window-controller/main-window-controller.js";
    import { useChatDataStore } from "../../store/ChatDataStore.js";
    import { useCurLoginUserStore } from "../../store/CurLoginUserStore.js";

    const router = useRouter();
    const websocketStore = useWebSocketStore();
    const chatDataStore = useChatDataStore();
    const curLoginUserStore = useCurLoginUserStore()

    // 用户状态切换对话框
    const onlineStateSwitcherDialogRef = ref();
    const handleOpenOnlineStateSwitcherDialog = () => {
        onlineStateSwitcherDialogRef.value.dialogOpen();
    };

    // 处理退出登录
    const handleLogout = () => {
        // 关闭socket连接
        websocketStore.closeSocket();

        // 处理登出
        MainWindowController.hideMainWindow();
        MainWindowController.systemLogout();
        MainWindowController.showMainWindow();
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
            name: "通知公告",
            index: 3,
            icon: BellOutlined,
            iconActive: BellFilled,
            path: "/app/notification"
        },
        {
            name: "应用中心",
            index: 4,
            icon: AppstoreOutlined,
            iconActive: AppstoreFilled,
            path: "/app/app-store"
        }
    ];

    const footerNavConfig = [
        {
            name: "系统设置",
            index: "footer-1",
            icon: SettingOutlined,
            iconActive: SettingFilled,
            path: "/app/system-setting"
        }
    ];

    // 选中的导航索引
    const selectedNavIndex = ref(0);

    // 处理导航切换
    const handleNavChange = (index, path) => {
        selectedNavIndex.value = index;
        router.push(path);
    };

    // 处理导航索引变化
    const handleNavIndexChange = (event) => {
        switch (event.detail) {
            case "chat":
                selectedNavIndex.value = 0;
                break;
            case "contacts":
                selectedNavIndex.value = 1;
                break;
            case "cloud-disk":
                selectedNavIndex.value = 2;
                break;
            case "app-store":
                selectedNavIndex.value = 3;
                break;
        }
    };

    onBeforeMount(async () => {
        // 订阅导航索引变化事件
        window.addEventListener("navForceChange", handleNavIndexChange);
    });

    onBeforeUnmount(() => {
        // 取消订阅导航索引变化事件
        window.removeEventListener("navForceChange", handleNavIndexChange);
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .app-nav-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: global-variable.$theme-color;

    .nav-main {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: 60%;

      .user-info-container {
        display: flex;
        justify-content: center;
        align-items: center;

        .user-avatar {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 40px;
          height: 40px;
          background-color: white;
          color: black;
          border-radius: 50%;
          margin: 10px;
          cursor: pointer;
        }
      }

      .online-switcher-container {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-bottom: 20px;
        cursor: pointer;

        .state {
          display: flex;
          justify-content: center;
          align-items: center;
          gap: 3px;
          padding: 5px 6px;
          border-radius: 10px;
          background-color: white;

          .state-icon {
            font-size: 12px;

            .online {
              color: #67C23A;
            }

            .outline {
              color: #F56C6C;
            }

            .hide {
              color: #C0C4CC;
            }
          }

          .state-label {
            font-size: 12px;
            color: gray;
            cursor: pointer;
          }

          &:hover {
            background-color: rgba(255, 255, 255, 0.2);
            color: white;

            .state-label {
              color: white;
            }
          }
        }
      }

      .nav-item {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 40px;
        height: 40px;
        border-radius: 10px;
        margin: 8px;
        transition: all 0.3s ease;
        cursor: pointer;

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
      height: 40%;

      .nav-item-active {
        background-color: rgba(255, 255, 255, 0.2);
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
        cursor: pointer;

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