<template>
  <el-row class="content">
    <el-col :span="16" :xs="0" class="content-left"> </el-col>
    <el-col :span="8" :xs="24" class="content-right">
       <Suspense>
        <template #default>
          <!--路由视图-->
            <router-view/>
        </template>

        <!-- 加载完成前的载入动画 -->
        <template #fallback>
            <div class="window">
                <loading class="winLoad"></loading>
            </div>
        </template>
    </Suspense>

    
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { ref } from "vue";

</script>

<style lang="less" scoped>

.content {
  height: 644px;
  background-image: url("/image/imgs/1.png");
  margin-bottom: -13px;
}
//让加载动画居中显示
.window {
    width: 100vw;
    height: 100vh;
    overflow: hidden;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>