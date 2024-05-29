<template>
  <div class="item">
    <svg-icon class="svg-icon" v-if="icon" :name="icon" />
    <span v-if="title">{{ title }}</span>
  </div>
</template>

<script setup>
import { ref, watchEffect } from "vue";

const icon = ref("");
const title = ref("");

const props = defineProps({
  icon: {
    type: String,
    default: "",
  },
  title: {
    type: String,
    default: "",
  },
});

// 监听传入的属性变化
watchEffect(() => {
  if (props.icon) {
    icon.value = props.icon;
  }
  title.value = props.title;
});
</script>

<style scoped lang="less" >
.item {
  display: flex; /* 使用 flex 布局 */
  align-items: center; /* 垂直居中对齐 */

  .svg-icon {
    margin-top: -40px;
    margin-right: 8px; /* 调整图标与文本之间的间距 */
  }
}
</style>