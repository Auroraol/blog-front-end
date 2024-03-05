<template>
  <div>
    <h2>当前路由信息</h2>
    <p>路径：{{ $route.path }}</p>
    <p>方法-接收参数: {{id}} -- {{title}}</p>
    <p>方法-接收默认参数: {{default}}</p>
  </div>
</template>


<script setup>
// 使用 props 直接接收路由参数
const props = defineProps(['id', 'title', 'default']);

</script>
