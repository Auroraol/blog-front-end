import {
  createRouter,
  createWebHistory,
  createWebHashHistory,
} from "vue-router";

// 导入路由404分模块
import NoFond from "./no-fond";
/* Layout */
import Layout from "/@/layout/index.vue";
//引入main.ts
import app from "../main";

/**
 *
 * redirect: noRedirect           设置noRedirect，则面包屑路径不可点
 * name:'router-name'             名称在 <keep-alive> 用到，后台管理必须设置！！！
 * meta : {
    roles: ['admin','test']      页面角色控制，可多角色
    title: 'title'               标题   使用window.document.title = to.meta.title as string
    icon: 'svg-name'             导航栏图标
    breadcrumb: false            false，将不显示面包屑
    activeMenu: '/example/list'  如果设置，则导航栏将高亮显示
  }
 *
 *自定义的路由元信息:
 *   hidden: true                   如果设置了true，则不在左边导航栏显示,即不是后台管理的页面
 *   alwaysShow: true               如果设置了true，则总在根菜单显示
 *
 */

/**
 * 常量路由，所有用户可见
 */
export const constantRoutes = [
  // 重定向
  {
    path: "/",
    redirect: "/index",
    hidden: true,
  },
  {
    path: "/index",
    name: "index",
    component: () => import("/@/views/index/index.vue"),
    hidden: true,
    meta: {
      title: "首页",
    },
  },
  {
    path: "/article",
    name: "article",
    component: () => import("/@/views/Article.vue"),
    meta: {
      title: "文章列表",
    },
    hidden: true,
  },
  {
    path: "/category",
    component: () => import("/@/views/category/index.vue"),
    hidden: true,
    meta: {
      title: "分类",
    },
    props($route) {
      return {
        id: $route.query.id,
      };
    },
  },
  {
    path: "/write",
    component: () => import("/@/views/write/index.vue"),
    meta: {
      title: "写作",
    },
    hidden: true,
  },
  {
    path: "/login-register",
    name: "login-register",
    component: () => import("/@/views/login-register/index.vue"),
    hidden: true,
    meta: {
      title: "登录注册",
    },
    children: [
      //通过children配置子级路由
      {
        path: "login",
        name: "login",
        component: () => import("/@/views/login-register/components/Login.vue"),
        hidden: true,
        meta: {
          title: "登录",
        },
      },
      {
        path: "register",
        name: "register",
        component: () =>
          import("/@/views/login-register/components/Register.vue"),
        hidden: true,
        meta: {
          title: "注册",
        },
      },
    ],
  },
  {
    path: "/reset-password",
    name: "reset-password",
    component: () => import("/@/views/reset-password/index.vue"),
    hidden: true,
  },
  {
    path: "/email-validate",
    name: "email-validate",
    component: () => import("/@/views/email-validate/index.vue"),
    hidden: true,
    meta: {
      title: "邮箱绑定",
    },
  },
  {
    path: "/bind-mobile",
    name: "bind-mobile",
    component: () => import("/@/views/bind-mobile/index.vue"),
    hidden: true,
    meta: {
      title: "手机号绑定",
    },
  },
  // {
  //   path: '/rebind-mobile',
  //   component: () => import('@/views/rebind-mobile/index'),
  //   hidden: true,
  // meta: {
  //   title: "手机号换绑",
  // },
  // },
  {
    path: "/about",
    name: "about",
    hidden: true,
    component: () => import("/@/views/about/index.vue"),
    meta: {
      title: "关于",
    },
  },
  {
    path: "/message",
    component: () => import("/@/views/message/index.vue"),
    hidden: true,
    meta: {
      title: "留言",
    },
  },
  {
    path: "/archives",
    component: () => import("/@/views/archives/index.vue"),
    hidden: true,
    meta: {
      title: "归档",
    },
  },
  {
    path: "/terms",
    name: "terms",
    component: () => import("/@/views/terms/index.vue"),
    hidden: true,
    meta: {
      title: "用户协议",
    },
  },
  {
    path: "/privacy",
    name: "privacy",
    component: () => import("/@/views/privacy/index.vue"),
    hidden: true,
    meta: {
      title: "隐私政策",
    },
  },
  {
    path: "/friend-link",
    name: "friend-link",
    component: () => import("/@/views/friend-link/index.vue"),
    hidden: true,
    meta: {
      title: "友链",
    },
  },
  {
    path: "/tag",
    name: "tag",
    component: () => import("/@/views/tag/index.vue"),
    hidden: true,
    meta: {
      title: "标签",
    },
    props($route) {
      return {
        id: $route.query.id,
      };
    },
  },
  {
    path: "/article/:id",
    component: () => import("/@/views/article-browser/index.vue"),
    hidden: true,
    meta: {
      title: "浏览文章",
    },
  },
  // {
  //   path: "/personalcenter",
  //   name: "personalcenter",
  //   component: () => import("/@/views/personal-center/index.vue"),
  //   meta: {
  //     title: "个人中心",
  //   },
  //   children: [
  //     {
  //       path: "write",
  //       name: "write",
  //       // component: () => import("/@/views/write/index.vue"),
  //       meta: {
  //         title: "写作",
  //       },
  //     },
  //     //TODO
  //     // {
  //     //   path: 'a',
  //     //   component: () => import('@/components/me/a.vue'),
  //     // },
  //     // {
  //     //   path: 'b',
  //     //   component: () => import('@/components/me/b.vue'),
  //     // },
  //     // {
  //     //   path: 'c',
  //     //   component: () => import('@/components/me/c.vue'),
  //     // },
  //   ],
  // },
  // 后台-公用
  {
    path: "/user",
    component: Layout,
    redirect: "/user/info",
    children: [
      {
        path: "info",
        name: "Info",
        component: () => import("/@/views/user/index.vue"),
        meta: {
          title: "基本信息",
          icon: "user",
        },
      },
    ],
  },
  {
    path: "/collect",
    component: Layout,
    children: [
      {
        path: "index",
        name: "collect",
        component: () => import("/@/views/collect-manage/index.vue"),
        meta: {
          title: "我的收藏",
          icon: "collect-manage",
        },
      },
    ],
  },

  // 路由分模块 404
  NoFond,
];

/**
 * 后台-根据用户角色动态加载路由
 */
export const asyncRoutes = [
  {
    path: "/user-manage",
    component: Layout,
    children: [
      {
        path: "index",
        name: "UserManage",
        component: () => import('/@/views/user-manage/index.vue'),
        meta: {
          title: "用户管理",
          icon: "user-manage",
          roles: ["admin"],
        },
      },
    ],
  },
  {
    path: "/art-manage",
    component: Layout,
    redirect: "noRedirect",
    meta: {
      title: "文章管理",
      icon: "art-manage",
      roles: ["admin"],
    },
    children: [
      {
        path: "edit",
        name: "Edit",
        // component: () => import('@/views/article-manage/edit'),
        meta: {
          title: "编辑",
          icon: "art-edit",
        },
      },
      {
        path: "list",
        name: "List",
        // component: () => import('@/views/article-manage/list'),
        meta: {
          title: "文章",
          icon: "art-list",
        },
      },
    ],
  },
  {
    path: "/recommend",
    component: Layout,
    children: [
      {
        path: "index",
        name: "RecommendManage",
        component: () => import('/@/views/recommend-manage/index.vue'),
        meta: {
          title: "推荐管理",
          icon: "recommend-manage",
          roles: ["admin"],
        },
      },
    ],
  },
  {
    path: "/tag-manage",
    component: Layout,
    children: [
      {
        path: "index",
        name: "TagManage",
        component: () => import('/@/views/tag-manage/index.vue'),
        meta: {
          title: "标签管理",
          icon: "tag-manage",
          roles: ["admin"],
        },
      },
    ],
  },
  {
    path: "/category-manage",
    component: Layout,
    children: [
      {
        path: "index",
        name: "CategoryManage",
        component: () => import('/@/views/category-manage/index.vue'),
        meta: {
          title: "分类管理",
          icon: "category-manage",
          roles: ["admin"],
        },
      },
    ],
  },
  {
    path: "/friend-link/manage",
    component: Layout,
    children: [
      {
        path: "index",
        name: "FriendChainManage",
        component: () => import('/@/views/friend-link-manage/index.vue'),
        meta: {
          title: "友链管理",
          icon: "friend-chain-manage",
          roles: ["admin"],
        },
      },
    ],
  },
  {
    path: "/client",
    component: Layout,
    children: [
      {
        path: "index",
        name: "client",
        component: () => import('/@/views/client-manage/index.vue'),
        meta: {
          title: "客户端管理",
          icon: "client-manage",
          roles: ["admin"],
        },
      },
    ],
  },
  {
    path: "/file",
    component: Layout,
    children: [
      {
        path: "index",
        name: "File",
        component: () => import('/@/views/file-manage/index.vue'),
        meta: {
          title: "文件管理",
          icon: "file-manage",
          roles: ["admin"],
        },
      },
    ],
  },
];

// 导出路由
const router = createRouter({
  history: createWebHistory(), //开启history模式
  // history: createWebHashHistory(), //开启hash模式
  routes: constantRoutes,
  // 路由滚动位置 解决vue页面之间跳转，页面不是在顶部的问题
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return Promise.resolve(savedPosition);
    } else {
      return Promise.resolve({ left: 0, top: 0 });
    }
  },
});

// router.beforeEach((to, from, next) 和 router.afterEach((to, from) 写在permission.ts中

/**
 * 重置路由
 */
export function resetRouter() {
  // 清空现有路由
  router.getRoutes().forEach((route) => {
    router.removeRoute(route.name);
  });

  // 添加新的路由
  constantRoutes.forEach((route) => {
    router.addRoute(route);
  });
}

export default router;
