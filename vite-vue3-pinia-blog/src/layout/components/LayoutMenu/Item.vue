<template>
  <div class="app">{ renderItems() }</div>
</template>

<script setup lang="ts">
import { defineProps, h, VNode } from "vue";
// import SvgIcon from "/@/components/SvgIcon/index.vue";
const vnodes: VNode[] = [];

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

function renderItems() {
  if (props.icon) {
    vnodes.push(h("svg-icon", { props: { iconClass: props.icon } }));
  }

  if (props.title) {
    vnodes.push(h("span", { slot: "title" }, props.title));
  }

  return vnodes;
}
</script>
