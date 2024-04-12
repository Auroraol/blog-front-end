<template>
    <ul v-if="ifHaveComments" v-infinite-scroll="loadComments" class="commentslist">
        <!-- 异步加载评论区 -->
        <Suspense v-for="item in comments.comment" :key="item.id">
            <template #default>
                <CommentBox :item="item" :articleId="commentsInject.id">
                </CommentBox>
            </template>
            <template #fallback>
                <Loading2></Loading2>
            </template>
        </Suspense>

        <div class="noneMore" v-if="ifHaveMoreComments">
            <span>没有更多啦</span>
        </div>
    </ul>
    <div v-else class="noComment">
        <span>还没有评论，来占个沙发吧~</span>
    </div>
</template>

<script setup lang="ts">
import { ref, inject, watchEffect, reactive, defineAsyncComponent } from "vue";
import { useStore } from "@/store/count";
import useAxios from "@/hooks/axios/axios";
import Loading2 from "@/components/loading/loading2.vue";
const CommentBox = defineAsyncComponent(() => import('@/components/articleBrowser/comments/commentBox.vue'))

const pinia = useStore()

type Comments = {
    id: number,
    account: string,
    time: string,
    content: string
}
type Inject = {
    id: string,
    comments: Array<Comments>
}

const ifHaveComments = ref(true)   //是否有评论
const ifHaveMoreComments = ref(false)   //是否还有未加载的评论
const commentsInject: Inject = inject('comments')!  //拿到祖组件依赖注入传过来的数据(文章id和评论列表)
let commentsList = commentsInject.comments    //把传过来的完整的评论列表拿出来
if (!commentsList) {   //如果没有评论
    ifHaveComments.value = false
}


const comments = reactive({   //一个响应式的对象性，包含一个要展示的评论列表和当前页码数
    comment: new Array,
    nowShowPage: 1
})

const loadComments = () => {  //当滚动到底部时展示多三个数据
    if (comments.comment.length === commentsList.length) {
        ifHaveMoreComments.value = true
        return
    }
    comments.nowShowPage += 1   //页码 + 1
    comments.comment = commentsList.slice(0, comments.nowShowPage * 3)  //取0到 页码数 * 3 的位置的评论
}




watchEffect(() => {
    if (pinia.articleCommentsAdd) {  //如果评论更新了就重新请求评论列表
        ifHaveComments.value = true
        useAxios.get('/getidarticle', {
            params: {
                id: commentsInject.id
            }
        }).then(res => {
            commentsList = res.data.data.comments
            comments.comment = res.data.data.comments
        })
    }
})
</script>

<style scoped lang="less">
.commentslist {
    .noneMore {
        display: flex;
        justify-content: center;
        font-size: 1.2rem;
        color: var(--font-gray-color);
    }
}

.noComment {
    display: flex;
    justify-content: center;
    margin: 1rem 0;
    font-size: 1.4rem;
    color: rgb(168, 168, 168);
}
</style>