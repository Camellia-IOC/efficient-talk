<template>
  <div class="app-container">
    <RouterView/>
  </div>
</template>

<script setup>
    import { RouterView } from "vue-router";
</script>

<style scoped
       lang="scss"
>
  .app-container {
    width: 100%;
    height: 100%;
  }
</style>
