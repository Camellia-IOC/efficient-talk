<template>
  <div class="icon-container">
    <svg class="icon"
         viewBox="0 0 1024 1024"
         version="1.1"
         xmlns="http://www.w3.org/2000/svg"
         :width="size"
         :height="size"
    >
      <path d="M622.55 564.39a202.44 202.44 0 0 0 89-167.59c0-111.65-90.83-202.48-202.48-202.48S306.58 285.15 306.58 396.8a202.45 202.45 0 0 0 89 167.59 289 289 0 0 0-174.94 265.29h58c0-127 103.36-230.4 230.4-230.4s230.41 103.35 230.41 230.4h58a289 289 0 0 0-174.9-265.29z m-258-167.59c0-79.66 64.8-144.47 144.46-144.47s144.51 64.81 144.51 144.47-64.81 144.47-144.47 144.47-144.46-64.81-144.46-144.47z"
            :fill="color"
      ></path>
      <path d="M294.13 542.12A108.26 108.26 0 0 1 186 434c0-51.15 36.3-95.7 86.31-105.93l-11.63-56.83a166.1 166.1 0 0 0-68.17 294.12C114.19 603.24 60.05 683.51 60.05 776.19h58c0-97.08 79-176.06 176.07-176.06zM825.6 565.35a166.12 166.12 0 0 0-68.17-294.12l-11.63 56.83c50 10.23 86.31 54.78 86.31 105.93A108.26 108.26 0 0 1 724 542.12v58c97.08 0 176.06 79 176.06 176.06h58c-0.01-92.67-54.14-172.94-132.46-210.83z"
            :fill="color"
      ></path>
    </svg>
  </div>
</template>

<script setup>
    import { themeColor } from "../../config/config.js";

    defineProps({
        size: {
            type: Number,
            default: 256
        },
        color: {
            type: String,
            default: themeColor
        }
    });
</script>

<style scoped
       lang="scss"
>
  .icon-container {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
  }
</style>