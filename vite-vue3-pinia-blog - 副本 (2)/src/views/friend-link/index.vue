<template>
  <div class="container">
    <div class="content-container">
      <ul class="list">
        <li v-for="(item, index) in list" :key="index" class="item">
          <a :href="item.url" target="_blank">
            <div class="logo-box">
              <el-avatar :src="item.icon" />
            </div>
            <div class="content-box">{{ item.name }}</div>
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { listFriendLink } from "/@/api/friend-link/friend-link";
import gsap from "gsap";

// 使用 ref 创建响应式数据
const list = ref([]);

// 使用 onMounted 来在组件挂载后初始化数据
onMounted(async () => {
  try {
    const res = await listFriendLink();
    list.value = res;
  } catch (error) {
    ElMessage.error("获取失败");
  }

  //动画
  gsap.from(".list", {
    duration: 0.5,
    x: -50,
    opacity: 0.2,
  });
});
</script>

<style lang="less" scoped>
.container {
  @import "/@/assets/styles/variables.css";
  width: 90%;
  margin: 0 auto;
  height: 100vh;
  background: #fff;

  .content-container {
    margin: 0 auto;
    margin-top: 5px;
    border-radius: 2px;

    .list {
      width: 100%;
      box-sizing: border-box;
      padding: 10px;

      display: flex;
      flex-wrap: wrap;

      @media screen and (max-width: 960px) {
        justify-content: space-around;
        margin-top: 0;
      }

      .item {
        list-style: none;

        margin: 10px;
        margin-bottom: 25px;
        position: relative;

        .logo-box {
          margin-top: 5px;
          overflow: hidden;
          border-radius: 50%;
          position: absolute;
          top: -20px;
          left: 50%;
          transform: translateX(-50%);
          background: #fff;
          border: 1px solid #f1f1f1;
        }

        .content-box {
          color: #333;
          font-weight: 600;
          font-size: 15px;
          text-align: center;
          width: 180px;
          height: 80px;
          box-sizing: border-box;
          padding-top: 40px;
          border: 1px solid #f1f1f1;

          @media screen and (max-width: 960px) {
            width: 150px;
          }
        }
      }
    }
  }
}
</style>
