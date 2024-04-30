<template>
  <el-row class="content">
    <el-col :span="16" :xs="0" class="content-left"> </el-col>
    <el-col :span="8" :xs="24" class="content-right">
       <!-- 使用 transition 组件包裹 router-view -->
      <transition name="fade">
        <!-- 路由 -->
        <router-view />  
      </transition>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { ref } from "vue";
</script>

<style lang="less" scoped>
.content {
  // height: 644px;
    height: 618px;

  background-image: url("/image/imgs/1.png");
  // margin-bottom: -13px;
}

/* 淡入淡出的过渡效果*/
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active 在 <2.1.8 版本中 */ {
  opacity: 0;
}
</style>