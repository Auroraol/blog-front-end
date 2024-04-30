<template>
  <el-backtop style="width: 60px; height: 60px" :bottom="10" :visibility-height="1000" />
</template>

/**  可以不使用这个插件, 直接使用下面方法, 就能实现回到顶部功能了
// 回到顶部
const goTopBtn = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};
 */