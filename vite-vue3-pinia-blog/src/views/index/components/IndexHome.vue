<template>
  <div class="card">
    <el-row :gutter="20">
      <!-- 左侧 -->
      <el-col :span="6" v-if="device == 'desktop'">
        <div class="card3 bg-purple">
          <Suspense>
            <template #default>
              <LeftToolsBox></LeftToolsBox>
            </template>
            <!-- 加载完成前的载入动画 -->
            <template #fallback>
              <div class="window">
                <loading-2 class="winLoad"></loading-2>
              </div>
            </template>
          </Suspense>
        </div>
      </el-col>
      <!--中间-->
      <el-col :span="12" :xs="24">
        <div class="card3 bg-purple-light">
          <!--每日一言 poetry诗歌-->
          <div class="title">
            <div class="poetry">
              <span>{{ poetry.content }}</span>
              &nbsp;&nbsp;
              <span>{{ `——${poetry.author}《${poetry.origin}》` }}</span>
            </div>
          </div>
          <!-- 文章盒子 -->
          <div class="articleBoxes">
            <!-- 使用了异步加载并且加上了加载动画的文章盒子 -->
            <ul class="list-header">
              <li
                v-for="(item, index) in mainTabs"
                :key="index"
                class="list-header-item"
                :class="{ 'main-active': mainActive === index }"
                @click="mainTabClick(index)"
              >
                {{ item }}
              </li>
            </ul>
            <Suspense>
              <template #default>
                <AsyncArticleList :list="artList" :loading="loading" />
              </template>
              <!-- 加载完成前的载入动画 -->
              <template #fallback>
                <div class="window">
                  <loading class="winLoad"></loading>
                </div>
              </template>
            </Suspense>

            <el-pagination
              v-if="device !== 'desktop'"
              background
              layout="prev, pager, next"
              hide-on-single-page
              :page-size="size"
              :current-page="current"
              :total="total"
              :pager-count="4"
              @current-change="currentChange"
            />
            <el-pagination
              v-else
              background
              layout="prev, pager, next"
              :page-size="size"
              :current-page="current"
              :hide-on-single-page="true"
              :total="total"
              @current-change="currentChange"
            />
          </div>
        </div>
      </el-col>
      <!-- 右侧 -->
      <el-col :span="6" v-if="device == 'desktop'">
        <div class="card3 bg-purple">
          <Suspense>
            <template #default>
              <RightToolsBox></RightToolsBox>
            </template>
            <!-- 加载完成前的载入动画 -->
            <template #fallback>
              <div class="window">
                <loading-2 class="winLoad"></loading-2>
              </div>
            </template>
          </Suspense>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { defineAsyncComponent } from "vue";
import { pagePublishedArticle } from "/@/api/article/article";
import { useGetters } from "/@/store/getters";

import axios from "axios";
import gsap from "gsap";

const LeftToolsBox = defineAsyncComponent(
  () => import("/@/components/Box/LeftToolsBox/index.vue")
);
const RightToolsBox = defineAsyncComponent(
  () => import("/@/components/Box/RightToolsBox/index.vue")
);

const TopArticleBox = defineAsyncComponent(
  () => import("/@/components/Box/TopArticleBox/index.vue")
);

const AsyncArticleList = defineAsyncComponent(
  () => import("/@/components/Box/ArticleBox/ArticleList.vue")
);

const useGettersPinia = useGetters();

const device = computed(() => useGettersPinia.device);

interface Article {
  // 定义文章类型
  // 根据您的数据，可能有其他字段，这里仅示例最基本的字段
  id: number;
  title: string;
  content: string;
  // 其他字段...
}

// 声明响应式数据
const mainTabs = ref<string[]>(["最新", "热门"]);
// 分页
const current = ref<number>(1);
const size = ref<number>(6);
const total = ref<number>(0);
//
const mainActive = ref<number>(0);
const loading = ref<boolean>(false);
const artList = ref<Article[]>([]); // artList 是一个 Article 类型的数组

//
const orderBy = computed(() =>
  mainActive.value === 0 ? "publish_time" : "view_count"
);

// 随机诗词
const { data: poetry } = await axios.get("https://v1.jinrishici.com/all.json");

onMounted(() => {
  //动画
  gsap.from(".title", {
    duration: 0.5,
    y: 50,
    opacity: 0.2,
  });
  // 获取文章列表
  getArtList();
});

// 获取文章列表
const getArtList = async () => {
  loading.value = true;
  const params = {
    current: current.value,
    size: size.value,
    //排序
    orderBy: orderBy.value,
  };
  try {
    const data = await pagePublishedArticle(params);
    console.log(data);

    loading.value = false;
    total.value = data.total;
    artList.value = data.records;
  } catch (error) {
    console.error(error);
    loading.value = false;
  }
};

//   监听页码
const currentChange = (currentNum) => {
  current.value = currentNum;
  getArtList();
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

// 热门|最新切换
const mainTabClick = (index) => {
  current.value = 1;
  total.value = 0;
  mainActive.value = index;
  getArtList();
};
</script>

<style scoped lang="less">
@fontColor: #fff;

.window {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

.card {
  margin-top: 5px;
  margin-left: 15px;
  margin-right: 15px;
}

.el-row {
  margin-bottom: 20px;

  &:last-child {
    margin-bottom: 0;
  }
}

.card3 {
  border-radius: 0.5rem;
  min-height: 650px;
}

.title {
  display: flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  padding: 1rem;
  background-color: #ffffff;
  min-height: 5rem;
  border-radius: 0.8rem;
  color: var(--special-font-color);
  font-weight: 700;
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.articleBoxes {
  .el-pagination {
    margin-top: 12px;
  }
}
.list-header {
  // border-radius: 1rem;
  border-top-left-radius: 1rem;
  border-top-right-radius: 1rem;
  background: #fff;
  margin: 0;
  box-sizing: border-box;
  padding: 15px 12px;
  display: flex;
  align-items: center;
  font-size: 14px;
  // border-bottom: 1px solid hsla(0, 0%, 19.2%, 0.1);
  border-bottom: 1px solid #1413132f;
  .list-header-item {
    list-style: none;
    padding: 0 15px;
    // border-right: 1px solid hsla(0, 0%, 19.2%, 0.2);
    border-right: 1px solid #1413132f;
    cursor: pointer;

    &:last-child {
      border-right: none;
    }

    &:hover {
      color: #007fff;
    }
  }

  .main-active {
    color: #007fff;
  }
}
</style>