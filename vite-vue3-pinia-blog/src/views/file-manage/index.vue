<template>
  <div class="container">
    <!-- 头部 -->
    <div class="head">
      <el-button
        type="primary"
        icon="uploadFilled"
        size="small"
        @click="dialogVisible = true"
      >
        Upload
      </el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border style="width: 100%">
      <el-table-column prop="name" label="名称" width="270" align="center" />
      <el-table-column prop="url" label="链接" width="520" align="center" />
      <el-table-column prop="date" label="时间" width="180" align="center" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            v-clipboard:copy="scope.row.url"
            v-clipboard:success="copySuccess"
            size="mini"
            type="success"
            >复制链接</el-button
          >
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
      <template #empty>
        <!-- 给一个空组件 -->
        <div>空空如也</div>
      </template>
    </el-table>

    <div class="btn-group">
      <el-button
        size="mini"
        type="primary"
        :disabled="pageNum === 1"
        @click="prevClick"
        >上一页</el-button
      >
      <el-button
        size="mini"
        type="primary"
        :disabled="loadedAll"
        @click="nextClick"
        >下一页</el-button
      >
    </div>

    <el-dialog
      title="上传文件"
      top="25vh"
      width="500px"
      v-model="dialogVisible"
      :close-on-click-modal="false"
      :lock-scroll="false"
      @closed="handleClosed"
    >
      <img-upload @uploadSuccess="coverUploadSuccess" />
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import { pageFile, deleteFile } from "/@/api/file/file";

const dialogVisible = ref(false); //弹窗

const markers = ref([null]);
let pageNum = 1;
const pageSize = 10;
let loadedAll = false;
const loading = ref(true);

//  表格数据
const tableData = ref([]);

onMounted(() => {
  init();
});

// 首次加载数据
const init = () => {
  loading.value = true;
  const params = { size: pageSize, marker: null };
  pageFile(params).then(
    (res) => {
      loading.value = false;
      console.error(res);

      tableData.value = res.records;
      markers.value.push(res.nextMarker);
      loadedAll = res.loadedAll;
    },
    (error) => {
      console.error(error);
      loading.value = false;
    }
  );
};

// 下一页点击
const nextClick = () => {
  loading.value = true;
  const params = { size: pageSize, marker: markers.value[pageNum] };
  pageFile(params).then(
    (res) => {
      loading.value = false;
      tableData.value = res.data.records;
      const marker = res.data.nextMarker;
      if (markers.value.indexOf(marker) === -1) {
        markers.value.push(marker);
      }
      pageNum++;
      loadedAll = res.data.loadedAll;
    },
    (error) => {
      console.error(error);
      loading.value = false;
    }
  );
};

// 上一页点击
const prevClick = () => {
  loading.value = true;
  const params = { size: pageSize, marker: markers.value[pageNum - 2] };
  pageFile(params).then(
    (res) => {
      loading.value = false;
      tableData.value = res.data.records;
      const marker = res.data.nextMarker;
      if (markers.value.indexOf(marker) === -1) {
        markers.value.push(marker);
      }
      pageNum--;
      loadedAll = res.data.loadedAll;
    },
    (error) => {
      console.error(error);
      loading.value = false;
    }
  );
};

// 复制链接
const copySuccess = () => {
  ElMessage({
    message: "复制链接成功",
    type: "success",
  });
};

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm("确定删除该文件吗", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    showClose: false,
    type: "warning",
  })
    .then(() => {
      const params = {
        fullPath: row.url,
      };
      deleteFile(params).then((res) => {
        ElMessage({
          message: "删除成功",
          type: "success",
        });
        markers.value = [null];
        pageNum = 1;
        init();
      });
    })
    .catch(() => {
      // TODO
    });
};
// 上传成功
const coverUploadSuccess = (url) => {
  markers.value = [null];
  pageNum = 1;
  init();
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

  .btn-group {
    width: 100%;
    display: flex;
    justify-content: center;
  }
}
</style>
