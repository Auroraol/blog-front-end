<template>
  <div v-loading="loading" class="edit-container">
    <!-- 头部 -->
    <div class="edit-head">
      <div class="edit-head-left">
        <span>编辑文章</span>
        <span>ARTICLE EDIT</span>
      </div>
      <div class="edit-head-right">
        <el-select v-model="original" class="col" style="width: 75px">
          <el-option
            v-for="(option, index) in options"
            :key="index"
            :label="option.name"
            :value="option.value"
          />
        </el-select>
        <el-select
          v-model="categoryId"
          class="col"
          placeholder="选择分类"
          style="width: 120px"
        >
          <el-option
            v-for="(category, index) in categorys"
            :key="index"
            :label="category.name"
            :value="category.id"
          />
        </el-select>
        <!-- <el-button type="primary" @click="dialogVisible = true"
          >预览封面</el-button
        > -->

        <el-button class="col" type="warning" @click="save(1)">保存</el-button>
        <el-button class="col" type="danger" @click="save(0)">发布</el-button>
      </div>
    </div>
    <div class="edit-head-box">
      <div class="sub-title">
        <label>标题:</label>
        <el-input
          class="input"
          maxlength="20"
          show-word-limit
          v-model="title"
          placeholder="输入文章标题"
        />
      </div>
      <div class="sub-title">
        <label>摘要:</label>
        <el-input
          class="input"
          maxlength="30"
          show-word-limit
          v-model="summary"
          placeholder="输入文章摘要"
        />
      </div>
      <div v-if="original === 0">
        <div class="sub-title">
          <label>转载地址:</label>
          <el-input
            class="input"
            maxlength="30"
            show-word-limit
            v-model="reproduce"
            placeholder="输入转载地址"
          />
        </div>
      </div>
    </div>
    <!-- 标签 -->
    <dynamic-tags :seleted-tags="seletedTags" @tagsChage="tagsChage" />
    <!-- 上传封面 -->
    <img-upload></img-upload>
    <!-- markdown编辑器 -->
    <MdEditor
      v-model="text"
      style="height: 45rem"
      previewTheme="vuepress"
      codeTheme="a11y"
    />

    <el-dialog
      :modal="false"
      v-model="dialogVisible"
      title="预览封面"
      :top="'25vh'"
      width="320px"
      :before-close="coverClose"
      :lock-scroll="false"
    >
      <div
        class="cover-pre"
        style="
          text-align: center;
          overflow: hidden;
          position: relative;
          border-radius: 4px;
        "
      >
        <el-image
          :src="preview_cover"
          style="height: 100%; width: 100%"
          :fit="contain"
        />
        <!-- <el-icon size="35px" style="margin-top: 20px;"><DeleteFilled /></el-icon> -->
      </div>
    </el-dialog>
  </div>
</template>
  
<script setup lang='ts'>
import { ref } from "vue";
import { MdEditor } from "md-editor-v3";
import "md-editor-v3/lib/style.css";

import DynamicTags from "./components/DynamicTags.vue";
import ImgUpload from "./components/ImgUpload.vue";

const text = ref("Hello Editor!");

// 定义响应式变量
const loading = ref(false);
const id = ref(null);
const tagIds = ref([]);
const categoryId = ref(null);
const title = ref("");
const summary = ref("");
const reproduce = ref("");
const content = ref("");
const htmlContent = ref("");
const preview_cover = ref("");
const coverVisible = ref(false);
const imgUploadVisible = ref(false);
// 上传类型，1：文章图片，2：文章封面
const uploadType = ref(1);
// 原创，1：是，0：否
const original = ref(1);

// 定义选项
const options = [
  { name: "原创", value: 1 },
  { name: "转载", value: 0 },
];

// 定义分类列表和已选标签列表
const categorys = ref([]);
const seletedTags = ref([]);

onMounted(() => {});

//
const dialogVisible = ref(false);

preview_cover.value =
  "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg";

  

</script>


<style lang="less" scoped>
.edit-container {
  margin-top: 5px;
  font-size: 1.4rem;
  margin-left: 10px;
  margin-right: 10px;
  background-color: #fff;

  //
  .edit-head {
    height: 50px;
    background: #d0d0d0;
    display: flex;
    align-items: center;
    .edit-head-left {
      margin-left: 1rem;
      font-size: 1.5rem;
      line-height: 25px;
      display: flex;
      flex-direction: column;
      align-items: flex-start;
    }
    .edit-head-right {
      margin-left: auto; /* 推到容器的右侧 */
      margin-right: 10px;
    }
    .col {
      margin: 5px;
    }
  }

  //
  .edit-head-box {
    background: #fff;
    margin-bottom: 20px;
    margin-top: 10px;

    .sub-title {
      display: flex;
      align-items: center;

      label {
        width: 10rem;
        margin-left: 1rem;
      }

      .input {
        margin-right: 1rem;
      }
    }
  }

  //标签
  .tag {
    margin-top: 1rem;
    display: flex;
    align-items: center;

    p {
      margin-right: 1.5rem;
    }
  }

}
</style>