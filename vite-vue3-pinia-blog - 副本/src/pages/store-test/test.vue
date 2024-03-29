// <script setup>
//   // 首先需要引入一下我们刚刚创建的store
//   import { useInfoStore } from '/@/store';
//   // 导入storeToRefs
//   import { storeToRefs } from 'pinia'
//   // 因为是个方法，所以我们得调用一下
//   const userInfo = useInfoStore()
//   // 包裹一下结构对象
//   const { name, age } = storeToRefs(userInfo)  //解构后的数据也变成响应式的

//   let change = () => {
//     userInfo.age++
//     /*或者
//       age.value++
//     */
//   }
// </script>
// <template>
//   <h3>pinia</h3>
//   <!-- 在页面中直接使用就可以了 -->
//   <p>解构前</p>
//   <p>pinia 中的信息： {{userInfo.name}} --- {{userInfo.age}}</p>
//   <p>解构后</p>
//   <p>pinia 中的信息： {{name}} --- {{age}}</p>

//   <el-button @click="change">Age +1</el-button>
// </template>
// <style scoped>

</style>