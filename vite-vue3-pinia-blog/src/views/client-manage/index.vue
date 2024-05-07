<template>
  <div class="container">
    <!-- 头部 -->
    <div class="head">
      <el-button type="primary" size="small" @click="dialogVisible = true"
        >+Add Client</el-button
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
        <el-switch
          v-model="scope.row.enable"
          active-color="#13ce66"
          inactive-color="#ff4949"
          @change="enableChange(scope.row)"
        />
      </template>
      <template #opera2="scope">
        <div class="button-container">
          <el-button size="small" type="primary" @click="handleEdit(scope.row)"
            >编辑</el-button
          >
          <el-button size="small" type="danger" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </div>
      </template>
    </mxe-table>

    <el-dialog
      title="保存客户端"
      top="25vh"
      width="350px"
      v-model="dialogVisible"
      :close-on-click-modal="false"
      :show-close="false"
      :lock-scroll="false"
      @closed="handleClosed"
    >
      <el-form ref="formRef" label-position="left" :model="form" :rules="rules">
        <el-form-item prop="clientId">
          <el-input v-model="form.clientId" placeholder="输入客户端ID" />
        </el-form-item>
        <el-form-item prop="clientSecret">
          <el-input v-model="form.clientSecret" placeholder="输入客户端秘钥" />
        </el-form-item>
        <el-form-item prop="accessTokenExpire">
          <el-input
            v-model="form.accessTokenExpire"
            type="number"
            placeholder="输入AccessToken时效(小时: 60L * 60* n)"
          />
        </el-form-item>
        <el-form-item>
          <el-switch
            v-model="form.enable"
            size="large"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="启用refresh"
            inactive-text="禁用refresh"
          />
        </el-form-item>
        <el-form-item v-if="form.enable" prop="refreshTokenExpire">
          <el-input
            v-model="form.refreshTokenExpire"
            type="number"
            placeholder="输入RefreshToken时效(天: 24*60L*60*n)"
          />
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

<script setup lang="ts">
import { ref } from "vue";
import {
  pageClient,
  saveClient as apiSaveClient,
  deleteClient,
} from "/@/api/client/client";

const loading = ref(true);
const dialogVisible = ref(false); //显示对话框

//表格
const formRef = ref();
const form = ref({
  clientId: "",
  clientSecret: "",
  accessTokenExpire: null,
  refreshTokenExpire: null,
  id: null,
  enableRefreshToken: 0,
  enable: false,
});

const rules = {
  clientId: [{ required: true, message: "请输入客户端ID", trigger: "blur" }],
  clientSecret: [
    {
      required: true,
      message: "请输入客户端秘钥",
      trigger: "blur",
    },
  ],
};

// 分页数据
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);

//  表格数据
const tableData = ref([]);
const columns = [
  {
    label: "客户端ID",
    prop: "clientId",
  },
  {
    label: "客户端密钥",
    prop: "clientSecret",
  },
  {
    label: "accessToken时效",
    prop: "accessTokenExpire",
  },
  {
    label: "refreshToken时效",
    prop: "refreshTokenExpire",
  },
  {
    label: "启用refreshToken",
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

const getList = () => {
  loadData();
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

// 弹框关闭
const handleClosed = () => {
  form.value.clientId = "";
  form.value.clientSecret = "";
  form.value.accessTokenExpire = null;
  form.value.refreshTokenExpire = null;
  form.value.id = null;
  form.value.enable = false;
  form.value.enableRefreshToken = 0;
};

// 编辑
const handleEdit = (row) => {
  form.value = JSON.parse(JSON.stringify(row)); //避免直接修改原始对象。
  dialogVisible.value = true;
};

const enableChange = (row) => {
  const enableRefreshToken = row.enable ? 1 : 0;
  const data = {
    id: row.id,
    enableRefreshToken: enableRefreshToken,
    clientId: row.clientId,
    clientSecret: row.clientSecret,
  };
  apiSaveClient(data);
};

//删除
const handleDelete = (row) => {
  ElMessageBox.confirm("确定删除该客户端吗", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    showClose: false,
    type: "warning",
  })
    .then(() => {
      deleteClient(row.id).then((res) => {
        ElMessage({
          message: "删除成功",
          type: "success",
        });
        pageNum.value = 1;
        loadData();
      });
    })
    .catch(() => {
      // TODO
    });
};

//提交
const saveSubmit = () => {
  // 进行表单效验
  formRef.value.validate((valid) => {
    if (valid) {
      saveClient();
    }
  });
};

const currentChange = (val) => {
  pageNum.value = val;
  loadData();
};

//加载数据
const loadData = () => {
  loading.value = true;
  const params = {
    current: pageNum.value,
    size: pageSize.value,
  };
  pageClient(params).then(
    (res) => {
      total.value = res.total;
      const records = res.records;
      records.forEach((ele) => {
        ele.enable = !!ele.enableRefreshToken;
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

const saveClient = () => {
  //将 form.value 中的 enable 属性转换为一个名为 enableRefreshToken 的新属性
  //然后删除了原始的 enable 属性。
  form.value.enableRefreshToken = form.value.enable ? 1 : 0;
  delete form.value.enable;
  apiSaveClient(form.value).then((res) => {
    ElMessage({
      message: "保存成功",
      type: "success",
    });
    dialogVisible.value = false;
    pageNum.value = 1;
    loadData();
  });
};
</script>


<style lang="less" scoped>
.container {
  width: 100%;

  /* 适应手机端屏幕 */
  @media screen and (max-width: 768px) {
    .button-container {
      display: flex;
      flex-direction: column; // 垂直排列
      align-items: flex-end;
    }

    .button-container .el-button {
      margin-bottom: 10px; // 按钮之间的间距
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
    text-align: center;
    margin-top: 10px;
  }
}
</style>
