<template>
  <div class="common-layout">
    <el-container>
      <el-main  width="80%;">
         <MdPreview
            :editorId="id"
            :modelValue="text"
            previewTheme="vuepress"
            codeTheme="a11y"
          />
      </el-main>
       <el-aside width="20%">
         <el-affix :offset="120">
            <el-card style="max-width: 300px">
              <template #header>
                <div class="card-header">
                  <span>目录</span>
                </div>
              </template>
              <MdCatalog :editorId="id" :scrollElement="scrollElement" />
            </el-card>
          </el-affix>
       </el-aside>
    </el-container>
  </div>
<!-- 
  <div class="docs-page">
    <div class="container">
      <div class="doc">
        <div class="content">
          <MdPreview
            :editorId="id"
            :modelValue="text"
            previewTheme="vuepress"
            codeTheme="a11y"
          />
        </div>
        <div class="catalog">
          <el-affix :offset="120">
            <el-card style="max-width: 300px">
              <template #header>
                <div class="card-header">
                  <span>目录</span>
                </div>
              </template>
              <MdCatalog :editorId="id" :scrollElement="scrollElement" />
            </el-card>
          </el-affix>
        </div>
      </div>
    </div>
  </div> -->
</template>

<script setup lang="ts">
import { useRequest } from "vue-hooks-plus";
import type { TagProps } from "element-plus";

import { MdEditor } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import { MdPreview, MdCatalog } from "md-editor-v3";
const id = "preview-only";
const scrollElement = document.documentElement;
const text = ref(`
# Hello Editor
# Hello 
This is a sample Markdown content. You can use Markdown syntax to format text, such as:

- **Bold text**
- *Italic text*
- Code blocks

\`\`\`javascript
function greet(name) {
    return 'Hello, ' + name + '!';
}
\`\`\`
## 第一
Enjoy writing in Markdown!
## 第二
### 一
`);
// const state = reactive({
//   text: '',
//   theme: 'cyanosis'
// });

// type Item = { type: TagProps["type"]; label: string };
// const items = ref<Array<Item>>([
//   { type: "primary", label: "Tag 1" },
//   { type: "success", label: "Tag 2" },
//   { type: "info", label: "Tag 3" },
//   { type: "warning", label: "Tag 4" },
//   { type: "danger", label: "Tag 5" },
// ]);
// // const token = window.atob(localStorage.getItem('userAccount')!)
// // const tokenInfo = JSON.parse(token)
// // // 上传文章
// type ArticleWrite = {
//   articleTitle: string;
//   articleText: string;
//   articleTags: Array<string>;
//   // author: string;
// };

// let articleWrite: ArticleWrite = reactive({
//   articleTitle: "",
//   articleText: "",
//   articleTags: [],
//   // author: tokenInfo.account
// });

// // 防抖动
// function getArticleTitle(): Promise<string> {
//   return new Promise((resolve, reject) => {
//     setTimeout(() => {
//       resolve("成功");
//     }, 300);
//   });
// }

// const { data, run } = useRequest(() => getArticleTitle(), {
//   debounceWait: 1000,
//   manual: true,
//   onSuccess: (data) => {
//     const draft = JSON.stringify(articleWrite);
//     sessionStorage.setItem("articleDraft", draft); // 保存到浏览器
//   },
// });

// watch(
//   () => articleWrite.articleTitle,
//   (c) => {
//     run();
//   }
// );

// onMounted(() => {
//   // 如果有草稿就同步一下草稿
//   const session = sessionStorage.getItem("articleDraft");
//   if (session) {
//     const draft = JSON.parse(session);
//     articleWrite.articleTitle = draft.articleTitle;
//     articleWrite.articleText = draft.articleText;
//     articleWrite.articleTags = draft.articleTags;
//     // articleWrite.author = draft.author
//   }
// });

// //选中筛选tag
// type Tag = {
//   articleTitle: string;
//   articleText: string;
//   articleTags: Array<string>;
//   // author: string;
// };
// // let allTags: Tag[] = ["c++", "java", "c"]; //推荐

// const chooseTag = (tagName: string) => {
//   //如果已经选中了则不要重复选中
//   if (articleWrite.articleTags.indexOf(tagName) !== -1) {
//     return;
//   } else {
//     if (articleWrite.articleTags.length < 6) {
//       articleWrite.articleTags.push(tagName);
//     } else {
//       alert("标签数量达到上限");
//     }
//   }
// };
</script>

<style lang="less" scoped>
// .docs-page {
//   color: #3f4a54;
//   background-color: #fff;
//   min-height: 100vh;
// }

// .container {
//   width: 1170px;
//   margin: 0 auto;
// }

// .doc {
//   display: flex;
//   padding: 2rem;
// }

// .content {
//   width: 75%;
//   padding-right: 1rem;
//   margin-bottom: 1rem;
//   box-shadow: 0 1px 12px 0 rgba(0, 0, 0, 0.1);
// }
// .catalog {
//   width: 25%;
// }
</style>
