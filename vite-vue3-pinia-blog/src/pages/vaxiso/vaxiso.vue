<template>
  <div>name：{{ loading ? 'loading' : data }}</div>
  {{ data }}
</template>

<script lang="ts" setup>
  import { useRequest } from 'vue-hooks-plus'

	// 参数 params: { desc: string }  表示params对象
  function getUsername(params: { desc: string }): Promise<string> {
    return new Promise(resolve => {
      setTimeout(() => {
        resolve(`vue-hooks-plus ${params.desc}`)
      }, 1000)
    })
  }

  let param: { desc: string } = { desc: 'good' };
  const { data, loading } = useRequest(() => getUsername(param),{
    devKey:'demo'  // 开发者密钥
  })
</script>