<template>
  <div class="edit-container">
    <!-- 头部 -->
    <div class="edit-head">
      <div v-if="device === 'desktop'" class="edit-head-left">
        <span>编辑文章</span>
        <span>ARTICLE EDIT</span>
      </div>

      <div
        class="edit-head-right"
        style="display: flex; justify-content: space-between"
      >
        <div v-if="device === 'desktop'" style="flex: 3"></div>
        <el-select
          v-model="articleWrite.original"
          class="col"
          style="flex: 1.5"
        >
          <el-option
            v-for="(option, index) in options"
            :key="index"
            :label="option.name"
            :value="option.value"
          />
        </el-select>

        <el-select
          v-model="articleWrite.categoryId"
          class="col"
          placeholder="选择分类"
          style="flex: 2"
        >
          <el-option
            v-for="(category, index) in categories"
            :key="index"
            :label="category.name"
            :value="category.id"
          />
        </el-select>

        <el-button
          class="col"
          type="warning"
          :loading="loading1"
          @click="save(1)"
          style="flex: 0.5"
          >保存</el-button
        >
        <el-button
          class="col"
          type="danger"
          :loading="loading"
          @click="save(0)"
          style="flex: 0.5"
          >发布</el-button
        >
      </div>
    </div>
    <div class="edit-head-box">
      <div class="sub-title">
        <label>标题:</label>
        <el-input
          class="input"
          maxlength="30"
          show-word-limit
          v-model="articleWrite.title"
          placeholder="输入文章标题"
        />
      </div>
      <div class="sub-title">
        <label>摘要:</label>
        <el-input
          class="input"
          maxlength="30"
          show-word-limit
          v-model="articleWrite.summary"
          placeholder="输入文章摘要"
        />
      </div>
      <div v-if="articleWrite.original === 0">
        <div class="sub-title">
          <label>转载地址:</label>
          <el-input
            class="input"
            maxlength="30"
            show-word-limit
            v-model="articleWrite.reproduce"
            placeholder="输入转载地址"
          />
        </div>
      </div>
    </div>
    <!-- 标签 -->
    <dynamic-tags
      :beSeletedTags="beSeletedTags"
      :beenSeletedTags="beenSeletedTags"
      @tagsChange="tagsChange"
    />
    <!-- 上传封面 -->
    <img-upload @uploadSuccess="coverUploadSuccess"></img-upload>
    <!-- markdown编辑器 -->
    <MdEditor
      editorId="uploadingArticle"
      v-model="articleWrite.content"
      style="height: 45rem"
      previewTheme="vuepress"
      codeTheme="a11y"
      :toolbarsExclude="[
        'quote',
        'save',
        'htmlPreview',
        'github',
        'pageFullscreen',
      ]"
      :showCodeRowNumber="true"
      :autoDetectCode="true"
      :showToolbarName="true"
      transformImgUrl
      @onHtmlChanged="htmlChange"
      @onUploadImg="onUploadImg"
      placeholder="在这里输入..."
    />
    <!-- @onSave="onSave" -->

    <el-dialog
      :modal="false"
      v-model="coverVisible"
      title="预览封面"
      :top="'25vh'"
      width="320px"
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
          :src="articleWrite.cover"
          style="height: 100%; width: 100%"
          :fit="contain"
        />
      </div>
    </el-dialog>
  </div>
</template>
  
<script setup lang='ts'>
import { ref } from "vue";
import { ElLoading } from "element-plus";
import { useRequest } from "vue-hooks-plus";
import { MdEditor, config } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import { ArticleRequestData, categoryItem } from "/@/typings.d";
import { categoryList } from "/@/api/category/category";
import { saveArticle, articleDetail } from "/@/api/article/article";
import { deleteFile } from "/@/api/file/file";
import { request } from "/@/utils/network/axios";
import { tagList } from "/@/api/tag/tag";
import TargetBlankExtension from "./components/TargetBlankExtension";
import DynamicTags from "/@/components/ArticleEdit/DynamicTags.vue";
import ImgUpload from "/@/components/ArticleEdit/ImgUpload.vue";
import { useGetters } from "/@/store/getters";

const router = useRouter();

const useGettersPinia = useGetters();
const device = computed(() => useGettersPinia.device);
// 定义响应式变量
// 上传文章
const articleWrite = ref<ArticleRequestData>({
  id: null,
  original: 1, // 原创，1：是，0：否
  status: 0,
  categoryId: "",
  title: "",
  summary: "",
  reproduce: "",
  content: "",
  htmlContent: "",
  cover: "",
  tagIds: [],
});

const loading1 = ref(false);
const loading = ref(false);
const uploadType = ref(1); // 上传类型，1：文章图片，2：文章封面
const preview_cover = ref("");
const coverVisible = ref(false);
const imgUploadVisible = ref(false);

// 定义选项
const options = [
  { name: "原创", value: 1 },
  { name: "转载", value: 0 },
];

// 定义分类列表和已选标签列表
const categories = ref<categoryItem[]>([]);
const beSeletedTags = ref([]); // 待选择
const beenSeletedTags = ref([]); // 已选择

// 初始化
onMounted(() => {
  initCategoryList();
  initTag();
  initEdit(Number(router.currentRoute.value.query.id)); // 当前路由的 id 参数
});

const initEdit = async (id: number | null) => {
  if (!id) return;

  try {
    loading.value = true;
    const res = await articleDetail(id);
    const {
      categoryId,
      title,
      summary,
      cover,
      content,
      original,
      reproduce,
      tagList,
      htmlContent,
    } = res;

    // console.error(res);

    articleWrite.value = {
      id,
      original,
      status: 0,
      categoryId,
      title,
      summary,
      reproduce,
      content,
      htmlContent,
      cover,
      tagIds: tagList.map((tag) => tag.id),
    };

    articleWrite.value.id = id;

    // 已选择标签
    beenSeletedTags.value = tagList;

    loading.value = false;
  } catch (error) {
    console.error(error);
    loading.value = false;
  }
};

// 获取分类列表
const initCategoryList = () => {
  categoryList().then((res) => {
    categories.value = res;
  });
};

// 加载标签
const initTag = async () => {
  try {
    const res = await tagList();
    beSeletedTags.value = res;
  } catch (error) {
    console.error(error);
  }
};

// const openFullScreen2 = () => {
//   const loading = ElLoading.service({
//     lock: true,
//     text: "Loading",
//     background: "rgba(0, 0, 0, 0.7)",
//   });
//   setTimeout(() => {
//     loading.close();
//   }, 2000);
// };

// 监控到已选标签(子传父)
const tagsChange = (tags) => {
  //赋值
  articleWrite.value.tagIds = tags.map((x) => {
    return x.id;
  });
};

// 预览封面
const coverPreview = () => {
  coverVisible.value = true;
};

//MdEditor  设置链接在新窗口打开
config({
  markdownItConfig(md) {
    md.use(TargetBlankExtension);
  },
});

//MdEditor 上传图片事件  上传图片事件，弹窗会等待上传结果，务必将上传后的 urls 作为 callback 入参回传。
const onUploadImg = async (files, callback) => {
  try {
    const res = await Promise.all(
      files.map((file) => {
        return new Promise((resolve, reject) => {
          const form = new FormData();
          form.append("file", file);

          // 发送文件上传请求
          request(
            import.meta.env.VITE_APP_BASE_API + "/file/upload",
            {
              method: "post",
              data: form,
              headers: { "Content-Type": "multipart/form-data" },
            },
            true
          )
            .then((response) => {
              resolve(response); // 将响应传递给 Promise.resolve
            })
            .catch((error) => {
              reject(error); // 将错误传递给 Promise.reject
            });
        });
      })
    );

    // 方式一
    callback(res.map((url) => url));

    // 方式二
    // callback(
    //   res.map((item: any) => ({
    //     url: item.data.url,
    //     alt: 'alt',
    //     title: 'title'
    //   }))
    // );

    ElMessage({
      message: "图片上传成功",
      type: "success",
    });
  } catch (error) {
    console.error(error);
  }
};

//MdEditor html 变化回调事件，用于获取预览 html 代码
const htmlChange = (h: string) => {
  articleWrite.value.htmlContent = h;
};

// 保存/发布
const save = (status: number) => {
  if (status === 0) {
    loading.value = true;
  } else {
    loading1.value = true;
  }

  articleWrite.value.status = status;
  const data = articleWrite.value;
  console.error(data);

  saveArticle(data).then(
    (res) => {
      articleWrite.value.id = res; // 保存成功服务器返回文章id
      loading1.value = false;
      loading.value = false;
      ElMessage({
        message: status === 0 ? "文章发布成功" : "文章保存成功",
        type: "success",
      });
    },
    (error) => {
      console.error(error);
      loading1.value = false;
      loading.value = false;
    }
  );
};

// 封面上传成功，删除原封面
const coverUploadSuccess = (url) => {
  const oldCover = articleWrite.value.cover;
  articleWrite.value.cover = url;
  const params = {
    fullPath: oldCover,
  };
  console.error(params.fullPath);

  if (oldCover) {
    deleteFile(params);
  }
  ElMessage({
    message: "封面上传成功",
    type: "success",
  });
};
</script>

<style lang="less">
.edit-container {
  font-size: 1.4rem;
  margin-left: 10px;
  margin-right: 10px;
  background-color: #fff;
  margin: 0 auto;
  width: 65%;
  margin-top: 5px;

  @media screen and (max-width: 960px) {
    background: #fff;
    margin: 0 auto;
    width: 90%;
    margin-top: 5px;
  }

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