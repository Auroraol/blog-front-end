<template>
  <div class="app">
    <navigation></navigation>
    <el-config-provider :locale="zhCn">
      <!-- 路由视图 -->
      <div id="v-content" v-bind:style="{ minHeight: Height + 'px' }">
        <transition name="fade" mode="in-out">
          <router-view></router-view>
        </transition>
      </div>
    </el-config-provider>
    <foot></foot>
  </div>
</template>


<script setup lang="ts">
import { defineComponent } from "vue";
import { ElConfigProvider } from "element-plus";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import { useAppStore } from "/@/store";

const Height = ref(0);
const useAppStorePinia = useAppStore();

onMounted(() => {
  // 动态设置内容高度，让 footer 始终居底，header+footer 的高度是 100
  // Height.value = document.documentElement.clientHeight - 100;
  Height.value = document.documentElement.clientHeight - 42;

  // 监听浏览器窗口变化
  window.onresize = () => {
    Height.value = document.documentElement.clientHeight - 42;
  };

  // 多端适配, 检测是手机端还是pc
  window.addEventListener("resize", resizeHandler);
  const mobile = isMobile();
  useAppStorePinia.toggleDevice(mobile ? "mobile" : "desktop");
});

const isMobile = () => {
  const { body } = document;
  const WIDTH = 992;
  const rect = body.getBoundingClientRect();
  return rect.width - 1 < WIDTH;
};

const resizeHandler = () => {
  if (!document.hidden) {
    const mobile = isMobile();
    useAppStorePinia.toggleDevice(mobile ? "mobile" : "desktop");
  }
};

onBeforeUnmount(() => {
  window.removeEventListener("resize", resizeHandler);
});
</script>

<style lang="less">
//进度条
#nprogress .bar {
  background: @color !important; //自定义颜色
}

// 淡出淡入
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

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