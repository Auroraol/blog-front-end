<template>
  <div class="card">
    <el-row :gutter="20">
      <!-- 左侧 -->
      <el-col :span="6">
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
      <el-col :span="12">
        <div class="card3 bg-purple-light">
          <!-- 置顶文章盒子 -->
          <Suspense>
            <template #default>
              <!-- <top-article-box
                class="itemBox"
                :info="topArticleInfo"
              ></top-article-box> -->
            </template>
            <!-- 加载完成前的载入动画 -->
            <template #fallback>
              <div class="window">
                <loading class="winLoad"></loading>
              </div>
            </template>
          </Suspense>
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
          </div>
        </div>
      </el-col>
      <!-- 右侧 -->
      <el-col :span="6">
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

const ArticleBox = defineAsyncComponent(
  () => import("/@/components/Box/ArticleBox/Article.vue")
);

const AsyncArticleList = defineAsyncComponent(
  () => import("/@/components/Box/ArticleBox/ArticleList.vue")
);

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
const current = ref<number>(1);
const size = ref<number>(10);
const total = ref<number>(0);
const mainActive = ref<number>(0);
const loading = ref<boolean>(false);
const artList = ref<Article[]>([]); // artList 是一个 Article 类型的数组

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
    // current: current.value,
    size: size.value,
    //排序
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

// .bg-purple {
//   background: #d3dce6;
// }

// .bg-purple-light {
//   background: #e5e9f2;
// }

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
</style>