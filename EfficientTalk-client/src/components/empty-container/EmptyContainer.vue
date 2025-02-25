<template>
  <div class="empty-container">
    <a-empty :image="simpleImage"
             :description="props.description"
    />
  </div>
</template>

<script setup>
    import { Empty } from "ant-design-vue";
    import { watch } from "vue";

    const simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;

    const props = defineProps({
        description: {
            type: String,
            default: ""
        },
        height: {
            type: Number,
            default: null
        }
    });

    watch(() => props.height, (newValue, oldValue) => {
        if(newValue !== null){
            console.error(props.height)
            document.getElementsByClassName("empty-container")[0].style.height = `${newValue}px`;
        }
    });
</script>

<style scoped
       lang="scss"
>
  .empty-container {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
  }
</style>