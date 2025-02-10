<template>
  <div class="chat-detail">
    <div class="chat-header">
      <div class="user-info">
        <div class="user-avatar">
          <img src="https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png"
               alt=""
          >
        </div>
        <div class="user-name">
          测试用户
        </div>
      </div>
    </div>
    <div class="chat-history">

    </div>
    <div class="chat-footer">
      <div class="operations-bar">
        <a-button></a-button>
      </div>
      <div class="input-area">
        <a-textarea class="input"
                    :bordered="false"
                    :autoSize="false"
                    @pressEnter=""
        ></a-textarea>
        <a-button type="primary"
                  class="btn-send"
        >发送
        </a-button>
      </div>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped
       lang="scss"
>
  .chat-detail {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;

    $header-height: 50px;
    $footer-height: 210px;

    .chat-header {
      display: flex;
      align-items: center;
      width: 100%;
      height: $header-height;
      padding: 20px;
      border-bottom: 1px solid rgba(0, 0, 0, 0.1);

      .user-info {
        display: flex;
        align-items: center;
        width: 100%;
        height: 100%;

        .user-avatar {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          margin-right: 15px;

          img {
            width: 100%;
            height: 100%;
          }
        }

        .user-name {
          font-size: 16px;
          font-weight: bold;
        }
      }
    }

    .chat-history {
      display: flex;
      flex-direction: column;
      width: 100%;
      height: calc(100% - #{$header-height + $footer-height});
    }

    .chat-footer {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: $footer-height;

      $operations-bar-height: 40px;

      .operations-bar {
        width: 100%;
        height: $operations-bar-height;
        padding: 0 20px;
        border-top: 1px solid rgba(0, 0, 0, 0.1);
      }

      .input-area {
        width: 100%;
        height: calc(100% - $operations-bar-height);
        padding: 0 12px;
        position: relative;

        .input {
          width: 100%;
          height: 100%;
          resize: none;
        }

        .btn-send {
          position: absolute;
          right: 15px;
          bottom: 15px;
        }
      }
    }
  }
</style>