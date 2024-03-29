import { defineStore } from "pinia";
import {
  accountLogin,
  codeLogin,
  logout,
  getUserInfo,
  thirdLogin,
} from "/@/api/user";

import {
  getAccessToken,
  setAccessToken,
  removeAccessToken,
} from "/@/utils/auth";

import { resetRouter } from "/@/route/router.ts";


// 第一个参数是应用程序中 store 的唯一 id
export const useUserStore = defineStore("user", {
  state: () => ({
    token: getAccessToken(),
    nickname: "",
    avatar: "",
    roles: [],
    userInfo: "",
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
  },
  actions: {
    /**
     * 账号登录
     */
    async accountLogin(params) {
      try {
        const { data } = await accountLogin(params);
        this.SET_TOKEN(data.access_token);
        return Promise.resolve();
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 第三方登录
     */
    async thirdLogin(params) {
      try {
        const { data } = await thirdLogin(params);
        this.SET_TOKEN(data.access_token);
        return Promise.resolve();
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 验证码登录
     */
    async codeLogin(params) {
      try {
        const { data } = await codeLogin(params);
        this.SET_TOKEN(data.access_token);
        return Promise.resolve();
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 获取用户信息
     */
    async getUserInfo() {
      try {
        const { data } = await getUserInfo();
        if (!data) {
          return Promise.reject("获取用户信息失败，请重新登录");
        }
        const { roles } = data;
        if (!roles || roles.length <= 0) {
          return Promise.reject("角色列表要求非null列表");
        }
        this.SET_ROLES(roles);
        this.SET_USER_INFO(data);
        return Promise.resolve(data);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 退出
     */
    async logout() {
      try {
        const access_token = this.token;
        this.SET_TOKEN("");
        this.SET_ROLES([]);
        this.SET_USER_INFO("");
        removeAccessToken();
        resetRouter();
        const params = {
          access_token: access_token,
        };
        await logout(params);
        return Promise.resolve();
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 重置
     */
    resetToken() {
      this.SET_TOKEN("");
      this.SET_ROLES([]);
      this.SET_USER_INFO("");
      removeAccessToken();
      resetRouter();
    },
    SET_TOKEN(token) {
      this.token = token;
      setAccessToken(token);
    },
    SET_USER_INFO(userInfo) {
      this.userInfo = userInfo;
    },
    SET_ROLES(roles) {
      this.roles = roles;
    },
  },
});
