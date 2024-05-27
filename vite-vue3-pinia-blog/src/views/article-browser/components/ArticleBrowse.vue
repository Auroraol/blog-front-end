<template>
  <el-row>
    <el-col :span="2" :xs="1">
      <el-affix offset="70">
        <BrowserSidePanel
          class="browserSide container"
          v-if="device === 'desktop' && !loading"
          :title="article.title"
        ></BrowserSidePanel>
      </el-affix>
    </el-col>
    <el-col :span="16" :xs="22">
      <div v-if="!loading">
        <transition name="fade">
          <div class="layout-left-side">
            <h2 class="art-title">
              <span v-if="article.original !== 1">【转载】</span>
              {{ article.title }}
            </h2>
            <!-- 用户 -->
            <div class="author-info-block">
              <div class="avatar-wrapper">
                <el-avatar
                  size="large"
                  :src="article.user.avatar || defaultAvatar"
                ></el-avatar>
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
        </transition>
        <div class="articleMd">
          <!-- 文章内容 -->
          <MdPreview
            editorId="preview-only"
            :modelValue="article.content"
            :showCodeRowNumber="true"
            previewTheme="vuepress"
            codeTheme="a11y"
          />
        </div>

        <!-- 版权信息  url: 文章地址 original:是否原创(1)-->
        <div class="article-bottom">
          <copy-right
            :url="article.original === 1 ? url : article.reproduce"
            :original="article.original"
          />
          <art-tags :tags="article.tagList" />
          <div class="pre-next">
            <ul>
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
        <!-- 评论组件 -->
        <comment-list :article-id="id" :author-id="article.user.id" />
      </div>
    </el-col>

    <el-col :span="6" :xs="1"
      ><div class="grid-content ep-bg-purple" v-if="device === 'desktop'">
        <el-affix offset="60">
          <!-- 相关阅读 -->
          <div v-if="!loading" class="related-articles">
            <interrelated-list :article-id="id" :authorId="authorId" />
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
      </div>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { MdPreview, MdCatalog } from "md-editor-v3";
import { useRoute, onBeforeRouteUpdate } from "vue-router";
import { viewArtilce, incrementView } from "/@/api/article/article";
import CopyRight from "./CopyRight.vue";
import ArtTags from "./ArtTags.vue";
import BrowserSidePanel from "./BrowserSidePanel.vue";
import InterrelatedList from "./InterrelatedList.vue";
import CommentList from "./CommentList.vue";

// preview.css相比style.css少了编辑器那部分样式
import "md-editor-v3/lib/preview.css";
import gsap from "gsap";
import { formatDate } from "/@/utils/format/format-time";
import { useSettingsStore } from "/@/store/index";
import { useGetters } from "/@/store/getters";

const useGettersPinia = useGetters();
const useSettingsStorePinia = useSettingsStore();
const defaultAvatar = computed(() => useSettingsStorePinia.defaultAvatar);
const device = computed(() => useGettersPinia.device);

//md-catalog目录的监听设置
const scrollElement = document.documentElement;

const article = ref();
const loading = ref(true);
const id = ref(0);
const url = ref("");

const router = useRouter();

// 初始化
onMounted(async () => {
  try {
    containerGsap();
    containerGsap1();
    //   id.value = route.params?.id
    //在Vue Router中，无论路由参数在URL中的形式是什么，都会以字符串的形式传递到路由组件中
    id.value = Number(router.currentRoute.value.params.id); // 当前路由的 id 参数
    initArticle(); //加载文章
    url.value = window.location.href;
  } catch (error) {
    console.error("Error fetching data:", error);
  }
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

// 动画
const containerGsap1 = () => {
  gsap.from(".articleMd", {
    duration: 0.3,
  });
};

// 加载文章数据
const initArticle = async () => {
  try {
    const data = await viewArtilce(id.value);
    article.value = data;
    loading.value = false;
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

const incrementViewCount = async () => {
  try {
    const res = await incrementView(id.value);
    if (res) {
      article.value.viewCount++;
    }
  } catch (error) {
    console.error(error);
  }
};

const spanelRef = ref(null);
</script>

<style lang="less" scoped>
// 过渡
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

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
</style>
