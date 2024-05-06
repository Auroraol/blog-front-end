<!-- 标签页面 -->
<template>
  <div ref="container" class="container">
    <el-row>
      <el-col :span="20" :xs="23">
        <ul v-if="device !== 'desktop'" class="list-header">
          <li
            v-for="(tag, index) in tags"
            :key="index"
            class="list-header-item"
            :class="{ 'header-item-active': tagId === tag.id }"
            @click="tagClick(tag.id)"
          >
            {{ tag.name }}
          </li>
        </ul>
        <!-- 左边 -->
        <div class="left-side">
          <div class="content-head">
            <p class="content-row">
              <span style="color: #00a4ff">#</span>{{ tagName }}
            </p>
            <p class="content-row content-des">
              当前标签共计{{ total }}篇文章。
            </p>
          </div>
          <div />
          <div class="content-list">
            <article-list :list="artList" :loading="loading" />

            <!-- 分页 -->
            <el-pagination
              v-model:current-page="current"
              v-model:page-size="size"
              :small="small"
              :disabled="disabled"
              :background="background"
              :total="total"
              layout="prev, pager, next"
              @current-change="currentChange"
            />
          </div>
        </div>
      </el-col>
      <el-col :span="4" v-if="device === 'desktop'">
        <!-- 右边 -->
        <div class="right-side">
          <div class="tag-box">
            <div class="box-head">全部标签</div>
            <ul class="tag-list">
              <li
                v-for="(tag, index) in tags"
                :key="index"
                class="list-item btn"
                :class="{ active: tagId === tag.id }"
                @click="tagClick(tag.id)"
              >
                {{ tag.name }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, computed } from "vue";
import { tagList } from "/@/api/tag/tag";
import { useRouter } from "vue-router";
import { useGetters } from "/@/store/getters";
import { tagListResponseType } from "/@/api/tag/data";
import { pagePublishedArticle } from "/@/api/article/article";
const AsyncArticleList = defineAsyncComponent(
  () => import("/@/components/Box/ArticleBox/ArticleList.vue")
);

const useGettersPinia = useGetters();
const router = useRouter();
const device = computed(() => useGettersPinia.device);

const tagId = ref<number>(0);
const current = ref(1);
const size = ref(5);
const tags = ref<tagListResponseType[]>([]);
const artList = ref([]);
const total = ref(0);
const loading = ref(false);

// 初始化
onMounted(() => {
  init();
});

const init = async () => {
  try {
    const res = await tagList();
    tags.value = res;

    //在Vue Router中，无论路由参数在URL中的形式是什么，都会以字符串的形式传递到路由组件中
    const id = Number(router.currentRoute.value.query.id); // 当前路由的 id 参数
    //console.log(typeof id);
    if (id && tags.value.some((ele) => ele.id === id)) {
      // id表示的id存在
      tagClick(id);
    } else {
      if (tags.value.length > 0) {
        tagClick(tags.value[0].id);
      } else {
        console.error("标签列表为空");
      }
    }
  } catch (error) {
    console.error(error);
  }
};

const tagName = computed(() => {
  const tag = tags.value.find((ele) => ele.id === tagId.value);
  return tag ? tag.name : null;
});

const tagClick = (id) => {
  tagId.value = id;
  current.value = 1;
  getArtList();
};

// 分页监听, 选择第几页
const currentChange = (cur) => {
  current.value = cur;
  getArtList();
};

// 获取文章列表
const getArtList = async () => {
  loading.value = true;
  const params = {
    current: current.value,
    size: size.value,
    tagId: tagId.value,
  };
  try {
    const data = await pagePublishedArticle(params);
    // console.log(data);
    loading.value = false;
    total.value = data.total;
    artList.value = data.records;
  } catch (error) {
    console.error(error);
    loading.value = false;
  }
};
</script>

<style lang="less" scoped>
.container {
  min-height: 605px;

  // 左
  .left-side {
    margin-top: 5px;
    margin-left: 20px;
    margin-right: 20px;
    flex: 1;
    border-radius: 0.5rem;
    margin-bottom: 20px;

    background: #fff;
    @media screen and (max-width: 922px) {
      margin-right: 0;
    }

    .content-head {
      padding: 30px 30px;
      font-weight: 700;
      font-size: 25px;
      border-bottom: 1px solid hsla(0, 0%, 59.2%, 0.1);

      .content-row {
        padding: 0;
        margin: 0;
      }

      .content-des {
        margin-top: 10px;
        font-size: 14px;
        font-weight: normal;
        color: #909090;
      }
    }

    .content-list {
      margin: 0;
      box-sizing: border-box;

      .el-pagination {
        display: flex;
        justify-content: center;
        text-align: center;
        padding: 30px;
        padding-bottom: 0;
        background: #eee;
      }
    }
  }

  // 右
  .right-side {
    margin-top: 5px;
    margin-right: 20px;
    border-radius: 0.5rem;
    background: #fff;
    font-size: 16px;
    min-height: 190px;

    .tag-box {
      width: 100%;
      display: flex;
      flex-direction: column;

      // 下划线
      .box-head {
        border-bottom: 1px solid hsla(0, 0%, 59.2%, 0.2);
        padding: 12px 10px;
      }

      .tag-list {
        margin: 10px;
        padding: 0;
        min-height: 310px;

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

        .active {
          background-color: #79bbdc;
          color: #fff;
        }
      }
    }

    @media screen and (max-width: 922px) {
      display: none;
    }
  }

  //
  .list-header {
    margin: 0;
    padding: 0;
    display: flex;
    align-items: center;
    white-space: nowrap;
    overflow-x: scroll;
    font-size: 14px;
    background: #fff;
    border-bottom: 1px solid hsla(0, 0%, 59.2%, 0.1);

    &:first-child {
      margin-left: 20px;
    }

    .list-header-item {
      list-style: none;
      cursor: pointer;
      padding: 15px;
      margin-right: 5px;
    }

    .header-item-active {
      color: #007fff;
    }
  }
}
</style>
