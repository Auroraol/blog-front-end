<template>
  <div class="container">
    <div class="left-list">
      <ul v-if="device === 'desktop'">
        <li
          v-for="(category, index) in categorys"
          :key="index"
          class="left-list-item"
          :class="{ 'left-list-item-active': categoryId === category.id }"
          @click="chageTab(category.name)"
        >
          <span class="item-content">{{ category.name }}</span>
        </li>
      </ul>
    </div>
    <div class="content-list">
      <ul v-if="device !== 'desktop'" class="list-header">
        <li
          v-for="(category, index) in categorys"
          :key="index"
          class="list-header-item"
          :class="{ 'header-item-active': categoryId === category.id }"
          @click="chageTab(category.id)"
        >
          {{ category.name }}
        </li>
      </ul>
      <article-list :list="artList" :loading="loading" />

      <el-pagination
        background
        layout="prev, pager, next"
        hide-on-single-page
        :page-size="size"
        :current-page="current"
        :total="total"
        @current-change="currentChange"
      />
    </div>
  </div>
</template>
<script setup>
import { categoryList } from "/@/api/category/category";
// import { pagePublishedArticle } from '@/api/article.js'
// import { ref } from 'vue'
// import { useRouter } from 'vue-router'
// import { mapGetters } from 'vuex'

import gsap from "gsap";

// 桌面美化
// 软件测试
// Mac教程
// 程序员日常
// 日常生活

const categoryId = ref(0);
const categorys = ref([]);
const loading = ref(true);
const artList = ref([]);
const current = ref(1);
const size = ref(10);
const total = ref(0);
const router = useRouter();

const device = ref("desktop");

// 初始化
onMounted(() => {
  init();
  //动画
  gsap.from(".left-list", {
    duration: 0.5,
    x: -50,
    opacity: 0.2,
  });
  //动画
  gsap.from(".content-list", {
    duration: 0.5,
    x: 50,
    opacity: 0.2,
  });
});

// 获取分类列表
const init = async () => {
  try {
    const res = await categoryList();
    categorys.value = res;
  } catch (error) {
    console.error("获取分类列表失败");
  }
};

// tab更改
const chageTab = (categoryId) => {
  // total.value = 0
  // current.value = 1
  // categoryId.value = categoryId
  // getArtList()
};

// 分页监听
const currentChange = (current) => {
  // current.value = current
  // getArtList()
};

// 获取文章列表
const getArtList = () => {
  // loading.value = true
  // const params = {
  //   current: current.value,
  //   size: size.value,
  //   categoryId: categoryId.value
  // }
  // pagePublishedArticle(params).then(
  //   res => {
  //     total.value = res.data.total
  //     artList.value = res.data.records
  //     loading.value = false
  //     // 如果你使用了 ref 来获取容器元素的引用，请确保该元素上有 ref 属性，如 ref="container"
  //     const container = document.querySelector('[ref="container"]')
  //     if (container) container.scrollTop = 0
  //   },
  //   error => {
  //     console.error(error)
  //     loading.value = false
  //   }
  // )
};
</script>

<style lang="less" scoped>
.container {

  margin-top: 5px;
  margin-left: 15px;
  margin-right: 15px;
  min-height: 631px;
  display: flex;

  align-items: flex-start;
  .left-list {
    margin-left: 100px;
    background: #fff;
    width: 112px;
    text-align: center;
    box-sizing: border-box;
    color: #909090;
    border-radius: 2px;
    left: calc(calc(100% - 845px) / 2);
    background: #fff;
    z-index: 999;

    .left-list-item {
      font-size: 15px;
      list-style: none;
      cursor: pointer;
      margin-top: 10px;

      &:last-child {
        margin-bottom: 10px;
      }

      .item-content {
        padding: 7px 0;
        display: inline-block;
        min-width: 86px;
        cursor: pointer;
        border-radius: 3px;

        &:hover {
          color: #007fff;
          background: #f4f5f5;
        }
      }
    }

    .left-list-item-active {
      color: #fff;
      font-weight: 700;

      .item-content {
        background: #007fff;

        &:hover {
          color: #fff;
          background: #007fff;
        }
      }
    }
  }

  .content-list {
    background: #fff;
    width: 1150px;
    margin: 0;
    box-sizing: border-box;
    border-radius: 2px;
    margin-left: 30px;
    margin-bottom: 20px;

    .list-header {
      margin: 0;
      padding: 0;
      display: flex;
      align-items: center;
      width: 100vw;
      white-space: nowrap;
      overflow-x: scroll;
      font-size: 14px;
      border-bottom: 1px solid hsla(0, 0%, 59.2%, 0.1);

      &:first-child {
        margin-left: 5px;
      }

      .list-header-item {
        list-style: none;
        cursor: pointer;
        padding: 15px;
        margin-right: 5px;
      }

      .header-item-active {
        color: #007fff;
      }
    }

    @media screen and (max-width: 960px) {
      margin-left: 0;
    }

    .el-pagination {
      text-align: center;
      padding: 30px;
      padding-bottom: 0;
      background: #eee;
    }
  }
}
</style>
