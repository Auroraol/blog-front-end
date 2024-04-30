<template>
  <div class="container">
    <div class="head">
      <el-select
        v-model="categoryId"
        class="col"
        size="small"
        placeholder="选择分类"
        style="width: 120px"
      >
        <el-option
          v-for="(category, index) in categorys"
          :key="index"
          :label="category.name"
          :value="category.id"
        />
      </el-select>
      <el-select
        v-model="tagId"
        class="col"
        size="small"
        placeholder="选择标签"
        style="width: 120px"
      >
        <el-option
          v-for="(tag, index) in tags"
          :key="index"
          :label="tag.name"
          :value="tag.id"
        />
      </el-select>
      <el-input
        v-model="title"
        style="width: 120px"
        placeholder="请输入标题"
        size="small"
        clearable
      />
      <el-button type="primary" size="small" icon="search" @click="search"
        >搜索</el-button
      >
      <el-button
        type="primary"
        size="small"
        class="icon-refresh"
        @click="refresh"
        circle
        icon="refresh"
      />
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
      <template #opera1="props">
        <el-form label-position="left" inline class="table-expand">
          <el-form-item label="浏览次数">
            <span>{{ props.row.viewCount }}</span>
          </el-form-item>
          <el-form-item label="评论数">
            <span>{{ props.row.commentCount }}</span>
          </el-form-item>
          <el-form-item label="点赞数">
            <span>{{ props.row.likeCount }}</span>
          </el-form-item>
          <el-form-item label="收藏数">
            <span>{{ props.row.collectCount }}</span>
          </el-form-item>
          <el-form-item label="时间">
            <span>{{ props.row.publishTime }}</span>
          </el-form-item>
        </el-form>
        <el-form label-position="left" class="table-expand">
          <el-form-item label="摘要">
            <span>{{ props.row.summary }}</span>
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
        <el-switch
          v-model="scope.row.published"
          active-color="#13ce66"
          inactive-color="#ff4949"
          @change="statusChange(scope.row)"
        />
      </template>
      <template #opera4="scope">
        <div style="display: flex; justify-content: center">
          <el-button type="success" size="small" @click="artPreview(scope.row)"
            >预览</el-button
          >
          <el-button type="primary" size="small" @click="handleEdit(scope.row)"
            >编辑</el-button
          >
        </div>
        <div style="display: flex; justify-content: center; margin-top: 10px">
          <el-button
            type="warning"
            size="small"
            @click="addRecommend(scope.row)"
            >推荐</el-button
          >
          <el-button
            v-if="scope.row.status != 2"
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
          <el-button
            v-else
            type="primary"
            size="small"
            @click="handleDelete(scope.row)"
            >恢复</el-button
          >
        </div>
      </template></mxe-table
    >
    <article-preview
      :id="previewId"
      :visible="preview"
      @beforeClose="previewClose"
    />
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { tagList } from "/@/api/tag/tag";
import {
  pageArticle as apiPageArticle,
  addRecommend as apiAddRecommend,
  deleteArticle as paiDeleteArticle,
  updateStatus as apiUpdateStatus,
} from "/@/api/article/article";
import { categoryList } from "/@/api/category/category";
import ArticlePreview from "./components/ArticlePreview.vue";

const router = useRouter();

const loading = ref(false);

const title = ref("");
const categoryId = ref(null);
const tagId = ref(null);
const tags = ref([]);
const preview = ref(false);
const previewId = ref("");
const categorys = ref([]);

// 分页
const pageSize = ref(10);
const pageNum = ref(1);
const total = ref(0);

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
    label: "发布",
    slots: {
      default: "opera3",
    },
  },
  {
    label: "操作",
    slots: {
      default: "opera4",
    },
  },
];

onMounted(() => {
  initCategoryList();
  initTagList();
  loadData();
});

// 初始化分类列表
const initCategoryList = () => {
  categoryList().then((res) => {
    categorys.value = res;
  });
};

// 初始化标签列表
const initTagList = () => {
  tagList().then((res) => {
    tags.value = res;
  });
};

// 加载数据
const loadData = () => {
  loading.value = true;
  const params = {
    current: pageNum.value,
    size: pageSize.value,
    categoryId: categoryId.value,
    tagId: tagId.value ? tagId.value : null,
    title: title.value ? title.value : null,
  };
  apiPageArticle(params).then(
    (res) => {
      total.value = res.total;
      const records = res.records;
      records.forEach((ele) => {
        ele.published = ele.status === 0;
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

// 条件查询
const search = () => {
  pageNum.value = 1;
  loadData();
};

//重写获取分页数据
const getList = () => {
  loadData();
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

// 编辑
const handleEdit = (row: any) => {
  router.push({
    path: "/art-manage/edit",
    query: {
      id: row.id,
    },
  });
};

// 状态修改:发布|暂存
const statusChange = (row: any) => {
  const params = {
    articleId: row.id,
    status: row.published ? 0 : 1,
  };
  apiUpdateStatus(params);
};

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm("确定删除该篇文章吗", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    showClose: false,
    type: "warning",
  })
    .then(() => {
      paiDeleteArticle(row.id).then((res) => {
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

// 添加推荐
const addRecommend = (row: any) => {
  const params = { articleId: row.id, score: row.id };
  apiAddRecommend(params).then((res) => {
    ElMessage({
      message: "添加成功",
      type: "success",
    });
  });
};

// 文章预览
const artPreview = (row: any) => {
  previewId.value = row.id;
  preview.value = true;
};

// 预览关闭
const previewClose = () => {
  preview.value = false;
};

// 右边的刷新
const refresh = () => {
  title.value = "";
  categoryId.value = null;
  tagId.value = "";
  pageNum.value = 1;
  loadData();
};
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

    /deep/ .el-form-item__label {
      font-weight: normal;
      font-size: 14px;
      color: #99a9bf;
    }

    /deep/ .el-form-item {
      margin-bottom: 0;
    }
  }

  .el-tag {
    cursor: default;
  }

  .el-pagination {
    margin-top: 10px;
  }

  /deep/ .el-table__expanded-cell[class*="cell"] {
    padding: 15px;
  }
}
</style>
