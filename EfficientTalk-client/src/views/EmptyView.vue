<template>
  <div class="empty-view-container">
    <a-empty :image="simpleImage" :description="'功能正在开发中'"/>
  </div>
</template>

<script setup>
  import { Empty } from 'ant-design-vue';
  const simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;
</script>

<style scoped lang="scss">
  .empty-view-container{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
  }
</style>