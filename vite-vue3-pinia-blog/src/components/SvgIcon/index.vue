<!--
  自定义svg图片组件
  使用方法：在阿里云iconfont下载需要图标并命名文件名为英文名，将命名好的文件放到icons文件夹下svg文件夹下，
  如logo图标使用：
  <svg-icon name="logo"  color="red" width="100px" height="100px" />
  这里的name即为logo文件名, 可自定义color, width, height
-->
<!-- src\components\SvgIcon.vue -->
<script lang="ts" setup>
defineProps({
  //xlink:href属性值的前缀
  prefix: {
    type: String,
    default: '#icon-',
  },
  // 接收父组件传来的svg矢量图的名字
  name: String,
  // 接收父组件传来的图标颜色
  color: {
    type: String,
    default: ''
  },
  // 接收父组件传来的图标宽度
  width: {
    type: String,
    default: '16px',
  },
  // 接收父组件传来的图标高度
  height: {
    type: String,
    default: '16px',
  },
})
</script>
 
<template>
  <div>
    <svg :style="{ width, height }">
      <use :xlink:href="prefix + name" :fill="color"></use>
    </svg>
  </div>
</template>