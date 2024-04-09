<template>
  <div class="container">
    <div v-if="!loading" class="content-container">
      <div class="coverImg">
        <img :src="article.cover" alt="" />
        <div class="mask">
          <div class="title">{{ article.title }}</div>
          <div class="author" @click="goToPersonalCenter">
            <div class="head">
              <img :src="article.user.avatar" alt="head" />
            </div>
            <div class="nickName">{{ article.user.nickname }}</div>
          </div>
          <div class="time">
            <span>{{ formatDateStr(article.publishTime) }}</span>
          </div>
        </div>
      </div>
      <!-- 左 -->
      <div class="layout-left-side">
        <h2 class="art-title">
          <span v-if="article.original !== 1">【转载】</span>
          {{ article.title }}
        </h2>
        <!-- 用户 -->
        <div class="author-info-block">
          <div class="avatar-wrapper">
            <img :src="article.user.avatar || defaultAvatar" class="avatar" />
          </div>
          <div class="author-info-box">
            <p class="nickename">{{ article.user.nickname }}</p>
            <div class="meta-box">
              <span class="time">{{ formatDateStr(article.publishTime) }}</span>
              <span class="views-count">浏览&ensp;{{ article.viewCount }}</span>
            </div>
          </div>
        </div>
        <!--文章内容-->
        <!-- <MdPreview
          editorId="preview-only"
          :modelValue="article.htmlContent"
          previewTheme="vuepress"
          codeTheme="a11y"
        /> -->
        <!-- 版权信息  url: 文章地址 original:是否原创(1)-->
        <copy-right
          :url="article.original === 1 ? url : article.reproduce"
          :original="article.original"
        />

        <art-tags :tags="article.tagList" />
        <ul class="pre-next">
          <li v-if="article.previous">
            <router-link :to="'/article/' + article.previous.id">
              上一篇&ensp;:&ensp;{{ article.previous.title }}
            </router-link>
          </li>
          <li v-if="article.next">
            <router-link :to="'/article/' + article.next.id">
              下一篇&ensp;:&ensp;{{ article.next.title }}
            </router-link>
          </li>
        </ul>
        <comment-list :article-id="id" :author-id="article.user.id" />
      </div>

      <!-- 右 -->
      <div v-if="device === 'desktop'" class="layout-right-side">
        <interrelated-list :article-id="id" />
      </div>
    </div>
    <!-- 点赞收藏 -->
    <suspended-panel
      v-if="device === 'desktop'"
      ref="spanel"
      :title="article.title"
      :like-count="article.likeCount"
      :collect-count="article.collectCount"
      @likeCountChanges="likeCountChanges"
      @collectCountChanges="collectCountChanges"
    />
    <div class="articleMd">
      <div class="contaier">
        <div class="mdTextTop">
          <!-- <div class="editor">
                    <button class="option" @click="expandEdiorBox">编辑</button>
                    <div class="ediorBox">
                        <button @click="topArticle">置顶</button>
                        <button @click="openEditor">编辑</button>
                        <button @click="openDel">删除</button>
                    </div>
                    <Teleport to="body">
                        <el-dialog v-model="dialogFormVisible" title="删除文章">
                            <div style="margin-bottom: 2rem ;">如果要删除此文章，请输入以下验证码数字：{{ randomNum }}</div>
                            <el-input v-model="code" placeholder="Please input" />
                            <template #footer>
                                <span class="dialog-footer">
                                    <el-button type="primary" @click="dialogFormVisible = false">取消</el-button>
                                    <el-button @click="trueDel">确定</el-button>
                                </span>
                            </template>
                        </el-dialog>
                    </Teleport>
                </div> -->
          <!-- 文章内容 -->
          <!-- <div class="articleIndex"> -->
          <MdPreview
            class="articleIndex"
            editorId="preview-only"
            :modelValue="article.htmlContent"
            previewTheme="vuepress"
            codeTheme="a11y"
          />
          <!-- </div> -->
        </div>
        <!-- 版权信息  url: 文章地址 original:是否原创(1)-->
        <copy-right
          :url="article.original === 1 ? url : article.reproduce"
          :original="article.original"
        />

        <!-- 点赞收藏数量显示组件 -->
        <!-- <BrowserBottom :articleInfo="browseArticle"></BrowserBottom> -->

        <!-- 评论区组件 -->
        <!-- <Suspense>
                <template #default>
                    <browserComment :articleId="(articleId as string)"></browserComment>
                </template>

                <template #fallback>
                    <Loading2></Loading2>
                </template>
            </Suspense> -->
      </div>
      <el-affix class="catalog" :offset="90">
        <div class="cataSide" ref="sticky">
          <div class="titleTop">文章目录</div>
          <!--文章目录-->
          <MdCatalog
            editorId="preview-only"
            class="browserCatalog"
            :scrollElement="scrollElement"
          />
          <BrowserSide
            class="browserSide"
            :id="(articleId as string)"
          ></BrowserSide>
        </div>
      </el-affix>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
// import { initAudio } from '@/assets/audio/index.js'
// import '@/assets/audio/index.css'
// import '@/styles/heilingt.css'
// import { mapGetters } from 'vuex'
// import { formatDate } from '@/utils/index.js'
// import AppHeader from '@/components/Header/index'
import { viewArtilce } from "/@/api/article/article";
// import CommentList from './components/CommentList'
import CopyRight from "./CopyRight.vue";
// import ArtTags from './components/ArtTags'
// import InterrelatedList from './components/InterrelatedList'
// import SuspendedPanel from './components/SuspendedPanel'
import { MdPreview, MdCatalog } from "md-editor-v3";
// preview.css相比style.css少了编辑器那部分样式
import "md-editor-v3/lib/preview.css";
import gsap from "gsap";
import { formatDate } from "/@/utils/format/format-time";

//md-catalog目录的监听设置
const scrollElement = document.documentElement;

const article = ref("");
const loading = ref(true);
const id = ref(0);
const url = ref("");

const router = useRouter();

// const defaultAvatar = mapGetters(["defaultAvatar"]);
// const device = mapGetters(["device"]);

onMounted(() => {
  gsapEnterCover();
  //   id.value = route.params?.id
  //在Vue Router中，无论路由参数在URL中的形式是什么，都会以字符串的形式传递到路由组件中
  id.value = Number(router.currentRoute.value.params.id); // 当前路由的 id 参数
  initArticle();
  url.value = window.location.href;
});

const containerGsap = () => {
  gsap.from(".contaier", {
    y: 50,
    duration: 0.3,
  });
};

const gsapEnterCover = () => {
  gsap.from(".coverImg", {
    duration: 0.5,
    x: 50, //右向左移动  -50左向右移动
    opacity: 0.2, //透明度
  });
};

// 路由变化，用于当前页进当前页
// beforeRouteUpdate(to, from, next) {
//     next();
// },

// onBeforeRouteUpdate((to, from, next) => {
//   id.value = to.params?.id
//   loading.value = true
//   if (device.value === 'desktop') {
//     spanelRef.value.changeUrl(url.value)
//     spanelRef.value.changeId(id.value)
//     spanelRef.value.isLiked()
//     spanelRef.value.isCollected()
//   }
//   url.value = 'http://www.poile.cn/article/' + id.value
//   initArticle()
//   next()
// })

// 加载文章数据
const initArticle = async () => {
  loading.value = true;
  try {
    const data = await viewArtilce(id.value);
    loading.value = false;
    article.value = data;
    // 初始化音频
    //   import('@/assets/audio/index.js').then(({ initAudio }) => {
    //     initAudio()
    //   })
    incrementViewCount();
  } catch (error) {
    console.error("加载文章数据失败");
  }
};

const formatDateStr = (str: string) => {
  str = str.replace(/-/g, "/");
  return formatDate(new Date(str), "YYYY年mm月dd日");
};

const incrementViewCount = () => {
  //   incrementView(id.value).then(res => {
  //     if (res.data) {
  //       article.value.viewCount++
  //     }
  //   })
};

const likeCountChanges = (val: number) => {
  //   article.value.likeCount += val
};

const collectCountChanges = (val: number) => {
  //   article.value.collectCount += val
};

const spanelRef = ref(null);
</script>



<style lang="less" scoped>
.container {
  @import "/@/assets/styles/variables.css";
  width: 100%;
  height: 100vh;
  //   overflow-x: hidden;
  //   overflow-y: auto;
  //   overflow-y: -webkit-overlay;
  //   overflow-y: overlay;

  .content-container {
    .coverImg {
      width: 90%;
      margin: 0 auto;
      display: flex;
      justify-content: center;
      height: 10rem;
      position: relative;
      border-radius: 1rem;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .mask {
        width: 100%;
        height: 50%;
        background-image: linear-gradient(
          rgba(255, 255, 255, 0),
          rgb(43, 43, 43)
        );
        position: absolute;
        bottom: 0;
        left: 0;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        color: rgb(245, 245, 245);

        .title {
          font-size: 3.5rem;
          font-weight: 600;
        }

        .author {
          display: flex;
          align-items: center;
          font-size: 1.5rem;
          padding: 1rem 0;

          &:hover {
            cursor: pointer;
          }

          .head {
            width: 3.5rem;
            height: 3.5rem;
            border-radius: 50%;
            overflow: hidden;
            margin-right: 1rem;

            img {
              width: 100%;
              height: 100%;
              object-fit: cover;
            }
          }
        }

        .time {
          display: flex;
          justify-content: center;
          flex-wrap: wrap;
          align-items: center;

          :nth-child(1) {
            padding: 0;
            font-size: 2.6rem;
          }

          span {
            margin-left: 0.2rem;
            font-size: 1.6rem;
          }

          .lastUpdataTime {
            margin-left: 3rem;

            & > span {
              font-size: 1.5rem;
              font-weight: 700;
              color: var(--special-font-color);
            }
          }
        }
      }
    }

    .layout-left-side {
      background: #fff;
      width: 720px;
      margin-left: 80px;
      box-sizing: border-box;
      padding: 0 15px 0 15px;
      margin-bottom: 25px;
      border-radius: 1rem;
      @media screen and (max-width: 960px) {
        width: 100%;
        margin-right: 0;
        margin-left: 0;
        margin-bottom: 0;
      }

      .art-title {
        font-size: 25px;
        font-weight: 700;
        color: #222;
      }

      .author-info-block {
        display: flex;

        .avatar-wrapper {
          width: 45px;
          height: 45px;
          border-radius: 50%;
          border: 1px solid rgba(0, 0, 0, 0.1);
          overflow: hidden;
          margin-right: 5px;

          .avatar {
            width: 100%;
            height: 100%;
          }
        }

        .author-info-box {
          .nickename {
            font-weight: 500;
            font-size: 15px;
            display: inline-block;
            margin: 5px;
            color: #000;
          }

          .meta-box {
            font-size: 12px;
            color: #909090;

            .views-count {
              margin-left: 5px;
            }
          }
        }
      }

      .text-container {
        font-size: 15px;
        margin-top: 24px;
      }

      .pre-next {
        padding: 0;
        margin: 0;
        margin-top: 15px;
        font-size: 14px;
        font-weight: 400;
        color: #007fff;

        li {
          list-style: none;
          margin: 5px;

          a:hover {
            text-decoration: underline;
          }
        }
      }
    }

    .layout-right-side {
      width: 240px;
      background: #fff;
      margin-left: 15px;
      border-radius: 2px;
      position: fixed;
      top: 75px;
      right: calc(calc(100% - 1100px) / 2);

      @media screen and (max-width: 960px) {
        display: none;
      }
    }
  }

  //  文章内容
  .articleMd {
    display: flex;
    width: 80%;
    margin: 0 auto;

    .contaier {
      width: 80%;

      .mdTextTop {
        background-color: #fff;
        padding: 1.4rem;
        border-radius: 0.5rem;
        box-shadow: 0.1rem 0.1rem 0.5rem var(--gray-sahdow);

        .editor {
          .option {
            background-color: #f8f8f8;
            color: #cecece;
            border-radius: 10rem;
            width: 100%;
            height: 2rem;
            cursor: pointer;
            transition: all 0.5s;

            &:hover {
              background-color: #f0f0f0;
              color: #646464;
            }
          }

          .ediorBox {
            display: flex;
            justify-content: center;
            align-items: center;
            overflow: hidden;
            margin-top: 0.5rem;
            width: 100%;
            height: 0;

            button {
              width: 9rem;
              height: 3rem;
              border-radius: 0.8rem;
              margin: 0 1rem;
              border: 0.2rem solid var(--border-line);
              background-color: transparent;
              cursor: pointer;
              transition: all 0.3s;

              &:hover {
                box-shadow: 0 0 10px 0 var(--border-line);
              }
            }

            :nth-child(3) {
              border: 0.2rem solid red;

              &:hover {
                box-shadow: 0 0 10px 0 red;
              }
            }
          }
        }
      }

      .articleIndex {
        min-height: 40rem;

        .mdText {
          background-color: transparent;
        }
      }
    }

    .catalog {
      margin-top: 6rem;
      flex: 1;
      margin-left: 2rem;

      .cataSide {
        max-width: 20rem;
        position: sticky;
        top: 1rem;

        .titleTop {
          width: 100%;
          background-color: rgb(255, 255, 255);
          margin-bottom: 0.5rem;
          display: flex;
          align-items: center;
          box-sizing: border-box;
          font-size: 1.7rem;
          font-weight: 600;
          padding: 1rem;
          border-radius: 0.5rem;
        }

        .browserCatalog {
          width: 100%;
          background-color: rgb(255, 255, 255);
          border-radius: 0.5rem;
          box-shadow: 0 0 0.3rem 0.1rem rgba(255, 255, 255, 0.4);
          box-sizing: border-box;
          padding: 2rem 1rem;
          font-size: 1.2rem;
        }
      }
    }
  }
}
</style>
