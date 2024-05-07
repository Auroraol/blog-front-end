<template>
  <!--如果item.hidden设置了true，则不在左边导航栏显示,即不是后台管理的页面  -->
  <!-- <div v-if="!props.item.hidden" class="menu-wrapper">  element最好是不要包裹div-->
  <!-- 没有子节点 -->
  <template
    v-if="
      hasOneShowingChild(props.item.children, props.item) &&
      (!onlyOneChild.children || onlyOneChild.noShowingChildren) &&
      !props.item.alwaysShow
    "
  >
    <el-menu-item
      v-if="onlyOneChild.meta"
      :index="resolvePath(onlyOneChild.path)"
      :class="{ 'submenu-title-noDropdown': !isNest }"
    >
      <item
        :icon="
          onlyOneChild.meta.icon || (props.item.meta && props.item.meta.icon)
        "
        :title="onlyOneChild.meta.title"
      />
    </el-menu-item>
  </template>

  <!-- 有子节点 -->
  <el-sub-menu
    v-else
    ref="subMenu"
    :index="resolvePath(props.item.path)"
    popper-append-to-body
  >
    <template #title>
      <item
        v-if="props.item.meta"
        :icon="props.item.meta.icon"
        :title="props.item.meta.title"
      />
    </template>
    <!-- 侧边栏项目 -->
    <sidebar-item
      v-for="child in props.item.children"
      :key="child.path"
      :is-nest="true"
      :item="child"
      :base-path="resolvePath(child.path)"
      class="nest-menu"
    />
  </el-sub-menu>
  <!-- </div> -->
</template>

<script setup lang="ts">
import { isExternal } from "/@/utils/validate";
import { defineProps, ref, watch, onMounted, watchEffect } from "vue";

import Item from "./Item.vue";
import AppLink from "./Link.vue";

const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
  isNest: {
    type: Boolean,
    default: false,
  },
  basePath: {
    type: String,
    default: "",
  },
});

const onlyOneChild = ref(null); //子

/**
 * 有孩子返回 false
 * 无孩子返回 true
 */
const hasOneShowingChild = (children = [], parent) => {
  // 从所有子节点中过滤出显示的子节点 //hidden=false的路由节点
  // 过滤隐藏的路由
  const showingChildren = children.filter((item) => {
    if (item.hidden) {
      // 如果子节点设置为隐藏，则不包含在显示子节点列表中
      return false;
    } else {
      onlyOneChild.value = item; //
      return true;
    }
  });

  // 如果只有一个显示子节点，则返回 true
  if (showingChildren.length === 1) {
    return true;
  }

  // 如果没有显示子节点，则创建一个特殊节点并返回 true
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: "", noShowingChildren: true };

    return true;
  }

  // 如果有多个显示子节点，则返回 false
  return false;
};

//绝对路径
const resolvePath = (routePath) => {
  if (isExternal(routePath)) {
    return routePath;
  }
  if (isExternal(props.basePath)) {
    return props.basePath;
  }
  return props.basePath + "/" + routePath;
};
</script>