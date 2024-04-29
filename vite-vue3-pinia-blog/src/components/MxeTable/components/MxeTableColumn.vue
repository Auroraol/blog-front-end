<script setup>
import { omit } from "lodash-es";
// 引入 lodash 库中的 omit 方法，用于排除对象中的特定属性
//它允许你创建一个不包含指定属性的新对象副本。当想要过滤掉某些属性而保留其他属性时特别有用。

const props = defineProps({
  columns: {
    type: Array,
    default: () => [],
  },
});
</script>
 
<template>
  <!-- 使用 el-table-column 组件来定义表格列 -->
  <!-- 使用 v-for 指令遍历 columns 数组中的每一个元素，并渲染对应的列 -->
  <!-- 使用 v-bind 指令动态绑定属性值，排除 column 对象中的 children 属性 -->
  <el-table-column
    v-for="column in columns"
    v-bind="omit(column, ['children'])"
  >
    <!-- <template #header="scope">是 el-table-column 组件自带的 用于自定义列头-->
    <template #header="scope">
      {{ column.label }}
      <!-- 自定义插槽 -->
      <!-- 使用 v-bind 指令绑定作用域对象到插槽中 -->
      <slot
        v-if="column?.slots?.header"
        :name="column.slots.header"
        v-bind="scope"
      />
    </template>
    <!-- <template #default="scope">是 el-table-column 组件自带的 用于自定义列内容-->
    <template #default="scope">
      <!-- 自定义插槽 -->
      <slot
        v-if="column?.slots?.default && !column.children?.length"
        :name="column.slots.default"
        v-bind="scope"
      />
      <template v-else-if="column.children?.length">
        <!-- 如果当前列有子列，则递归调用 MxeTableColumn 组件 -->
        <!-- 递归调用, .vue文件名 是非index的，可以直接使用文件名调用自己 -->
        <MxeTableColumn :columns="column.children">
          <!-- 遍历当前组件的插槽，并将作用域对象传递给插槽 -->
          <template
            v-for="name in Object.keys($slots)"
            :key="name"
            #[name]="scope"
          >
            <!-- 使用插槽渲染内容，并绑定作用域对象 -->
            <slot :name="name" v-bind="scope" />
          </template>
        </MxeTableColumn>
      </template>
    </template>
  </el-table-column>
</template>
 
<style scoped></style>