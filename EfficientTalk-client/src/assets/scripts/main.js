import { createApp } from 'vue';
import { createPinia } from 'pinia'
import '../style/global-style.css';

// 路由
import router from '../../router/index.js';

// Ant Design UI
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';

import App from '../../App.vue';

const pinia = createPinia()
const app = createApp(App);

app.use(pinia);
app.use(router);
app.use(Antd);

app.mount('#app');
