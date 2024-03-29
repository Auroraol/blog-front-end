<template>
  <div id="loading" v-show="visible">
    <div class="circle"></div>
    <br />
    疯狂加载中...
  </div>
</template>

<script>
import { defineComponent, ref } from "vue";
export default defineComponent({
  name: "Loading",
  setup() {
    // 控制组件的状态
    let visible = ref(false);

    // 显示组件
    let showLoading = () => {
      visible.value = true;
    };
    // 隐藏组件
    let hideLoading = () => {
      visible.value = false;
    };

    return { visible, showLoading, hideLoading };
  },
});
</script>
<style scoped>
#loading {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 999;
  display: flex;
  align-items: center;
  justify-content: center;
}
@keyframes loading {
  0% {
    transform: rotate(0);
  }

  50% {
    transform: rotate(160deg);
  }

  100% {
    transform: rotate(360deg);
  }
}
.circle {
  width: 45px;
  height: 45px;
  background-color: transparent;
  border-radius: 50%;
  border: 5px solid #528bee;
  border-left-color: #ccc;

  animation: loading 1s linear 1s infinite;
}
</style>
