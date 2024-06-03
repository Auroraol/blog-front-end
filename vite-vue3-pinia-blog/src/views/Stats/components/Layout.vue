<script setup lang="ts">
</script>

<template>
  <div class="layout">
    <h1 class="title">
      <slot></slot>
    </h1>
  </div>
</template>

<style scoped lang="less">
.layout {
  margin: 0 auto;
  margin-bottom: 20px;
  background-color: #fff;

  // 标题
  .title {
    padding: 30px 0;
    text-align: center;
    font-family: LongZhuTi;
    font-weight: 100;
    font-size: 32px;
  }
}
</style>
