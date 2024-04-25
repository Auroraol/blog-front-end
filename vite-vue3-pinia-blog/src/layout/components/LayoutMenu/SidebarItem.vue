<template>
  <!--如果item.hidden设置了true，则不在左边导航栏显示,即不是后台管理的页面  -->
  <div v-if="!props.item.hidden" class="menu-wrapper">
    <template
      v-if="
        hasOneShowingChild(props.item.children, props.item) &&
        (!onlyOneChild.children || onlyOneChild.noShowingChildren) &&
        !props.item.alwaysShow
      "
    >
      <!-- <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item
          :index="resolvePath(onlyOneChild.path)"
          :class="{ 'submenu-title-noDropdown': !isNest }"
        >
          <item
            :icon="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"
            :title="onlyOneChild.meta.title"
          />
        </el-menu-item>
      </app-link> -->
    </template>

    <!--  -->
    <el-sub-menu
      v-else
      ref="subMenu"
      :index="resolvePath(props.item.path)"
      popper-append-to-body
    >
      <template>
        <item
          v-if="props.item.meta"
          :icon="props.item.meta && props.item.meta.icon"
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
  </div>
</template>

<script setup lang="ts">
// import path from "path";
import { isExternal } from "/@/utils/validate";

import Item from "./Item.vue";
// import FixiOSBug from "./FixiOSBug.vue";

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

const onlyOneChild = ref(null);

onMounted(() => {
  if (!props.item.hidden) {
    if (props.item.children) {
      console.error(props.basePath);
      console.error(props.item);
      console.error(props.item.path);
      console.error(props.item.children[0].path);
      console.error(resolvePath(props.item.children[0].path));
    }
  }
});

// 有孩子节点
const hasOneShowingChild = (children = [], parent) => {
  const showingChildren = children.filter((item) => {
    if (item.hidden) {
      return false;
    } else {
      onlyOneChild.value = item;
      return true;
    }
  });

  if (showingChildren.length === 1) {
    return true;
  }

  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: "", noShowingChildren: true };
    return true;
  }

  return false;
};

//绝对路径
function resolvePath(routePath) {
  if (isExternal(routePath)) {
    return routePath;
  }
  if (isExternal(props.basePath)) {
    return props.basePath;
  }
  return "/@" + props.basePath + "/" + routePath;
}
</script>