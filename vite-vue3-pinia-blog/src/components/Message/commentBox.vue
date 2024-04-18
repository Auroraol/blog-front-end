<template>
    <div class="commentBox-container">
        <!-- 头像 -->
        <div class="headimg" @click="goToPersonalCenter">
            <img :src="userInfo.headImg" alt="head">
        </div>
        <!-- 内容 -->
        <div class="contentBox">
            <!-- 昵称 -->
            <div class="nickname" @click="goToPersonalCenter">
                <div class="block">
                    <span>{{ userInfo.nickName }}</span>
                    <div class="brand" v-if="root">咕咕</div>
                </div>
            </div>
            <!-- 内容 -->
            <div class="commentCox">{{ commentInfo.content }}</div>
            <!-- 时间 -->
            <div class="commentTime">
                <p>{{ commentInfo.time }}</p>
                <p>第{{ commentInfo.id + 1 }}楼</p>
                <span class="replyComment" @click="replyComment">回复</span>
                <span class="delBtn" @click="delComment">删除</span>
                <Teleport to="body">
                    <el-dialog v-model="dialogVisible" title="Tips" width="30%">
                        <span>确定要删除吗</span>
                        <template #footer>
                            <span class="dialog-footer">
                                <el-button @click="dialogVisible = false">取消</el-button>
                                <el-button type="primary" @click="sendDelComment"> 确定</el-button>
                            </span>
                        </template>
                    </el-dialog>
                </Teleport>
            </div>
            <!-- 回复评论的盒子 -->
            <ReplyCommentBox v-if="ifReplyComment" @cancelReplyComment="cancelReplyComment" :commentId="commentInfo.id"
                :replyAccount="commentInfo.account">
            </ReplyCommentBox>
            <!-- 回复的评论列表 -->
            <div v-if="ifHaveReply" v-for="item in commentInfo.reply" :key="item.id">
                <CommentMiniBox :reply="item" :commentId="commentInfo.id"></CommentMiniBox>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import useAxios from '@/hooks/axios/axios';
import { useStore } from "@/store/count";
import { ElMessage } from 'element-plus'
import router from "@/router/index";
import ReplyCommentBox from "@/components/articleBrowser/comments/replyCommentBox.vue";
import CommentMiniBox from "@/components/articleBrowser/comments/commentMiniBox.vue";

const pinia = useStore()
const root = ref(false)  //是否是管理员
const dialogVisible = ref(false)  //是否弹出确认删除的弹窗
type Reply = {
    id: number,
    replyTo: string,
    account: string,
    time: string,
    content: string,
}
type Comments = {
    id: number,
    account: string,
    time: string,
    content: string,
    reply: Array<Reply>
}
type Props = {
    item: Comments,
    articleId: string
}
const props = defineProps<Props>()
const commentInfo = props.item  //传过来的props(评论信息)
const ifHaveReply = ref(false)
if (commentInfo.reply) {
    ifHaveReply.value = true
}

//请求对应用户的头像和昵称信息
const { data: res } = await useAxios.get('/userinfo', {
    params: {
        account: commentInfo.account
    }
})
const userInfo = res.data   //用户信息
if (userInfo.root === true) {
    root.value = true
}

const delComment = () => {
    const token = localStorage.getItem('userAccount')
    if (token) {
        const tokenInfo = JSON.parse(window.atob(token))
        if (tokenInfo.account === userInfo.account) {
            dialogVisible.value = true
        } else {
            if (tokenInfo.root === true) {
                dialogVisible.value = true
                return
            }
            ElMessage.error('不能删除他人的回复')
        }
    } else {
        ElMessage.error('未登录')
    }
}

const sendDelComment = async () => {
    dialogVisible.value = false
    const { data: res } = await useAxios.get('/articledelcomment', {
        params: {
            articleId: props.articleId,
            commentId: commentInfo.id
        }
    })
    if (res.status === 0) {
        ElMessage({
            message: '删除成功',
            type: 'success',
        })
        router.go(0)
    } else {
        ElMessage.error('操作失败')
    }
}

const token = localStorage.getItem('userAccount')  //获取登录的人的信息
const goToPersonalCenter = () => {
    if (token) {
        const tokenInfo = JSON.parse(window.atob(token))
        if (tokenInfo.account === props.item.account) {   //如果点击的是自己的留言的头像，就跳转到自己的个人中心
            router.push('/personalcenter')
        } else {    //不是就正常跳转到别人的个人中心
            router.push({
                path: '/otherspersonalcenter',
                query: {
                    account: props.item.account
                }
            })
        }
    } else {    //未登录，直接跳转到别人的个人中心
        router.push({
            path: '/otherspersonalcenter',
            query: {
                account: props.item.account
            }
        })
    }
}


const ifReplyComment = ref(false)
const replyComment = () => {
    if (token) {
        ifReplyComment.value = !ifReplyComment.value
    } else {
        ElMessage({
            message: '登录后才能回复',
            type: 'warning',
        })
    }
}

const cancelReplyComment = (val: boolean) => {  //取消回复评论的组件（子传父false）
    ifReplyComment.value = val
}

</script>

<style scoped lang="less">
.commentBox-container {
    padding: 1rem;
    display: flex;

    .headimg {
        width: 5rem;

        img {
            width: 3.5rem;
            height: 3.5rem;
            background-color: rgb(204, 204, 204);
            border-radius: 50%;
            overflow: hidden;
            object-fit: cover;
            transition: all .5s;

            &:hover {
                transform: rotateZ(360deg);
                cursor: pointer;
            }
        }
    }

    .contentBox {
        flex: 1;
        min-width: 10rem;
        padding-bottom: 1rem;
        border-bottom: .1rem solid var(--gray-light-border);

        &:hover {
            .commentTime {
                .delBtn {
                    display: block;
                }
            }
        }

        .nickname {
            font-size: 1.2rem;
            font-weight: 700;
            color: var(--font-gray-color-dark);
            display: inline-block;

            .block {
                display: flex;

                .brand {
                    font-size: 1.1rem;
                    width: 3rem;
                    height: 1.5rem;
                    color: var(--white-font-color);
                    background-color: rgb(101, 183, 250);
                    border-radius: .5rem;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    margin-left: 1rem;
                    font-weight: 400;
                }

                &:hover {
                    cursor: pointer;
                }
            }
        }

        .commentCox {
            font-size: 1.1rem;
            font-weight: 500;
            margin: .5rem 0;
        }

        .commentTime {
            color: var(--font-gray-color);
            display: flex;
            justify-content: flex-start;
            margin-bottom: 1rem;

            span {
                margin-left: 1rem;
            }

            p {
                margin-right: 1rem;
            }

            .delBtn {
                display: none;
                color: rgb(255, 88, 88);
                cursor: pointer;

            }

            .replyComment {
                color: var(--special-font-color);
                cursor: pointer;
            }
        }
    }
}
</style>