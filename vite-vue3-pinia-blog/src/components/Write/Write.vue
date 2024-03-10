<template>
  <div class="write">
    <el-row>
      <el-col :span="24">
        <div class="bg-purple">
          <div class="page">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-16"></use>
            </svg>
            <div class="pageChild">
              <span>编辑文章</span>
              <span>ARTICLE EDIT</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div>
          <div class="flex gap-4">
            <span>标题:</span>
            <el-input
              v-model="articleWrite.articleTitle"
              placeholder="输入文章标题"
  
            >
              <template #suffix>
                <el-icon><CloseBold @click="handleClear"/></el-icon>
              </template>
            </el-input>
          </div>
 <el-input v-model="input3" style="width: 240px" placeholder="Pick a date">
      <template #suffix>
        <el-icon class="el-icon-close"></el-icon>
      </template>
    </el-input>
          <div class="tag">
            <p>选择标签:</p>
            <div>
              <span
                class="labelTag no-choose"
                @click="chooseTag(tag.content)"
                v-for="tag in allTags"
                :key="tag.id"
                >{{ tag.content }}</span
              >
            </div>
          </div>
          <div class="choose">
            <p>已选择({{ articleWrite.articleTags.length }} /6):</p>
            <div>
              <el-tag
                v-for="tag in articleWrite.articleTags"
                :key="tag"
                class="mx-1"
                closable
                :disable-transitions="false"
                @close="handleClose(tag)"
              >
                {{ tag }}
              </el-tag>
              <el-input
                v-if="inputVisible"
                ref="InputRef"
                v-model="inputValue"
                class="ml-1 w-20"
                size="small"
                @keyup.enter="handleInputConfirm"
                @blur="handleInputConfirm"
              />
              <el-button
                v-else
                class="button-new-tag ml-1"
                size="small"
                @click="showInput"
              >
                + New Tag
              </el-button>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div class="article">
          <MdEditor v-model="text" />
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div class="flex gap-2 mt-4">
          <span>Plain</span>
          <el-tag
            v-for="item in items"
            :key="item.label"
            :type="item.type"
            effect="plain"
          >
            {{ item.label }}
          </el-tag>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { useRequest } from "vue-hooks-plus";
import type { TagProps } from "element-plus";

type Item = { type: TagProps["type"]; label: string };
const items = ref<Array<Item>>([
  { type: "primary", label: "Tag 1" },
  { type: "success", label: "Tag 2" },
  { type: "info", label: "Tag 3" },
  { type: "warning", label: "Tag 4" },
  { type: "danger", label: "Tag 5" },
]);
// const token = window.atob(localStorage.getItem('userAccount')!)
// const tokenInfo = JSON.parse(token)
// // 上传文章
type ArticleWrite = {
  articleTitle: string;
  articleText: string;
  articleTags: Array<string>;
  // author: string;
};

let articleWrite: ArticleWrite = reactive({
  articleTitle: "",
  articleText: "",
  articleTags: [],
  // author: tokenInfo.account
});

// 防抖动
function getArticleTitle(): Promise<string> {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve("成功");
    }, 300);
  });
}

const { data, run } = useRequest(() => getArticleTitle(), {
  debounceWait: 1000,
  manual: true,
  onSuccess: (data) => {
    const draft = JSON.stringify(articleWrite);
    sessionStorage.setItem("articleDraft", draft); // 保存到浏览器
  },
});

watch(
  () => articleWrite.articleTitle,
  (c) => {
    run();
  }
);

onMounted(() => {
  // 如果有草稿就同步一下草稿
  const session = sessionStorage.getItem("articleDraft");
  if (session) {
    const draft = JSON.parse(session);
    articleWrite.articleTitle = draft.articleTitle;
    articleWrite.articleText = draft.articleText;
    articleWrite.articleTags = draft.articleTags;
    // articleWrite.author = draft.author
  }
});

//选中筛选tag
type Tag = {
  articleTitle: string;
  articleText: string;
  articleTags: Array<string>;
  // author: string;
};
// let allTags: Tag[] = ["c++", "java", "c"]; //推荐

const chooseTag = (tagName: string) => {
  //如果已经选中了则不要重复选中
  if (articleWrite.articleTags.indexOf(tagName) !== -1) {
    return;
  } else {
    if (articleWrite.articleTags.length < 6) {
      articleWrite.articleTags.push(tagName);
    } else {
      alert("标签数量达到上限");
    }
  }
};

import { MdEditor } from "md-editor-v3";
import "md-editor-v3/lib/style.css";

const text = ref("Hello Editor!");
</script>

<style lang="less" scoped>
.write {
  margin-top: 0px;
  margin-left: 30px;
  margin-right: 30px;
  font-size: 1.4rem;
  border: white;
  // border-style: solid;background-color: white;
}

.el-row {
  margin-bottom: 20px;
}

.double {
  border-radius: 4px;
  min-height: 490px;
}

.article .bg-purple {
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

// .title {
//   display: flex;
//   align-items: center;
//   margin: 1rem 0;

//   label {
//     width: 5rem;
//   }

//   input {
//     width: 10%;
//   }
// }

// .title {
//     display: flex;
//     width: 100%;
//     margin: 8px 16px 8px 8px;
//     border: 1px solid #ccc;
//     border-radius: 21px;
//     background-color: #fff;
//     /* position: relative; */
// }

.md-editor {
  font-size: 55px;
}

.md-editor-dark {
  --md-bk-color: #333 !important;
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
