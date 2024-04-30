<template>
  <div class="container">
    <div class="content-container animated fadeInUp">
      <h3>重置密码</h3>
      <el-form :model="form" :rules="formRules" ref="formRef">
        <el-form-item prop="mobile">
          <el-input
            size="large"
            v-model="form.mobile"
            placeholder="输入手机号"
          />
        </el-form-item>
        <el-form-item prop="code">
          <el-input
            size="large"
            v-model="form.code"
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
        <el-form-item prop="password">
          <el-input
            size="large"
            show-password
            v-model="form.password"
            placeholder="密码至少6位数"
          />

        </el-form-item>
        <el-form-item prop="password2">
          <el-input
            size="large"
             show-password
            v-model="form.password2"
             type="password"
            placeholder="确认密码"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            size="large"
            type="primary"
            style="width: 100%"
            :loading="loading"
            @click="submit"
            >确定</el-button
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

<script setup>
import { useSmsCodeMixin } from "/@/mixins/smsMixin";
import { useRouter } from "vue-router";
import { resetPassword } from "/@/api/user/user";
import { validMobile } from "/@/utils/validate";

    const router = useRouter();
// 在 setup 中引入 mixin
const { getCode, codeCount } = useSmsCodeMixin();

const form = reactive({
  mobile: "",
  code: "",
  password: "",
  password2: "",
});

const validatePassword = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error("两次密码不一致"));
  } else {
    callback();
  }
};

// 表单验证
const formRules = ref({
  mobile: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    {
      pattern: /^[1][3-9][0-9]{9}$/,
      message: "手机号格式不正确",
      trigger: "blur",
    },
  ],
  code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
  password: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, message: "密码至少6位数", trigger: "blur" },
  ],
  password2: [
    { required: true, message: "请确认密码", trigger: "blur" },
    { validator: validatePassword, trigger: "blur" },
  ],
});

// 发送验证码
const sendCode = () => {
  getCode(form.mobile);
};

//提交验证
const submitValidation = () => {
  const mobile = form.mobile;
  if (mobile === "") {
    console.log("请输入手机号");
    return false;
  }
  if (!validMobile(mobile)) {
    console.log("手机号格式不正确");
    return false;
  }
  const code = form.code;
  if (code === "") {
    console.log("请输入验证码");
    return false;
  }
  const password = form.password;
  if (password === "") {
    console.log("请输入新密码");
    return false;
  }
  if (password.length < 6) {
    console.log("密码不能少于6位数");
    return false;
  }
  if (password !== form.password2) {
    console.log("两次密码不一致");
    return false;
  }
  return true;
};

const loading = ref(false);
const submit = () => {
  if (submitValidation()) {
    loading.value = true;
    const params = {
      mobile: form.mobile,
      code: form.code,
      password: form.password,
    };
    resetPassword(params).then(
      (res) => {
        loading.value = false;
        ElMessage.success('重置成功');
        router.push('/index')
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
  @import "/@/assets/styles/variables.css";
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

    .el-form {
      width: 80%;
      margin: 0 auto;

      .code-btn {
        color: #007fff;
        position: relative;
        right: 10px;
      }
    }
  }
  .code {
    cursor: pointer; /* 将鼠标指针设置为手指形状 */
  }
}
</style>
