<template>
  <el-dialog
    top="10vh"
    width="720px"
    v-model="props.visible"
    :destroy-on-close="true"
    :show-close="false"
    :close-on-click-modal="false"
    :lock-scroll="false"
    @opened="opened"
  >
    <template #header="{ titleId, titleClass }">
      <div class="my-header">
        <h4 :id="titleId" :class="titleClass">预览</h4>
        <el-button type="danger" @click="bClose">
          <el-icon class="el-icon--left"><CircleCloseFilled /></el-icon>
          关闭
        </el-button>
      </div>
    </template>
    <div
      v-loading="loading"
      class="content markdown-body"
      v-html="articleHtmlContent"
    />
  </el-dialog>
</template>
<script setup>
import { articleDetail } from "/@/api/article/article";
import { MdEditor } from "md-editor-v3";
const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  id: {
    type: [Number, String],
    default: "",
  },
});
const emits = defineEmits(["defineEmits"]);

const articleHtmlContent = ref("");
const loading = ref(true);

const opened = () => {
  loading.value = true;
  articleDetail(props.id).then((res) => {
    loading.value = false;

    let htmlContent = res.htmlContent;

    articleHtmlContent.value = htmlContent.replace(
      /<video/g,
      "<video style='width:100%;height:auto;'"
    );

    articleHtmlContent.value = htmlContent.replace(
      /<img/g,
      "<img style='width:100%;height:auto;'"
    );
  });
};

const bClose = () => {
  articleHtmlContent.value = "";
  loading.value = true;
  emits("beforeClose");
};
</script>



<style lang="less" scoped>
.content {
  padding: 15px;
  min-height: 70vh;
  text-align: left;
}

[alt] {
  max-width: 100%;
}

.el-dialog {
  max-height: 90vh;
  overflow-y: scroll;
}
</style>
