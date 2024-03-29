import { createApp } from "vue";
import BackTop from "./MyBackTop.vue";
export default {
    BackTop: null,
    install(app) {
        if (this.BackTop) {
            app.config.globalProperties.$BackTop = this.BackTop;
            return;
        }
        let instance = createApp(BackTop);
        let div = document.createElement("div");
        let body = document.body;
        body.appendChild(div);
        this.BackTop = instance.mount(div);
        app.config.globalProperties.$BackTop = this.BackTop;
    },
};