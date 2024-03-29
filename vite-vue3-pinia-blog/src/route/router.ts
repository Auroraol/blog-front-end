import {
  createRouter,
  createWebHistory,
  createWebHashHistory,
} from "vue-router";

// 导入路由404分模块
import NoFond from "./no-fond";

//引入main.ts
import app from "../main";
import { useStore } from "../store2";


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
    path: '/',
    redirect: '/index',
},  {
  path: '/index',
  name: 'index',
  component: () => import('/@/view/index.vue'),
  meta: {
      title: '首页'
  }
}, {
  path: '/article',
  name: 'article',
  component: () => import('/@/view/Article.vue'),
  meta: {
      title: '文章列表'
  }
}, 
{
  path: '/write',
  name: 'write',
  component: () => import('/@/view/Write.vue'),
  meta: {
      title: '写作'
  }
},
//{
//   path: '/leavemsg',
//   name: 'leavemsg',
//   component: () => import('/@/view/LeaveMsg.vue'),
//   meta: {
//       title: '留言板'
//   }
// }, 
{
  path: '/login',
  name: 'login',
  component: () => import('/@/view/login.vue'),
  meta: {
      title: '登录'
  }
},
{
  path: '/register',
  name: 'register',
  component: () => import('/@/view/register.vue'),
  meta: {
      title: '注册'
  }
},
 {
  path: '/about',
  name: 'about',
  component: () => import('/@/view/about.vue'),
  meta: {
      title: '更多'
  }
}
// , {
//   path: '/articleeditor',
//   name: 'articleeditor',
//   component: () => import('/@/view/Editor.vue'),
//   meta: {
//       title: '编辑文章'
//   }
// }
,{
  path: '/personalcenter',
  name: 'personalcenter',
  component: () => import('/@/view/personalcenter.vue'),
  meta: {
      title: '个人中心'
  },
  children: [
    //TODO
    // {
    //   path: 'a',
    //   component: () => import('@/components/me/a.vue'),
    // },
    // {
    //   path: 'b',
    //   component: () => import('@/components/me/b.vue'),
    // },
    // {
    //   path: 'c',
    //   component: () => import('@/components/me/c.vue'),
    // },
  ]  
},
// {
//   path: '/archives',
//   component: () => import('/@/view/archives/index'),
//   hidden: true
// },
//  {
//   path: '/otherspersonalcenter',
//   name: 'otherspersonalcenter',
//   component: () => import('/@/view/OthersPersonalCenter.vue'),
//   meta: {
//       title: '个人中心'
//   }
// }, {
//   path: '/userfocus',
//   name: 'userfocus',
//   component: () => import('@/view/UserFocus.vue'),
//   meta: {
//       title: '关注列表'
//   },
//   children: [
//       {
//           path: "allfocus",
//           name: 'allfocus',
//           component: () => import('@/components/personalCenter/userfocus/userfocusShow.vue')
//       },
//       {
//           path: "fans",
//           name: 'fans',
//           component: () => import('@/components/personalCenter/userfocus/userfocusShow.vue')
//       }
//   ]
// }, {
//   path: '/search',
//   name: 'search',
//   component: () => import('@/view/Search.vue'),
//   meta: {
//       title: '搜索'
//   }
// }, {
//   path: '/chat',
//   name: 'chat',
//   component: () => import('@/view/Chat.vue'),
//   meta: {
//       title: '聊天室'
//   },
// }

  // {
  //   path: "/home",
  //   name: "home",
  //   component: () => import("/@/pages/home/home.vue"),
  //   meta: {
  //     loading: true,
  //     title: "首页",
  //   },
  // },
  // {
  //   path: "/vaxiso",
  //   component: () => import("/@/pages/vaxiso/vaxiso.vue"),
  //   meta: {
  //     loading: true,
  //     title: "vaxiso",
  //   },
  // },
  // {
  //   path: "/editTable",
  //   component: () => import("/@/pages/edit-table/index.vue"),
  //   meta: {
  //     loading: true,
  //     title: "editTable",
  //   },
  // },
  // {
  //   path: "/test",
  //   component: () => import("/@/pages/vue-hook-test/test.vue"),
  //   meta: {
  //     loading: true,
  //     title: "test",
  //   },
  // },
  // {
  //   path: "/test2",
  //   component: () => import("/@/pages/vue-hook-test2/test.vue"),
  //   meta: {
  //     loading: true,
  //     title: "test2",
  //   },
  // },
  // {
  //   path: "/test3",
  //   component: () => import("/@/pages/vue-hook-test3/test.vue"),
  //   meta: {
  //     loading: true,
  //     title: "test3",
  //   },
  // },
  // // Pinia
  // {
  //   path: "/storeTest1",
  //   component: () => import("../pages/store-test/test.vue"),
  //   meta: {
  //     loading: true,
  //     title: "storeTest1",
  //   },
  // },
  // // 测试$route使用
  // {
  //   // url http://localhost:8080/routeTest1/20
  //   path: "/routeTest1/:id",
  //   component: () => import("../pages/route-test/test1.vue"),
  //   meta: {
  //     loading: true,
  //     title: "routeTest1",
  //   },
  // },
  // {
  //   // url http://localhost:8080/routeTest2/20
  //   path: "/routeTest2/:id",
  //   component: () => import("../pages/route-test/test2.vue"),
  //   meta: {
  //     loading: true,
  //     title: "routeTest2",
  //   },
  // },
  // // 路由参数
  // {
  //   // url http://localhost:3000/routeTest3/20/定积分
  //   path: "/routeTest3/:id/:title",
  //   component: () => import("../pages/route-test/test3.vue"),
  //   meta: {
  //     loading: true,
  //     title: "routeTest3",
  //   },
  // },
  // {
  //   // url http://localhost:3000/routeTest4?id=20&title=定积分
  //   path: "/routeTest4",
  //   component: () => import("../pages/route-test/test4.vue"),
  //   props($route){
  //     return {
  //       id:$route.query.id,
  //       title:$route.query.title,
  //       // 可以携带默认值
  //       default: '我是默认参数'
  //     }
  //   },
  //   meta: {
  //     loading: true,
  //     title: "routeTest4",
  //   }
  // },
  // 路由分模块
  // NoFond,
];

// 导出路由
const router = createRouter({
  history: createWebHistory(), //开启history模式
  // history: createWebHashHistory(), //开启hash模式
  routes,
  // 路由滚动位置
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { x: 0, y: 0 };
    }
  },
});

// 在路由元信息配置守卫 requiredPath为true, 适合守卫多个页面 vue3next() 变成return true
// router.beforeEach((to, from, next) => {
//   // //判断是否是登录状态
//   // // const sessionAccount = localStorage.getItem('userAccount')
//   // const sessionAccount = getUserAccountInfo();
//   // if (sessionAccount) {
//   //     const pinia = useStore()
//   //     pinia.userInfo = sessionAccount
//   // }

//   // // 进入写文页面，判断是否有权限
//   // if (to.path === '/write') {
//   //     if (!sessionAccount) {
//   //         alert('还没有登录，去登录吧')
//   //         router.push('/login')
//   //     }
//   // }

//   // window.document.title = to.meta.title as string
//   // //到新页面要把页面滚动到最顶
//   // window.scrollTo({
//   //     top: 0,
//   // })
//   next()


//   // if (to.meta.loading) {
//   //   app.config.globalProperties.$loading.showLoading();
//   //   next();
//   // } else {
//   //   next();
//   // }

// });

// router.afterEach((to, from) => {
//   // if (to.meta.loading) {
//   //   app.config.globalProperties.$loading.hideLoading();
//   // }
// });


// const createRouter = () => new Router({
//   // mode: 'history', // require service support
//   // 路由滚动位置
//   scrollBehavior(to, from, savedPosition) {
//     if (savedPosition) {
//       return savedPosition
//     } else {
//       return { x: 0, y: 0 }
//     }
//   },
//   mode: 'history',
//   routes: routes
// })


/**
 * 重置路由
 */
export function resetRouter() {
  // 清空现有路由
  router.getRoutes().forEach((route) => {
    router.removeRoute(route.name);
  });

  // 添加新的路由
  routes.forEach((route) => {
    router.addRoute(route);
  });
}

export default router;
