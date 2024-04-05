<template>
  <div class="container">
    <div class="tagsTitle">
      <svg-icon name="24gl-tags3" width="30px" height="30px"></svg-icon>
      <div class="title">标签</div>
      <router-link class="right btn" to="/tag">全部标签</router-link>
    </div>
    <div class="tags">
      <ul
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="#fff"
        class="tag-list"
      >
        <li
          v-for="(tag, index) in tags"
          :key="index"
          class="list-item btn"
          @click="tagClick(tag.id)"
        >
          {{ tag.name }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { tagList } from "/@/api/tag/tag";
import { ref, onMounted } from "vue";
import { RouteLocationRaw, useRoute, useRouter } from "vue-router";
import { tagListResponseType } from "/@/api/tag/data";

const router = useRouter();
const tags = ref<tagListResponseType[]>([]);
const loading = ref(true);

const init = async () => {
  try {
    const res = await tagList();
    tags.value = res.slice(0, 10);
    loading.value = false;
  } catch (error) {
    console.error(error);
    loading.value = false;
  }
};

// 跳转
const tagClick = (id: number) => {
  const query = { path: "/tag", query: { id } };
  router.push(query);
};

onMounted(init);
</script>


<style lang="less" scoped>
.container {
  display: flex;
  flex-direction: column;
  margin-top: 2rem;
  width: 100%;
  background-color: #fff;
  box-shadow: 0 0 0.5rem 0.2rem var(--gray-light-sahdow);
  border-radius: 0.5rem;
  transition: all 0.3s;

  // 鼠标悬停
  &:hover {
    box-shadow: 0.1rem 0.1rem 0.5rem var(--gray-sahdow); //阴影
  }

  .tagsTitle {
    width: 90%;
    margin-left: 1rem;
    font-size: 1.8rem;
    height: 5rem;
    display: flex;
    align-items: center;
    margin-bottom: 0.5rem;
    border-bottom: 1px solid hsla(0, 0%, 59.2%, 0.2);

    .title {
      margin-left: 0.5rem;
    }

    .right {
      margin-left: auto; /* 推到容器的右侧 */
      font-size: 14px;
      color: #007fff;
      // color: #53b7e9;
    }
  }

  // 彩色下划线
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

  .tag-list {
    min-height: 200px;
    width: 100%;
    margin: 10px;
    padding: 0;
    font-size: 15px;

    .list-item {
      list-style: none;
      float: left;
      color: inherit;
      background-color: #f3f6f3;
      margin-right: 12px;
      margin-bottom: 12px;
      padding: 10px 22px;
      border-radius: 30px;
      cursor: pointer;

      &:hover {
        background-color: #79bbdc;
        color: #fff;
      }
    }
  }
}
</style>
