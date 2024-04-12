<template>
  <el-row>
    <el-col :span="2">
      <el-affix offset="70">
        <BrowserSidePanel
          class="browserSide container"
        ></BrowserSidePanel>
      </el-affix>
    </el-col>
    <el-col :span="16">
      <transition name="el-fade-in">
        <div v-if="!loading">
          <div class="layout-left-side">
            <h2 class="art-title">
              <span v-if="article.original !== 1">【转载】</span>
              {{ article.title }}
            </h2>
            <!-- 用户 -->
            <div class="author-info-block">
              <div class="avatar-wrapper">
                <el-avatar size="large" :src="article.user.avatar"></el-avatar>
              </div>
              <div class="author-info-box">
                <p class="nick-name">{{ article.user.nickname }}</p>
                <div class="meta-box">
                  <span class="time">{{
                    formatDateStr(article.publishTime)
                  }}</span>
                  <span class="views-count"
                    >浏览&ensp;{{ article.viewCount }}</span
                  >
                </div>
              </div>
            </div>
          </div>
          <div class="articleMd">
            <!-- 文章内容 -->
            <MdPreview
              editorId="preview-only"
              :modelValue="article.htmlContent"
              previewTheme="vuepress"
              codeTheme="a11y"
              @onGetCatalog="getCatalog"
            />
          </div>
          <!-- 版权信息  url: 文章地址 original:是否原创(1)-->
          <div class="article-bottom">
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
          </div>
        </div>
      </transition>
    </el-col>

    <el-col :span="6"
      ><div class="grid-content ep-bg-purple" />
      <el-affix offset="60">
        <!-- 相关阅读 -->
        <div v-if="device === 'desktop' && !loading" class="related-articles">
          <interrelated-list :article-id="id" />
        </div>
        <!--文章目录-->
        <div class="catalog">
          <div class="titleTop">文章目录</div>
          <MdCatalog
            offsetTop="200"
            scrollElementOffsetTop="100"
            editorId="preview-only"
            class="browserCatalog"
            :scrollElement="scrollElement"
          />
        </div>
      </el-affix>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, onBeforeRouteUpdate } from "vue-router";
// import { initAudio } from '@/assets/audio/index.js'
// import '@/assets/audio/index.css'
// import '@/styles/heilingt.css'
// import { mapGetters } from 'vuex'
// import { formatDate } from '@/utils/index.js'
// import AppHeader from '@/components/Header/index'
import { viewArtilce } from "/@/api/article/article";
import CopyRight from "./CopyRight.vue";
import ArtTags from "./ArtTags.vue";
import BrowserSidePanel from "./BrowserSidePanel.vue";
import InterrelatedList from "./InterrelatedList.vue";
// import CommentList from './CommentList.vue'
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
const device = ref("desktop");

const router = useRouter();

// 监听路由变化，处理滚动位置
router.afterEach(() => {
  // 将滚动位置重置为页面顶部
  window.scrollTo(0, 0);
});

// 初始化
onMounted(() => {
  containerGsap();
  //   id.value = route.params?.id
  //在Vue Router中，无论路由参数在URL中的形式是什么，都会以字符串的形式传递到路由组件中
  id.value = Number(router.currentRoute.value.params.id); // 当前路由的 id 参数
  initArticle(); //加载文章
  url.value = window.location.href;
});

// 失效,选择使用watch
// onBeforeRouteUpdate((to, from, next) => {

//   id.value = Number(router.currentRoute.value.params.id); // 当前路由的 id 参数 // 更新 id 数据
//   console.error(id.value);
//   loading.value = true;
//   // 更新 url 和重新初始化文章
//   url.value = "http://localhost:8888/article/" + id.value;
//   initArticle();
//   next();
// });

// 使用 watch 监听路由参数的变化, 因为当使用路由参数时，例如从 `/user/foo` 导航到 `/user/bar`，
//原来的组件实例会被复用。因为两个路由都渲染同个组件，比起销毁再创建，复用则显得更加高效。不过，这也意味着组件的生命周期钩子不会再被调用。
watch(
  () => Number(router.currentRoute.value.params.id),
  (newValue, oldValue) => {
    // 当路由参数发生变化时执行逻辑
    id.value = newValue;
    url.value = "http://localhost:8888/article/" + id.value;
    initArticle();
  }
);

// 动画
const containerGsap = () => {
  gsap.from(".container", {
    x: -50,
    duration: 0.3,
  });
};

const catalogList = ref([]);
const getCatalog = (list) => {
  console.log(list);
  catalogList.value = list;
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
  try {
    const data = await viewArtilce(id.value);
    article.value = data;
    loading.value = false;
    // 初始化音频
    //   import('@/assets/audio/index.js').then(({ initAudio }) => {
    //     initAudio()
    //   })
    incrementViewCount();
  } catch (error) {
    loading.value = true;
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
// 标题
.layout-left-side {
  background: #fff;
  padding: 0 15px 0 15px;
  border-radius: 0.5rem;

  .art-title {
    font-size: 2rem;
    padding-top: 10px;
  }

  .author-info-block {
    display: flex;
    align-content: center;

    margin-bottom: 10px;
    margin-top: 20px;

    .avatar-wrapper {
      margin-right: 10px;
    }

    .author-info-box {
      .nick-name {
        margin-top: 6px;
        font-size: 1.4rem;
        color: #000;
      }

      .meta-box {
        padding-top: 10px;
        font-size: 1.3rem;
        color: #909090;

        .views-count {
          margin-left: 10px;
        }
      }
    }
  }
}

// 内容
.articleMd {
  min-height: 350px;
  background-color: #fff;
  padding: 1.4rem;
  border-radius: 0.5rem;
  box-shadow: 0.1rem 0.1rem 0.5rem var(--gray-sahdow);
}

// 目录
.catalog {
  margin-top: 35px;
  margin-left: 1rem;
  margin-right: 2rem;

  .titleTop {
    width: 100%;
    background-color: rgb(255, 255, 255);
    margin-bottom: 0.5rem;
    box-sizing: border-box;
    font-size: 1.7rem;
    // font-weight: 600;
    padding: 1rem;
    border-radius: 0.5rem;
  }

  .browserCatalog {
    max-height: 400px; /* 设置最大高度 */
    overflow-y: auto; /* 当内容溢出时显示滚动条 */
    width: 100%;
    background-color: rgb(255, 255, 255);
    border-radius: 0.5rem;
    box-shadow: 0 0 0.3rem 0.1rem rgba(255, 255, 255, 0.4);
    box-sizing: border-box;
    padding: 2rem 1rem;
    font-size: 1.2rem;
  }
}

// 相关阅读
.related-articles {
  margin-left: 1rem;
  margin-right: 2rem;
}

// 文章底部
.article-bottom {
  margin-top: 10px;
  background-color: #fff;
  border-radius: 0.5rem;
  font-size: 1.4rem;

  .pre-next {
    margin-top: 15px;
    padding-bottom: 5px;
    font-size: 14px;
    font-weight: 400;
    color: #007fff;

    li {
      margin: 5px;
      a:hover {
        text-decoration: underline;
      }
    }
  }
}

// 过渡
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
