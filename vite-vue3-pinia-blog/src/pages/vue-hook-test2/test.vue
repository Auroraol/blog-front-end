<template>
  <div>
    {{data.hitokoto}}
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRequest } from 'vue-hooks-plus';
import { getUserInfo} from './services';

const { data, error, loading, run } =  useRequest(getUserInfo,{
   manual: true, // 手动触发请求
    devKey: "demo1", // 开发者密钥
    onSuccess: data => {
      //注意: 在手动触发的情况下, ts中使用data,error,....中的属性
      alert(data.hitokoto); // 有自动补全提示
    },
    onError: error => {
      alert(error);
    },
});

run('b')
</script>
