<template>
  <div class="app-container">
  <navigation></navigation>
  <el-config-provider :locale="zhCn">
    <!-- 路由视图 -->
    <router-view></router-view>
  </el-config-provider>
  <foot class="foot"></foot>
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


.app-container {
 position: relative;
  min-height: 100vh;
}


.foot {
  /* Footer 组件样式 */
  position: absolute;
  bottom: 0;
  width: 100%;
}

// @font-face {
//   font-family: "思源黑体 Normal";
//   font-weight: 350;
//   src: url("@/font-family/YkF40zSSGGwEVPSKgCFB7.woff2") format("woff2"),
//     url("@/font-family/YkF40zSSGGwEVPSKgCFB7.woff") format("woff");
//   font-display: auto;
// }

// .default-theme p .figure img {
//   p .figure {
//     padding: 0;
//     border: none;
//   }
//   display: inline-block;
//   width: 20rem;
//   margin: 0;
//   border: none;
// }

// .chatMask {
//   width: 100%;
//   height: 100%;
//   background-color: rgba(61, 61, 61, 0.6);
//   position: fixed;
//   top: 0;
//   left: 0;
//   z-index: 4;
// }

// //标签tag的通用样式
// .labelTag {
//   display: inline-block;
//   font-size: 1.2rem;
//   background-color: var(--light-gray-blue);
//   color: #000;
//   margin: .2rem;
//   padding: .3rem 1rem;
//   border-radius: .7rem;
//   cursor: pointer;
// }

// //弹出的消息框通用样式
// .el-dialog {
//   width: 50rem;
// }

// @media only screen and (max-width: 800px) {

//   body,
//   html,
//   #app {
//     font-size: 10px;
//     overflow-x: hidden;
//   }

//   .el-dialog {
//     width: 80%;
//   }

// }



//滚动条样式
::-webkit-scrollbar {
  width: .5rem;
  height: .5rem;
  background: rgba(255, 255, 255, 0.6);
}

::-webkit-scrollbar-track {
  border-radius: 0;
}

::-webkit-scrollbar-thumb {
  border-radius: 0;
  background-color: rgb(218, 218, 218);
  transition: all .2s;
  border-radius: .5rem;

  &:hover {
    background-color: rgb(172, 172, 172);
  }
}
</style>