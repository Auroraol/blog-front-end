<template>
  <div class="container">
    <div class="content-container animated fadeInUp">
      <h3>绑定手机号</h3>
      <el-form>
        <el-form-item>
          <el-input v-model="mobile" placeholder="输入手机号" />
        </el-form-item>
        <el-form-item>
          <el-input
            size="large"
            v-model="code"
            placeholder="请输入验证码"
            inline-message
          >
            <template #suffix>
              <i
                v-show="!codeCount"
                class="code"
                style="font-style: normal; margin-right: 10px"
                @click="sendCode"
                >获取验证码</i
              >
              <el-text v-show="codeCount" type="primary"
                >{{ codeCount }}s</el-text
              >
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            style="width: 100%"
            :loading="loading"
            @click="submit"
            >提交</el-button
          >
        </el-form-item>
        <el-form-item style="text-align: center">
          <router-link
            to="/"
            type="text"
            style="width: 100%; text-align: center; color: #007fff"
            >返回首页</router-link
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, defineExpose } from "vue";
import { validMobile } from "/@/utils/validate";
import { useSmsCodeMixin } from "/@/mixins/smsMixin";
import { bindMobile } from "/@/api/user/user";

// 在 setup 中引入 mixin
const { getCode, codeCount } = useSmsCodeMixin();

// 定义 props 和 emits
const props = defineProps({});
const emits = defineEmits([]);

// 定义响应式数据
const loading = ref(false);
const code = ref("");
const mobile = ref("");

// 发送验证码
const sendCode = () => {
  getCode(mobile);
};

// 提交校验
const vsubmit = () => {
  const mobileValue = mobile.value;
  if (mobileValue === "") {
    ElMessage.warning("请输入手机号");
    return false;
  }
  if (!validMobile(mobileValue)) {
    ElMessage.warning("手机号格式不正确");
    return false;
  }
  if (code.value === "") {
    ElMessage.warning("请输入验证码");
    return false;
  }
  return true;
};

// 提交按钮点击
const submit = () => {
  if (vsubmit()) {
    loading.value = true;
    const params = { mobile: mobile.value, code: code.value };
    bindMobile(params).then(
      (res) => {
        loading.value = false;
        ElMessage.success("绑定成功");
      },
      (error) => {
        console.error(error);
        loading.value = false;
      }
    );
  }
};
</script>


<style lang="less" scoped>
.container {
  width: 100%;
  height: 100vh;
  overflow-x: hidden;
  overflow-y: -webkit-overlay;
  overflow-y: overlay;

  @media screen and (max-width: 922px) {
    background: #fff;
  }

  .content-container {
    width: 100%;
    max-width: 470px;
    box-sizing: border-box;
    margin: 0 auto;
    margin-top: 7%;
    background: #fff;
    border-radius: 4px;
    padding: 30px;
    padding-bottom: 5px;

    @media screen and (max-width: 922px) {
      margin-top: 0;
    }

    h3 {
      font-weight: 700;
      font-size: 20px;
      text-align: center;
      margin: 0;
      padding: 0;
      margin-bottom: 30px;
    }

    .el-steps {
      width: 100%;
      margin: 0 auto;
    }

    .el-form {
      width: 70%;
      margin: 0 auto;
      margin-top: 30px;

      .code-btn {
        color: #007fff;
        position: relative;
        right: 10px;
      }
    }

    // /deep/ .el-step__title.is-process {
    //   font-weight: normal;
    // }
  }
}
</style>
