<template>
  <div :class="{ 'has-logo': showLogo }">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
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
// import variables from "@/styles/variables.scss";
import { useAppStore, useSettingsStore } from "/@/store/index";
import { RouteLocationNormalized, useRoute } from "vue-router";

const useAppStorePinia = useAppStore();
const useSettingsStorePinia = useSettingsStore();

const sidebar = computed(() => useAppStorePinia.sidebar);

const route: RouteLocationNormalized = useRoute();

// 活动菜单
const activeMenu = () => {
  const { meta, path } = route;
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
  return sidebar.value.opened;
};

// 样式变量
// const variables = () => {
//   return variables;
// };
</script>

/*
一个侧边栏菜单，根据条件显示一个 logo，并使用了 Element UI 库中的一些组件和属性。
侧边栏菜单的内容通过 permission_routes 数组来动态生成，每个菜单项使用了 sidebar-item 组件进行渲染。
 */