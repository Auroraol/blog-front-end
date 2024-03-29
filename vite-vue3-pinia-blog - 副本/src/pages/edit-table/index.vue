<template>
  <!-- 可编辑表格V3 -->
  <div id="hello">
    <!-- 表格 -->
    <p class="tips">单击 右键菜单，单击 左键编辑</p>
    <el-table
      :data="tableData"
      height="500px"
      stripe
      border
      style="width: 100%; margin-top: 10px"
      @cell-click="cellDblclick"
      @header-contextmenu="(column, event) => rightClick(null, column, event)"
      @row-contextmenu="rightClick"
      :row-class-name="tableRowClassName"
    >
      <el-table-column
        width="50"
        v-if="columnList.length > 0"
        type="index"
        :label="'No.'"
      />
      <el-table-column
        v-for="(col, idx) in columnList"
        :key="col.prop"
        :prop="col.prop"
        :label="col.label"
        :index="idx"
      />
    </el-table>

    <div>
      <h3 style="text-align: center">实时数据展示</h3>
      <label>当前目标：</label>
      <p>{{ JSON.stringify(curTarget) }}</p>
      <label>表头：</label>
      <p v-for="col in columnList" :key="col.prop">{{ JSON.stringify(col) }}</p>
      <label>数据：</label>
      <p v-for="(data, idx) in tableData" :key="idx">
        {{ JSON.stringify(data) }}
      </p>
    </div>

    <!-- 右键菜单框 -->
    <div v-show="showMenu" id="contextmenu" @mouseleave="showMenu = false">
      <p style="margin-bottom: 10px">列：</p>
      <el-button
        :icon="CaretTop"
        size="small"
        type="success"
        @click="addColumn()"
      >
        前方插入一列
      </el-button>
      <el-button
        :icon="CaretBottom"
        size="small"
        type="success"
        @click="addColumn(true)"
      >
        后方插入一列
      </el-button>
      <el-button
        :icon="DeleteFilled"
        type="success"
        size="small"
        @click="openColumnOrRowSpringFrame('列')"
      >
        删除当前列
      </el-button>
      <el-button
        size="small"
        type="success"
        @click="renameCol($event)"
        :icon="EditPen"
      >
        更改列名
      </el-button>

      <div style="color: #ccc">--------------</div>

      <p style="margin-bottom: 12px">行：</p>
      <el-button
        :icon="CaretTop"
        size="small"
        type="success"
        @click="addRow()"
        v-show="!curTarget.isHead"
      >
        上方插入一行
      </el-button>
      <el-button
        :icon="CaretBottom"
        size="small"
        type="success"
        @click="addRow(true)"
        v-show="!curTarget.isHead"
      >
        下方插入一行
      </el-button>
      <el-button
        :icon="DeleteFilled"
        size="small"
        type="success"
        @click="addRowHead(true)"
        v-show="curTarget.isHead"
      >
        下方插入一行
      </el-button>
      <el-button
        type="success"
        :icon="DeleteFilled"
        size="small"
        @click="openColumnOrRowSpringFrame('行')"
        v-show="!curTarget.isHead"
      >
        删除当前行
      </el-button>
    </div>

    <!-- 单元格/表头内容编辑框 -->
    <div v-show="showEditInput" id="editInput">
      <el-input
        ref="iptRef"
        placeholder="请输入内容"
        v-model="curTarget.val"
        clearable
        @change="updTbCellOrHeader"
        @blur="showEditInput = false"
        @keyup="onKeyUp($event)"
      >
        <template #prepend>{{ curColumn.label || curColumn.prop }}</template>
      </el-input>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, toRefs } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  DeleteFilled,
  EditPen,
  CaretBottom,
  CaretTop,
} from "@element-plus/icons-vue";

const state = reactive({
  columnList: [
    { prop: "name", label: "姓名" },
    { prop: "age", label: "年龄" },
    { prop: "city", label: "城市" },
    { prop: "tel", label: "电话" },
  ],
  tableData: [
    { name: "张三", age: 24, city: "广州", tel: "13312345678" },
    { name: "李四", age: 25, city: "九江", tel: "18899998888" },
  ],

  showMenu: false, // 显示右键菜单
  showEditInput: false, // 显示单元格/表头内容编辑输入框
  curTarget: {
    // 当前目标信息
    rowIdx: null, // 行下标
    colIdx: null, // 列下标
    val: null, // 单元格内容/列名
    isHead: undefined, // 当前目标是表头？
  },
  countCol: 0, // 新建列计数
});
const iptRef = ref(null);
const { columnList, tableData, showMenu, showEditInput, curTarget, countCol } =
  toRefs(state);
const curColumn = computed(() => {
  return columnList.value[curTarget.value.colIdx] || {};
});

// 删除当前列或当前行
const openColumnOrRowSpringFrame = (type) => {
  ElMessageBox.confirm(
    `此操作将永久删除该 ${type === "列" ? "列" : "行"}, 是否继续 ?, '提示'`,
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(() => {
      if (type === "列") {
        delColumn();
      } else if (type === "行") {
        delRow();
      }
      ElMessage({
        type: "success",
        message: "删除成功!",
      });
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "已取消删除",
      });
    });
};

// 回车键关闭编辑框
const onKeyUp = (e) => {
  if (e.key === "Enter") {
    showEditInput.value = false;
  }
};

// 单元格双击事件 - 更改单元格数值
const cellDblclick = (row, column, cell, event) => {
  showEditInput.value = false;
  if (column.index == null) return;
  locateMenuOrEditInput("editInput", 200, event); // 编辑框定位
  showEditInput.value = true;
  iptRef.value.focus();

  // 当前目标
  curTarget.value = {
    rowIdx: row.row_index,
    colIdx: column.index,
    val: row[column.property],
    isHead: false,
  };
};

// 单元格/表头右击事件 - 打开菜单
const rightClick = (row, column, event) => {
  // 阻止浏览器自带的右键菜单弹出
  event.preventDefault(); // window.event.returnValue = false
  showMenu.value = false;
  if (column.index == null) return;
  locateMenuOrEditInput("contextmenu", 140, event); // 菜单定位
  showMenu.value = true;
  // 当前目标
  curTarget.value = {
    rowIdx: row ? row.row_index : null, // 目标行下标，表头无 row_index
    colIdx: column.index, // 目标项下标
    val: row ? row[column.property] : column.label, // 目标值，表头记录名称
    isHead: !row,
  };
};

// 更改列名
const renameCol = ($event) => {
  showEditInput.value = false;
  if (curTarget.value.colIdx === null) return;
  locateMenuOrEditInput("editInput", 200, $event); // 编辑框定位
  showEditInput.value = true;
  iptRef.value.focus();
};

// 更改单元格内容/列名
const updTbCellOrHeader = (val) => {
  if (!curTarget.value.isHead)
    // 更改单元格内容
    tableData.value[curTarget.value.rowIdx][curColumn.value.prop] = val;
  else {
    // 更改列名
    if (!val) return;
    columnList.value[curTarget.value.colIdx].label = val;
  }
};

// 新增行
const addRow = (later) => {
  showMenu.value = false;
  const idx = later ? curTarget.value.rowIdx + 1 : curTarget.value.rowIdx;
  let obj = {};
  columnList.value.forEach((p) => (obj[p.prop] = ""));
  tableData.value.splice(idx, 0, obj);
};

// 表头下新增行
const addRowHead = () => {
  // 关闭菜单
  showMenu.value = false;
  // 新增行
  let obj = {};
  // 初始化行数据
  columnList.value.forEach((p) => (obj[p.prop] = ""));
  // 插入行数据
  tableData.value.unshift(obj);
};

// 删除行
const delRow = () => {
  showMenu.value = false;
  curTarget.value.rowIdx !== null &&
    tableData.value.splice(curTarget.value.rowIdx, 1);
};

// 新增列
const addColumn = (later) => {
  showMenu.value = false;
  const idx = later ? curTarget.value.colIdx + 1 : curTarget.value.colIdx;
  const colStr = { prop: "col_" + ++countCol.value, label: "" };
  columnList.value.splice(idx, 0, colStr);
  tableData.value.forEach((p) => (p[colStr.prop] = ""));
};

// 删除列
const delColumn = () => {
  showMenu.value = false;
  tableData.value.forEach((p) => {
    delete p[curColumn.value.prop];
  });
  columnList.value.splice(curTarget.value.colIdx, 1);
};

// 添加表格行下标
const tableRowClassName = ({ row, rowIndex }) => {
  row.row_index = rowIndex;
};

// 定位菜单/编辑框
const locateMenuOrEditInput = (eleId, eleWidth, event) => {
  let ele = document.getElementById(eleId);
  ele.style.top = event.clientY - 30 + "px";
  ele.style.left = event.clientX - 100 + "px";
  if (window.innerWidth - eleWidth < event.clientX) {
    ele.style.left = "unset";
    ele.style.right = 0;
  }
};
</script>

<style lang="scss" scoped>
#hello {
  position: relative;
  height: 100%;
  width: 100%;
}

.tips {
  margin-top: 10px;
  color: #999;
  font-style: italic;
  font-size: 13px;
}

#contextmenu {
  position: absolute;
  z-index: 999;
  top: 0;
  left: 0;
  height: auto;
  width: 136px;
  border-radius: 3px;
  filter: drop-shadow(-1px 0 20px rgba(0, 0, 0, 0.2));
  background-color: #fff;
  border-radius: 6px;
  padding: 15px 10px 14px 12px;

  button {
    display: block;
    margin: 0 0 5px;
  }
  :deep(el-button) {
    color: red;
  }
}

.hideContextMenu {
  position: absolute;
  top: -40px;
  right: -5px;
}

#editInput,
#headereditInput {
  position: absolute;
  z-index: 999;
  top: 0;
  left: 0;
  height: auto;
  min-width: 200px;
  max-width: 400px;
  padding: 0;
}
</style>
