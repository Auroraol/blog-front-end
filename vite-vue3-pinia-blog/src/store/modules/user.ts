import { defineStore } from "pinia";
import {
  accountLogin,
  codeLogin,
  logout,
  getUserInfo,
  // thirdLogin,
} from "/@/api/user/user";

import {
  getAccessToken,
  setAccessToken,
  removeAccessToken,
} from "/@/utils/auth";

import { resetRouter } from "/@/route/router";

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
    // 注意，这里就不要写箭头函数了，不然 this 指向会出问题。
    //TODO 负责更新应用的状态，比如保存用户的访问令牌(access_token)等

    //
    SET_TOKEN(token) {
      this.token = token;
      setAccessToken(token); //保存cookie
    },
    SET_USER_INFO(userInfo) {
      this.userInfo = userInfo;
    },
    SET_ROLES(roles) {
      this.roles = roles;
    },
    /**
     * 账号登录
     */
    async accountLogin(params) {
      // async 基于promise的解决异步的最终方案。
      try {
        const {access_token} = await accountLogin(params); // 作用域不同,可同名  //await理解为等待返回成功结果
        this.SET_TOKEN(access_token); //pinia中有this.
        // console.log(data);
        // console.log(data.access_token);
      } catch (error) {
        throw error;
      }
    },
    // /**
    //  * 第三方登录
    //  */
    // async thirdLogin(params) {
    //   try {
    //     const { data } = await thirdLogin(params);
    //     this.SET_TOKEN(data.access_token);
    //     return Promise.resolve();
    //   } catch (error) {
    //     return Promise.reject(error);
    //   }
    // },
    /**
     * 验证码登录
     */
    async codeLogin(params) {
      try {
        const { access_token } = await codeLogin(params);
        this.SET_TOKEN(access_token);
      } catch (error) {
        throw error;
      }
    },
    /**
     * 获取用户信息
     */
    async getUserInfo() {
      try {
        const data  = await getUserInfo();
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
        throw error;
      }
    },
    /**
     * 退出
     */
    async logout() {
      try {
        const access_token = this.token;
        this.resetToken();
        const params = {
          access_token: access_token,
        };
        await logout(params);
        return Promise.resolve();
      } catch (error) {
        throw error;
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
  },
});
