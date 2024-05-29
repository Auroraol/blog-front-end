<!-- Navigation.vue -->
<template>
  <el-affix>
    <!-- 手机端 -->
    <el-row class="nav-mobile" v-if="device !== 'desktop'">
      <el-col :span="24">
        <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
          background-color="transparent"
          active-text-color="#79bbff"
        >
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="info" style="cursor: default">
            <el-dropdown size="large" @command="handleSelect">
              <span class="more"> 更多 </span>
              <template #dropdown>
                <el-dropdown-menu style="width: 80px">
                  <el-dropdown-item command="/category">
                    <el-text>分类</el-text>
                  </el-dropdown-item>
                  <el-dropdown-item command="/write"> 文章 </el-dropdown-item>
                  <el-dropdown-item command="/archives">归档</el-dropdown-item>
                  <el-dropdown-item command="/message">留言</el-dropdown-item>
                  <el-dropdown-item command="/friend-link"
                    >友链</el-dropdown-item
                  >
                  <el-dropdown-item command="/about">关于</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-menu-item>

          <el-menu-item
            index="info"
            class="nav-user-info"
            style="cursor: default"
          >
            <el-button-group v-if="!userInfo">
              <el-button @click="toLogin">登录</el-button>
              <el-button @click="toRegister" color="#79BBDC">注册</el-button>
            </el-button-group>
            <!-- 组件nav-user-info -->
            <nav-user-info :userInfo="userInfo" v-else></nav-user-info>
          </el-menu-item>
        </el-menu>
      </el-col>
    </el-row>
    <!-- 电脑端 -->
    <el-row class="nav" v-if="device === 'desktop'">
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
              class="nav-img"
              src="https://poile-img.nos-eastchina1.126.net/logo.png"
            />
            <el-text class="logo-title" style="font-size: 1.4rem" type="primary"
              >悦读博客</el-text
            >

            <el-menu-item index="/" style="margin-left: 110px">
              <el-icon><House /></el-icon> 首页
            </el-menu-item>
            <el-menu-item index="/category">
              <el-icon><document /></el-icon>
              分类
            </el-menu-item>
            <el-menu-item index="/write">
              <el-icon><Reading /></el-icon>
              文章
            </el-menu-item>
            <el-menu-item index="/archives">
              <el-icon><Files /></el-icon>
              归档
            </el-menu-item>

            <el-menu-item index="/message">
              <el-icon><ChatLineRound /></el-icon>
              留言
            </el-menu-item>
            <el-menu-item index="/friend-link">
              <el-icon><Link /></el-icon>
              友链
            </el-menu-item>

            <el-menu-item index="/about">
              <el-icon><Warning /></el-icon>
              关于
            </el-menu-item>
            <!-- 搜索框 -->
            <el-input
              style="width: 350px; padding: 0 70px"
              v-model="keyword"
              placeholder="搜索文章"
              @focus="inputFocus"
              @blur="inputBlur"
              @keyup.enter.native="search"
              suffix-icon="el-icon-search"
            ></el-input>
            <el-menu-item index="info" style="cursor: default">
              <el-button-group v-if="!userInfo">
                <el-button @click="toLogin"
                  ><span class="but"> 登录 </span></el-button
                >
                <el-button @click="toRegister" color="#79BBDC">
                  <span class="but"> 注册 </span></el-button
                >
              </el-button-group>
              <!-- 组件nav-user-info -->
              <nav-user-info :userInfo="userInfo" v-else></nav-user-info>
            </el-menu-item>
          </el-menu>
        </div>
      </el-col>
    </el-row>
  </el-affix>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { mapState } from "pinia";
import { useGetters } from "/@/store/getters";
import { useUserStore } from "/@/store/index";
import { getAccessToken } from "/@/utils/auth";

const useUserPinia = useUserStore();
const useGettersPinia = useGetters();
const router = useRouter();
const activeIndex = ref("");
//搜索响应数据
const keyword = ref("");
const inputIconColor = ref("");

// 计算属性
const userInfo = computed(() => {
  const info = useGettersPinia.userInfo;
  return Object.keys(info).length === 0 ? null : info;
});

const device = computed(() => useGettersPinia.device);

const checkUserInfo = async () => {
  // //判断是否是登录状态
  if (getAccessToken()) {
    try {
      await useUserPinia.getUserInfo();
    } catch (error) {
      console.error(error);
    }
  }
};

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

//搜索
// 搜索框聚焦
const inputFocus = () => {
  inputIconColor.value = "#1989fa";
};

// 搜索框失焦
const inputBlur = () => {
  inputIconColor.value = "";
};

// 搜索
const search = () => {
  const keywordValue = keyword.value;
  if (keywordValue) {
    router.push({
      path: "/search",
      query: { keyword: keywordValue },
    });
  }
};
</script>
<style scoped lang="less">
.nav {
  background-color: #2580b3;
  // width: 100%;
  height: 60px;

  backdrop-filter: blur(5px);
  // transition: background-color $move;

  .nav-img {
    width: 100px;
    height: 50px;

    @media screen and (max-width: 1024px) {
      display: none;
    }
  }

  // 平板
  .logo-title {
    @media screen and (max-width: 1024px) {
      display: none;
    }
  }
}

.el-menu {
  border-right: none;
}

// 去除自带的底部border
.el-menu.el-menu--horizontal {
  border: none;
}

.el-menu-demo,
.el-menu-item,
.el-sub-menu,
.logo-title,
.more {
  display: flex;
  align-items: center;
  // font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  //   "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-family: "LXGW Wenkai";
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

:deep(:focus-visible) {
  outline: none;
}
.but {
  font-family: "LXGW Wenkai";
  font-weight: normal;
  font-size: 15px;
}
// 手机端布局
.nav-mobile {
  background-color: #2580b3;
  display: flex;
  justify-content: space-between; // 让子元素在父元素两端对齐

  .el-menu-demo {
    flex-grow: 1; // 让菜单占据剩余空间

    .nav-user-info {
      margin-left: auto; // 将组件移到最右侧
    }
  }
}
</style>
