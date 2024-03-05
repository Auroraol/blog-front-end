<template>
  <div class="card">
    <el-row :gutter="20">
      <!-- 左侧 -->
      <el-col :span="6">
        <div class="card3 bg-purple">
          <Suspense>
            <template #default>
              <article-left></article-left>
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
              <top-article-box
                class="itemBox"
                :info="topArticleInfo"
              ></top-article-box>
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
            <Suspense v-for="item in showList" :key="item.id">
              <template #default>
                <article-box :info="item" class="itemBox"></article-box>
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
              <article-right></article-right>
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
import axios from "axios";
const ArticleRight = defineAsyncComponent(
  () => import("/@/components/Box/RightToolsBox/ArticleRight.vue")
);
const ArticleLeft = defineAsyncComponent(
  () => import("/@/components/Box/leftToolsBox/ArticleLeft.vue")
);

const TopArticleBox = defineAsyncComponent(
  () => import("/@/components/Box/TopArticleBox/TopArticleBox.vue")
);

const ArticleBox = defineAsyncComponent(
  () => import("/@/components/Box/ArticleBox/ArticleBox.vue")
);


//请求置顶文章信息
// const { data: topRes } = await axios.get("/gettoparticle")
// const topArticleInfo = topRes.data
const topArticleInfo = ""

//随机诗词
const { data: poetry } = await axios.get('https://v1.jinrishici.com/all.json')


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
  margin-top: 0px;
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

.bg-purple {
  background: #d3dce6;
}

.bg-purple-light {
  background: #e5e9f2;
}


.title {
    display: flex;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    padding: 1rem;
    background-color: #ffffff;
    min-height: 5rem;
    border-radius: .8rem;
    color: var(--special-font-color);
    font-weight: 700;
    font-size: 1.5rem;
    margin-bottom: 1rem;
}


</style>