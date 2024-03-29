<!-- 头部导航栏 -->
<template>
  <div class="main-header-box">
    <header class="main-header">
      <div class="container">
        <div class="logo">
          <!-- 手机端 -->
          <div v-if="device !== 'desktop'" class="menu-wrapper">
            <el-dropdown trigger="click" placement="bottom">
              <span style="color: #007fff; font-weight: 700">
                {{
                  navItemActive === 0
                    ? "首页"
                    : navItemActive === 1
                    ? "分类"
                    : navItemActive === 2
                    ? "归档"
                    : navItemActive === 3
                    ? "友链"
                    : navItemActive === 4
                    ? "留言"
                    : "首页"
                }}
                <i class="el-icon-caret-bottom" />
              </span>
              <el-dropdown-menu slot="dropdown">
                <a
                  v-for="(nav, index) in navItems"
                  :key="index"
                  @click="$router.push(nav.to)"
                >
                  <el-dropdown-item>{{ nav.name }}</el-dropdown-item>
                </a>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <img
            class="logo-img"
            src="https://poile-img.nos-eastchina1.126.net/logo.png"
          />
          <span class="logo-title">悦读博客</span>
        </div>

        <nav class="main-nav">
          <!-- 导航栏目 -->
          <ul class="main-nav-list">
            <li
              v-for="(nav, index) in navItems"
              :key="index"
              class="main-nav-item"
              :class="{ 'main-nav-item-active': navItemActive === index }"
              @click="$router.push(nav.to)"
            >
              {{ nav.name }}
            </li>
          </ul>
          <!-- 搜索框 -->
          <div class="search-box">
            <el-autocomplete
              v-if="!isMobile"
              style="padding: 0 70px"
              v-model="state"
              :fetch-suggestions="search"
              @focus="inputFocus"
              @blur="inputBlur"
              placeholder="搜索文章"
              class="search"
              suffix-icon="el-icon-search"
            ></el-autocomplete>
          </div>
          <!-- 右边box -->
          <div class="right-box">
            <!-- 登录·注册 -->
            <div v-if="!userInfo" class="nologin">
              <div class="login-btn" @click="loClick">登录</div>
              <div class="reg-btn" @click="reClick">注册</div>
            </div>
            <div v-else class="logined">
              <router-link to="/user" class="console">个人中心</router-link>
              <el-dropdown trigger="click" placement="bottom">
                <div class="avatar-wrapper">
                  <img
                    :src="userInfo.avatar || defaultAvatar"
                    class="user-avatar"
                  />
                </div>
                <el-dropdown-menu slot="dropdown">
                  <router-link v-if="!userInfo.mobile" to="/bind-mobile">
                    <el-dropdown-item>绑定手机号</el-dropdown-item>
                  </router-link>
                  <router-link v-if="!userInfo.email" to="/email-validate">
                    <el-dropdown-item>绑定邮箱</el-dropdown-item>
                  </router-link>
                  <el-dropdown-item>
                    <span style="display: block" @click="logout">退 出</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
        </nav>
      </div>
    </header>
    <register-dialog ref="reDialog" />
    <login-dialog ref="loDialog" />
  </div>
</template>
<script setup>
import RegisterDialog from "./RegisterDialog";
import LoginDialog from "./LoginDialog";

defineProps({
  navItemActive: {
    type: Number,
    default: 0,
  },
});

// // 计算属性
// const doubleCount = computed(() =>
//   mapGetters(["userInfo", "defaultAvatar", "device"])
// );

const keyword = "";
const inputIconColor = "";
const navItems = [
  { name: "首页", to: "/" },
  { name: "分类", to: "/category" },
  { name: "归档", to: "/archives" },
  { name: "友链", to: "/friend-link" },
  { name: "留言", to: "/message" },
];

const { userInfo, defaultAvatar, device } = mapGetters([
  "userInfo",
  "defaultAvatar",
  "device",
]);

onMounted(() => {
  if (props.$route.path === "/search") {
    keyword.value = props.$route.query && props.$route.query.keyword;
  }
});

const drawerClose = () => {
  // 实现抽屉关闭的逻辑
};

const inputFocus = () => {
  // 实现搜索框聚焦的逻辑
  inputIconColor.value = "#1989fa";
};

const inputBlur = () => {
  // 实现搜索框失焦的逻辑
  inputIconColor.value = "";
};

const reClick = () => {
  // 实现注册点击的逻辑
  this.$refs.reDialog.open();
};

const loClick = () => {
  // 实现登录点击的逻辑
  this.$store.commit("login/CHANGE_VISIBLE", true);
};

const logout = () => {
  // 实现退出的逻辑
  this.$store.dispatch("user/logout").then((res) => {
    this.$router.push("/");
  });
};

const search = () => {
  // 实现搜索的逻辑
  const keywordValue = keyword.value;
  if (keywordValue) {
    this.$router.push({
      path: "/search",
      query: {
        keyword: keywordValue,
      },
    });
  }
};
</script>

<style lang="scss" scoped>
.main-header-box {
  height: 60px;
  width: 100%;
  position: relative;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);

  .main-header {
    width: 100%;
    position: fixed;
    top: 0;
    left: 0;
    background: #fff;
    border-bottom: 1px solid #f1f1f1;
    color: #909090;
    height: 60px;
    z-index: 250;

    .container {
      height: 100%;
      max-width: 100%;
      width: 960px;
      margin: auto;
      display: flex;
      align-items: center;
      overflow: hidden;

      @media screen and (max-width: 922px) {
        padding: 0 15px;
      }

      .logo {
        min-width: 80px;
        color: #007fff;
        display: flex;
        align-items: center;

        .menu-icon {
          margin: 5px;
          margin-right: 10px;
          color: #444;
          width: 25px;
          height: 25px;
        }

        .logo-img {
          width: 32px;
          height: 32px;

          @media screen and (max-width: 922px) {
            width: 25px;
            height: 25px;
          }
        }

        .logo-title {
          font-size: 20px;
          display: inline-block;
          font-weight: bold;
          padding-left: 5px;

          @media screen and (max-width: 922px) {
            font-size: 18px;
          }
        }
      }

      .main-nav {
        flex: 1;
        height: 100%;
        align-items: center;
        display: flex;
        flex-wrap: nowrap;

        @media screen and (max-width: 922px) {
          justify-content: flex-end;
        }

        .main-nav-list {
          margin: 0;
          display: flex;
          align-items: center;
          cursor: pointer;

          .main-nav-item {
            list-style: none;
            line-height: 60px;
            padding-right: 18px;
            transition: all 0.3s;

            &:hover {
              color: #007fff;
            }
          }

          .main-nav-item-active {
            color: #007fff;
          }

          @media screen and (max-width: 922px) {
            display: none;
          }
        }

        .search-box {
          display: flex;
          align-items: center;
          flex-direction: row-reverse;
          width: 290px;
          box-sizing: border-box;
          padding-left: 130px;

          /deep/ .el-input__inner {
            background: #f4f5f5;
          }

          .el-icon-search {
            font-size: 18px;
            cursor: pointer;
          }

          @media screen and (max-width: 922px) {
            display: none;
          }
        }

        .right-box {
          display: flex;
          align-items: center;
          width: 150px;
          flex-direction: row-reverse;

          @media screen and (max-width: 922px) {
            width: auto;
          }

          .nologin {
            height: 100%;
            color: #007fff;
            display: flex;
            align-items: center;

            .login-btn {
              cursor: pointer;
              line-height: 60px;

              &:after {
                content: "·";
                font-weight: bold;
                margin: 0 5px;
              }
            }

            .reg-btn {
              cursor: pointer;
              line-height: 60px;
            }
          }

          .logined {
            height: 100%;
            color: #007fff;
            font-weight: 600;
            display: flex;
            align-items: center;

            .console {
              font-size: 12px;
              font-weight: 600;
              padding: 0 8px;
              margin-right: 8px;
              border-right: 1px solid hsla(0, 0%, 59.2%, 0.2);

              @media screen and (max-width: 922px) {
                font-weight: 700;
              }
            }

            .avatar-wrapper {
              margin-top: 5px;
              position: relative;

              .user-avatar {
                cursor: pointer;
                width: 40px;
                height: 40px;
                border-radius: 50%;
                border: 1px solid rgba(0, 0, 0, 0.1);
              }
            }
          }
        }
      }
    }
  }
}
</style>
