<template>
  <div class="articleRight">
    <right-side-recommend></right-side-recommend>
    <!-- </div> -->
    <!-- 标签 -->
    <right-side-tags></right-side-tags>

    <!-- <div class="message">
      <div class="messageTitle">
        <div class="title">
          <svg-icon name="liuyan" width="30px" height="30px"></svg-icon>
          <div>最新评论</div>
        </div>
        <span></span>
      </div>
      <div class="messageContent"> -->
    <right-side-comment></right-side-comment>
    <!-- </div> -->
    <!-- </div> -->
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
// import useAxios from "@/hooks/axios/axios";
// import RandomArticle from '/@/components/Box/RightToolsBox/randomArticle.vue'
// import miniMessage from '/@/components/Box/RightToolsBox/miniMessage.vue'

import gsap from "gsap";

const router = useRouter();

onMounted(() => {
  //动画
  gsap.from(".articleRight", {
    duration: 0.5,
    x: 50,
    opacity: 0.2,
  });
});

// // //请求文章列表
// const { data: res } = await useAxios.get('/getarticle')
// const list = res.data
// let randomNunMin = list.length
// if (list.length > 4) {
//     randomNunMin = 4
// }
// //请求所有标签
// const { data: tags } = await useAxios.get('/gettags')
// const allTags = tags.data

// //随机挑选5个文章出来组成数组用来循环随即文章组件
// const refreshDom = ref(0)  //刷新dom
// let listArr = ref(new Array)  //准备一个存放文章的空数组
// let randomNum = new Array  //存放随机数的数组
// const getRandomList = () => {
//     while (listArr.value.length < randomNunMin) {
//         const getNum = Math.floor(Math.random() * list.length)
//         if (randomNum.indexOf(getNum) === -1) { //如果抽出来的数字在随机数数组里面不存在
//             randomNum.push(getNum)  //放到随机数数组里面
//             listArr.value.push(list[getNum])  //拿到那个随机数的对应的文章
//         } else {
//             continue
//         }
//     }
// }
// getRandomList()
// // 刷新随机文章
// const refreshRandomArticle = () => {
//     listArr.value = []
//     randomNum = []
//     getRandomList()
// }

// type Tag = {
//     _id: string
//     id: number
//     content: string
// }
// //点击标签快捷跳转到文章页面并且选中筛选这个标签
// const quickTag = (tag: Tag) => {
//     router.push({
//         path: '/article',
//         query: {
//             quickTag: tag.content
//         }
//     })
// }

// //请求留言列表
// const { data: message } = await useAxios.get('/getmessages')
// const messageLists = message.data
// const messageList = messageLists.reverse().slice(0, 7)  //展示七条留言
</script>

<style scoped lang="less">
.articleRight {
  width: 25rem;
  margin-left: 0.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

@media screen and (max-width: 800px) {
  .articleRight {
    display: none;
    width: 80%;
    margin: 0 auto;
    margin-top: 2rem;
  }
}
</style>