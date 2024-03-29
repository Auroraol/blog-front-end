import {
  createRouter,
  createWebHistory,
  createWebHashHistory,
} from "vue-router";

// 导入路由404分模块
import NoFond from "./no-fond";
// 导出多个要导出一个数组形式，在router.js中使用,必须使用扩展运算全部导入

//引入main.ts
import app from "../main";


/**
 *
 *
 * hidden: true                   如果设置了true，则不在左边导航栏显示,即不是后台管理的页面
 * alwaysShow: true               如果设置了true，则总在根菜单显示
 *
 * redirect: noRedirect           设置noRedirect，则面包屑路径不可点
 * name:'router-name'             名称在 <keep-alive> 用到，后台管理必须设置！！！
 * meta : {
    roles: ['admin','test']      页面角色控制，可多角色
    title: 'title'               标题
    icon: 'svg-name'             导航栏图标
    breadcrumb: false            false，将不显示面包屑
    activeMenu: '/example/list'  如果设置，则导航栏将高亮显示
  }
 */

const routes = [
  // 重定向
  {
    path: "/index.html",
    redirect: "/",
  },
  {
    path: "/",
    name: "index",
    component: () => import('@/views/index/index.vue'),
    meta: {
      loading: true,
      title: "首页",
    },
  },
  // {
  //   path: '/tag',
  //   component: () => import('@/views/tag/index'),
  //   hidden: true
  // },
  // {
  //   path: '/category',
  //   component: () => import('@/views/category/index'),
  //   hidden: true
  // },
  // {
  //   path: '/archives',
  //   component: () => import('@/views/archives/index'),
  //   hidden: true
  // },
  // {
  //   path: '/message',
  //   component: () => import('@/views/message/index'),
  //   hidden: true
  // },
  // {
  //   path: '/friend-link',
  //   component: () => import('@/views/friend-link/index'),
  //   hidden: true
  // },
  // {
  //   path: '/article/:id',
  //   component: () => import('@/views/article/index'),
  //   hidden: true
  // },
  // {
  //   path: '/reset-password',
  //   component: () => import('@/views/reset-password/index'),
  //   hidden: true
  // },
  // {
  //   path: '/email-validate',
  //   component: () => import('@/views/email-validate/index'),
  //   hidden: true
  // },
  // {
  //   path: '/rebind-mobile',
  //   component: () => import('@/views/rebind-mobile/index'),
  //   hidden: true
  // },
  // {
  //   path: '/bind-mobile',
  //   component: () => import('@/views/bind-mobile/index'),
  //   hidden: true
  // },
  // {
  //   path: '/about',
  //   component: () => import('@/views/about/index'),
  //   hidden: true
  // },
  // {
  //   path: '/terms',
  //   component: () => import('@/views/terms/index'),
  //   hidden: true
  // },
  // {
  //   path: '/privacy',
  //   component: () => import('@/views/privacy/index'),
  //   hidden: true
  // },
  // {
  //   path: '/search',
  //   component: () => import('@/views/search/index'),
  //   hidden: true
  // },
  // // {
  // //   path: '/email-bind',
  // //   component: () => import('@/views/404'),
  // //   hidden: true
  // // },
  // {
  //   path: '/oauth',
  //   component: () => import('@/views/oauth'),
  //   hidden: true
  // },
  // // {
  // //   path: '/404',
  // //   component: () => import('@/views/404'),
  // //   hidden: true
  // // },
  // {
  //   path: '/user',
  //   component: Layout,
  //   redirect: '/user/info',
  //   children: [{
  //     path: 'info',
  //     name: 'Info',
  //     component: () => import('@/views/user/index'),
  //     meta: {
  //       title: '基本信息',
  //       icon: 'user'
  //     }
  //   }]
  // },
  // {
  //   path: '/collect',
  //   component: Layout,
  //   children: [{
  //     path: 'index',
  //     name: 'collect',
  //     component: () => import('@/views/collect-manage/index'),
  //     meta: {
  //       title: '我的收藏',
  //       icon: 'collect-manage'
  //     }
  //   }]
  // },
  // 路由分模块
  NoFond,
];

// 导出路由
const router = createRouter({
  history: createWebHistory(), //开启history模式
  // history: createWebHashHistory(), //开启hash模式
  routes,
});

// 在路由元信息配置守卫 requiredPath为true, 适合守卫多个页面 vue3next() 变成return true
router.beforeEach((to, from, next) => {
  if (to.meta.loading) {
    // app.config.globalProperties.$loading.showLoading();
    next();
  } else {
    next();
  }
});

router.afterEach((to, from) => {
  if (to.meta.loading) {
    // app.config.globalProperties.$loading.hideLoading();
  }
});
export default router;
