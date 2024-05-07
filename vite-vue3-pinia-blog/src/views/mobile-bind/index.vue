// todo

<template>
  <div class="container">
    <div class="content-container animated fadeInUp">
      <h3>绑定手机号</h3>
      <el-form>
        <el-form-item>
          <el-input v-model="mobile" placeholder="输入手机号" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="code" placeholder="验证码">
            <span
              v-show="!codeCount"
              slot="suffix"
              class="code-btn btn"
              @click="sendCode"
              >获取验证码</span
            >
            <el-button
              v-show="codeCount"
              slot="suffix"
              type="primary"
              size="mini"
              disabled
              style="margin-top: 6px"
              >{{ codeCount }}s</el-button
            >
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
// 导入所需的组件和函数
import AppHeader from "@/components/AppHeader.vue";
import { defineProps, defineEmits, defineExpose } from "vue";
import { validMobile } from "/@/utils/validate";
import { sendCode } from "/@/api/sms/sms";

// 定义 props 和 emits
const props = defineProps({});
const emits = defineEmits([]);

// 定义响应式数据
const loading = ref(false);
const code = ref("");
const mobile = ref("");
const codeCount = ref(0);
let timer: NodeJS.Timeout | null = null;

// 提交校验
const vsubmit = () => {
  const mobileValue = mobile.value;
  if (mobileValue === "") {
    return showMessage("请输入手机号");
  }
  if (!validMobile(mobileValue)) {
    return showMessage("手机号格式不正确");
  }
  if (code.value === "") {
    return showMessage("请输入验证码");
  }
  return true;
};

// 发送验证码
const sendCode = () => {
  const mobileValue = mobile.value;
  if (mobileValue === "") {
    return showMessage("请输入手机号");
  }
  if (!validMobile(mobileValue)) {
    return showMessage("手机号格式不正确");
  }
  // 120倒数计时
  const TIME_COUNT = 120;
  if (!timer) {
    codeCount.value = TIME_COUNT;
    timer = setInterval(() => {
      if (codeCount.value > 0 && codeCount.value <= TIME_COUNT) {
        codeCount.value--;
      } else {
        clearInterval(timer as NodeJS.Timeout);
        timer = null;
      }
    }, 1000);
  }
  const params = { mobile: mobileValue };
  sendCode(params).then((res) => {
    showMessage("发送成功", "success");
  });
};

// 提交按钮点击
const submit = () => {
  if (vsubmit()) {
    loading.value = true;
    const params = { mobile: mobile.value, code: code.value };
    bindMobile(params).then(
      (res) => {
        loading.value = false;
        showMessage("绑定成功", "success");
        // dispatch action
        // redirect to info page
      },
      (error) => {
        console.error(error);
        loading.value = false;
      }
    );
  }
};

// 显示消息
const showMessage = (message: string, type: string = "error") => {
  // show message
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
