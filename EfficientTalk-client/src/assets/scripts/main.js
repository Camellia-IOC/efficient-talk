import { createApp } from "vue";
import { createPinia } from "pinia";

// 全局样式表
import "../style/global-style.css";
import "../style/global-variable.scss";

// 动画库
import "animate.css"

// markdown样式表
import hljs from "highlight.js";
import 'highlight.js/styles/github.css';
import "github-markdown-css/github-markdown.css";

// 路由
import router from "../../router/index.js";

// Ant Design UI
import Antd from "ant-design-vue";
import "ant-design-vue/dist/reset.css";

// 图片预览
import "viewerjs/dist/viewer.css";
import VueViewer from "v-viewer";

// Emoji表情库
// import Vue3EmojiPicker from 'vue3-emoji-picker';
// import 'vue3-emoji-picker/css'

import App from "../../App.vue";

const pinia = createPinia();
const app = createApp(App);

// 注册全局自定义指令
app.directive('highlight', {
    // 当被绑定的元素插入到 DOM 中时，进行代码高亮
    mounted(el) {
        const blocks = el.querySelectorAll('pre code');
        blocks.forEach((block) => {
            hljs.highlightBlock(block);
        });
    },
    // 当被绑定元素的子节点更新之后，重新进行代码高亮
    updated(el) {
        const blocks = el.querySelectorAll('pre code');
        blocks.forEach((block) => {
            hljs.highlightBlock(block);
        });
    }
});

app.use(pinia);
app.use(router);
app.use(Antd);
app.use(VueViewer);
// app.component("Vue3EmojiPicker", Vue3EmojiPicker)

app.mount("#app");
