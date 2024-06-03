import { pathToRegexp } from "path-to-regexp";

import router from "/@/route/router";
import { usePermissionStore, useUserStore } from "/@/store/index";
import { useGetters } from "/@/store/getters";

import NProgress from "nprogress"; // 进度条
import "nprogress/nprogress.css"; //进度条样式必须引入

import { getAccessToken } from "/@/utils/auth";
import getPageTitle from "/@/utils/get-page-title";
import { bindEmail } from "/@/api/user/user";

NProgress.configure({
  // 动画方式
  easing: "ease",
  showSpinner: false,
  // 递增进度条的速度
  speed: 500,
  // 自动递增间隔
  trickleSpeed: 200,
  // 更改启动时使用的最小百分比
  minimum: 0.1,
});

// 免登录白名单(不强制登录的)
const whiteList = [
  "/",
  "/index",
  "/tag",
  "/category",
  "/archives",
  "/message",
  "/friend-link",
  "/about",
  "/article/:id",
  "/reset-password",
  "/login-register/login",
  "/login-register/register",
  "/about",
  "/search",
  "/terms",
  "/privacy",
  "/email-bind",
  "/404",
  "/oauth",
];

// 路由守卫
router.beforeEach(async (to, from, next) => {
  // 百度统计
  if (window.location.hostname === "http://101.37.165.220/" && to.path) {
    // eslint-disable-next-line
    if (window._hmt) {
      window._hmt.push(["_trackPageview", "/#" + to.fullPath]);
    } else {
      // eslint-disable-next-line
      var _hmt = _hmt || [];
      // eslint-disable-next-line
      window._hmt = _hmt;
      (function () {
        let hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?0365897af075de8b1b3ba64f3cc7b423";
        let s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
      })();
    }
  }

  // 进度条开始
  NProgress.start();

  // 获取AccessToken，判断是否已登录
  const hasAccessToken = getAccessToken();

  // 是否邮箱绑定路由, 邮箱绑定成功如果有token则刷新用户信息
  // 并且跳转用户信息页，没token跳回首页，绑定失败或没获取到code跳404
  if (pathToRegexp("/email-bind").exec(to.path)) {
    const code = to.query.code;
    if (code) {
      const params = { code: code };
      let result = true;
      await bindEmail(params).then(
        (res) => {
          result = true;
        },
        (error) => {
          result = false;
        }
      );
      if (!result) {
        next("/404");
        NProgress.done();
      }
      if (result && hasAccessToken) {
        // 获取用户线信息，从用户信息中获取角色
        const { roles } = await useUserStore().getUserInfo();
        // 根据角色动态加载路由
        const accessRoutes = await usePermissionStore().generateRoutes(roles);
        // 添加动态路由
        accessRoutes.forEach((res) => {
          router.addRoute(res);
        });
        next("/user/info");
        NProgress.done();
      }
      if (result && !hasAccessToken) {
        next("/");
        NProgress.done();
      }
    }
  }

  // 页面标题
  document.title = getPageTitle(to.meta.title);

  if (hasAccessToken) {
    // 获取角色，判断是否已调获取用户信息接口
    const hasRoles = useGetters().roles && useGetters().roles.length > 0;

    if (hasRoles) {
      next();
    } else {
      // 管理员用户
      try {
        // 获取用户线信息，从用户信息中获取角色
        const { roles } = await useUserStore().getUserInfo();

        // 根据角色动态加载路由
        const accessRoutes = await usePermissionStore().generateRoutes(roles);

        // 添加动态路由
        accessRoutes.forEach((res) => {
          router.addRoute(res);
        });

        next({ ...to, replace: true });
      } catch (error) {
        // 获取用户信息失败重置登录信息
        await useUserStore().resetToken();
        ElMessage.error(error || "Has Error");
        // 跳回首页
        next("/");
        NProgress.done();
      }
    }
  } else {
    // 未登录，并且路径存在免登录白名单中
    const included = whiteList.some((ele) => pathToRegexp(ele).exec(to.path));
    if (included) {
      next();
    } else {
      // const path = to.matched.length === 0 ? "/404" : "/";  //=0表示当前路由没有匹配到任何路由记录
      // next(path);
      // 进入写文页面，判断是否有权限 // 进入聊天室页面，判断是否有权限
      if (to.path === "/write" || to.path === "/chat") {
        alert("还没有登录，去登录吧");
        router.push("/login-register/login");
      }
      // 转到404
      next();
      NProgress.done();
    }
  }
});

//
router.afterEach(() => {
  // 进度条结束
  NProgress.done();
});

/*
项目配置 permission.ts
1、导入进度条、路由、element-plus组件
2、定义白名单
*/
