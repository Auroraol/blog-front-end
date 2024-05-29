<template>
  <el-row
    class="content"
    :style="{ backgroundImage: `url(/image/imgs/1.png)` }"
  >
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
  min-height: 710px;
  margin-bottom: -15px;

  background: url("") center;
  background-size: cover;

  // 标签加载之后
  &::after {
    content: "";
    width: 100%;
    height: 20%;
  }
}

/* 淡入淡出的过渡效果*/
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active 在 <2.1.8 版本中 */ {
  opacity: 0;
}
</style>