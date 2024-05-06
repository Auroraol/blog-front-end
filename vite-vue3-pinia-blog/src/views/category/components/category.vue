<template>
  <div class="container">
    <!-- 左 -->
    <el-affix :offset="70" v-if="device === 'desktop'">
      <div class="left-list">
        <ul>
          <li
            v-for="(category, index) in categorys"
            :key="index"
            class="left-list-item"
            :class="{ 'left-list-item-active': categoryId === category.id }"
            @click="changeTab(category.id)"
          >
            <span class="item-content">{{ category.name }}</span>
          </li>
        </ul>
      </div>
    </el-affix>
    <!-- 右 -->
    <div class="content-list">
      <ul v-if="device !== 'desktop'" class="list-header">
        <li
          v-for="(category, index) in categorys"
          :key="index"
          class="list-header-item"
          :class="{ 'header-item-active': categoryId === category.id }"
          @click="changeTab(category.id)"
        >
          {{ category.name }}
        </li>
      </ul>
      <!-- 文章 -->
      <article-list :list="artList" :loading="loading" />

      <el-pagination
        v-if="device !== 'desktop'"
        background
        layout="prev, pager, next"
        hide-on-single-page
        :page-size="size"
        :current-page="current"
        :total="total"
        @current-change="currentChange"
      />

      <!-- 分页 -->
      <el-pagination
        v-else
        v-model:current-page="current"
        v-model:page-size="size"
        :page-sizes="[5, 10, 20, 30]"
        :small="small"
        :disabled="disabled"
        :background="background"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="currentChange"
      />
    </div>
  </div>
</template>
<script setup>
import { categoryList } from "/@/api/category/category";
import ArticleList from "/@/components/Box/ArticleBox/ArticleList.vue";
import { pagePublishedArticle } from "/@/api/article/article";
import { useGetters } from "/@/store/getters";
import gsap from "gsap";

const useGettersPinia = useGetters();

const categorys = ref([]);
const categoryId = ref(0);
const loading = ref(true);
const artList = ref([]);
const current = ref(1);
const size = ref(4);
const total = ref(0);
const router = useRouter();

const device = computed(() => useGettersPinia.device);

// 初始化
onMounted(() => {
  init();
  //动画
  gsap.from(".left-list", {
    duration: 0.8,
    x: -50,
    opacity: 0.2,
  });
  //动画
  gsap.from(".content-list", {
    duration: 0.8,
    x: 50,
    opacity: 0.2,
  });
});

// 获取分类列表
const init = async () => {
  try {
    const res = await categoryList();
    categorys.value = res;
    const id = Number(router.currentRoute.value.query.id); // 当前路由的 id 参数
    if (id && categorys.value.some((ele) => ele.id === id)) {
      // 显示成路由参数所表示的Tab
      changeTab(id);
    } else {
      if (categorys.value.length > 0) {
        changeTab(categorys.value[0].id);
      } else {
        console.error("分类列表为空");
      }
    }
  } catch (error) {
    console.error("获取分类列表失败");
  }
};

// tab更改
const changeTab = (id) => {
  total.value = 0;
  current.value = 1;
  categoryId.value = id;
  getArtList();
};

// 分页监听, 选择第几页
const currentChange = (cur) => {
  current.value = cur;
  getArtList();
};

// 分页监听, 每页数量
const handleSizeChange = (selectSize) => {
  size.value = selectSize;
  getArtList();
};

// 获取文章列表
const getArtList = async () => {
  loading.value = true;
  const params = {
    current: current.value,
    size: size.value,
    categoryId: categoryId.value,
  };
  try {
    const data = await pagePublishedArticle(params);
    // console.log(data);
    loading.value = false;
    total.value = data.total;
    artList.value = data.records;
  } catch (error) {
    console.error(error);
    loading.value = false;
  }
};
</script>

<style lang="less" scoped>
.container {
  margin-top: 5px;
  margin-left: 15px;
  margin-right: 15px;
  min-height: 631px;
  display: flex;
  border-radius: 0.5rem;
  align-items: flex-start;

  @media screen and (max-width: 960px) {
    margin: 0 auto;
    width: 90%;
    margin-top: 5px;
  }

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
        background: #79bbdc;

        &:hover {
          color: #fff;
          background: #79bbdc;
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

    // 手机端显示
    .list-header {
      margin: 0;
      padding: 0;
      display: flex;
      align-items: center;
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

    @media only screen and (max-width: 960px) {
      // margin-left: 0;
      margin: 0;
      .content-list {
        max-width: 300px;
      }
    }
    .el-pagination {
      display: flex;
      justify-content: center;
      text-align: center;
      padding: 30px;
      padding-bottom: 0;
      background: #eee;
    }
  }
}
</style>
