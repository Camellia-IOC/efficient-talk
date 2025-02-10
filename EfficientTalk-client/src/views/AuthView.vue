<template>
  <div class="auth-view-container">
    <div class="container-header">
      <a-button class="window-header-btn no-drag"
                @click="windowClose"
      >
        <CloseOutlined/>
      </a-button>
    </div>
    <div class="container-body">
      <div class="user-avatar">
        <img class="avatar"
             src="https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png"
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
        <a-radio>已阅读并同意服务协议</a-radio>
      </div>
      <div class="operation">
        <a-button class="operation-btn"
                  type="primary"
                  @click="handleLogin"
        >登录
        </a-button>
      </div>
    </div>
  </div>
</template>

<script setup>
    import { ref } from "vue";
    import { useRouter } from "vue-router";
    import { CloseOutlined } from "@ant-design/icons-vue";
    import AuthApi from "../api/modules/AuthApi.js";
    import { db } from "../database/db.js";
    import { saveCurUserData } from "../database/cur-user.js";

    const router = useRouter();

    const accountInput = ref("");
    const passwordInput = ref("");

    // 关闭窗口
    const windowClose = () => {
        windowController.close();
    };

    // 处理登录操作
    const handleLogin = () => {
        AuthApi.userLogin({
            userId: accountInput.value,
            password: passwordInput.value
        }).then(async (res) => {
            if (res.code === 0) {
                if (res.data.permission === true) {
                    const data = res.data.userData;
                    // TODO 使用electron运行时记得取消相应注释
                    // windowController.hide();
                    // appController.login();

                    // 保存当前登录用户信息到数据库中
                    await saveCurUserData(data.userId, data.userName, data.userAvatar);

                    await router.push("/app/chat");
                    // windowController.show();
                }
            }
        });
    };
</script>

<style scoped
       lang="scss"
>
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
        }
      }
    }
  }
</style>