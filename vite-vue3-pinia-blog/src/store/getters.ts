import { defineStore,mapStores } from 'pinia';
// import { appStore } from "./modules/app";
import { useLoginStore } from "./modules/login";
// import { usePermissionStore } from "./modules/permission";
// import { settingsStore } from "./modules/settings";
import { useUserStore } from "./modules/user";


export const useGetters = defineStore('getters', {
  getters: {
    // ...mapStores(useLoginStore, useUserStore),
    // sidebar: (state) => state.sidebar,
    // device: (state) => state.app.device,
    // defaultAvatar: (state) => state.settings.defaultAvatar,
    token: (state) => useUserStore().token,
    userInfo: (state) => useUserStore().userInfo,
    // roles: (state) => state.user.roles,
    // permission_routes: (state) => state.permission.routes,
    // loginVisible: (state) => state.login.visible,
    loginUsername: (state) => useLoginStore().username,
    loginPassword: (state) => useLoginStore().password
  }
});

/*感觉使用pinia不需要用这个*/