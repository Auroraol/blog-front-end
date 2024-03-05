<template>
  <div class="write">
    <el-row>
      <el-col :span="24">
        <div class="double bg-purple">
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
            <el-input
              maxlength="30"
                        show-word-limit
              v-model="articleWrite.articleTitle"
              placeholder="输入文章标题"
              type="text"
              name="title"
    
            />
          </div>
         <div class="tag">
            <p>选择标签:</p>
            <div>
                <span class="labelTag no-choose" @click="chooseTag(tag.content)" v-for="tag in allTags" :key="tag.id">{{
                    tag.content
                }}</span>
            </div>
        </div>
         <!-- <div class="choose">
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
        </div> -->
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div class="double bg-purple"></div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div class="double bg-purple"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">

const token = window.atob(localStorage.getItem('userAccount')!)
const tokenInfo = JSON.parse(token)
// // 上传文章
type ArticleWrite = {
  articleTitle: string;
  articleText: string;
  articleTags: Array<string>;
  author: string;
}

let articleWrite: ArticleWrite = reactive({
  articleTitle: '',
  articleText: '',
  articleTags: [],
  author: tokenInfo.account
})

function getArticleTitle(): Promise<string> {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve('成功')
    }, 300)
  })
}

const { data, run } = useRequest(() => getArticleTitle(), {
  debounceWait: 1000,
  manual: true,
    onSuccess: data => {
    const draft = JSON.stringify(articleWrite)
    sessionStorage.setItem('articleDraft', draft)   // 保存到浏览器
    }
})

watch(() => articleWrite.articleTitle, c => {
  run()
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





</script>

<style lang="less" scoped>
.write {
  margin-top: 0px;
  margin-left: 15px;
  margin-right: 15px;
 font-size: 1.4rem;

}

.el-row {
  margin-bottom: 20px;
}

.double {
  border-radius: 4px;
  min-height: 490px;
}

.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}

.page {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  border-bottom: 0.4rem solid var(--border-line);
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


   .tag {
        margin-top: 1rem;
        display: flex;
        align-items: center;

        p {
            margin-right: 1.5rem;
        }
    }

</style>
