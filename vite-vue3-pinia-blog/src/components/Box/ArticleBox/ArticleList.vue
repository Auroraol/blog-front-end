<template>
  <ul
    class="note-list"
    v-loading="loading"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="#fff"
  >
    <transition-group name="fade-list">
      <li v-for="(item, index) in list" :key="index" class="list-item">
        <!-- 封面 -->
        <div class="wrap-img">
          <img :src="item.cover" />
        </div>
        <!-- 用户  -->
        <div class="wrapper-meta">
          <div class="avatar-wrapper">
            <img class="user-avatar" :src="item.user.avatar" />
          </div>
          <span class="right-solt">{{ item.user.nickname }}</span>
          <span class="right-solt">{{ formatD(item.publishTime) }}</span>
          <span class="active" @click="categoryClick(item.categoryId)">{{
            item.categoryName
          }}</span>
        </div>
        <!-- 文章内容 -->
        <div class="content">
          <router-link :to="'/article/' + item.id" class="title"
            ><span v-if="item.original !== 1">【转载】</span>
            {{ item.title }}</router-link
          >
          <p class="abstract multi-ellipsis--l3">{{ item.summary }}</p>
          <!-- 文章标签 -->
          <div class="tags-wrapper">
            <span
              v-for="(tag, index2) in item.tagList"
              :key="index2"
              class="tag active btn"
              @click="tagClick(tag.id)"
            >
              {{ tag.name }}
            </span>
          </div>
          <!-- 评论点赞收藏浏览 -->
          <div class="meta">
            <span>{{ item.commentCount }}&ensp;评论</span>
            <span>{{ item.likeCount }}&ensp;点赞</span>
            <span>{{ item.collectCount }}&ensp;收藏</span>
            <span>{{ item.viewCount }}&ensp;浏览</span>
          </div>
        </div>
      </li>
    </transition-group>
    <div v-show="list.length === 0 && !loading" class="list-empty">
      <!-- 列表为空 -->
      <el-empty description="列表为空" />
    </div>
  </ul>
</template>

<script setup lang="ts">
import { formatDate } from "/@/utils/format/format-time";
import { useRouter } from "vue-router";

const router = useRouter();

// 传参 props
const props = defineProps({
  list: {
    type: Array,
    default() {
      return [];
    },
  },
  loading: {
    type: Boolean,
    default: true,
  },
});

const formatD = (str: string): string => {
  //2024-04-08 15:03:51 -> 2024/04/08 15:03:51
  str = str.replace(/-/g, "/");
  const date = new Date(str);
  const now = new Date();
  // 是当前年,就不显示年份
  return date.getFullYear() === now.getFullYear()
    ? formatDate(new Date(str), "mm月dd日")
    : formatDate(new Date(str), "YYYY年mm月dd日");
};

const categoryClick = (id: string) => {
  if (router.currentRoute.value.path !== "/category") {
    router.push({
      path: "/category",
      query: { id: id },
    });
  }
};

const tagClick = (id: string) => {
  if (router.currentRoute.value.path !== "/tag") {
    router.push({
      path: "/tag",
      query: { id: id },
    });
  }
};
</script>


<style lang="less" scoped>
.note-list {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  min-height: 50px;
  background-color: #fff;
  border-bottom-left-radius: 1rem;
  border-bottom-right-radius: 1rem;
  .list-item {
    position: relative; /*相对定位*/
    min-height: 200px;
    width: 100%;
    border-bottom: 1px solid #1413132f;
    padding: 15px 2px 15px 30px;

    @media screen and (max-width: 922px) {
      padding-left: 15px;
      max-width: 380px;
    }

    .active {
      cursor: pointer;
      &:hover {
        color: #007fff;
      }
    }

    // 封面
    .wrap-img {
      position: absolute; /*绝对定位 根据父类偏移距离 left right top bottom*/
      width: 150px;
      height: 90px;
      top: 50%;
      transform: translateY(-50%);
      right: 20px;

      overflow: hidden;
      border-radius: 4px;
      border: 1px solid #f3f7fa;

      @media screen and (max-width: 922px) {
        width: 100px;
        height: 60px;
        right: 10px;
      }

      img {
        width: 100%;
        height: 100%;
      }
    }

    //用户
    .wrapper-meta {
      font-size: 14px;
      font-weight: 700;
      display: flex;
      align-items: center;
      margin-bottom: 10px;

      .avatar-wrapper {
        position: relative;
        display: inline-block;
        margin-right: 5px;

        img {
          width: 30px;
          height: 30px;
          border-radius: 50%;
          border: 1px solid rgba(0, 0, 0, 0.1);
        }
      }

      .right-solt:after {
        content: "·";
        margin: 0 5px;
        color: #b2bac2;
      }
    }

    // 文章内容
    .content {
      width: 100%;
      padding-right: 180px;

      @media screen and (max-width: 922px) {
        padding-right: 120px;
      }

      .title {
        margin: -7px 0 4px;
        display: inline-block;
        font-size: 18px;
        font-weight: 700;
        line-height: 1.5;
        color: #2f2f2f;

        @media screen and (max-width: 922px) {
          width: 100%;
          font-size: 15px;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }

        &:hover {
          text-decoration: underline;
        }
      }

      .abstract {
        margin: 0;
        font-size: 13px;
        line-height: 24px;
        color: #999;
        margin-bottom: 5px;

        @media screen and (max-width: 922px) {
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          text-overflow: ellipsis;
          overflow: hidden;
        }
      }

      .tags-wrapper {
        font-size: 11px;
        margin-bottom: 15px;
        display: flex;
        flex-wrap: wrap;

        .tag {
          border: 1px #999 solid;
          border-radius: 14px;
          padding: 5px 12px;
          margin-right: 3px;

          &:hover {
            border: 1px #007fff solid;
          }
        }
      }

      .meta {
        padding-right: 0 !important;
        font-size: 12px;
        font-weight: normal;
        line-height: 20px;

        span {
          margin-right: 10px;
        }
      }
    }
  }

  // .list-empty {
  //   background: #fff;
  //   width: 100%;
  //   height: 100px;
  //   line-height: 100px;
  //   border-bottom: 1px solid #f0f0f0;
  //   text-align: center;
  // }
}
</style>
