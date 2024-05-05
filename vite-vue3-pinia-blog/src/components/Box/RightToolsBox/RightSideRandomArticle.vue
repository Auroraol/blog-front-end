// TODO
<template>
  <div class="randomArticle-container" @click="goBrowse(article.id)">
    <img :src="article.cover" alt="cover" />
    <div class="bgcolor">
      <div class="time">{{ article.time }}</div>
      <div class="title">{{ article.title }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useStore } from "/@/store";
import { useRouter } from "vue-router";

const router = useRouter();
const pinia = useStore();

type Article = {
  id: number;
  title: string;
  author?: string;
  tag: Array<string>;
  cover?: string;
  coverName?: string;
  mdImgName?: string;
  time: string;
  oneSentence: string;
  content: string;
};

type Props = {
  article: Article;
};
defineProps<Props>();

const goBrowse = (id: number) => {
  router.push({
    path: "/articlebrowse",
    query: {
      id: id,
    },
  });
};
</script>

<style scoped lang="less">
.randomArticle-container {
  margin: 0.5rem 0;
  width: 100%;
  height: 10rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border-radius: 0.5rem;
  overflow: hidden;
  position: relative;
  cursor: pointer;

  img {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 0;
    object-fit: cover;
  }

  .bgcolor {
    width: 100%;
    height: 50%;
    // background-color: rgb(41, 53, 49);
    background-image: linear-gradient(rgba(255, 255, 255, 0), rgb(54, 54, 54));
    display: flex;
    flex-direction: column;
    justify-content: center;
    box-sizing: border-box;
    padding: 0 0.5rem;
    font-size: 1.1rem;
    color: var(--white-font-color);
    position: absolute;
    z-index: 1;
    left: 0;
    bottom: 0;

    .title {
      margin-top: 0.2rem;
    }
  }
}
</style>