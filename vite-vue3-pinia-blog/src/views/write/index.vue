<template>
  <Suspense>
    <template #default>
      <AsyncWrite></AsyncWrite>
    </template>
    <!-- 加载完成前的载入动画 -->
    <template #fallback>
      <div class="window">
        <loading class="winLoad"></loading>
      </div>
    </template>
  </Suspense>
</template>

<script setup lang="ts">
import { defineAsyncComponent } from "vue";
//Suspense要异步引入组件
const AsyncWrite = defineAsyncComponent(
  () => import("/@/views/write/components/Write.vue")
);
</script>

<style scoped lang="less">
//让加载动画居中显示
.window {
  position: relative; /* 父级元素相对定位 */
  width: 100%;
  height: 100%;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>