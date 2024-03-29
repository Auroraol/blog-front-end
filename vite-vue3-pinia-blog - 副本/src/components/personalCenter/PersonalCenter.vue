<template>
  <div class="common-layout">
    <el-container>
      <el-container>
        <!--左侧-->
        <el-aside width="200px" class="aside">
          <el-menu
            background-color="#f3f3f3"
            default-active="2"
            class="el-menu-vertical"
            :collapse="isCollapse"
            @open="handleOpen"
            @close="handleClose"
          >
            <el-menu-item index="1">
              <template #title>
                <el-icon><CreditCard /></el-icon>
                <span>个人资料</span>
              </template>
            </el-menu-item>
            <el-menu-item index="2">
              <template #title>
                <el-icon><Setting /></el-icon>
                <span>安全设置</span>
              </template>
            </el-menu-item>

            <el-sub-menu index="3">
              <template #title>
                <el-icon><Document /></el-icon>
                <span>文章管理</span>
              </template>
              <el-menu-item index="3-1"
                ><el-icon><Finished /></el-icon>
                <router-link to="/about">文章管理</router-link>
              </el-menu-item>
              <el-menu-item index="3-2"
                ><el-icon><Collection /></el-icon>收藏的文章</el-menu-item
              >
              <el-menu-item index="3-3"
                ><el-icon><Menu /></el-icon> 分类管理</el-menu-item
              >
            </el-sub-menu>

            <el-menu-item index="4">
              <template #title>
                <el-icon><ChatLineSquare /></el-icon>
                <span>评论留言</span>
              </template>
            </el-menu-item>
            <el-menu-item index="5">
              <template #title>
                <el-icon><Link /></el-icon>
                <span>友链简理</span>
              </template>
            </el-menu-item>
            <el-menu-item index="6">
              <template #title>
                <el-icon><EditPen /></el-icon>
                <span>悄悄话</span>
              </template>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <!--右侧-->
        <el-main class="main">
          <!-- 路由视图 -->
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, defineExpose } from "vue";
import { Management } from "@element-plus/icons-vue";

const isCollapse = ref(false);

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath);
};
const handleClose = (key, keyPath) => {
  console.log(key, keyPath);
};

defineExpose({
  isCollapse,
});
</script>

<style scoped lang="less">
.aside {
  margin-left: 30px;
}

.el-menu-vertical,
.el-menu-vertical:not(.el-menu--collapse) {
  height: 100vh;
}

.el-menu-vertical {
  font-size: 14px;
  border-radius: 0.5rem;
}

.main {
  border-radius: 0.5rem;
  min-height: 650px;
  background: #ffffff;
  margin-left: 20px;
  margin-right: 30px;
}
</style>