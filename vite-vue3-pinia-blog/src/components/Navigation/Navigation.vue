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
            active-text-color="#fff"
          >
            <!--  active-text-color="var(--color)" -->
            <img
              class="nav-img"
              src="https://poile-img.nos-eastchina1.126.net/logo.png"
            />
            <!-- 图片LOGO模式 -->
            <router-link to="/" class="one_item_nav" v-if="false">
            </router-link>
            <!-- 文字LOGO模式 -->
            <router-link v-else to="/" class="one_item_nav">
              <b>
                <el-text
                  class="logo-title hvr-grow"
                  style="font-size: 1.4rem"
                  type="primary"
                  >悦读博客</el-text
                ></b
              >
            </router-link>

            <el-menu-item index="/" style="margin-left: 110px">
              <!-- <el-icon><House /></el-icon> -->
              🏠 首页
            </el-menu-item>
            <el-menu-item index="/category">
              <!-- <el-icon><document /></el-icon> -->
              🗂️ 分类
            </el-menu-item>
            <el-menu-item index="/write">
              <!-- <el-icon><Reading /></el-icon> -->
              ✍️ 文章
            </el-menu-item>
            <el-menu-item index="/archives">
              <!-- <el-icon><Files /></el-icon> -->
              🗄️ 归档
            </el-menu-item>

            <el-menu-item index="/message">
              <!-- <el-icon><ChatLineRound /></el-icon> -->
              🏄‍♂️ 留言
            </el-menu-item>
            <el-menu-item index="/friend-link">
              <!-- <el-icon><Link /></el-icon> -->
              🌎 友链
            </el-menu-item>

            <!-- <el-menu-item index="/about">
              <el-icon><Warning /></el-icon> 
              🎗️ 关于
            </el-menu-item> -->
            <ul class="one">
              <!-- <li class="one_item">
              </li> -->
              <!-- 扩展页面 -->
              <li class="one_item">
                <router-link :to="ExtendPage.url" class="one_item_nav">
                  {{ ExtendPage.icon }} {{ ExtendPage.name }}

                  <!-- 判断有没有二级分类，有就显示下拉箭头 -->
                  <svg
                    v-if="ExtendPage.children.length"
                    preserveAspectRatio="xMidYMid meet"
                    xmlns="http://www.w3.org/2000/svg"
                    width="1em"
                    height="1em"
                    fill="none"
                    viewBox="0 0 48 48"
                  >
                    <!--?lit$369315321$-->
                    <g>
                      <path
                        stroke-linejoin="round"
                        stroke-width="4"
                        stroke="currentColor"
                        d="M36 18 24 30 12 18"
                        data-follow-stroke="currentColor"
                      ></path>
                    </g>
                  </svg>
                </router-link>

                <!-- 二级导航 -->
                <ul class="two">
                  <li
                    class="two_item"
                    v-for="two in ExtendPage.children"
                    :key="two.id"
                  >
                    <router-link
                      :to="{ path: two.url, query: two.date }"
                      class="two_item_nav"
                      >{{ two.name }}</router-link
                    >
                  </li>
                </ul>
              </li>
            </ul>
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
import { isEmpty } from "/@/utils/isEmpty";

const useUserPinia = useUserStore();
const useGettersPinia = useGetters();
const router = useRouter();
const activeIndex = ref("");
//搜索响应数据
const keyword = ref("");
const inputIconColor = ref("");

//
const ExtendPage = {
  name: "扩展",
  icon: "💡",
  url: "",
  children: [
    {
      id: 1,
      name: "我的相册",
      url: "/photo",
    },
    {
      id: 2,
      name: "聊天室",
      url: "/chat",
      date: {
        name: "默认房间",
      },
    },
    {
      id: 3,
      name: "关于",
      url: "/about",
    },
  ],
};

// 计算属性
const userInfo = computed(() => {
  const info = useGettersPinia.userInfo;
  if (isEmpty(info)) {
    return null;
  }
  // return Object.keys(info).length === 0 ? null : info;
  return info;
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
  // transition: background-color 0.3s;
  z-index: 999;

  .nav-img {
    width: 100px;
    height: 50px;

    @media screen and (max-width: 1024px) {
      display: none;
    }
  }
  /* Grow */
  .hvr-grow {
    display: inline-block;
    vertical-align: middle;
    -webkit-transform: perspective(1px) translateZ(0);
    transform: perspective(1px) translateZ(0);
    box-shadow: 0 0 1px rgba(0, 0, 0, 0);
    -webkit-transition-duration: 0.3s;
    transition-duration: 0.3s;
    -webkit-transition-property: transform;
    transition-property: transform;
  }
  .hvr-grow:hover,
  .hvr-grow:focus,
  .hvr-grow:active {
    -webkit-transform: scale(1.1);
    transform: scale(1.1);
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
  font-family: "LXGW Wenkai";
  font-weight: bold;
  font-size: 15px;
  color: #fff;
}

.el-menu-demo,
.el-menu-item,
.el-sub-menu {
  // 鼠标悬停效果
  &:hover {
    color: @color !important;
  }
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

// 一级导航
.one {
  display: flex;
  align-items: center;
  height: 60px;

  // 导航列表
  .one_item {
    position: relative;
    // 导航
    .one_item_nav {
      display: inline-block;
      padding: 20px;
      // color: #333;
      color: #fff;
      font-size: 15px;
      transition: color 0.3s;
      // 鼠标悬停效果
      &:hover {
        color: @color !important;
      }

      .down {
        color: #fff;
      }
    }

    // 二级导航
    .two {
      display: none;
      overflow: hidden;
      position: absolute;
      top: 50px;
      width: 100%;

      border-radius: 5px;
      background-color: #f9f9f9;
      box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1), 0 2px 6px rgba(0, 0, 0, 0.08);

      .two_item {
        .two_item_nav {
          position: relative;
          display: inline-block;
          width: 100%;
          padding: 10px;
          padding-left: 10px;
          font-size: 15px;
          box-sizing: border-box;
          color: #666;
          transition: all 0.3s;

          // 鼠标经过的小横线
          &::after {
            content: "";
            position: absolute;
            left: 10px;
            top: 50%;
            transform: translateY(-50%);
            width: 0;
            height: 3px;
            background-color: @color;
            transition: width 0.3s;
          }
        }

        // 鼠标经过二级导航的效果
        &:hover .two_item_nav {
          color: @color !important;
          background-color: #f2f2f2;
          padding-left: 30px;

          &:hover::after {
            width: 10px;
          }
        }
      }
    }

    // 鼠标经过哪个，就让哪个二级导航显示
    &:hover .two {
      display: block;
    }
  }
}
</style>
