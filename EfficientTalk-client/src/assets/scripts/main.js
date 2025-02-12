import { createApp } from 'vue';
import { createPinia } from 'pinia'

// 全局样式表
import '../style/global-style.css';
import '../style/global-variable.scss'

// 路由
import router from '../../router/index.js';

// Ant Design UI
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';

// Emoji表情库
// import Vue3EmojiPicker from 'vue3-emoji-picker';
// import 'vue3-emoji-picker/css'

import App from '../../App.vue';

const pinia = createPinia()
const app = createApp(App);

app.use(pinia);
app.use(router);
app.use(Antd);
// app.component("Vue3EmojiPicker", Vue3EmojiPicker)

app.mount('#app');
