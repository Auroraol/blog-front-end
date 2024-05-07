<template>
  <div class="container">
    <!-- 头部 -->
    <div class="head">
      <dynamic-input
        placeholder="输入名称"
        content="+ New Tag"
        @inputConfirm="inputConfirm"
      />
    </div>
    <mxe-table
      :data="tableData"
      :columns="columns"
      highlight-current-row
      v-model:pageIndex="pageNum"
      v-model:pageSize="pageSize"
      :total="total"
      @getList="getList"
    >
      <template #opera1="scope">
        <span v-if="!scope.row.edit">{{ scope.row.name }}</span>
        <el-input
          v-else
          v-model="tempTag.name"
          size="small"
          placeholder="输入名称"
        />
      </template>

      <template #opera2="scope">
        <div class="button-container">
          <el-button
            v-if="!scope.row.edit"
            type="primary"
            size="small"
            @click="handleEdit(scope.row)"
            >编辑</el-button
          >
          <span class="button-container2" v-else>
            <el-button type="info" size="small" @click="scope.row.edit = false"
              >取消</el-button
            >
            <el-button type="success" size="small" @click="saveSubmit"
              >保存</el-button
            >
          </span>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </div>
      </template>
    </mxe-table>
  </div>
</template>
<script setup>
import { ref, reactive } from "vue";
import DynamicInput from "/@/components/DynamicInput/index.vue";
import { addTag, pageTag, updateTag, deleteTag } from "/@/api/tag/tag";

const loading = ref(true);
const tempTag = ref(null);

// 分页
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);

//  表格数据
const tableData = ref([]);
const columns = [
  {
    label: "ID",
    prop: "id",
  },
  {
    label: "名称",
    slots: {
      default: "opera1",
    },
  },
  {
    label: "操作",
    slots: {
      default: "opera2",
    },
  },
];

onMounted(() => {
  loadData();
});

// 重新获取分页数据
const getList = () => {
  loadData();
};

// 加载数据
const loadData = () => {
  loading.value = true;
  const params = {
    current: pageNum.value,
    size: pageSize.value,
  };
  pageTag(params).then(
    (res) => {
      total.value = res.total;
      const records = res.records.map((ele) => {
        ele.edit = false;
        return ele;
      });
      tableData.value = records;
      loading.value = false;
    },
    (error) => {
      console.error(error);
      loading.value = false;
    }
  );
};

// 编辑
const handleEdit = (row) => {
  tempTag.value = JSON.parse(JSON.stringify(row));
  row.edit = true;
};

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm("确定删除该标签吗", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    showClose: false,
    type: "warning",
  })
    .then(() => {
      console.error(row.id);
      deleteTag(row.id).then((res) => {
        ElMessage.success("删除成功");
        pageNum.value = 1;
        loadData();
      });
    })
    .catch(() => {
      // TODO
    });
};

// 编辑保存
const saveSubmit = () => {
  if (!tempTag.value || tempTag.value.name === "") {
    ElMessage.error("没有内容呢");
    return;
  }
  const params = { id: tempTag.value.id, name: tempTag.value.name };
  updateTag(params).then((res) => {
    ElMessage.success("修改成功");
    pageNum.value = 1;
    loadData();
  });
};

// 动态输入框回车事件
const inputConfirm = (val) => {
  if (!val) {
    ElMessage.error("标签名称不能为空");
    return;
  }
  const params = { tagName: val };
  addTag(params).then((res) => {
    ElMessage.success("新增成功");
    pageNum.value = 1;
    loadData();
  });
};
</script>


<style lang="less" scoped>
.container {
  text-align: center;

  /* 适应手机端屏幕 */
  @media screen and (max-width: 768px) {
    .button-container {
      display: flex;
      align-items: center;
      padding-right: 10px;
    }
    .button-container2 {
      display: flex;
      align-items: center;
    }
    .el-button + .el-button {
      margin-left: 4px;
    }
  }
  .head {
    height: 50px;
    background: #d0d0d0;
    display: flex;
    align-items: center;
    flex-direction: row-reverse;
    padding-right: 10px;
  }

  .el-table {
    margin: 10px;
  }

  .el-pagination {
    margin-top: 10px;
  }
}
</style>
