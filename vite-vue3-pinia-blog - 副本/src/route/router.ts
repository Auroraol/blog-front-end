import {
  createRouter,
  createWebHistory,
  createWebHashHistory,
} from "vue-router";

// 导入路由404分模块
import NoFond from "./no-fond";
// 导出多个要导出一个数组形式，在router.js中使用,必须使用扩展运算全部导入

import {  getUserAccountInfo  } from '/@/utils/network/auth.js'


//引入main.ts
import app from "../main";
import { useStore } from "../store";

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
  path: '/about',
  name: 'about',
  component: () => import('/@/view/About.vue'),
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
});

// 在路由元信息配置守卫 requiredPath为true, 适合守卫多个页面 vue3next() 变成return true
router.beforeEach((to, from, next) => {
  //判断是否是登录状态
  // const sessionAccount = localStorage.getItem('userAccount')
  const sessionAccount = getUserAccountInfo();
  if (sessionAccount) {
      const pinia = useStore()
      pinia.userInfo = sessionAccount
  }

  // 进入写文页面，判断是否有权限
  if (to.path === '/write') {
      if (!sessionAccount) {
          alert('还没有登录，去登录吧')
          router.push('/login')
      }
  }

  window.document.title = to.meta.title as string
  //到新页面要把页面滚动到最顶
  window.scrollTo({
      top: 0,
  })
  next()


  // if (to.meta.loading) {
  //   app.config.globalProperties.$loading.showLoading();
  //   next();
  // } else {
  //   next();
  // }

});

router.afterEach((to, from) => {
  // if (to.meta.loading) {
  //   app.config.globalProperties.$loading.hideLoading();
  // }
});
export default router;
