<template>
    <div class="userbox-container">
        <!-- 头像信息 -->
        <div class="userInfo">
            <!-- 头像 -->
            <div class="headimg" @click="goToPersonalCenter">
                <img :src="userInfo.headImg" alt="head">
            </div>
            <!-- 信息 -->
            <div class="info">
                <!-- 昵称 -->
                <div class="nickname">{{ userInfo.nickName }}</div>
                <!-- 签名 -->
                <div class="introduce">{{ userInfo.introduce }}</div>
            </div>
        </div>
        <!-- 操作 -->
        <div class="userOption">
        </div>
    </div>
</template>

<script setup lang="ts">
import useAxios from '@/hooks/axios/axios';
import { useStore } from "@/store/count";
import { useRouter } from "vue-router";

const router = useRouter()
const pinia = useStore()
type Props = {
    account: string
}
const props = defineProps<Props>()
//请求相应的user信息
const { data: res } = await useAxios.get('userinfo', {
    params: {
        account: props.account
    }
})
const userInfo = res.data

const goToPersonalCenter = () => {
    const token = localStorage.getItem('userAccount')
    if (token) {
        const tokenInfo = JSON.parse(window.atob(token))
        if (tokenInfo.account === userInfo.account) {   //如果点击的是自己的留言的头像，就跳转到自己的个人中心
            router.push('/personalcenter')
        } else {    //不是就正常跳转到别人的个人中心
            router.push({
                path: '/otherspersonalcenter',
                query: {
                    account: userInfo.account
                }
            })
        }
    } else {    //未登录，直接跳转到别人的个人中心
        router.push({
            path: '/otherspersonalcenter',
            query: {
                account: userInfo.account
            }
        })
    }
}

</script>

<style scoped lang="less">
.userbox-container {
    height: 6.5rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 0 2rem;
    border-bottom: .1rem solid var(--gray-light-border);

    .userInfo {
        flex: 1;
        height: 100%;
        display: flex;
        align-items: center;

        .headimg {
            width: 5rem;
            height: 5rem;
            background-color: rgb(224, 224, 224);
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
            overflow: hidden;
            margin-right: 1rem;
            transition: all .5s;

            &:hover {
                cursor: pointer;
                transform: rotateZ(360deg);
            }

            img {
                width: 100%;
                height: 100%;
            }
        }

        .info {
            height: 100%;
            width: 45rem;
            display: flex;
            flex-direction: column;
            justify-content: center;

            .nickname {
                padding: .5rem 0;
                font-size: 1.4rem;
            }

            .introduce {
                width: 100%;
                height: 1rem;
                padding: .5rem 0;
                color: rgb(73, 73, 73);
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
        }
    }

    .userOption {
        width: 10rem;
        height: 100%;
    }
}
</style>