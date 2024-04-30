<template>
  <div class="container">
    <div class="randomArticleTitle">
      <div class="title" @click="refreshRandomArticle">
        <svg-icon name="shuaxin" width="30px" height="30px"></svg-icon>
        <div>推荐文章</div>
      </div>
      <span></span>
    </div>
    <ul
      v-loading="loading"
      class="content-list"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="#fff"
    >
      <transition-group name="fade-list">
        <li v-for="(item, index) in list" :key="index" class="list-item">
          <router-link class="content-row title" :to="'/article/' + item.id">{{
            item.title
          }}</router-link>
          <p class="content-row">浏览&ensp;{{ item.viewCount }}</p>
        </li>
      </transition-group>
    </ul>
  </div>
</template>

<script setup>
import { recommendList } from "/@/api/article/article";

const loading = ref(true);
const list = ref([]);

onMounted(() => {
  recommendList().then((res) => {
    loading.value = false;
    list.value = res;
  });
});
</script>

<style lang="less" scoped>
.container {
  border-radius: 0.5rem;
  width: 100%;
  background: #fff;
  margin-bottom: 10px;
  padding-bottom: 10px;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: all 0.3s;
  padding: 0 1rem;

  .randomArticleTitle {
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    height: 5rem;
    font-size: 1.8rem;

    .title {
      display: flex;
      align-items: center;
      justify-content: flex-start;
      margin-bottom: 0.5rem;

      .icon {
        font-size: 2.5rem;
      }

      div {
        margin-left: 0.5rem;
      }
    }

    span {
      display: block;
      width: 100%;
      height: 0.5rem;
      background-image: linear-gradient(
        to left,
        var(--gradient-start-one),
        var(--gradient-end-one)
      );
      border-radius: 1rem;
    }
  }

  .content-list {
    margin: 10px;
    padding: 0;
    width: 100%;
    overflow-y: auto; /* 当内容溢出时显示滚动条 */

    .list-item {
      line-height: 15px;
      margin-bottom: 10px;

      .content-row {
        margin: 0;
        padding: 0;
        font-size: 12px;
        margin-bottom: 5px;
      }

      .title {
        font-size: 15px;
        color: #000;
        cursor: pointer;
        line-height: 18px;

        &:hover {
          color: #007fff;
          text-decoration: underline;
        }
      }
    }
  }
}
</style>
