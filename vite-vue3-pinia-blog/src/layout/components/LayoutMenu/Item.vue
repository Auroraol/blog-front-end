<template>
  <svg-icon v-if="icon" :iconClass="icon" />
  <span v-if="title">{{ title }}</span>
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

<style scoped>
span {
  margin-left: 16px;
}
</style>