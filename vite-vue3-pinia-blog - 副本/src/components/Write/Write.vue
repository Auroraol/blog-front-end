<template>
      <MdEditor v-model="text" previewTheme="vuepress" codeTheme="a11y"/>
</template>
  
<script setup lang='ts'>
  import { ref } from 'vue';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';

const text = ref('Hello Editor!');
</script>
  
<style>
  
</style>