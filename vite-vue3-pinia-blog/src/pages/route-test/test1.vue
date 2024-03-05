<template>
  <div>
    <h2>当前路由信息</h2>
    <p>路径：{{ currentRoutePath }}</p>
  </div>
</template>



<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const currentRoutePath = ref('');
currentRoutePath.value = route.path;
console.log('当前路由路径：', currentRoutePath.value);

</script>

// script setup 中使用