<template>
  <div
    v-loading="loading"
    class="container"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
  >
    <div v-if="!loading && !success" class="content-box">
      <h3 class="name">登录失败</h3>
      <p class="content">{{ content }}</p>
      <router-link
        to="/"
        type="text"
        style="width: 100%; text-align: center; color: #007fff"
        >返回首页</router-link
      >
    </div>
  </div>
</template>
<script setup lang="ts">
import { useRoute, useRouter } from "vue-router";
import { useGetters } from "/@/store/getters";
import { useUserStore, usePermissionStore } from "/@/store/index";

const route = useRoute();
const router = useRouter();
const useUserStorePinia = useUserStore();
const usePermissionStorePinia = usePermissionStore();

let loading = ref(true);
let success = ref(true);
let content = ref("第三方登录失败，请联系管理员。");

onMounted(() => {
  oauth();
});

const oauth = () => {
  const code = route.query.code || null;
  const state = route.query.state || null;
  if (!code || !state) {
    loading.value = false;
    success.value = false;
    content.value = "参数缺失，第三方登录失败";
    return;
  }
  const params = { type: state, code: code };
  new Promise(async (resolve, reject) => {
    try {
      useUserStorePinia.thirdLogin(params);
      let { roles } = await useUserStorePinia.getUserInfo();
      const accessRoutes = await usePermissionStorePinia.generateRoutes(roles);
      // 添加动态路由
      accessRoutes.forEach((res) => {
        router.addRoute(res);
      });
      loading.value = false;
      router.push("/");
      resolve("");
    } catch (error) {
      content.value = String(error);
      success.value = false;
      loading.value = false;
      reject(error);
    }
  });
};
</script>

<style lang="less" scoped>
.container {
  width: 100%;
  height: 100vh;
  position: relative;

  .content-box {
    text-align: center;
    width: 100%;
    max-width: 470px;
    box-sizing: border-box;
    background: #fff;
    border-radius: 4px;
    padding: 30px;
    position: absolute;
    top: 45%;
    left: 50%;
    transform: translate(-50%, -50%);

    .content {
      color: #f56c6c;
    }
  }
}
</style>
