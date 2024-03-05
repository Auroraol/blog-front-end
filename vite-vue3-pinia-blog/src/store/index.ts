import { defineStore } from "pinia";
import { Names } from "./store_name";

export const useStore = defineStore(Names.piniaStore, {
  state: () => {
    return {
      bodyWidth: 0, //做响应式的nav使用，判断窗口的宽度
      sessionInfo: "", //判断登录状态，重新渲染nav里面的用户信息的dom
      ifDelMessage: 0, //删除留言重新渲染dom
      pariseOrCollection: 0, //点赞或者收藏更新数据
      nickNameLength: 16, //昵称的最大字符数
      articleCommentsAdd: 0, //文章评论更新后更新dom
      refreshSearchRes: 0, //搜索页面重新搜索刷新搜索结果
    };
  },

  getters: {},

  actions: {
    setBodyWidth(val: number) {
      this.bodyWidth = val;
    },
  },
});
