<template>
  <div class="container">
    <div class="head">
      <el-button
        type="primary"
        size="small"
        class="icon-refresh"
        @click="refresh"
        circle
        icon="refresh"
      />
    </div>
    <mxe-table :data="tableData" :columns="columns" stripe>
      <template #opera1="scope">
        <el-form
          label-position="left"
          label-width="auto"
          inline
          class="table-expand"
        >
          <el-form-item label="浏览次数">
            <span>{{ scope.row.viewCount }}</span>
          </el-form-item>
          <el-form-item label="评论数">
            <span>{{ scope.row.commentCount }}</span>
          </el-form-item>
          <el-form-item label="点赞数">
            <span>{{ scope.row.likeCount }}</span>
          </el-form-item>
          <el-form-item label="收藏数">
            <span>{{ scope.row.collectCount }}</span>
          </el-form-item>
        </el-form>
        <el-form label-position="left" class="table-expand">
          <el-form-item label="摘要">
            <span>{{ scope.row.summary }}</span>
          </el-form-item>
        </el-form>
      </template>
      <template #opera2="scope">
        <el-popover trigger="hover" placement="top">
          <el-tag
            v-for="item in scope.row.tagList"
            :key="item.id"
            size="medium"
            style="margin-right: 6px"
            >{{ item.name }}</el-tag
          >
          <template #reference>
            <span
              v-if="
                Array.isArray(scope.row.tagList) &&
                scope.row.tagList.length === 0
              "
              class="tag"
              >无</span
            >
            <el-tag v-else-if="scope.row.tagList && scope.row.tagList[0]">{{
              scope.row.tagList[0].name
            }}</el-tag>
          </template>
        </el-popover>
      </template>
      <template #opera3="scope">
        <div style="display: flex; justify-content: center">
          <el-button type="success" size="small" @click="artPreview(scope.row)"
            >预览</el-button
          >
          <el-button
            type="primary"
            size="small"
            @click="addRecommend(scope.row)"
            >刷新</el-button
          >
        </div>
        <div style="display: flex; justify-content: center; margin-top: 10px">
          <el-button type="warning" size="small" @click="handleEdit(scope.row)"
            >排序</el-button
          >
          <el-button size="small" type="danger" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </div>
      </template>
    </mxe-table>
    <article-preview
      :id="previewId"
      :visible="preview"
      @beforeClose="previewClose"
    />
  </div>
</template>

<script setup>
import {
  recommendList,
  addRecommend as apiAddRecommend,
  deleteRecommend,
} from "/@/api/article/article";
import { ref } from "vue";
import ArticlePreview from "./components/ArticlePreview.vue";

// 数据
const loading = ref(false);
const preview = ref(false);
const previewId = ref("");

//  表格数据
const tableData = ref([]);
const columns = [
  {
    type: "expand",
    slots: {
      default: "opera1",
    },
  },
  {
    label: "ID",
    prop: "id",
  },
  {
    label: "标题",
    prop: "title",
  },
  {
    label: "分类",
    prop: "categoryName",
  },
  {
    label: "标签",
    slots: {
      default: "opera2",
    },
  },
  {
    label: "排序",
    prop: "recommendScore",
  },
  {
    label: "操作",
    slots: {
      default: "opera3",
    },
  },
];

onMounted(() => {
  loadData();
});

//TODO 分页

// 刷新
const refresh = () => {
  loadData();
};

// 加载数据
const loadData = () => {
  loading.value = true;
  recommendList().then(
    (res) => {
      tableData.value = res;
      loading.value = false;
    },
    (error) => {
      console.error(error);
      loading.value = false;
    }
  );
};

// 排序/添加
const handleEdit = (row) => {
  ElMessageBox.prompt("请输入序号", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    showClose: false,
    inputPattern: /^[0-9]*$/,
    inputErrorMessage: "只能输入数字",
  })
    .then(({ value }) => {
      const params = { articleId: row.id, score: value };
      apiAddRecommend(params).then((res) => {
        console.error(res);
        ElMessage({
          message: "修改成功",
          type: "success",
        });
        loadData();
      });
    })
    .catch(() => {});
};

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm("确定删除该推荐吗", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    showClose: false,
    type: "warning",
  })
    .then(() => {
      deleteRecommend(row.id).then((res) => {
        ElMessage({
          message: "删除成功",
          type: "success",
        });
        loadData();
      });
    })
    .catch(() => {});
};

// 刷新
const addRecommend = (row) => {
  const params = { articleId: row.id, score: row.recommendScore };
  console.error(params);
  apiAddRecommend(params).then((res) => {
    console.error(res);
    ElMessage({
      message: "刷新成功",
      type: "success",
    });
    loadData();
  });
};

// 文章预览
const artPreview = (row) => {
  previewId.value = row.id;
  preview.value = true;
};

// 预览关闭
const previewClose = () => {
  preview.value = false;
};

// 分页
</script>

<style lang="less" scoped>
.container {
  text-align: center;

  .head {
    height: 50px;
    background: #d0d0d0;
    display: flex;
    align-items: center;
    position: relative;

    .icon-refresh {
      position: absolute;
      right: 20px;
      top: 50%;
      transform: translateY(-50%);
      color: #fff;
      font-weight: 700;

      @media screen and (max-width: 922px) {
        display: none;
      }
    }

    > * {
      margin-left: 10px;
    }
  }

  .el-table {
    margin: 10px;
  }

  .table-expand {
    font-size: 0;

    .el-form-item__label {
      font-weight: normal;
      font-size: 14px;
      color: #99a9bf;
    }

    .el-form-item {
      margin-bottom: 0;
    }
  }

  .el-tag {
    cursor: default;
  }

  .el-pagination {
    margin-top: 10px;
  }
}
</style>
