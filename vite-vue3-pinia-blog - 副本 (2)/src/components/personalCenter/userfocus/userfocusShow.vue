<template>
    <div class="userfocusshow-container">
        <div class="tagTitle">
            {{ showDate.title }}
        </div>
        <div class="tagShow">
            <Suspense v-for="item in showDate.data" :key="item">
                <template #default>
                    <UserBox :account="item"></UserBox>
                </template>
                <template #fallback>
                    <Loading></Loading>
                </template>
            </Suspense>
        </div>
    </div>
</template>

<script setup lang="ts">
import { watchEffect, defineAsyncComponent, reactive } from "vue";
import { useRoute } from "vue-router";
import useAxios from "@/hooks/axios/axios";
import Loading from "@/components/loading/loading2.vue";
const UserBox = defineAsyncComponent(() => import('@/components//personalCenter/userfocus/userBox.vue'))

const route = useRoute()

const showDate = reactive({
    title: "全部关注",
    data: {}
})

const switchTag = async () => {
    const routeQuery = route.query  //传过来的路由参数
    switch (routeQuery.tag) {
        case '0':
            showDate.title = "全部关注"
            //请求对应用户的全部关注列表
            //请求对应用户的信息
            const { data: focus } = await useAxios.get('/userinfo', {
                params: {
                    account: routeQuery.account
                }
            })
            showDate.data = focus.data.focus
            break;
        case '1':
            showDate.title = "全部粉丝"
            //请求对应用户的全部粉丝列表
            //请求对应用户的信息
            const { data: fans } = await useAxios.get('/userinfo', {
                params: {
                    account: routeQuery.account
                }
            })
            showDate.data = fans.data.fans
            break;
        default:
            break;
    }
}

watchEffect(() => {
    switchTag()
})



</script>

<style scoped lang="less">
.userfocusshow-container {
    width: 100%;
    box-sizing: border-box;
    padding: .5rem;

    .tagTitle {
        box-sizing: border-box;
        padding: 1.7rem 0;
        border-bottom: .1rem solid var(--gray-light-border);
        font-size: 1.8rem;
    }
}
</style>