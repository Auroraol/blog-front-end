<template>
  <div :class="{ 'has-logo': showLogo }">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        class="menu"
        :default-active="activeMenu"
        @select="handleSelect"
        :collapse="isCollapse"
        :collapse-transition="false"
      >
        <!-- 侧边栏项目 -->
        <sidebar-item
          v-for="route in filteredRoutes"
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
import { RouteLocationNormalized, useRouter } from "vue-router";

const useAppStorePinia = useAppStore();
const useSettingsStorePinia = useSettingsStore();
const usePermissionStorePinia = usePermissionStore();

const sidebar = computed(() => useAppStorePinia.sidebar);
const permission_routes = computed(() => usePermissionStorePinia.routes);
// 过滤隐藏的路由
const filteredRoutes = computed(() => {
  return permission_routes.value.filter((route) => !route.hidden);
});

// 导航栏收缩
const isCollapse = computed(() => {
  return !sidebar.value.opened;
});

//
const activeIndex = ref("");
const router = useRouter();

const handleSelect = (index: string) => {
  activeIndex.value = index; // 活动菜单
  router.push({ path: `${index}` });
};

// 显示logo
const showLogo = () => {
  return useSettingsStorePinia.sidebarLogo;
};
</script>

<style lang="less" scoped>
// 引入LESS文件，或者直接在这里定义LESS变量
@import "/@/assets/styles/variables.less";

.menu {
  @media screen and (max-width: 960px) {
    background: #fff;
    margin: 0 auto;
    // width: 90%;
  }

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