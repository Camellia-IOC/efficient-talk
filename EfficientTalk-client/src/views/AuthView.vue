<template>
  <div class="auth-view-container window-frame-border">
    <div class="container-header draggable">
      <a-button class="window-header-btn no-drag"
                @click="windowClose"
      >
        <CloseOutlined/>
      </a-button>
    </div>
    <div class="container-body">
      <div class="user-avatar">
        <img class="avatar"
             src="../assets/resources/logo.png"
             alt="avatar"
        >
      </div>
      <div class="input">
        <a-input v-model:value="accountInput"
                 placeholder="请输入账号"
                 :bordered="false"
                 class="input-item"
        ></a-input>
        <a-input v-model:value="passwordInput"
                 placeholder="请输入密码"
                 type="password"
                 :bordered="false"
                 class="input-item"
        ></a-input>
      </div>
      <div class="license">
        <a-radio v-model:checked="isAcceptLicense">已阅读并同意服务协议</a-radio>
      </div>
      <div class="operation">
        <a-button class="operation-btn"
                  type="primary"
                  @click="handleLogin"
                  :disabled="!isAcceptLicense"
        >登录
        </a-button>
      </div>
    </div>
  </div>
</template>

<script setup>
    import { ref } from "vue";
    import { CloseOutlined } from "@ant-design/icons-vue";
    import AuthApi from "../api/modules/AuthApi.js";
    import { saveCurUserData } from "../database/cur-user.js";
    import { message } from "ant-design-vue";
    import MainWindowController from "../window-controller/main-window-controller.js";
    import UserApi from "../api/modules/UserApi.js";

    const accountInput = ref("");
    const passwordInput = ref("");
    const isAcceptLicense = ref(false);

    // 关闭窗口
    const windowClose = () => {
        MainWindowController.closeMainWindow();
    };

    // 处理登录操作
    const handleLogin = async () => {
        try {
            // 请求数据
            const response = await AuthApi.userLogin({
                userId: accountInput.value,
                password: passwordInput.value
            });

            const res = response.data;
            if (res.code === 0) {
                if (res.data.permission === true) {
                    const data = res.data.userData;
                    // 保存当前登录用户信息到数据库中
                    await saveCurUserData(data);

                    // 加载云端用户设置
                    let params = {
                        userId: data.userId,
                        config: null
                    };
                    if (res.data.systemConfig === null) {
                        MainWindowController.loadSystemSettingConfig(params).then((config) => {
                            UserApi.saveUserSystemSetting({
                                userId: data.userId,
                                config: JSON.stringify(config)
                            });
                        });
                    }
                    else {
                        params.config = JSON.parse(res.data.systemConfig);
                        MainWindowController.loadSystemSettingConfig(params);
                    }

                    // 处理登录
                    MainWindowController.hideMainWindow();
                    MainWindowController.systemLogin();
                    MainWindowController.showMainWindow();
                }
            }
            else {
                message.error("账号或密码错误");
            }
        } catch (err) {
            console.error(err);
            message.error("请求失败，请稍后重试");
        }
    };
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .auth-view-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: linear-gradient(to bottom, #e2fcff, #befffb);

    .container-header {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      width: 100%;
      height: 40px;
      padding: 10px;
    }

    .container-body {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: calc(100% - 40px);
      gap: 25px;

      .user-avatar {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 80%;

        .avatar {
          width: 90px;
          height: 90px;
          border-radius: 50%;
        }
      }

      .input {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 80%;
        gap: 10px;

        .input-item {
          width: 100%;
          height: 40px;
          font-size: 15px;
          text-align: center;
          background-color: white;
        }
      }

      .license {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 80%;
      }

      .operation {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 80%;

        .operation-btn {
          width: 100%;
          background-color: global-variable.$theme-color;
        }
      }
    }
  }
</style>