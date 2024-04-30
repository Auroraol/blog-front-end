<template>
    <div class="userInfo">
        <div class="userhead">
            <div class="headImg" @click="changeHeadImg">
                <img :src="userInfo.headImg" alt="head">
                <div class="mask">点击更换头像</div>
            </div>
            <Teleport to="body">
                <el-dialog v-model="ifUploadHead" title="更换头像">
                    <uploadHeadImg :ifsendHead="ifsendHead" @uploadHeadSuccess="uploadHeadSuccess"></uploadHeadImg>
                    <template #footer>
                        <span class="dialog-footer">
                            <el-button @click="ifUploadHead = false">取消</el-button>
                            <el-button type="primary" @click="sendNewHeadImg">提交</el-button>
                        </span>
                    </template>
                </el-dialog>
            </Teleport>
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
                <div class="focus" @click="focusList(userInfo.account, 'myself')">
                    <p>{{ userInfo.focus ? userInfo.focus.length : 0 }}</p>
                    <span>关注的人</span>
                </div>
                <div class="focus" @click="fansList(userInfo.account, 'myself')">
                    <p>{{ userInfo.fans ? userInfo.fans.length : 0 }}</p>
                    <span>粉丝</span>
                </div>
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
                    <el-input v-model="userInfo.nickName" placeholder="昵称" :maxlength="pinia.nickNameLength"
                        :show-word-limit="true" v-if="ifChangePersonalDate" />
                    <p v-else>{{ userInfo.nickName ? userInfo.nickName : userInfo.account }}</p>
                </div>
                <div class="titleBottomDiv">
                    <span class="iptBeforeTitle">性别:</span>
                    <el-radio-group v-model="userInfo.sex" v-if="ifChangePersonalDate">
                        <el-radio label="男" class="checkSex">男</el-radio>
                        <el-radio label="女" class="checkSex">女</el-radio>
                        <el-radio label="保密" class="checkSex">保密</el-radio>
                    </el-radio-group>
                    <p v-else>{{ userInfo.sex ? userInfo.sex : "保密" }}</p>
                </div>
                <div class="titleBottomDiv">
                    <span class="iptBeforeTitle">邮箱:</span>
                    <el-input v-model="userInfo.eMail" placeholder="xxxx@xx.xxx" v-if="ifChangePersonalDate"
                        @blur="emailBlur(userInfo.eMail)" />
                    <p v-else>{{ userInfo.eMail ? userInfo.eMail : "暂无" }}</p>
                </div>
                <div class="titleBottomDiv">
                    <span class="iptBeforeTitle">个人网站:</span>
                    <el-input v-model="userInfo.personalWeb" placeholder="要加上http://或者https://前缀"
                        v-if="ifChangePersonalDate" @blur="webIptBlur(userInfo.personalWeb)" />
                    <a v-else target="_blank" :href="userInfo.personalWeb ? userInfo.personalWeb : 'javascript;'">{{
                        userInfo.personalWeb ?
                        userInfo.personalWeb : ""
                    }}</a>
                </div>
                <div class="titleBottomDiv personalIntroduce">
                    <span class="iptBeforeTitle">个性签名:</span>
                    <el-input v-model="userInfo.introduce" :autosize="{ minRows: 2 }" type="textarea"
                        placeholder="这个人很懒，什么也没有留下..." v-if="ifChangePersonalDate" resize="none" class="personalIpt"
                        :maxlength="100" />
                    <p v-else class=" personalIptSpan">{{ userInfo.introduce ? userInfo.introduce : "这个人很懒，什么也没有留下..."
                    }} </p>
                </div>
                <div class="titleBottomBtn">
                    <!-- 修改密码 -->
                    <div class="btn" @click="changePassword" v-if="!ifChangePersonalDate">修改密码</div>
                    <Teleport to="body">
                        <el-dialog v-model="TableVisibleChangePassword" title="修改密码">
                            <div class="passwordBox">
                                <label for="oldPassword">旧密码：</label>
                                <el-input v-model.trim="passwordChange.oldPassword" name="oldPassword" maxlength="16"
                                    placeholder="输入旧密码" show-word-limit type="text" class="passwordInput" />
                            </div>

                            <div class="passwordBox">
                                <label for="newPassword">新密码：</label>
                                <el-input v-model.trim="passwordChange.newPassword" maxlength="16" placeholder="输入新密码"
                                    show-word-limit type="text" class="passwordInput" name="newPassword" />
                                <el-input v-model.trim="passwordChange.newPasswordNext" maxlength="16" placeholder="再次输入新密码"
                                    show-word-limit type="text" class="passwordInput" />
                            </div>

                            <template #footer>
                                <span class="dialog-footer">
                                    <el-button @click="TableVisibleChangePassword = false">取消</el-button>
                                    <el-button type="primary" @click="sendChangePassword">提交</el-button>
                                </span>
                            </template>
                        </el-dialog>
                    </Teleport>

                    <!-- 修改个人资料 -->
                    <div class="btn btnOver" v-if="ifChangePersonalDate" @click="changePersonalIntroduce">完成</div>
                    <div class="btn" v-else @click="changePersonalDate">修改个人资料</div>

                    <!-- 注销登录 -->
                    <div class="btn btnLogout" @click="changeOutlog" v-if="!ifChangePersonalDate">注销登录</div>
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
    </div>
</template>

<script setup lang="ts">
// import useAxios from '@/hooks/axios/axios';
import { useStore } from "/@/store";
import { ElMessage } from 'element-plus'
import { ref, reactive, defineAsyncComponent, watchEffect } from 'vue'
import { useRouter } from 'vue-router';
// import { getAccessToken, getRefreshToken, getUserAccountInfo, removeUserAccountInfo  } from '/@/utils/network/auth.js'

const uploadHeadImg = defineAsyncComponent(() => import('/@/components/personalCenter/uploadHead.vue'))

const pinia = useStore()
const router = useRouter()
//请求用户信息
// const tokenInfo = JSON.parse(window.atob(localStorage.getItem('userAccount')!))
const tokenInfo = JSON.parse(window.atob(getUserAccountInfo()!))
const { data: res } = await useAxios.get('/userinfo', {
    params: {
        account: tokenInfo.account
    }
})
const userInfo = reactive(res.data)

const ifChangePersonalDate = ref(false)  //是否更改个人资料
const changePersonalDate = () => ifChangePersonalDate.value = !ifChangePersonalDate.value
//修改个人资料
const changePersonalIntroduce = async () => {
    const newIntroduce = {
        nickName: userInfo.nickName ? userInfo.nickName : '',
        sex: userInfo.sex,
        eMail: userInfo.eMail ? userInfo.eMail : '',
        personalWeb: userInfo.personalWeb ? userInfo.personalWeb : '',
        introduce: userInfo.introduce,
    }

    if (!newIntroduce.introduce) {
        newIntroduce.introduce = "这个人很懒，什么也没有留下..."
    }

    //获取用户账号
    // const token = JSON.parse(window.atob(localStorage.getItem('userAccount')!))
    const token = JSON.parse(window.atob(getUserAccountInfo()!))
    const changeIntroduceJson = {
        account: token.account,
        data: newIntroduce
    }
    const { data: res } = await useAxios.post('/changepersonalintroduce', changeIntroduceJson)
    const changeIntroduce = res.status
    if (changeIntroduce === 0) {
        ElMessage({
            message: '更新资料成功',
            type: 'success',
        })
        changePersonalDate()
    } else {
        ElMessage.error('更新资料失败')
        changePersonalDate()
    }
}

//验证邮箱
const emailBlur = (val: string) => {
    const emailReg = new RegExp("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")
    if (val !== '' && val !== undefined) {
        if (emailReg.test(val) === true) {
            return
        } else {
            ElMessage.error('邮箱格式错误')
            userInfo.eMail = ''
        }
    } else {
        return
    }

}

//验证网站
const webIptBlur = (val: string) => {
    const webReg = new RegExp("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]")
    if (val !== '' && val !== undefined) {
        if (webReg.test(val) === true) {
            return
        } else {
            ElMessage.error('网站格式错误')
            userInfo.personalWeb = ''
        }
    } else {
        return
    }
}


//退出登录
const TableVisibleOutlog = ref(false)
const changeOutlog = () => {
    TableVisibleOutlog.value = true
}
//注销登录
const exitAccount = () => {
    localStorage.removeItem('userAccount')
    pinia.sessionInfo = ''
    alert('注销成功')
    router.replace('/login')
}

//修改密码
const TableVisibleChangePassword = ref(false)
const passwordChange = reactive({
    oldPassword: '',
    newPassword: '',
    newPasswordNext: ''
})
const changePassword = () => {
    TableVisibleChangePassword.value = true
}
const sendChangePassword = async () => {
    if (passwordChange.newPassword === '' || passwordChange.newPasswordNext === '' || passwordChange.oldPassword === '') {
        alert('请完整填写表单')
    } else {
        if (passwordChange.newPassword !== passwordChange.newPasswordNext) {
            alert('旧密码或新密码填写有误')
        } else {
            const { data: res } = await useAxios.get('/changepassword', {
                params: {
                    account: userInfo.account,
                    newPassword: passwordChange.newPassword
                }
            })
            if (res.status === 0) {
                alert('修改密码成功')
                localStorage.removeItem('userAccount')
                pinia.sessionInfo = ''
                router.replace('/login')
            } else {
                alert('出现错误')
                localStorage.removeItem('userAccount')
                pinia.sessionInfo = ''
                router.replace('/login')
            }
        }
    }
}

//更换头
//弹出更换头像的弹窗
const ifUploadHead = ref(false)
const changeHeadImg = () => {
    ifUploadHead.value = !ifUploadHead.value
}

//是否点击提交，传给子组件submit
const ifsendHead = ref(false)
const sendNewHeadImg = () => {
    ifsendHead.value = true
}

//更换完成,关闭弹窗
const uploadHeadSuccess = (val: boolean) => {
    ifUploadHead.value = val
    // 再发送一次请求，请求新的头像
    useAxios.get('/userinfo', {
        params: {
            account: tokenInfo.account
        }
    }).then((res) => {
        userInfo.headImg = res.data.data[0].headImg
    })
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
            path: "/userfocus/allfocus",
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

            &:hover {
                .mask {
                    width: 100%;
                    height: 100%;
                    background-color: rgba(0, 0, 0, .5);
                    position: absolute;
                    top: 0;
                    left: 0;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    color: #fff;
                    font-weight: 600;
                    font-size: 1.5rem;
                }
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
    }

    .moreInfo {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;

        .title {
            font-size: 2.5rem;
            padding: 1rem 0;
        }

        .titleBottom {
            width: 100%;
            border-top: .1rem solid #000;

            .titleBottomDiv {
                font-size: 1.4rem;
                display: flex;
                padding: 1rem;

                .iptBeforeTitle {
                    width: 11rem;
                }

                .checkSex {
                    font-size: 1.2rem;
                    width: 1.5rem;
                }
            }

            .personalIntroduce {
                display: flex;
                flex-direction: column;

                .personalIpt {
                    margin: 1rem 0;
                }

                .personalIptSpan {
                    margin: 1rem 0;
                    margin-left: 2em;
                }
            }

            .titleBottomBtn {
                display: flex;
                align-items: center;

                .btn {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    width: 10rem;
                    height: 2.5rem;
                    margin: 0 2rem;
                    border-radius: .5rem;
                    border: .15rem solid var(--border-line);
                    transition: all .1s;


                    &:hover {
                        cursor: pointer;
                        box-shadow: 0 0 .5rem .1rem var(--box-shadow);
                    }
                }

                .btnLogout {
                    border: .15rem solid rgb(219, 37, 37);

                    &:hover {
                        cursor: pointer;
                        box-shadow: 0 0 .5rem .1rem rgb(219, 37, 37);
                    }
                }

                .btnOver {
                    border: .15rem solid rgb(37, 219, 52);

                    &:hover {
                        cursor: pointer;
                        box-shadow: 0 0 .5rem .1rem rgb(37, 219, 52);
                    }
                }
            }
        }
    }
}


.passwordBox {
    margin-top: 1rem;
    font-size: 1.5rem;

    .passwordInput {
        margin-top: 1rem;
    }
}

@media screen and (max-width: 1200px) {

    .userInfo {
        flex-direction: column;
    }

    .moreInfo {
        box-sizing: border-box;
        padding: 5vw;
    }
}

@media screen and (max-width: 800px) {
    .userInfo {
        .moreInfo {
            .title {
                padding-left: 1.5rem;
            }

            .titleBottom {
                .titleBottomDiv {
                    font-size: 1.5rem;
                    padding: 1.5rem;
                }
            }

        }
    }

}
</style>