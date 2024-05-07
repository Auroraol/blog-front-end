<!-- 标签页面 -->
<template>
  <div class="container">
    <div class="content-container">
      <!-- 左边 -->
      <div class="left-side">
        <div class="content-head">
          <p class="content-row">
            <span style="color: #00a4ff">#</span>{{ keyword }}
          </p>
          <p class="content-row content-des">
            当前关键字搜索共计{{ total }}篇文章。
          </p>
        </div>
        <div />
        <div class="content-list">
          <article-list :list="artList" :loading="loading" />
          <el-pagination
            background
            layout="prev, pager, next"
            :page-size="size"
            :current-page="current"
            :total="total"
            :hide-on-single-page="true"
            @current-change="currentChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import { useRoute } from "vue-router";
import { pagePublishedArticle } from "/@/api/article/article";

const current = ref(1);
const size = ref(5);
const artList = ref([]);
const total = ref(0);
const loading = ref(false);
const keyword = ref("");

const route = useRoute();

watch(route, (newRoute, oldRoute) => {
  keyword.value = newRoute.query && newRoute.query.keyword;
  getArtList();
});

onMounted(() => {
  keyword.value = route.query && route.query.keyword;
  getArtList();
});

const currentChange = (currentNum: number) => {
  current.value = currentNum;
  getArtList();
};

function getArtList() {
  loading.value = true;
  const params = {
    current: current.value,
    size: size.value,
    title: keyword.value,
  };
  pagePublishedArticle(params)
    .then((res) => {
      total.value = res.total;
      artList.value = res.records;
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
      loading.value = false;
    });
}
</script>

<style lang="less" scoped>
.container {
  margin: 0 auto;
  width: 80%;
  .content-container {
    margin-top: 15px;
    box-sizing: border-box;
    display: flex;
    align-items: flex-start;

    @media screen and (max-width: 922px) {
      margin-top: 0;
      width: 100%;
    }

    .left-side {
      margin-top: 5px;
      margin-left: 20px;
      margin-right: 20px;
      flex: 1;
      background: #fff;
      border-radius: 0.5rem;
      margin-bottom: 20px;

      .content-head {
        padding: 30px 30px;
        font-weight: 700;
        font-size: 25px;
        border-bottom: 1px solid hsla(0, 0%, 59.2%, 0.1);

        .content-row {
          padding: 0;
          margin: 0;
        }

        .content-des {
          margin-top: 10px;
          font-size: 14px;
          font-weight: normal;
          color: #909090;
        }
      }

      .content-list {
        margin: 0;
        box-sizing: border-box;

        .el-pagination {
          text-align: center;
          padding: 30px;
          background: #eee;
        }
      }
    }
  }
}
</style>
