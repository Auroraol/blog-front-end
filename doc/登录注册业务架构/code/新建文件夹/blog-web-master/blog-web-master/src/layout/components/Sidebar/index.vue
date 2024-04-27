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
        <template v-for="route in filteredRoutes">
          <sidebar-item
            :key="route.path"
            :item="route"
            :base-path="route.path"
          />
          <template v-if="route.children">
            <template v-for="childRoute in route.children">
              <sidebar-item
                :key="childRoute.path"
                :item="childRoute"
                :base-path="childRoute.path"
              />
            </template>
          </template>
        </template>
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
import { computed } from "vue";

const useAppStorePinia = useAppStore();
const useSettingsStorePinia = useSettingsStore();
const usePermissionStorePinia = usePermissionStore();

const sidebar = computed(() => useAppStorePinia.sidebar);
const permission_routes = computed(() => usePermissionStorePinia.routes);

const routeMenu = useRoute();
// 活动菜单
const activeMenu = computed(() => {
  const { meta, path } = routeMenu.value;
  if (meta.activeMenu) {
    return meta.activeMenu;
  }
  return path;
});

// 显示logo
const showLogo = computed(() => {
  return useSettingsStorePinia.sidebarLogo;
});

// 导航栏收缩
const isCollapse = computed(() => {
  return !sidebar.value.opened;
});
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