<template>
  <div class="empty-container">
    <a-empty :image="simpleImage"
             :description="props.description"
    />
  </div>
</template>

<script setup>
    import { Empty } from "ant-design-vue";

    const simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;

    const props = defineProps({
        description: {
            type: String,
            default: ""
        }
    });
</script>

<style scoped
       lang="scss"
>
  .empty-container{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
  }
</style>