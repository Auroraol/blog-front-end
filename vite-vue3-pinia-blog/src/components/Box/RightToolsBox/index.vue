<template>
  <div class="articleRight">
    <!-- 推荐文章 -->
    <right-side-recommend></right-side-recommend>
    <!-- 标签 -->
    <right-side-tags></right-side-tags>
    <!-- 最新评论 -->
    <right-side-comment></right-side-comment>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";

import gsap from "gsap";

const router = useRouter();

onMounted(() => {
  //动画
  gsap.from(".articleRight", {
    duration: 0.5,
    x: 50,
    opacity: 0.2,
  });
});
</script>

<style scoped lang="less">
.articleRight {
  width: 25rem;
  margin-left: 0.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

@media screen and (max-width: 800px) {
  .articleRight {
    display: none;
    width: 80%;
    margin: 0 auto;
    margin-top: 2rem;
  }
}
</style>