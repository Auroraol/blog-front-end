import { createApp } from "vue";
// 导入写好的Loading.vue文件
import Loading from "./Loading.vue";

// 导出一个对象，包含一个名为 install 的方法
export default {
  loading: null,
  /*    每当这个插件被添加到应用程序中时，如果它是一个对象，就会调用 install 方法。
  如果它是一个 function，则函数本身将被调用。在这两种情况下——它都会收到两个参数：
  由 Vue 的 createApp 生成的 app 对象和用户传入的选项。 */
  install(app) {
    // 如果插件已经被添加过，防止多次载入
    if (this.loading) {
      // 防止多次载入
      app.config.globalProperties.$loading = this.loading;
      return;
    }
    // 创建一个 Vue 应用实例，用于挂载 Loading 组件
    let instance = createApp(Loading);
    // 创建div元素装载Loading对象
    let div = document.createElement("div");
    let body = document.body;
    // 将 div 元素添加到 body 中
    body.appendChild(div);
    // 将 Loading 组件的实例挂载到 div 元素上
    this.loading = instance.mount(div);
    // 挂载vue身上
    app.config.globalProperties.$loading = this.loading;
  },
};