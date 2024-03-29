import { createApp } from "vue";
import App from "./App.vue";

// 引入重置与全局样式
import "./assets/style/reset.css";
import "./assets/style/common.css";
//加载阿里巴巴矢量图标库
import './assets/iconfont/iconfont.js'

//引入 Pinia (必须)
import { createPinia } from 'pinia';
const state = createPinia()

//导入router配置文件
import route from "@/route/router";

// 引入element-icon集合
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

// 自定义loading组件
import Loading from "./plugins/Loading/index";

// 自定义回到顶部组件
import BackTop from "./plugins/BackTop/index";

const app = createApp(App);


// 打印环境变量
// console.log(process.env.NODE_ENV);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

//全局注册
app.use(route).use(state).use(Loading).use(BackTop).mount("#app");
// app.use(route).use(pinia).use(Loading).use(BackTop).mount("#app");

export default app; // 这里导出app的目的，是为了在后面演示js文件中使用插件
