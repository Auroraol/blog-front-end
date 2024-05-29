<template>
  <div class="toolBar">
    <!-- 文章目录 -->
    <svg-icon
      width="30px"
      height="30px"
      name="directory"
      alt=""
      class="item"
      @click="isDirectory"
    />
    <!-- 黑夜 -->
    <svg-icon
      name="sun"
      width="30px"
      height="30px"
      class="item"
      style="color: #f5a630"
      @click="isTheme = !isTheme"
      v-if="isTheme"
    />

    <!-- 白天 -->
    <svg-icon
      name="xingxingtaoyueliang"
      width="30px"
      height="30px"
      class="item"
      style="color: #62acf9"
      @click="isTheme = !isTheme"
      v-else
    />

    <!-- 个性化 -->
    <svg-icon
      name="custom"
      width="30px"
      height="30px"
      alt=""
      class="item"
      @click="topicDialog = true"
    />
  </div>

  <!-- 弹出框：自定义颜色 -->
  <el-dialog v-model="topicDialog" title="Tips" width="30%">
    <!-- 自定义头部标题 -->
    <template #header>
      <div
        style="
          display: flex;
          align-items: center;
          font-weight: bold;
          font-size: 16px;
        "
      >
        <svg-icon
          name="custom"
          width="30px"
          height="30px"
          style="width: 30px; margin-right: 10px"
        />
        <span>不止一种色彩！</span>
      </div>
    </template>

    <!-- 颜色列表 -->
    <div class="list">
      <div
        class="item"
        style="background-color: #539dfd"
        @click="color = '#539DFD'"
      ></div>
      <div
        class="item"
        style="background-color: #42b983"
        @click="color = '#42b983'"
      ></div>
      <div
        class="item"
        style="background-color: #727cf5"
        @click="color = '#727cf5'"
      ></div>
      <div
        class="item"
        style="background-color: #fa6946"
        @click="color = '#fa6946'"
      ></div>
      <div
        class="item"
        style="background-color: #fab64f"
        @click="color = '#fab64f'"
      ></div>
      <div
        class="item"
        style="background-color: #44b6a0"
        @click="color = '#44b6a0'"
      ></div>
      <div
        class="item"
        style="background-color: #30362f"
        @click="color = '#30362f'"
      ></div>
      <div
        class="item"
        style="background-color: #dc382f"
        @click="color = '#dc382f'"
      ></div>
      <div
        class="item"
        style="background-color: #a18acd"
        @click="color = '#a18acd'"
      ></div>
    </div>

    <!-- 自定义颜色 -->
    <div style="margin: 30px 10px -10px">
      <span>自定义主题色：</span>
      <el-color-picker v-model="color" />
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="topicDialog = false">取消更改</el-button>

        <el-button type="primary" @click="updateColor">更改色彩</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { h } from "vue";
// 是否显示目录
const isDirectory = () => {
  const essay = document.querySelector(".main .left") as HTMLElement;
  const directory = document.querySelector(".main .directory") as HTMLElement;

  let essayWidth = essay.style.width;

  if (essayWidth === "100%") {
    essay.style.width = "73%";
    directory.style.display = "block";
  } else {
    essay.style.width = "100%";
    directory.style.display = "none";
  }
};

// 简化写法
const $css = document.documentElement.style;

// 白天黑夜切换
const isTheme = ref<boolean>(false);

// import { useConfigStore } from "/@/stores";
// const store = useConfigStore();

watch(isTheme, (is) => {
  const root: HTMLHtmlElement = document.querySelector("html")!;

  // 主题切换
  if (is) {
    // 暗黑主题
    root.className = "dark";

    $css.setProperty("--borderColor", "#2b333e");
    $css.setProperty("--shadeColor", "0px 4px 15px 0px rgba(255,255,255,0.05)");
    $css.setProperty("--underBorderColor", "#464646");
    $css.setProperty("--textColor", "#fff");
  } else {
    // 光亮主题
    root.className = "light";

    $css.setProperty("--borderColor", "#eee");
    $css.setProperty("--shadeColor", "0 2px 8px rgba(186, 186, 186, 0.15)");
    $css.setProperty("--underBorderColor", "#eee");
    $css.setProperty("--textColor", "#333");
  }

  //   store.updateIsTheme(is);
});

// 颜色弹出框
const topicDialog = ref(false);

// 自定义主题颜色
const localColor = localStorage.getItem("topicColor");
const color = ref(localColor || "#539dfd");

$css.setProperty("--color", color.value);

// 更新主题色
const updateColor = () => {
  topicDialog.value = false;

  // 更改css变量--color的值
  $css.setProperty("--color", color.value);

  // 修改进度条颜色
  $css.setProperty("--nprogress-color", color.value);

  // 将主题色存储在本地
  localStorage.setItem("topicColor", color.value);

  ElNotification({
    title: "通知",
    message: "更改主题色成功",
    type: "success",
  });
};
</script>

<style scoped lang="less">
.toolBar {
  display: flex;
  flex-direction: column;
  position: fixed;
  right: 3px;
  bottom: 150px;
  width: 35px;
  border: 1px solid #eee;
  background-color: #fff;
  user-select: none;
  z-index: 999;
  //   @include container;

  .item {
    padding: 3px;
    font-size: 30px;
    border-radius: 5px;
    border-bottom: 1px solid #eee;
    transition: all 0.3s;
    cursor: pointer;

    &:hover {
      background-color: #f7f4f4;
    }

    &:last-child {
      border-bottom: none;
    }
  }
}

.list {
  display: flex;
  justify-content: center;

  .item {
    width: 30px;
    height: 30px;
    margin-right: 20px;
    // border-radius: $round;
    cursor: pointer;
  }
}

// 自定义element-plus 弹框样式
.dialog-footer button:first-child {
  margin-right: 10px;
}

.dialog-footer button {
  padding: 10px;
}

// 自定义颜色的宽度
:deep(.el-color-picker__trigger) {
  width: 330px;
}
</style>
