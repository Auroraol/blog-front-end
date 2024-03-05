<template>
    <div class="userHead">
        <div class="headBox">
            <img :src="userInfo.headImg" alt="none" class="defaultHead">
        </div>

        <div class="infoBox" v-show="ifBox">
            <div class="boxImg" :key="userInfo.id">
                <p>{{ userInfo.nickName }}</p>
            </div>
            <div class="operation" @click="changeNikeName">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-bianji"></use>
                </svg>
                <span>更换昵称</span>
                <Teleport to="body">
                    <el-dialog v-model="TableVisibleNickname" title="更换昵称">
                        <el-input v-model="newNickName" :maxlength="pinia.nickNameLength" placeholder="输入新的昵称!"
                            show-word-limit type="text" />
                        <template #footer>
                            <span class="dialog-footer">
                                <el-button @click="TableVisibleNickname = false">取消</el-button>
                                <el-button type="primary" @click="sendNewNickName">提交</el-button>
                            </span>
                        </template>
                    </el-dialog>
                </Teleport>
            </div>
            <div class="operation" @click="goPersonalCenter">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-bianji"></use>
                </svg>
                <span>个人中心</span>
            </div>
            <div class="operation exitLog" @click="changeOutlog">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-tuichu"></use>
                </svg>
                <span>退出登录</span>
                <Teleport to="body">
                    <el-dialog v-model="TableVisibleOutlog" title="退出">
                        <p>确定要退出登录吗</p>
                        <template #footer>
                            <span class="dialog-footer">
                                <el-button @click="TableVisibleOutlog = false">取消</el-button>
                                <el-button type="primary" @click="exitAccount">确定</el-button>
                            </span>
                        </template>
                    </el-dialog>
                </Teleport>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useStore } from '/@/store';
// import useAxios from "/@/hooks/axios/axios";
import { useRouter } from "vue-router";

const router = useRouter()
const pinia = useStore()

const TableVisibleNickname = ref(false)
const TableVisibleOutlog = ref(false)
const newNickName = ref('')

//获取session里面的account
const sessionInfo = JSON.parse(window.atob(localStorage.getItem('userAccount')!))

type User = {
    id: number
    account: string
    password: string
    nickName: string
    headImg: string
    ifroot: boolean
}
let userInfo = ref<User>({
    id: -1,
    account: 'account',
    password: 'password',
    nickName: 'nickName',
    headImg: '',
    ifroot: false
})  //给一个初始值作为key，当请求到数据时更新这个变量，key也就随之更新，就会更新dom

onMounted(async () => {
    //登录了，请求用户的信息
    // const { data: res } = await useAxios.get('/userinfo', {
    //     params: {
    //         account: sessionInfo.account
    //     }
    // })
    // userInfo.value = res.data
})

const ifBox = ref(false)
onMounted(() => {
    const boxHead = document.querySelector('.userHead') as HTMLElement
    boxHead.addEventListener('mouseenter', () => {
        boxHead.style.transform = 'translateX(-20%) translateY(50%) scale(1.6)'
        ifBox.value = true
    })
    boxHead.addEventListener('mouseleave', () => {
        boxHead.style.transform = ''
        ifBox.value = false
    })
})

//注销登录
const exitAccount = () => {
    localStorage.removeItem('userAccount')
    pinia.sessionInfo = ''
    alert('注销成功')
    router.go(0)
}



//去个人中心页面
const goPersonalCenter = () => {
    router.push('/personalcenter')
}


//更换昵称
const changeNikeName = () => {
    TableVisibleNickname.value = true
}
//退出登录
const changeOutlog = () => {
    TableVisibleOutlog.value = true
}

const sendNewNickName = async () => {
    if (newNickName.value === '') {
        alert('昵称不能为空')
    } else {
        // const { data: res } = await useAxios.get('/updatanicknane', {
        //     params: {
        //         account: sessionInfo.account,
        //         nickName: newNickName.value,
        //     }
        // })
        // if (res.status === 0) {
        //     alert('修改成功！')
        //     router.go(0)
        // } else {
        //     alert('修改失败')
        // }
    }
}
</script>

<style scoped lang="less">
.userHead {
    margin-top: .2rem;
    width: 4rem;
    height: 4rem;
    background-color: rgb(214, 214, 214);
    border-radius: 50%;
    transition: all .3s;
    position: relative;
    transform-style: preserve-3d;
    border: .2rem solid #fff;
    z-index: 2;

    .headBox {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        overflow: hidden;
        cursor: pointer;

        .defaultHead {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

    }


    .infoBox {
        position: absolute;
        top: 50%;
        right: -10%;
        border: .1rem solid var(--gray-sahdow);
        box-shadow: .1rem .1rem .2rem var(--gray-sahdow);
        width: 10rem;
        background-color: rgb(255, 255, 255);
        transform: translateZ(-1rem);
        border-radius: .5rem;
        overflow: hidden;
        padding-bottom: 1.5rem;

        .exitLog {
            color: rgb(255, 77, 77);
        }

        .operation {
            width: 80%;
            margin: 0 auto;
            margin-top: .5rem;
            height: 2rem;
            border-bottom: .2rem dashed var(--border-line);
            display: flex;
            align-items: flex-end;
            cursor: pointer;
            box-sizing: border-box;
            padding: 0 .5rem;
            font-size: 1.1rem;

            &:hover {
                color: var(--special-font-color);
            }

            span {
                margin-left: .5rem;
                font-size: 1rem;
            }
        }

        .boxImg {
            height: 5.5rem;
            border-bottom: .2rem solid var(--border-line);
            display: flex;
            flex-direction: column;
            justify-content: center;
            box-sizing: border-box;
            padding: 1rem;

            p {
                margin-top: 1.5rem;
            }
        }
    }
}
</style>