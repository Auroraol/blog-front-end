<template>
  <el-config-provider :locale="zhCn">
    <!-- 路由视图 -->
    <router-view></router-view>
  </el-config-provider>
  <foot></foot>
</template>

<script setup>
// 面包屑 不需要引入因为已经在 vite.config.ts 中引入了Components
// import breadcrumb from "/@/components/breadcrumb.vue";
import { defineComponent } from "vue";
import { ElConfigProvider } from "element-plus";
// import zhCn from "element-plus/lib/locale/lang/zh-cn";
import zhCn from "element-plus/es/locale/lang/zh-cn";
</script>
