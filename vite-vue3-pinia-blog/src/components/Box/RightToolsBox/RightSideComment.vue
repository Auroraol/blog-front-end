// 评论
<template>
  <div class="container">
    <div class="message">
      <div class="messageTitle">
        <div class="title">
          <svg-icon name="liuyan" width="30px" height="30px"></svg-icon>
          <div>最新评论</div>
        </div>
        <span></span>
      </div>
      <div class="messageContent">
        <ul
          v-loading="loading"
          class="content-list"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="#fff"
        >
          <li v-for="(item, index) in list" :key="index" class="list-item">
            <div class="avatar-wrapper">
              <img
                class="user-avatar"
                :src="(item.fromUser && item.fromUser.avatar) || defaultAvatar"
              />
            </div>
            <div class="content-box">
              <p class="content-row text-ellipsis">
                {{ item.fromUser.nickname }}&emsp;<span style="float: right">{{
                  parseDate(item.commentTime)
                }}</span>
              </p>
              <router-link
                class="content-row text text-ellipsis"
                v-if="item.article"
                :to="'/article/' + item.article.id"
                v-html="item.content"
              />
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, onMounted } from "vue";
import { formatPast } from "/@/utils/format/format-time";
import { latestCommentList } from "/@/api/comment/comment";
import { useSettingsStore } from "/@/store/index";

const useSettingsStorePinia = useSettingsStore();
const defaultAvatar = computed(() => useSettingsStorePinia.defaultAvatar);

//
const loading = ref(false);
const list = ref();

// 在组件挂载时调用初始化函数
onMounted(() => {
  init();
});

// 初始化函数
const init = async () => {
  const params = { limit: 9 };
  loading.value = true;
  try {
    const res = await latestCommentList(params); // 获取评论,需要token
    list.value = res;
    loading.value = false;
  } catch (error) {
    console.error(error);
  }
};

// 日期转换函数
const parseDate = (str) => {
  // iOS 兼容性问题
  str = str.replace(/-/g, "/");
  return formatPast(new Date(str));
};
</script>


<style lang="less" scoped>
.container {
  width: 100%;
  background: #fff;
  margin-bottom: 10px;
  padding-bottom: 5px;
  color: #2e3135;
  position: relative;
  margin-top: 2rem;
  border-radius: 0.5rem;
  transition: all 0.3s;
  padding: 0 1rem;

  .message {
    // box-shadow: 0 0 0.5rem 0.2rem var(--gray-light-sahdow);
    // 鼠标悬停
    // &:hover {
    //   box-shadow: 0.1rem 0.1rem 0.5rem var(--gray-sahdow); //阴影
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

      .content-list {
        width: 100%;
        padding: 0;
        margin: 0px;
        box-sizing: border-box;
        padding: 10px;
        min-height: 120px;

        .list-item {
          width: 100%;
          list-style: none;
          display: flex;
          margin-bottom: 5px;
          font-size: 15px;
          .avatar-wrapper {
            position: relative;

            .user-avatar {
              width: 40px;
              height: 40px;
              border-radius: 50%;
              border: 1px solid rgba(0, 0, 0, 0.1);
            }
          }

          .content-box {
            flex: 1;
            overflow: hidden;
            // font-size: 12px;
            height: 40px;
            padding-left: 5px;
            padding-top: 3px;
            padding-bottom: 2px;
            line-height: 18px;
            color: #909090;

            .content-row {
              padding: 0;
              margin: 0;
            }

            .text {
              color: #2e3135;
              cursor: pointer;

              &:hover {
                color: #007fff;
                text-decoration: underline;
              }
            }
          }
        }
      }
    }
  }
}
</style>
