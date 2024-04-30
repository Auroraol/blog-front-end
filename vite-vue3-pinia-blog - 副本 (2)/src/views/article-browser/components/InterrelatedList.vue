<template>
  <div class="in-container">
    <div class="head">相关阅读</div>
    <ul
      v-loading="loading"
      class="content-list"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="#fff"
    >
      <li v-for="(item, index) in list" :key="index" class="list-item">
        <router-link class="content-row title" :to="'/article/' + item.id">{{
          item.title
        }}</router-link>
        <p class="content-row">阅读&ensp;{{ item.viewCount }}</p>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { interrelated } from "/@/api/article/article";

const props = defineProps({
  articleId: {
    type: [String, Number],
    required: true,
  },
});

const loading = ref(true);
const list = ref<any[]>([]);

onMounted(() => {
  init();
});

const init = async () => {
  try {
    const params = { articleId: props.articleId, limit: 6 };
    const res = await interrelated(params);    
    list.value = res;
    loading.value = false;
  } catch (error) {
    console.error("相关文章获取失败");
    
  }
};
</script>

<style lang="less" scoped>
.in-container {

 
  width: 100%;
  background: #fff;
  border-radius: 0.5rem;
  margin-bottom: 10px;

  .head {
    font-size: 1.7rem;
    color: #2e3135;
    border-bottom: 1px solid hsla(0, 0%, 59.2%, 0.2);
    padding: 12px 10px;
  }

  .content-list {
    max-height: 30px;
    overflow-y: auto; /* 当内容溢出时显示滚动条 */
    margin: 10px;
    min-height: 65px;

    .list-item {
      list-style: none;
      line-height: 15px;
      margin-bottom: 10px;

      .content-row {
        margin: 0;
        padding: 0;
        font-size: 12px;
        margin-bottom: 5px;
      }

      .title {
        font-size: 14px;
        color: #000;
        cursor: pointer;
        line-height: 18px;

        &:hover {
          color: #007fff;
          text-decoration: underline;
        }
      }
    }
  }
}
</style>
