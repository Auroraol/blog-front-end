// todo
<template>
  <div class="topArticleBox-container" @click="goBrowse(info.id)">
    <div class="leftSide">
      <!-- coverImage -->
      <img :src="info.cover" alt="" class="coverImage" />
      <div class="mask"></div>
    </div>
    <div class="rightSide">
      <!-- 上半部分 -->
      <div class="top">
        <!-- 标题 -->
        <p>{{ info.title }}</p>
        <span>{{ info.content }}</span>
      </div>
      <!-- 下半部分 -->
      <div class="bottom">
        <!-- 标签 -->
        <div class="tags">
          <span v-for="tag in info.tag" :key="tag">#{{ tag }}</span>
        </div>
        <!-- 作者信息 -->
        <div class="authorInfo">
          <div class="head">
            <img :src="authorInfo.headImg" alt="head" />
          </div>
          <div class="headRight">
            <div class="nickName">{{ authorInfo.nickName }}</div>
            <div class="time">发布于:{{ info.time }}</div>
          </div>
        </div>
        <!-- 点赞收藏 -->
        <div class="interaction">
          <div class="praise">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-31dianzan"></use>
            </svg>
            <span>{{ pariseNum }}</span>
          </div>
          <div class="collection">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-shoucang"></use>
            </svg>
            <span>{{ collectionNum }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const router = useRouter();

type Props = {
  info: any;
};
const props = defineProps<Props>();

const info = props.info;
const pariseNum = info.parise.length; //点赞数量
const collectionNum = info.collections.length;

// //请求文章作者信息
const { data: res } = await useAxios.get("/userinfo", {
  params: {
    account: info.author,
  },
});
const authorInfo = res.data;

// //点击浏览文章
const goBrowse = (id: number) => {
  router.push({
    path: "/articlebrowse",
    query: {
      id: id,
    },
  });
};

const sliceTextCount = 125; //截取的长度
const reContent = extractMarkdownText(info.content);
if (reContent.length > sliceTextCount) {
  info.content = reContent.slice(0, sliceTextCount) + "..."; //展示部分正文内容，并且过滤掉md文档的关键字
} else {
  info.content = reContent.slice(0, sliceTextCount);
}
</script>

<style scoped lang="less">
.topArticleBox-container {
  display: flex;
  background-color: #84c4d4;
  margin-bottom: 1rem;
  border-radius: 1rem;
  overflow: hidden;
  transition: all 0.2s;
  position: relative;

  &::after {
    content: "置顶文章";
    display: flex;
    justify-content: center;
    align-items: center;
    color: rgb(85, 222, 240);
    font-size: 1.2rem;
    position: absolute;
    top: 1rem;
    left: 1rem;
    width: 8rem;
    height: 2.5rem;
    border: 0.2rem solid rgb(85, 222, 240);
    border-radius: 0.5rem;
  }

  &:hover {
    cursor: pointer;
    transform: translateY(-0.3rem);
  }

  .leftSide {
    flex: 6;
    position: relative;

    .coverImage {
      width: 100%;
      height: 100%;
      object-fit: cover;
      position: relative;
    }

    .mask {
      width: 40%;
      height: 100%;
      position: absolute;
      right: 0;
      top: 0;
      background-image: linear-gradient(
        to right,
        rgba(255, 255, 255, 0),
        rgba(255, 255, 255, 1)
      );
    }
  }

  .rightSide {
    box-sizing: border-box;
    flex: 4;
    padding: 2rem;
    background-color: #ffffff;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .top {
      display: flex;
      flex-direction: column;
      align-items: center;

      p {
        font-size: 2rem;
        font-weight: 700;
      }

      span {
        align-self: flex-start;
        font-size: 1.6rem;
      }
    }

    .bottom {
      .tags {
        span {
          font-style: italic;
          color: var(--special-font-color);
          font-size: 1.3rem;
          font-weight: 500;
          margin: 0.5rem;
        }
      }

      .authorInfo {
        margin-top: 1rem;
        width: 100%;
        display: flex;
        align-items: center;

        .head {
          width: 3.5rem;
          height: 3.5rem;
          background-color: rgb(214, 214, 214);
          border-radius: 50%;
          overflow: hidden;

          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }
        }

        .headRight {
          flex: 1;
          margin-left: 1rem;

          .nickName {
            margin-right: 1.5rem;
            font-size: 1.4rem;
          }

          .time {
            color: var(--font-gray-color);
          }
        }
      }

      .interaction {
        margin-top: 1rem;
        display: flex;
        align-items: center;
        font-size: 1.4rem;

        .collection {
          margin-left: 1rem;
        }
      }
    }
  }
}

@media screen and (max-width: 1100px) {
  .topArticleBox-container {
    display: flex;
    flex-direction: column;
  }
}
</style>