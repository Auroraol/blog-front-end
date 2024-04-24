<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item
        v-for="(description, index) in descriptions"
        :key="description"
        :to="{ name: routeNames[index] }"
        class="breadcrumb-item"
      >
        {{ description }}
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup lang="ts">
import { getNames, getDescriptions, getRouteByName } from "./handleRoutes";
import { useRouter, useRoute, RouteRecordNormalized } from "vue-router";

const router = useRouter();
const route = useRoute();
let descriptions = ref<string[]>([]);
let routeNames = ref<string[]>([]);

//获取面包屑的数据
routeNames.value = getNames(route.name as string, router.getRoutes());
descriptions.value = getDescriptions(routeNames.value, router.getRoutes());

//在每次路由跳转后，更新面包屑的数据
router.afterEach((to, from) => {
  //不知道为什么，进入页面时，会有两次路由跳转，route.name为空
  if (!route.name) {
    console.log(route.name, route.meta);
    return;
  }

  routeNames.value = getNames(route.name as string, router.getRoutes());
  descriptions.value = getDescriptions(routeNames.value, router.getRoutes());
});

onMounted(() => {
  console.error(routeNames);
});
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
</style>
