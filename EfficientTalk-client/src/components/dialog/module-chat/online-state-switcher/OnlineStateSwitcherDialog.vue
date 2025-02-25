<template>
  <a-modal v-model:open="dialogOpenFlag"
           centered
           :mask="false"
           width="350px"
           :footer="null"
           @cancel="dialogClose"
  >
    <template #title>
      <div class="dialog-title">
        <div class="logo">
          <Logo :color="themeColor" :size="30"/>
        </div>
        <label>用户状态</label>
      </div>
    </template>
    <div class="online-state-switcher-container">
      <div class="cur-state-container">
        <div class="cur-state"
             v-if="websocketStore.onlineState==='ONLINE'"
        >
          <SmileFilled style="color: #67C23A"/>
          <label>在线</label>
        </div>
        <div class="cur-state"
             v-else-if="websocketStore.onlineState==='OUTLINE'"
        >
          <FrownFilled style="color:#F56C6C;"/>
          <label>离线</label>
        </div>
        <div class="cur-state"
             v-else-if="websocketStore.onlineState==='LEAVE'"
        >
          <ClockCircleFilled style="color: #E6A23C"/>
          <label>离开</label>
        </div>
      </div>
      <div class="online-state-switcher">
        <div class="state-item"
             :class="{'active-state-item': websocketStore.onlineState==='ONLINE'}"
             @click="websocketStore.switchOnlineState('ONLINE')"
        >
          <SmileFilled style="color: #67C23A"
                       class="state-icon"
          />
          <label>在线</label>
        </div>
        <div class="state-item"
             :class="{'active-state-item': websocketStore.onlineState==='OUTLINE'}"
             @click="websocketStore.switchOnlineState('OUTLINE')"
        >
          <FrownFilled style="color:#F56C6C;"
                       class="state-icon"
          />
          <label>离线</label>
        </div>
        <div class="state-item"
             :class="{'active-state-item': websocketStore.onlineState==='LEAVE'}"
             @click="websocketStore.switchOnlineState('LEAVE')"
        >
          <ClockCircleFilled style="color: #E6A23C"
                             class="state-icon"
          />
          <label>离开</label>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
    import { ref } from "vue";
    import {
        SmileFilled,
        FrownFilled,
        ClockCircleFilled
    } from "@ant-design/icons-vue";
    import { useWebSocketStore } from "../../../../store/WebSocketStore.js";
    import Logo from "../../../logo/Logo.vue";
    import { themeColor } from "../../../../config/config.js";

    const websocketStore = useWebSocketStore();

    // 对话框控制
    const dialogOpenFlag = ref(false);
    const dialogOpen = () => {
        dialogOpenFlag.value = true;
    };
    const dialogClose = () => {
        dialogOpenFlag.value = false;
    };

    defineExpose({
        dialogOpen,
        dialogClose
    });
</script>

<style scoped
       lang="scss"
>
  @use "../../../../assets/style/global-variable.css";

  .dialog-title {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 10px;
    font-size: 18px;
    width: 100%;
  }

  .online-state-switcher-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 400px;

    .cur-state-container {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 300px;

      .cur-state {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 10px;
        font-size: 18px;
      }
    }

    .online-state-switcher {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      grid-column-gap: 30px;

      .state-item {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        width: 80px;
        height: 80px;
        background-color: rgba(0, 0, 0, 0.03);
        border-radius: 20px;
        gap: 10px;
        cursor: pointer;
        transition: all 0.3s ease;

        .state-icon {
          font-size: 24px;
        }

        label {
          cursor: pointer;
        }
      }

      .active-state-item {
        color: white;
        background-color: global-variable.$theme-color;
      }
    }
  }
</style>