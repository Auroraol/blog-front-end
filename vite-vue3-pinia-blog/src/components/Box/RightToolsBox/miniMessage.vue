<template>
    <div class="messageBox">
        <div class="headImg">
            <div class="mesageHead" @click="goToPersonalCenter">
                <img :src="info.headImg" alt="head">
            </div>
        </div>
        <div class="messageContent">
            {{ item.content }}
        </div>
    </div>
</template>

<script setup lang="ts">
// import useAxios from '@/hooks/axios/axios';
import { useStore } from '/@/store'
// import { Message } from "@/hooks/Types/types"
// import { goToPersonalCenterHook } from '@/hooks/goToPersonalCenter/goToPersonalCenter'
const pinia = useStore()


type Props = {
    item: Message
}
const props = defineProps<Props>()

//请求对应account的信息
const { data: res } = await useAxios.get('/userinfo', {
    params: {
        account: props.item.account
    }
})
const info = res.data

const goToPersonalCenter = () => {
    goToPersonalCenterHook(props.item.account)
}

</script>

<style scoped lang="less">
.messageBox {
    width: 100%;
    height: 4rem;
    display: flex;
    align-items: center;
    margin: .5rem 0;

    .headImg {
        display: flex;
        justify-content: center;
        align-items: center;

        .mesageHead {
            width: 3.5rem;
            height: 3.5rem;
            border-radius: 50%;
            margin-right: 1rem;
            overflow: hidden;
            transition: all .5s;

            &:hover {
                cursor: pointer;
                transform: rotateZ(360deg);
            }

            img {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }
        }
    }

    .messageContent {
        width: 100%;
        padding: .5rem 0;
        font-size: 1.5rem;
        font-weight: 500;
        background-color: var(--light-gray-blue);
        box-sizing: border-box;
        border-radius: .5rem;
        padding: .5rem;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }

}
</style>