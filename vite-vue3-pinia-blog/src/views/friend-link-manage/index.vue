<template>
  <div class="container">
    <!-- 头部 -->
    <div class="head">
      <el-button type="primary" size="small" @click="dialogVisible = true"
        >+ New FriendLink</el-button
      >
    </div>

    <mxe-table
      v-loading="loading"
      :data="tableData"
      :columns="columns"
      highlight-current-row
      v-model:pageIndex="pageNum"
      v-model:pageSize="pageSize"
      :total="total"
      @getList="getList"
    >
      <template #opera1="scope">
        <el-button type="success" size="small"
          ><a :href="scope.row.url" target="_blank">打开</a>
        </el-button>
        <el-button
          type="primary"
          size="small"
          @click="handleEdit(scope.$index, scope.row)"
          >编辑</el-button
        >
        <el-button
          size="small"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)"
          >删除</el-button
        >
      </template>
    </mxe-table>

    <el-dialog
      title="保存友链"
      top="25vh"
      width="400px"
      v-model="dialogVisible"
      :close-on-click-modal="false"
      :show-close="false"
      :lock-scroll="false"
      @closed="handleClosed"
    >
      <el-form ref="formRef" label-position="left" :model="form" :rules="rules">
        <el-form-item prop="name">
          <el-input v-model="form.name" placeholder="输入名称" />
        </el-form-item>
        <el-form-item prop="url">
          <el-input v-model="form.url" placeholder="输入链接" />
        </el-form-item>
        <el-form-item prop="icon">
          <el-input v-model="form.icon" placeholder="输入图标" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="medium" @click="dialogVisible = false"
          >取 消</el-button
        >
        <el-button size="medium" type="primary" @click="saveSubmit"
          >确 定</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref } from "vue";
import {
  saveFriendLink,
  pageFriendLink,
  deleteFriendLink,
} from "/@/api/friend-link/friend-link";

const loading = ref(true);
const dialogVisible = ref(false);
const formRef = ref(null); // 创建表单引用
const form = ref({
  icon: "",
  id: null,
  name: "",
  url: "",
});
const rules = ref({
  name: [{ required: true, message: "请输入名称", trigger: "blur" }],
  url: [{ required: true, message: "请输入链接", trigger: "blur" }],
  icon: [{ required: true, message: "请输入链接", trigger: "blur" }],
});

//分页
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
    prop: "name",
  },
  {
    label: "链接",
    prop: "url",
  },
  {
    label: "图标",
    prop: "icon",
  },
  {
    label: "操作",
    slots: {
      default: "opera1",
    },
  },
];

onMounted(() => {
  loadData();
});

const getList = () => {
  loadData();
};

// 编辑
const handleEdit = (index, row) => {
  form.value = JSON.parse(JSON.stringify(row));
  dialogVisible.value = true;
};

// 删除
const handleDelete = (index, row) => {
  ElMessageBox.confirm("确定删除该友链吗", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    showClose: false,
    type: "warning",
  })
    .then(() => {
      deleteFriendLink(row.id).then((res) => {
        ElMessage({
          message: "删除成功",
          type: "success",
        });
        pageNum.value = 1;
        loadData();
      });
    })
    .catch(() => {});
};

// 弹框关闭
const handleClosed = () => {
  form.value.id = null;
  form.value.icon = "";
  form.value.name = "";
  form.value.url = "";
};
// 保存提交
const saveSubmit = () => {
  formRef.value.form.validate((valid) => {
    if (valid) {
      saveFriendLink(form.value).then((res) => {
        ElMessage({
          message: "保存成功",
          type: "success",
        });
        dialogVisible.value = false;
        pageNum.value = 1;
        loadData();
      });
    }
  });
};

// 加载数据
const loadData = () => {
  loading.value = true;
  const params = {
    current: pageNum.value,
    size: pageSize.value,
  };
  pageFriendLink(params).then(
    (res) => {
      total.value = res.total;
      tableData.value = res.records;
      loading.value = false;
    },
    (error) => {
      console.error(error);
      loading.value = false;
    }
  );
};
</script>


<style lang="less" scoped>
.container {
  width: 100%;

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
    text-align: center;
    margin-top: 10px;
  }
}
</style>
