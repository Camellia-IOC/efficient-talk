<template>
  <div class="cloud-disk-view-container">
    <!--导航栏-->
    <div class="nav-bar">
      <!--组织信息-->
      <div class="org-info">
        <div class="org-logo">
          <img :src="orgInfo.orgLogo"
               alt="logo"
               class="logo"
          >
        </div>
        <div class="org-detail">
          <div class="org-name">{{ orgInfo.orgName }}</div>
          <div class="disk-tag">
            <a-tag>组织云盘</a-tag>
          </div>
        </div>
      </div>

      <!--导航列表-->
      <div class="nav-list-container">
        <div v-for="(item,index) in navItemList"
             :key="index"
             class="nav-item"
             :class="{'nav-item-active': curSelectedNavIndex === item.index}"
             @click="handleSelectNavItem(item.index, item.path)"
        >
          {{ item.name }}
        </div>
      </div>
    </div>

    <!--内容显示区域-->
    <div class="content-container">
      <RouterView/>
    </div>
  </div>
</template>

<script setup>
    import {
        onBeforeMount,
        ref
    } from "vue";
    import { useRouter } from "vue-router";
    import { message } from "ant-design-vue";
    import SocialApi from "../api/modules/SocialApi.js";
    import { getCurUserData } from "../database/cur-user.js";

    const router = useRouter();

    // 当前登录的用户信息
    const curLoginUser = ref({});
    const updateCurLoginUser = async () => {
        curLoginUser.value = await getCurUserData();
    };

    // 导航项配置
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
    ];

    // 选择导航
    const curSelectedNavIndex = ref(0);
    const handleSelectNavItem = (index, path) => {
        curSelectedNavIndex.value = index;
        router.push(path);
    };

    // 获取组织信息
    const orgInfo = ref({});
    const getOrganizationInfo = async () => {
        const response = await SocialApi.getOrganizationInfo({
            orgId: curLoginUser.value.orgId
        });

        const res = response.data;
        if (res.code === 0) {
            if (res.data != null) {
                orgInfo.value = res.data.orgInfo;
            }
        }
        else {
            message.error("获取组织数据失败");
            orgInfo.value = {};
        }
    };

    onBeforeMount(async () => {
        // 初始化当前登录的用户信息
        await updateCurLoginUser();

        // 获取组织信息
        await getOrganizationInfo();
    });
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .cloud-disk-view-container {
    display: flex;
    align-items: center;
    width: 100%;
    height: 100%;
    border-top: global-variable.$border-line-width solid global-variable.$border-line-color;

    .nav-bar {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 250px;
      height: 100%;
      border-right: global-variable.$border-line-width solid global-variable.$border-line-color;

      .org-info {
        display: flex;
        align-items: center;
        width: 100%;
        height: 120px;
        padding: 20px;
        gap: 15px;

        .org-logo {
          display: flex;
          justify-content: center;
          align-items: center;

          .logo {
            $logo-size: 80px;
            width: $logo-size;
            height: $logo-size;
            border-radius: 50%;
          }
        }

        .org-detail{
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 15px;

          .org-name{
            display: flex;
            justify-content: flex-start;
            align-items: center;
            font-weight: bold;
            font-size: 20px;
          }

          .disk-tag{
            display: flex;
            justify-content: flex-start;
            align-items: center;
          }
        }
      }

      .nav-list-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        padding: 20px;
        gap: 10px;

        .nav-item {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 100%;
          height: 40px;
          background-color: #F5F7FA;
          border-radius: 10px;
          font-size: 14px;
          cursor: pointer;
          transition: all 0.3s ease;
        }

        .nav-item-active {
          background-color: global-variable.$theme-color;
          color: white;
        }
      }
    }

    .content-container {
      display: flex;
      align-items: center;
      width: calc(100% - 250px);
      height: 100%;
    }
  }
</style>