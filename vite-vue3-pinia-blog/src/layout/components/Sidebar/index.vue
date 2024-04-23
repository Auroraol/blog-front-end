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
// import { mapGetters } from 'vuex';
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/styles/variables.scss";
import { RouteLocationNormalized, useRoute } from "vue-router";

const route: RouteLocationNormalized = useRoute();

const activeMenu = () => {
  const { meta, path } = route.value;
  if (meta.activeMenu) {
    return meta.activeMenu;
  }
  return path;
};

// const { permission_routes, sidebar } = mapGetters(['permission_routes', 'sidebar']);

const showLogo = () => {
  return $store.state.settings.sidebarLogo;
};

const isCollapse = () => {
  return !$store.state.sidebar.opened;
};

export {
  Logo,
  SidebarItem,
  activeMenu,
  permission_routes,
  sidebar,
  showLogo,
  variables,
  isCollapse,
};
</script>