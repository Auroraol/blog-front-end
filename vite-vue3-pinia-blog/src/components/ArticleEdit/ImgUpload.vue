<!-- 图片上传组件 -->
<template>
  <div class="upload-img">
    <p class="title">上传封面:</p>
    <el-upload
      class="upload-demo"
      ref="uploadCover"
      drag="true"
      multiple="false"
      :limit="1"
      list-type="picture"
      :action="path"
      :headers="headers"
      :auto-upload="false"
      :on-success="successUpCover"
      :on-error="errorUpCover"
      :on-change="onChangeCover"
      :on-remove="onRemove"
      :on-exceed="onExceed"
      :before-upload="beforeUpload"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        拖拽至此或者<em>点击上传</em>
        只能上传一张封面,支持(jpg/jpeg/png),不超过500kb
      </div>
      <template #tip>
        <!-- <div class="el-upload__tip">大小小于500kb的Jpg /png文件</div> -->
      </template>
    </el-upload>
    <el-button
      style="margin-left: 10px; margin-right: 10px; margin-bottom: 1rem"
      type="success"
      @click="coverSubmit"
      >上传文件</el-button
    >
  </div>
</template>

<script setup lang="ts">
import Vue from "vue";
import { UploadFilled } from "@element-plus/icons-vue";
import type { UploadInstance } from "element-plus";
import { getAccessToken } from "/@/utils/auth";
import type { UploadProps } from "element-plus";

//提交文章
const uploadCover = ref<UploadInstance>(); //绑定<el-upload></el-upload>
const path = ref(import.meta.env.VITE_APP_BASE_API + "/file/upload"); // 上传图片接口
const limitSize = 500 * 1024; //封面大小限制500kb   3 * 1024 * 1024; 限制3MB
const coverTypeArr = ["image/jpeg", "image/jpg", "image/png", "image/webp"];

const headers = computed(() => {
  return { Authorization: "Bearer " + getAccessToken() }; //上传文件请求头
});

// 子->父
const emits = defineEmits(["uploadSuccess"]);

//封面上传成功的钩子
type Status = {
  status: number;
  code: number;
  message: string;
};
const successUpCover = (res) => {
  // 这里是:action="path" 之后, 自己服务响应结果
  if (res.code !== 200000) {
    ElMessage.error("文件上传失败");
    return;
  }
  // console.error(res.data);
  emits("uploadSuccess", res.data); // 传父  //element-plus 中el-upload 上传成功返回URL
};

//文件列表移除文件时的钩子
const onRemove = (e: any) => {};

//封面上传失败的钩子
const errorUpCover = () => {
  ElMessage.error("封面上传失败");
};

//封面超出上传限制
const onExceed = (e: any) => {
  ElMessage.error("封面超出上传限制，上传失败");
};

// //文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
const onChangeCover = (e: any) => {
  //文件格式校验
  if (!coverTypeArr.includes(e.raw.type)) {
    ElMessage({
      showClose: true,
      message: "选择的文件格式错误，错误的格式将不会被上传",
      type: "error",
    });
    return false;
  }
  // 文件大小效验
  const isLtSize = e.size > limitSize;
  if (isLtSize) {
    ElMessage.error("文件大小不能大于500kb");
    return false;
  }
  return true;
};

//上传前钩子  在 before-upload 钩子中限制用户上传文件的格式和大小。
const beforeUpload: UploadProps["beforeUpload"] = (rawFile) => {
  //文件格式校验
  if (!coverTypeArr.includes(rawFile.type)) {
    ElMessage({
      showClose: true,
      message: "选择的文件格式错误，错误的格式将不会被上传",
      type: "error",
    });
    return false;
  }
  // 文件大小效验
  const isLtSize = rawFile.size > limitSize;
  if (isLtSize) {
    ElMessage.error("文件大小不能大于500kb");
    return false;
  }
  return true;
};

const coverSubmit = () => {
  uploadCover.value!.submit();
};
</script>

<style lang="less" scoped>
// 上传封面
.upload-img {
  margin-top: 2rem;

  display: flex;
  flex-direction: column;
  .title {
    margin-left: 1rem;
    margin-right: 1rem;
    margin-bottom: 1rem;
  }
  .upload-demo {
    margin-left: 1rem;
    margin-right: 1rem;
  }

  .el-upload__tip {
    margin-left: 1rem;
  }
}
</style>
