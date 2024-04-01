<!-- Navigation.vue -->
<template>
  <el-affix>
    <!-- 手机端 -->
    <el-row class="nav" v-if="isMobile">
      <el-col :span="24">
        <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
          background-color="transparent"
          active-text-color="#79bbff"
        >
          <el-menu-item index="/index">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-shouye1"></use>
            </svg>
            首页
          </el-menu-item>
          <el-sub-menu index="">
            <template #title>更多</template>
            <el-menu-item index="/article">文章</el-menu-item>
            <el-menu-item index="/leavemsg">留言</el-menu-item>
            <el-menu-item index="/write">写文</el-menu-item>
            <el-menu-item>
              <router-link :to="{ path: '/chat', query: { name: '默认房间' } }">
                聊天室</router-link
              >
            </el-menu-item>
            <el-menu-item index="/more">关于</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-col>
    </el-row>
    <!-- 电脑端 -->
    <el-row class="nav" v-if="!isMobile">
      <el-col :span="24">
        <div class="grid-content">
          <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="transparent"
            active-text-color="#79bbff"
          >
            <img
              style="width: 100px; height: 50px"
              src="https://poile-img.nos-eastchina1.126.net/logo.png"
            />
            <el-text class="logo-title" type="primary">悦读博客</el-text>

            <el-menu-item index="/index" style="margin-left: 130px">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-shouye1"></use>
              </svg>
              首页
            </el-menu-item>

            <el-menu-item index="/article">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-wenzhang1"></use></svg
              >文章
            </el-menu-item>
            <el-menu-item index="/leavemsg">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-rili"></use></svg
              >留言
            </el-menu-item>
            <el-menu-item index="/write">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-24"></use></svg
              >写文
            </el-menu-item>
            <el-menu-item>
              <router-link :to="{ path: '/chat', query: { name: '默认房间' } }">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-liaotian"></use></svg
                >聊天室</router-link
              >
            </el-menu-item>
            <el-menu-item index="/about">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-gengduo4"></use></svg
              >关于
            </el-menu-item>
            <!-- 搜索框 -->
            <el-autocomplete
              style="padding: 0 70px"
              v-model="state"
              :fetch-suggestions="querySearchAsync"
              placeholder="搜索"
              @select="handleSelect"
              class="search"
              suffix-icon="el-icon-search"
            ></el-autocomplete>
            <el-menu-item index="info">
              <!-- <div class="nologin" style="margin-bottom: 22px">
               
              </div> -->
              <el-button-group v-if="ifLog">
                <el-button @click="toLogin">登录</el-button>
                <el-button @click="toRegister" color="#79BBDC">注册</el-button>
              </el-button-group>
              <!-- <div v-else class="logined">
          </div> -->
              <!-- 组件nav-user-info -->
              <nav-user-info v-else></nav-user-info>
            </el-menu-item>
          </el-menu>
        </div>
      </el-col>
    </el-row>
  </el-affix>
</template>

<script setup lang="ts">
// import { SortUp } from "@element-plus/icons-vue/dist/types";
import { useRouter } from "vue-router";
// import { useStore } from "/@/store";
// import { getAccessToken, getRefreshToken, getUserAccountInfo, removeUserAccountInfo  } from '/@/utils/network/auth.js'

const router = useRouter();
const activeIndex = ref("1");

const handleSelect = (index: string) => {
  activeIndex.value = index;
  if (index === "info") {
    // 某个页面
  } else {
    router.push({ path: `${index}` });
  }
};

//跳转到登录
const toLogin = () => {
  router.push("/login-register/login");
};

//跳转到注册
const toRegister = () => {
  router.push("/login-register/register");
};

const ifLog = ref(true); //当它是false时是已登录
// const pinia = useStore();
const isMobile = ref(false); //根据页面响应使用哪个样式的标题栏

const getScreen = () => {
  let screenWidth = document.body.clientWidth;
  let screenHeight = document.body.clientHeight;
  return screenWidth / screenHeight;
};
const listenScreen = () => {
  let initScale = getScreen();
  if (initScale < 1) {
    isMobile.value = true;
  }
  window.addEventListener("resize", () => {
    isMobile.value = getScreen() < 1 ? true : false;
  });
};

// 在页面加载时从本地存储中获取用户信息并设置到 Pinia 中
const initializeUserInfo = () => {
  // const storedUserInfo = localStorage.getItem("userAccount");
  // const storedUserInfo = getUserAccountInfo();
  // if (storedUserInfo) {
  //   pinia.setUserInfo(storedUserInfo);
  // }
};

// 监听
watchEffect(async () => {
  listenScreen();
  // 登录了!修改状态
  // if (pinia.getUserInfo()) {
  //   ifLog.value = false;
  // } else {
  //   ifLog.value = true;
  // }
});

onMounted(() => {
  // 页面加载时初始化用户信息
  initializeUserInfo();
});
</script>
<style scoped lang="less">
.nav {
  background-color: #2580b3;
}

.el-menu {
  border-right: none;
}

// 去除自带的底部border
.el-menu.el-menu--horizontal {
  border: none;
}

.el-menu-demo {
  display: flex;
  align-items: center;
}

.el-menu-item,
.el-sub-menu,
.logo-title {
  display: flex;
  align-items: center;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-weight: bold;
  font-size: 15px;
  color: #fff;
}

/* 点击出来的下划线进行隐藏 */
.el-menu-item.is-active {
  border-bottom: none !important;
  color: rgba(0, 0, 0, 0);
}

/* 点击出来的下划线动效残留进行隐藏 */
.el-menu--horizontal > .el-menu-item {
  border-bottom: none;
}

/* 点击以后的背景色进行隐藏 */
.el-menu--horizontal > .el-menu-item:not(.is-disabled):focus,
.el-menu--horizontal > .el-menu-item:not(.is-disabled):hover,
.el-menu--horizontal > .el-submenu .el-submenu__title:hover {
  background-color: rgba(0, 0, 0, 0);
}

.login {
  color: #fff;
}
</style>