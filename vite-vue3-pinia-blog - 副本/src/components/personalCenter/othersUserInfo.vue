<template>
    <div class="userInfo">
        <div class="userhead">
            <div class="headImg">
                <img :src="userInfo.headImg" alt="head">
            </div>
            <div class="headMore">
                <div class="headbottom">
                    <span>账号:</span>
                    <span>{{ userInfo.account }}</span>
                </div>
                <div class="headbottom">
                    <span>创建日期:</span>
                    <span>{{ userInfo.createDate }}</span>
                </div>
            </div>
            <div class="focusList">
                <div class="focus" @click="focusList(userInfo.account, 'other')">
                    <p>{{ userInfo.focus ? userInfo.focus.length : 0 }}</p>
                    <span>关注的人</span>
                </div>
                <div class="focus" @click="fansList(userInfo.account, 'other')">
                    <p>{{ userInfo.fans ? userInfo.fans.length : 0 }}</p>
                    <span>粉丝</span>
                </div>
            </div>
            <div class="focusUser">
                <div class="focused" v-if="ifFocus" @click="focusDialogVisible = true">已关注</div>
                <div class="focusClick" v-else @click="addFocus">点击关注</div>
                <el-dialog v-model="focusDialogVisible" title="Tips" width="30%">
                    <span>确定要取消关注吗？</span>
                    <template #footer>
                        <span class="dialog-footer">
                            <el-button @click="focusDialogVisible = false">取消</el-button>
                            <el-button type="primary" @click="delFocus">
                                确定
                            </el-button>
                        </span>
                    </template>
                </el-dialog>
            </div>
        </div>
        <div class="moreInfo">
            <div class="title">
                <span>详细信息</span>
            </div>
            <div class="titleBottom">
                <div class="titleBottomDiv">
                    <span class="iptBeforeTitle">用户名:</span>
                    <p>{{ userInfo.account }}</p>
                </div>
                <div class="titleBottomDiv">
                    <span class="iptBeforeTitle">昵称:</span>
                    <p>{{ userInfo.nickName ? userInfo.nickName : userInfo.account }}</p>
                </div>
                <div class="titleBottomDiv">
                    <span class="iptBeforeTitle">性别:</span>
                    <p>{{ userInfo.sex ? userInfo.sex : "保密" }}</p>
                </div>
                <div class="titleBottomDiv">
                    <span class="iptBeforeTitle">邮箱:</span>
                    <p>{{ userInfo.eMail ? userInfo.eMail : "暂无" }}</p>
                </div>
                <div class="titleBottomDiv">
                    <span class="iptBeforeTitle">个人网站:</span>
                    <a target="_blank" :href="userInfo.personalWeb ? userInfo.personalWeb : 'javascript;'">{{
                        userInfo.personalWeb ?
                        userInfo.personalWeb : ""
                    }}</a>
                </div>
                <div class="titleBottomDiv personalIntroduce">
                    <span class="iptBeforeTitle">个性签名:</span>
                    <p class=" personalIptSpan">{{ userInfo.introduce ? userInfo.introduce :
                        "这个人很懒，什么也没有留下..."
                    }}
                    </p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import useAxios from '@/hooks/axios/axios';
import { useStore } from "@/store/count";
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from "vue-router";

const pinia = useStore()
const router = useRouter()

type Props = {
    account: any
}
const props = defineProps<Props>()

const userPropsAccount = props.account

//请求用户信息
const { data: res } = await useAxios.get('/userinfo', {
    params: {
        account: userPropsAccount
    }
})
const userInfo = reactive(res.data)  //请求的用户的信息


//请求自己的信息   进入页面先验证是否已经关注了这个人,异步验证
const logToken = localStorage.getItem('userAccount')
if (logToken) {  //如果登录了验证是否已经关注目标用户
    const logTokenInfo = JSON.parse(window.atob(logToken))   //拿到目前登录的账号的token，知道是谁点击了关注按钮
    //请求登录的账号的信息
    useAxios.get('/userinfo', {
        params: {
            account: logTokenInfo.account
        }
    }).then((res) => {  //请求回执
        const info = res.data.data
        if (info.focus.indexOf(userInfo.account) !== -1) {  //已经关注了
            ifFocus.value = true
        } else {
            ifFocus.value = false   //还没有关注
        }
    })
}

const ifFocus = ref(false)  //是否关注了
const focusDialogVisible = ref(false)
const focusReq = async () => {  //关注按钮和取消关注都会调用的请求方法
    const token = localStorage.getItem('userAccount')
    if (token) {
        const tokenInfo = JSON.parse(window.atob(token))   //拿到目前登录的账号的token，知道是谁点击了关注按钮
        const { data: res } = await useAxios.get('/focususers', {
            params: {
                account: tokenInfo.account,  //谁要关注
                focus: userPropsAccount,   //关注谁
                ifFocus: ifFocus.value  //是否已经关注了
            }
        })
        return res;
    } else {
        ElMessage.error('请先登录')
    }
}
//点击关注按钮
const addFocus = async () => {
    let res: any = await focusReq()
    if (res.status === 0) {
        ElMessage({
            message: '添加关注成功',
            type: 'success',
        })
        ifFocus.value = true

    }
}

//已关注，再点击就取消关注了
const delFocus = async () => {
    let res: any = await focusReq()
    if (res.status === 1) {
        ElMessage({
            message: '取消关注成功',
            type: 'warning',
        })
        ifFocus.value = false
        focusDialogVisible.value = false
    }
}

//查看关注列表
const focusList = (account: string, from: string) => {
    if (from === 'other') {
        router.push({
            path: "/userfocus/allfocus",
            query: {
                account: account,
                tag: 0,
                from: 'other'
            }
        })
    } else {
        //点击跳转到关注列表页面，并且传递参数，要看谁的关注列表
        router.push({
            path: "/userfocus/allfocus",
            query: {
                account: account,
                tag: 0
            }
        })
    }

}

const fansList = (account: string, from: string) => {
    if (from === 'other') {
        router.push({
            path: "/userfocus/fans",
            query: {
                account: account,
                tag: 1,
                from: 'other'
            }
        })
    } else {
        //点击跳转到粉丝列表页面，并且传递参数，要看谁的粉丝列表
        router.push({
            path: "/userfocus/fans",
            query: {
                account: account,
                tag: 1
            }
        })
    }

}

</script>

<style scoped lang="less">
.userInfo {
    display: flex;
    align-items: center;
    box-sizing: border-box;
    padding: 1rem;

    .userhead {
        width: 30rem;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;


        .headImg {
            width: 20rem;
            height: 20rem;
            border-radius: 50%;
            overflow: hidden;
            cursor: pointer;
            position: relative;

            img {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }
        }

        .headMore {
            color: var(--font-gray-color-dark);
            margin-top: 1rem;
            font-size: 1.2rem;

            .headbottom {
                margin: .5rem;
            }
        }

        .focusList {
            display: flex;
            width: 15rem;
            justify-content: space-around;
            margin-top: 1rem;
            font-size: 1.4rem;
            font-weight: 600;

            .focus {
                display: flex;
                font-size: 1.5rem;
                border-bottom: .2rem solid transparent;

                &:hover {
                    cursor: pointer;
                    border-bottom: .2rem solid var(--border-line);
                }

                p {
                    color: var(--box-shadow);
                }
            }
        }

        .focusUser {
            margin-top: 1.5rem;

            .focusClick {
                width: 12rem;
                height: 2.5rem;
                background-color: var(--border-line);
                display: flex;
                justify-content: center;
                align-items: center;
                font-size: 1.2rem;
                color: #fff;
                border-radius: .5rem;
                box-shadow: 0 0 .5rem .1rem rgb(202, 202, 202);

                &:hover {
                    cursor: pointer;
                    background-color: rgb(118, 201, 250);
                }
            }

            .focused {
                width: 12rem;
                height: 2.5rem;
                display: flex;
                justify-content: center;
                align-items: center;
                background-color: var(--font-gray-color);
                font-size: 1.2rem;
                border-radius: .5rem;
                box-shadow: 0 0 .5rem .1rem rgb(202, 202, 202);

                &:hover {
                    cursor: pointer;
                }

            }
        }
    }

    .moreInfo {
        flex: 1;
        height: 100%;
        display: flex;
        flex-direction: column;

        .title {
            font-size: 2.5rem;
            border-bottom: .1rem solid var(--gray-light-sahdow);
            padding: 1rem 0;
        }

        .titleBottom {


            .titleBottomDiv {
                font-size: 1.4rem;
                display: flex;
                padding: 1rem;

                .iptBeforeTitle {
                    width: 11rem;
                }

            }

            .personalIntroduce {
                display: flex;
                flex-direction: column;


                .personalIptSpan {
                    margin: 1rem 0;
                    margin-left: 2em;
                }
            }
        }
    }
}

@media screen and (max-width: 800px) {

    .userInfo {
        flex-direction: column;
    }
}
</style>