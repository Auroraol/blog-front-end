// <template>
  //
  <div :class="classObj" class="app-wrapper">
    // <div // v-if="device === 'mobile' && sidebar.opened" // class="drawer-bg"
    // @click="handleClickOutside" // /> //
    <!-- 侧边栏 -->
    //
    <!-- <sidebar class="sidebar-container" /> -->
    //
    <div class="main-container">
      //
      <div :class="{ 'fixed-header': fixedHeader }">
        //
        <!-- 导航栏 -->
        // <navbar /> //
      </div>
      // <app-main /> //
    </div>
    //
  </div>
  //
</template>

<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="200px">Aside</el-aside>
      <el-container>
        <el-header>Header</el-header>
        <el-main>Main</el-main>
      </el-container>
    </el-container>
  </div>
</template>



<script setup lang="ts">
// import { Navbar, Sidebar, AppMain } from "./components/index";
import { AppMain } from "./components/index";

// import ResizeMixin from "./mixin/ResizeHandler";
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
// @import "/@/assets/styles/mixin.scss";
// @import "/@/assets/styles/variables.css";
.app-wrapper {
  // @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;
  background: #fff;
  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
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

// .fixed-header {
//   position: fixed;
//   top: 0;
//   right: 0;
//   z-index: 9;
//   width: calc(100% - #{$sideBarWidth});
//   transition: width 0.28s;
// }

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.mobile .fixed-header {
  width: 100%;
}
</style>
