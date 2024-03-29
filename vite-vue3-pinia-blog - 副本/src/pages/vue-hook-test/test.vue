<template>
  <div>
    <h1>Post Titles</h1>
    <ul>
      <li v-for="post in data">
        {{ post.title }}
        {{ post.body }}
        {{ post.id }}
        {{ post.userId }}
        <hr />
      </li>
      {{
        response
      }}
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import { useRequest } from "vue-hooks-plus";
import { getUserInfo } from "./services";

const { data, error, loading } = useRequest(() => getUserInfo("2"));

//注意: 在自动触发的情况下, ts中不能使用data,error,...., 能直接在template使用
console.log(data.value?.at(0)?.title); //undefined
console.log(data.value?.at(0)?.body); //undefined
console.log(data.value?.at(0)?.id); //undefined
console.log(data.value?.at(0)?.userId); //undefined
</script>
