<template>
  <div class="navbar">
    <!-- 菜单收缩展开按钮 -->
    <hamburger
      class="hamburger-container"
      :is-active="sidebar.opened"
      @toggleClick="toggleSideBar"
    />
    <!-- 后台管理面包屑 -->
    <breadcrumb class="breadcrumb-container" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { updatePassword } from "/@/api/user/user";
import { useAppStore, useUserStore, useSettingsStore } from "/@/store/index";
import Breadcrumb from "./Breadcrumb/index.vue";
import Hamburger from "./Hamburger/index.vue";

const useAppStorePinia = useAppStore();
const sidebar = computed(() => useAppStorePinia.sidebar);

// 切换SideBar
const toggleSideBar = () => {
  useAppStorePinia.toggleSidebar();
};
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

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

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
