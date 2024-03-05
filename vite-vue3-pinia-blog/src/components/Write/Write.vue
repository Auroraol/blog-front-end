<template>
    <form class="contaier">
        <div class="page">
            <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-16"></use>
            </svg>
            <div class="pageChild">
                <span>编辑文章</span>
                <span>ARTICLE EDIT</span>
            </div>
        </div>
        <div class="title">
            <label for="title">标题:</label>
            <el-input @input="saveEditor" maxlength="30" v-model="articleWrite.articleTitle" placeholder="输入文章标题"
                show-word-limit type="text" name="title" />
        </div>
        <div class="tag">
            <p>选择标签:</p>
            <div @click="saveEditor">
                <span class="labelTag no-choose" @click="chooseTag(tag.content)" v-for="tag in allTags" :key="tag.id">{{
                    tag.content
                }}</span>
            </div>
        </div>
        <div class="choose">
            <p>已选择({{ articleWrite.articleTags.length }} /6):</p>
            <div>
                <el-tag v-for="tag in articleWrite.articleTags" :key="tag" class="mx-1" closable
                    :disable-transitions="false" @close="handleClose(tag)">
                    {{ tag }}
                </el-tag>
                <el-input v-if="inputVisible" ref="InputRef" v-model="inputValue" class="ml-1 w-20" size="small"
                    @keyup.enter="handleInputConfirm" @blur="handleInputConfirm" />
                <el-button v-else class="button-new-tag ml-1" size="small" @click="showInput">
                    + New Tag
                </el-button>
            </div>
        </div>
        <div class="uploadimg">
            <p>上传封面:</p>
            <div>
                <el-upload class="upload-demo" :drag="true" :action="pinia.apiRoot + '/api/uploadarticle'" :limit="1"
                    :auto-upload="false" list-type="picture" ref="uploadCover" :on-success="successUpCover"
                    :on-error="errorUpCover" :on-change="onChangeCover" :on-remove="onRemove" multiple :on-exceed="onExceed"
                    :before-upload="beforeUpload" :http-request="elUploadFunc">
                    <el-icon class="el-icon--upload">
                        <upload-filled />
                    </el-icon>
                    <div class="el-upload__text">
                        拖拽至此或者<em>点击上传</em>
                        只能上传一张封面,支持(jpg/jpeg/png/webp)
                    </div>
                </el-upload>
            </div>
        </div>
        <div class="article">
            <md-editor @input="saveEditor" v-model="articleWrite.articleText"
                :toolbarsExclude="['quote', 'save', 'htmlPreview',]" :showToolbarName="true" style="height:60rem"
                @onUploadImg="onUploadImg" :preview="false" :showCodeRowNumber="true" placeholder="在这里输入...">
            </md-editor>
        </div>
        <div class="uploadArticle">
            <div @click="uploadArticle(articleWrite)" v-if="ifUpload">提交</div>
            <div v-else class="uploading">提交中</div>
        </div>
    </form>
</template>

<script setup lang="ts">
import { UploadFilled } from '@element-plus/icons-vue'
import { ElInput, ElMessage } from 'element-plus'
import type { UploadInstance } from 'element-plus'
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
// import useAxios from '@/hooks/axios/axios'
// import { ArticleWrite } from "@/hooks/Types/types"
import { useRouter } from "vue-router";
// import { debonce } from '@/hooks/debonceAndThrottle/debonceAndThrottle'
import { useStore } from "/@/store";
import axios from "axios";
const pinia = useStore()
const router = useRouter()

//请求出完整的标签列表
const { data: res } = await axios.get('/gettags')
const allTags = res.data

const token = window.atob(localStorage.getItem('userAccount')!)
const tokenInfo = JSON.parse(token)

type ArticleWrite = {
    articleTitle: string
    articleText: string
    articleTags: Array<string>
    author: string,
}

//编辑文章里面的文章内容
let articleWrite: ArticleWrite = reactive({
    articleTitle: '',
    articleText: '',
    //已经选择的标签
    articleTags: [],
    author: tokenInfo.account
})


// 如果有草稿就同步一下草稿
const session = sessionStorage.getItem('articleDraft')
if (session) {
    const draft = JSON.parse(session)
    articleWrite.articleTitle = draft.articleTitle
    articleWrite.articleText = draft.articleText
    articleWrite.articleTags = draft.articleTags
    articleWrite.author = draft.author
}


//选中筛选tag
const chooseTag = (tagName: string) => {
    //如果已经选中了则不要重复选中
    if (articleWrite.articleTags.indexOf(tagName) !== -1) {
        return
    } else {
        if (articleWrite.articleTags.length < 6) {
            articleWrite.articleTags.push(tagName)
        } else {
            alert('标签数量达到上限')
        }
    }
}

//已选择的标签，可自定义添加新标签
const inputValue = ref('')
const inputVisible = ref(false)
const InputRef = ref<InstanceType<typeof ElInput>>()

const handleClose = (tag: string) => {
    articleWrite.articleTags.splice(articleWrite.articleTags.indexOf(tag), 1)
    // saveEditor()
}

const showInput = () => {
    inputVisible.value = true
    nextTick(() => {
        InputRef.value!.input!.focus()
    })
}

const handleInputConfirm = () => {
    if (inputValue.value) {
        //判断，如果标签数量已经达到最大数量则不添加新标签并且给出弹窗
        if (articleWrite.articleTags.length < 6) {
            articleWrite.articleTags.push(inputValue.value)
        } else {
            alert('标签数量达到上限')
        }
    }
    saveEditor()
    inputVisible.value = false
    inputValue.value = ''
}


//md上传图片
const onUploadImg = async (files: any, callback: any) => {
    const res = await Promise.all(
        files.map((file: any) => {
            return new Promise((rev, rej) => {
                const form = new FormData();
                form.append('file', file);
                form.append("account", tokenInfo.account)
                
                axios.post('/api/uploadmdimg', form, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    })
                    .then((res) => rev(res))
                    .catch((error) => {
                        alert("图片太大了")
                    });
            });
        })
    );
    callback(res.map((item) => item.data.data));
};

//配置md编辑器
MdEditor.config({
    //设置链接在新窗口打开
    markedRenderer(renderer) {
        renderer.link = (href, title, text) => {
            return `<a href="${href}" title="${title}" target="_blank">${text}</a>`;
        };

        return renderer;
    }
});


//提交文章
const ifUpload = ref(true)  //提交按钮点击后换位提交中的按钮
const uploadCover = ref<UploadInstance>()
let coverNum = 0  //选择上传的封面数量
const biggerCover = ref(false)  //封面大小是否超出了限制
const limitSize = 3 * 1024 * 1024   //封面大小限制3MB
let coverFile: any;
const coverTypeArr = [
    'image/jpeg',
    'image/jpg',
    'image/png',
    'image/webp',
]

//保存草稿（防抖,避免一直触发）
// const saveEditor = debonce(() => {
//     const draft = JSON.stringify(articleWrite)
//     sessionStorage.setItem('articleDraft', draft)
// }, 500)

//封面上传成功的钩子
type Status = {
    status: number
}
const successUpCover = (res: Status) => {

}

//封面上传失败的钩子
const errorUpCover = () => {
    alert('封面上传失败')
}

//封面超出上传限制
const onExceed = (e: any) => {
    alert('封面超出上传限制，上传失败')
}

//文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
const onChangeCover = (e: any) => {
    if (!coverTypeArr.includes(e.raw.type)) {
        ElMessage({
            showClose: true,
            message: '选择的文件格式错误，错误的格式将不会被上传',
            type: 'error',
        })
        return
    }
    e.size > limitSize ? biggerCover.value = true : biggerCover.value = false
    coverNum++
}
//文件列表移除文件时的钩子
const onRemove = (e: any) => {
    coverNum--
}

//上传前钩子
const beforeUpload = (e: any) => { }

// 上传按钮
const uploadArticle = (articleWrite: ArticleWrite) => {
    if (articleWrite.articleTags.length === 0 || articleWrite.articleTitle === '' || articleWrite.articleText === '') {
        ElMessage({
            showClose: true,
            message: '请检查标签或者标题、内容是否为空！',
            type: 'error',
        })
        return
    }

    if (coverNum > 1) {
        ElMessage({
            showClose: true,
            message: '封面数量超出限制',
            type: 'error',
        })
        return
    }

    if (biggerCover.value) {
        ElMessage({
            showClose: true,
            message: '封面大小超出限制',
            type: 'error',
        })
        return
    }

    // 上传文章
    ifUpload.value = false
    if (coverNum == 0) {
        // 没有上传封面
        const formArticle = new FormData();
        formArticle.append("writeArticle", new Blob([JSON.stringify(articleWrite)], { type: 'application/json' }))
        axios.post("/uploadarticle", formArticle).then((res) => {
            if (res.data.status === 0) {
                alert('文章上传成功')
                router.replace('/article')
            } else {
                alert('文章上传失败')
            }
        }).catch(err => {
            alert('出现错误:' + err)
            ifUpload.value = true
        })
    } else {
        // 有封面
        uploadCover.value!.submit()  //调用submit会自动调用http-request的函数(elUploadFunc)
    }

}

const elUploadFunc = (domParam: any) => {
    const formArticle = new FormData();
    formArticle.append("writeArticle", new Blob([JSON.stringify(articleWrite)], { type: 'application/json' }))
    formArticle.append("file", domParam.file)
    axios.post("/uploadarticle", formArticle).then((res) => {
        if (res.data.status === 0) {
            alert('文章上传成功')
            router.replace('/article')
        } else {
            alert('文章上传失败')
        }
    }).catch(err => {
        alert('出现错误:' + err)
        ifUpload.value = true
    })
}


</script>

<style scoped lang="less">
.contaier {
    margin: 0 auto;
    // border: .1rem solid black;
    // box-shadow: .1rem .1rem .5rem var(--gray-sahdow);
    border-radius: .5rem;
    width: 65%;
    display: flex;
    flex-direction: column;
    font-size: 1.4rem;
    padding: 3rem;
    background-color: var(--white-background-color);

    .page {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        border-bottom: .4rem solid var(--border-line);
        font-size: 4rem;
        padding-bottom: 2rem;

        .pageChild {
            margin-left: 1rem;
            font-size: 1.5rem;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
        }
    }

    .uploadArticle {
        margin-top: 1.5rem;
        display: flex;
        align-items: center;
        justify-content: flex-end;

        .uploading {
            cursor: wait;
        }

        div {
            width: 7rem;
            height: 3rem;
            border: .2rem solid black;
            border-radius: .5rem;
            cursor: pointer;
            display: flex;
            justify-content: center;
            align-items: center;
            transition: all .2s;

            &:hover {
                transform: translateY(-.2rem);
                box-shadow: .3rem .3rem var(--box-shadow);
            }
        }
    }

    .tag {
        margin-top: 1rem;
        display: flex;
        align-items: center;

        p {
            margin-right: 1.5rem;
        }
    }

    .uploadimg {
        margin-top: 2rem;
        display: flex;
        flex-direction: column;

        p {
            margin-right: 1rem;
            margin-bottom: 1rem;
        }
    }

    .choose {
        margin-top: 2rem;
        display: flex;
        align-items: center;

        p {
            margin-right: .5rem;
        }

        :deep(.el-tag) {
            margin: 0 .3rem;
        }
    }

    .title {
        display: flex;
        align-items: center;
        margin: 1rem 0;


        label {
            width: 5rem;
        }

        input {
            width: 10%;
        }
    }

    .article {
        margin-top: 3rem;
    }

}

@media screen and (max-width: 800px) {
    .contaier {
        width: 90%;
        border: none;
    }
}

.no-choose {
    color: black;
    background-color: #fff;
    border: .1rem solid black;
    cursor: pointer;
    transition: all .1s;

    &:hover {
        box-shadow: .2rem .2rem var(--box-shadow);
        transform: translateY(-0.1rem);
    }
}
</style>