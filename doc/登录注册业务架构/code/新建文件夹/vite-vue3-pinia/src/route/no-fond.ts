export default {
  // 路由分模块
  // 如果url找不到就会报404,必须放在路由页面最下面
  //   !!! 项目中不能以关键字命名，否则会报错 包括不限于 xx.vue组件页面
  path: "/:pathMatch(.*)",
  component: () => import("@/pages/error/404.vue"),
  meta: {
    loading: true,
    title: "404",
  },
};
