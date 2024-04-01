<template>
    <Suspense>
        <template #default>
            <index></index>
        </template>
        <!-- 加载完成前的载入动画 -->
        <template #fallback>
            <div class="window">
                <loading class="winLoad"></loading>
            </div>
        </template>
    </Suspense>
</template>

<script setup lang="ts">
import { defineAsyncComponent } from "vue";
//Suspense要异步引入组件
const Index = defineAsyncComponent(() => import('/@/components/Index/Index.vue'))
</script>

<style scoped lang="less">
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