<script setup>
import { ref, computed } from "vue";
import MxeTableColumn from "./components/MxeTableColumn.vue";
import { useGetters } from "/@/store/getters";

const useGettersPinia = useGetters();

const device = computed(() => useGettersPinia.device);

// 父->子
const props = defineProps({
  data: {
    type: Array,
    default: () => [],
  },
  columns: {
    type: Array,
    default: () => [],
  },
  pageIndex: {
    type: Number,
  },
  pageSize: {
    type: Number,
  },
  total: {
    type: Number,
  },
});

// 子->父
const emits = defineEmits(["update:pageIndex", "update:pageSize", "getList"]);

// 计算属性
const page = ref({
  pageIndex: computed({
    get: () => props.pageIndex,
    set(pageIndex) {
      emits("update:pageIndex", pageIndex);
    },
  }),
  pageSize: computed({
    get: () => props.pageSize,
    set(pageSize) {
      emits("update:pageSize", pageSize);
    },
  }),
});

// 分页
const handleSizeChange = () => {
  page.value.pageIndex = 1;
  console.error(page.value.pageIndex);
  emits("getList");
};
</script>
 
<template>
  <!-- v-bind="$attrs" 将不被props接收的参数都绑定到 el-table -->
  <div class="table-and-pager">
    <div class="table-box">
      <el-table :data="data" style="width: 100%; height: 100%" v-bind="$attrs">
        <mxe-table-column :columns="columns">
          <!-- 插槽透传, 不然, 如果是多级表头, 里层接收不到 -->
          <!-- $slots 是所有传进来的插槽的信息 -->
          <template
            v-for="name in Object.keys($slots)"
            :key="name"
            #[name]="scope"
          >
            <slot :name="name" v-bind="scope" />
          </template>
        </mxe-table-column>
        <template #empty>
          <!-- 给一个空组件 -->
          <div>空空如也</div>
        </template>
      </el-table>
    </div>
    <!-- 分页 -->
    <div
      class="pager-box"
      v-if="device === 'desktop' && props.pageIndex && props.pageSize"
    >
      <el-pagination
        v-model:current-page="page.pageIndex"
        v-model:page-size="page.pageSize"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, prev, pager, next, sizes, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="emits('getList')"
      />
    </div>
    <div class="pager-box" v-else>
      <el-pagination
        background
        layout="prev, pager, next"
        v-model:page-size="page.pageSize"
        v-model:current-page="page.pageIndex"
        :total="total"
        :pager-count="4"
        :hide-on-single-page="true"
        @current-change="emits('getList')"
      />
    </div>
  </div>
</template>
 
<style scoped lang="less">
.table-and-pager {
  width: 100%;
  height: 100%;
  .table-box {
    width: 100%;
    height: calc(100% - 50px);
    // 当有表位有合计行时, 改变其背景颜色, 使用与表格主体颜色统一一点
    :deep(.el-table tfoot td.el-table__cell) {
      background: #fff;
    }
  }
  .pager-box {
    width: 100%;
    height: 50px;
    display: flex;
    justify-content: flex-end;
    align-items: center;
  }
}
</style>