import { defineStore } from 'pinia';

export const useGetters = defineStore({
  id: 'getters',
  getters: {
    sidebar: (state) => state.app.sidebar,
    device: (state) => state.app.device,
    defaultAvatar: (state) => state.settings.defaultAvatar,
    token: (state) => state.user.token,
    userInfo: (state) => state.user.userInfo,
    roles: (state) => state.user.roles,
    permission_routes: (state) => state.permission.routes,
    loginVisible: (state) => state.login.visible,
    loginUsername: (state) => state.login.username,
    loginPassword: (state) => state.login.password
  }
});