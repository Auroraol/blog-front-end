<template>
  <div class="app">
    <navigation></navigation>
    <el-config-provider :locale="zhCn">
      <!-- 路由视图 -->
      <div id="v-content" v-bind:style="{ minHeight: Height + 'px' }">
        <router-view></router-view>
      </div>
    </el-config-provider>
    <foot></foot>
  </div>
</template>


<script setup lang="ts">
// 面包屑 不需要引入因为已经在 vite.config.ts 中引入了Components
// import breadcrumb from "/@/components/breadcrumb.vue";
import { defineComponent } from "vue";
import { ElConfigProvider } from "element-plus";
// import zhCn from "element-plus/lib/locale/lang/zh-cn";
import zhCn from "element-plus/es/locale/lang/zh-cn";
// import { useStore } from '/@/store';
// import { useStore } from '/@/store'
const Height = ref(0);

onMounted(() => {
  // 动态设置内容高度，让 footer 始终居底，header+footer 的高度是 100
  // Height.value = document.documentElement.clientHeight - 100;
  Height.value = document.documentElement.clientHeight - 40;

  // 监听浏览器窗口变化
  window.onresize = () => {
    Height.value = document.documentElement.clientHeight - 40;
  };
});

// const pinia = useStore()

// // 初始化
// onMounted(() => {
//   const bodyWidth = ref(document.querySelector('body')?.clientWidth)
//   pinia.setBodyWidth(bodyWidth.value!)

//   window.onresize = () => {
//     return (() => {
//       bodyWidth.value = document.querySelector('body')?.clientWidth
//       pinia.setBodyWidth(bodyWidth.value!)
//     })()
//   }
// })

// //判断登录凭证
// const tokenPwd = localStorage.getItem('userAccount')
// if (tokenPwd) {
//   const token = JSON.parse(window.atob(tokenPwd))
//   const nowTime = Math.floor(new Date().getTime() / 1000 / 60 / 60)  //现在的时间戳（对应token中存的格式，要换成小时）
//   const tokenTime = token.time
//   if (!token.time || nowTime - tokenTime > 30) {  //时间大于30小时，就算过期了
//     alert("登录凭证过期，请重新登录")
//     localStorage.removeItem('userAccount')
//     pinia.userInfo = ''
//   }
// }
</script>

<style lang="less">
//滚动条样式
::-webkit-scrollbar {
  width: 0.5rem;
  height: 0.5rem;
  background: rgba(255, 255, 255, 0.6);
}

::-webkit-scrollbar-track {
  border-radius: 0;
}

::-webkit-scrollbar-thumb {
  border-radius: 0;
  background-color: rgb(218, 218, 218);
  transition: all 0.2s;
  border-radius: 0.5rem;

  &:hover {
    background-color: rgb(172, 172, 172);
  }
}
</style>