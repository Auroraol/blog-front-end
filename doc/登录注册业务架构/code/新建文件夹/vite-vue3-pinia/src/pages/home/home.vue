<template>
  <!-- 跳转about -->


  <div class="time">{{ hCurrentTime }}</div>
  <div class="time">{{ hFormatDate }}</div>
  <div class="time">当前日期是第{{ hGetWeek }}周</div>
</template>
<script setup>
// 因为我下载插件了所以不用引入vue相关依赖
import { formatAxis, formatDate, getWeek } from "/@/utils/format/format-time.ts";
// 时间问候语
const hCurrentTime = computed(() => {
  return formatAxis(new Date());
});
// 时间格式化
const hFormatDate = computed(() => {
  return formatDate(new Date(), "YYYY-mm-dd HH:MM:SS WWW QQQQ ZZZ");
});
// 时间格式化
const hGetWeek = computed(() => {
  return getWeek(new Date());
});
</script>
<style lang="scss" scoped>
.time {
  font-size: 25px;
  color: red;
  font-weight: bold;
}
</style>
