<template>
  <Suspense>
    <template #default>
     <async-category></async-category>
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
const AsyncCategory = defineAsyncComponent(
  () => import("./components/category.vue")
);
</script>