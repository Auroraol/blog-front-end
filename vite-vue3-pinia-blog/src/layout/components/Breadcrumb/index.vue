<!-- 后台管理面包屑 -->
<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <span
          v-if="item.redirect === 'noRedirect' || index == levelList.length - 1"
          class="no-redirect"
          >{{ item.meta.title }}</span
        >
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import { Router } from "vue-router";
import * as pathToRegexp from "path-to-regexp";

const router = useRouter();

// const levelList = ref<RouteLocationNormalized[]>([]);
const levelList = ref([]);

onMounted(() => {
  getBreadcrumb();
});

const getBreadcrumb = () => {
  // 只取标题
  const matched = router.currentRoute.value.matched.filter(
    (item) => item.meta && item.meta.title
  );
  levelList.value = matched.filter(
    (item) => item.meta && item.meta.title && item.meta.breadcrumb !== false
  );
};

const pathCompile = (path: string) => {
  // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
  const params = router.currentRoute.value.params;
  //pathToRegexp 把字符串转为正则表达式
  //pathToRegexp.compile() 快速填充 url 字符串的参数值
  const toPath = pathToRegexp.compile(path);
  return toPath(params);
};

const handleLink = (item: any) => {
  const { redirect, path } = item;
  if (redirect) {
    router.push(redirect);
    return;
  }
  router.push(pathCompile(path));
};

watch(() => router.currentRoute.value.path, getBreadcrumb);
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
