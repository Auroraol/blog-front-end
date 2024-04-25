<template>
  <div :class="{ 'has-logo': showLogo }">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        class="menu"
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="false"
        :collapse-transition="false"
      >
        <!-- 侧边栏项目 -->
        <sidebar-item
          v-for="route in permission_routes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>
<script setup lang="ts">
import Logo from "./Logo.vue";
import SidebarItem from "./SidebarItem.vue";
import {
  useAppStore,
  useSettingsStore,
  usePermissionStore,
} from "/@/store/index";
import { RouteLocationNormalized, useRoute } from "vue-router";

const useAppStorePinia = useAppStore();
const useSettingsStorePinia = useSettingsStore();
const usePermissionStorePinia = usePermissionStore();

const sidebar = computed(() => useAppStorePinia.sidebar);
const permission_routes = computed(() => usePermissionStorePinia.routes);

const routeMenu = useRoute();
// 活动菜单
const activeMenu = () => {
  const { meta, path } = routeMenu;
  if (meta.activeMenu) {
    return meta.activeMenu;
  }
  return path;
};

// 显示logo
const showLogo = () => {
  return useSettingsStorePinia.sidebarLogo;
};

// 导航栏收缩
const isCollapse = () => {
  return !sidebar.value.opened;
};
</script>

<style lang="less" scoped>
// 引入LESS文件，或者直接在这里定义LESS变量
@import "/@/assets/styles/variables.less";
.menu {
  .el-menu {
    background-color: @menuBg;
    color: @menuText;
  }

  .el-menu-item.is-active {
    color: @menuActiveText;
  }

  .el-menu-item:hover {
    background-color: @menuHover;
  }
}
</style>
/*
一个侧边栏菜单，根据条件显示一个 logo，并使用了 Element UI 库中的一些组件和属性。
侧边栏菜单的内容通过 permission_routes 数组来动态生成，每个菜单项使用了 sidebar-item 组件进行渲染。
 */