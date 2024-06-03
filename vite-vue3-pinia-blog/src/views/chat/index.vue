<template>
  <Suspense>
    <template #default>
      <div class="chatbox">
        <!-- <List /> -->
        <Chat></Chat>
      </div>
    </template>

    <!-- 加载完成前的载入动画 -->
    <template #fallback>
      <div class="window">
        <loading class="winLoad"></loading>
      </div>
    </template>
  </Suspense>
</template>
./components/Chat6.vue
<script setup lang="ts">
//Suspense要异步引入组件
const List = defineAsyncComponent(() => import("./components/List.vue"));
const Chat = defineAsyncComponent(() => import("./components/Chat.vue"));
</script>

<style scoped lang="less">
//让加载动画居中显示
.window {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chatbox {
  margin-top: 5px;
  // overflow: hidden;
  // display: flex;
  // justify-content: space-between;
  // width: 60%;
  // height: 700px;
  // margin: 100px auto;
  // border-radius: 40px;
  // border: 1px solid #eee;
  // background-color: #fff;
  // // transition: all $move;

  // &:hover {
  //   box-shadow: 0 10px 20px 1px rgb(83, 157, 253, 0.1);
  // }
}
</style>