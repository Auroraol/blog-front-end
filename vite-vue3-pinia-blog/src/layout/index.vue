<template>
  <el-container class="app-wrapper">
    <!-- 左侧侧边栏 -->
    <el-aside class="sidebar-container" :style="{ width: sidebarWidth }">
      <layout-menu />
    </el-aside>
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

const sidebarWidth = computed(() => {
  if (device.value === "mobile" && sidebar.value.opened) {
    return "200px";
  } else {
    return sidebar.value.opened ? "200px" : "0px";
  }
});
</script>
<style lang="less" scoped>
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
  background: #fff;

  .sidebar-container {
    transition: width 0.3s ease; /* 添加过渡效果 */
  }
}
</style>