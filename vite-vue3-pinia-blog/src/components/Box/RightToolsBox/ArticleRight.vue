<template>
  <div class="articleRight">
    <div class="randomArticle">
      <div class="randomArticleTitle">
        <div class="title" @click="refreshRandomArticle">
          <svg-icon name="shuaxin" width="30px" height="30px"></svg-icon>
          <div>随机文章</div>
        </div>
        <span></span>
      </div>
      <!-- <div class="randomArticleContent" :key="refreshDom">
        <RandomArticle
          v-for="item in listArr"
          :key="item.id"
          :article="item"
          class="randomArticlerrr"
        >
        </RandomArticle>
      </div> -->
    </div>
    <div class="tags">
      <div class="tagsTitle">
        <div class="title">
          <svg-icon name="24gl-tags3" width="30px" height="30px"></svg-icon>
          <div>所有标签</div>
        </div>
        <span></span>
      </div>
      <div class="allTags">
        <!-- <span
          class="labelTag"
          @click="quickTag(tag)"
          v-for="tag in allTags"
          :key="tag.id"
          >{{ "#" + tag.content }}</span
        > -->
      </div>
    </div>
    <div class="message">
      <div class="messageTitle">
        <div class="title">
          <svg-icon name="liuyan" width="30px" height="30px"></svg-icon>
          <div>最新留言</div>
        </div>
        <span></span>
      </div>
      <div class="messageContent">
        <!-- <miniMessage
          v-for="item in messageList"
          :key="item.id"
          :item="item"
        ></miniMessage> -->
      </div>
    </div>
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

  .message {
    margin-top: 2rem;
    width: 100%;
    height: 40rem;
    background-color: #fff;
    // box-shadow: 0 0 .5rem .2rem var(--gray-light-sahdow);
    border-radius: 0.5rem;
    transition: all 0.3s;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    box-sizing: border-box;
    padding: 0 1rem;

    // &:hover {
    //     box-shadow: .1rem .1rem .5rem var(--gray-sahdow);

    // }

    .messageTitle {
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
      height: 5rem;
      width: 100%;
      font-size: 1.8rem;

      .title {
        display: flex;
        align-items: center;
        margin-bottom: 0.5rem;

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

    .messageContent {
      width: 100%;
      overflow: hidden;
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
    }
  }

  .tags {
    margin-top: 2rem;
    width: 100%;
    height: 30rem;
    background-color: #fff;
    // box-shadow: 0 0 .5rem .2rem var(--gray-light-sahdow);
    border-radius: 0.5rem;
    display: flex;
    flex-direction: column;
    transition: all 0.3s;

    // &:hover {
    //     box-shadow: .1rem .1rem .5rem var(--gray-sahdow);

    // }

    .tagsTitle {
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
      width: 90%;
      margin-left: 1rem;
      font-size: 1.8rem;
      height: 5rem;

      .title {
        display: flex;
        align-items: center;
        justify-content: flex-start;
        margin-bottom: 0.5rem;

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

    .allTags {
      display: flex;
      flex-wrap: wrap;
      padding: 1rem;
    }
  }
}

.randomArticle {
  width: 100%;
  border-radius: 0.5rem;
  // box-shadow: 0 0 .5rem .2rem var(--gray-light-sahdow);
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff;

  // &:hover {
  //     box-shadow: .1rem .1rem .5rem var(--gray-sahdow);
  // }

  .randomArticleTitle {
    width: 95%;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    height: 5rem;
    font-size: 1.8rem;

    .title {
      &:hover {
        cursor: pointer;

        .icon {
          transition: all 0.5s;
          transform: rotateZ(360deg);
          color: var(--special-font-color);
        }
      }
    }

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

  .randomArticleContent {
    width: 100%;
    box-sizing: border-box;
    padding: 1rem;
  }
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