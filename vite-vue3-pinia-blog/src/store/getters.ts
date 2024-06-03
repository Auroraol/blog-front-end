import { defineStore,mapStores } from 'pinia';
// import { appStore } from "./modules/app";
import { useLoginStore } from "./modules/login";
// import { usePermissionStore } from "./modules/permission";
// import { settingsStore } from "./modules/settings";
import { useUserStore } from "./modules/user";
import { useAppStore } from "./modules/app";
import { useChatStore } from "./modules/chat";

export const useGetters = defineStore('getters', {
  getters: {
    // ...mapStores(useLoginStore, useUserStore),
    // sidebar: (state) => state.sidebar,
    device: (state) => useAppStore().device,
    // defaultAvatar: (state) => state.settings.defaultAvatar,
    token: (state) => useUserStore().token,
    userInfo: (state) => useUserStore().userInfo,
    roles: (state) => useUserStore().roles,
    // permission_routes: (state) => state.permission.routes,
    // loginVisible: (state) => state.login.visible,
    loginUsername: (state) => useLoginStore().username,
    loginPassword: (state) => useLoginStore().password,
    chatUserInfo: (state) => useChatStore().chatUserInfo,
  }
});

/*
这些getter函数都是计算属性，它们会根据store中的状态或其他getter的返回值进行计算，而不是直接存储数据。
在这种情况下，当您调用这些getter时，它们会根据相应的store状态或getter函数进行计算，并返回相应的值。
感觉使用pinia不需要用这个*/