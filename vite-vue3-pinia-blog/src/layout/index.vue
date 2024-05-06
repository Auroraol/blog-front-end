<template>
  <el-container class="app-wrapper" :class="classObj">
    <div
      v-if="device === 'mobile' && sidebar.opened"
      class="drawer-bg"
      @click="handleClickOutside"
    />
    <!-- 左侧侧边栏 -->
    <el-aside class="sidebar-container"><layout-menu /> </el-aside>
    <el-container>
      <el-header>
        <!-- 导航栏 -->
        <navbar></navbar>
      </el-header>
      <el-main><app-main /></el-main>
    </el-container>
  </el-container>
</template>


<script setup lang="ts">
import { layoutMenu, AppMain, Navbar } from "./components/index";
import { useAppStore, useSettingsStore } from "/@/store/index";

const useAppStorePinia = useAppStore();
const useSettingsStorePinia = useSettingsStore();

const sidebar = computed(() => useAppStorePinia.sidebar);
const device = computed(() => useAppStorePinia.device);

const fixedHeader = computed(() => useSettingsStorePinia.fixedHeader);

//
const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === "mobile",
}));

function handleClickOutside() {
  useAppStorePinia.closeSidebar(false);
}
</script>
<style lang="less" scoped>
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
  background: #fff;
  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .sidebar-container {
    width: 200px;

    @media screen and (max-width: 960px) {
      width: 0px;
    }
  }
}
</style>