<template>
  <div class="login-form">
    <div class="form-header">
      <div>
        <span>登录可享更多权益</span>
      </div>
      <span class="sub-header">与专业的创作者进行 </span>
      <span class="sub-header introduce">深度的互动交流 </span>
      <span> </span>
    </div>

    <div class="form-fields">
      <el-tabs :stretch="true">
        <el-tab-pane label="账号密码登录" class="toLogin">
          <!-- loginForm: 表单数据对象-->
          <el-form :model="loginForm" :rules="loginFormRules">
            <el-form-item label="" prop="username">
              <el-input
                size="large"
                :prefix-icon="User"
                placeholder="用户名"
                v-model="loginForm.username"
                inline-message
              ></el-input>
            </el-form-item>
            <el-form-item label="" prop="password">
              <el-input
                size="large"
                placeholder="密码"
                show-password
                v-model="loginForm.password"
                inline-message
              ></el-input>
            </el-form-item>
            <div class="button-top">
              <div class="left-element">
                <el-checkbox v-model="checked" label="记住密码" />
              </div>
              <div class="right-element">
                <el-text @click="forgetClick" class="forget" type="primary"
                  >忘记密码</el-text
                >
              </div>
            </div>
            <el-form-item>
              <el-button
                size="large"
                type="primary"
                class="button"
                :loading="loading"
                @click="passwordLogin"
                :disabled="!isFormValid"
                :style="{
                  cursor: isFormValid ? 'pointer' : 'not-allowed',
                }"
              >
                登录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="手机号码登录" class="toLogin">
          <!-- loginForm: 表单数据对象-->
          <el-form :model="loginFormPhone" :rules="loginFormPhoneRules">
            <el-form-item label="" prop="mobile">
              <el-input
                placeholder="请输入手机号"
                v-model="loginFormPhone.mobile"
                inline-message
                size="large"
              >
                <template #prepend>
                  <span style="width: 0px; margin: 0 0 0 -25px">+86</span>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input
                size="large"
                v-model="loginFormPhone.code"
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
            <div style="height: 20px"></div>

            <el-form-item>
              <el-button
                size="large"
                type="primary"
                class="button"
                @click="codeLogin"
                :disabled="!isFormValidPhone"
                :style="{
                  cursor: isFormValidPhone ? 'pointer' : 'not-allowed',
                }"
                click="login2"
              >
                登录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div class="form-footer">
      <div class="text">
        <el-text>注册登录即表示同意 </el-text>
        <el-link @click="terms">用户协议</el-link>
        <el-text class="h">和</el-text>
        <el-link @click="privacy">隐私政策</el-link>
      </div>
      <!-- 下划线 -->
      <el-divider> 其他登录方式 </el-divider>
      <!-- <my-divider content-position="center" label="其他登录方式" /> -->
      <div class="login-third-items">
        <a href="">
          <svg-icon
            name="qq-login"
            color="green"
            width="24"
            height="24"
          ></svg-icon>
        </a>
        <a href="">
          <svg-icon name="github-login" width="24" height="24" />
        </a>
        <a
          href="https://gitee.com/oauth/authorize?client_id=62d3af1f2058b5facec5316ab9d18b8d3602406fcb704adde7f5b38de381996b&redirect_uri=http%3A%2F%2Flocalhost%3A9000%2Foauth&response_type=code&state=3"
        >
          <svg-icon name="gitee-login" width="24" height="24" />
        </a>
        <!-- 其他图标同理 -->
        <!-- <svg-icon name="qq-login" width="24" height="24"></svg-icon>
          <svg-icon name="gitee-login"  /> -->
      </div>
    </div>
  </div>
  <!-- <login-dialog></login-dialog> -->
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useUserStore, useLoginStore } from "/@/store/index";
import { setRemember, getRemember } from "/@/utils/auth";

import { useSmsCodeMixin } from "/@/mixins/smsMixin";
import { useRouterMixin } from "/@/mixins/routerMixin";

// 在 setup 中引入 mixin
const { getCode, codeCount } = useSmsCodeMixin();
const { toPage } = useRouterMixin();

// pinia
const useUserPinia = useUserStore();
const useLoginPinia = useLoginStore();

//
const loading = ref(false);
const checked = ref(false);

// 表单数据
const loginForm = reactive({
  username: "",
  password: "",
});

const loginFormRules = ref({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
});

const loginFormPhone = reactive({
  mobile: "",
  code: "",
});

const loginFormPhoneRules = ref({
  mobile: [{ required: true, message: "请输入手机号", trigger: "blur" }],
  code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
});

// 初始化
onMounted(() => {
  //从pinia中获得保存的状态
  loginForm.username = useLoginPinia.username || "";
  loginForm.password = useLoginPinia.password || "";
  checked.value = getRemember() === "1";
});

// 忘记密码点击
const forgetClick = () => {
  useLoginPinia.changeVisible(false);
  toPage("/reset-password");
};

// 发送验证码
const sendCode = () => {
  getCode(loginFormPhone.mobile);
};

// 点击密码登录按钮
const passwordLogin = async () => {
  useUserPinia.SET_TOKEN(null); // 用于在用户登出或者 token 失效时清除用户的身份认证信息。
  loading.value = true;
  const params = toRaw(loginForm);
  try {
    await useUserPinia.accountLogin(params);
    // TODO获得用户信息, 确定roles, 实现动态路由
    let { roles } = await useUserPinia.getUserInfo();
    console.error(roles);

    //  const accessRoutes = await this.$store.dispatch('permission/generateRoutes', roles)
    // this.$router.addRoutes(accessRoutes)
    // 是否记住密码
    let checkedValue = checked.value;
    setRemember(checkedValue ? "1" : "0"); // 浏览器
    if (checkedValue) {
      useLoginPinia.setUsernameAndPassword(params);
    } else {
      useLoginPinia.clearUsernameAndPassword();
    }

    loading.value = false;
    ElMessage.success("登录成功");
    // 跳转到首页
    toPage("/index");
  } catch (error) {
    loading.value = false;
    console.error(error);
  }
};

// 点击验证码登录按钮
const codeLogin = async () => {
  useUserPinia.SET_TOKEN(null); // 用于在用户登出或者 token 失效时清除用户的身份认证信息。
  loading.value = true;
  const params = toRaw(loginFormPhone);
  try {
    await useUserPinia.codeLogin(params);
    // TODO获得用户信息, 确定roles, 实现动态路由
    let { roles } = await useUserPinia.getUserInfo();

    loading.value = false;
    // 跳转到首页
    ElMessage.success("登录成功");
    toPage("/index");
  } catch (error) {
    loading.value = false;
    console.error(error);
  }
};

// 按钮使能
const isFormValid = computed(() => {
  return loginForm.username && loginForm.password;
});
const isFormValidPhone = computed(() => {
  return loginFormPhone.mobile && loginFormPhone.code;
});

// 路由跳转
const terms = () => {
  toPage("/terms");
};

const privacy = () => {
  toPage("/privacy");
};
</script>

<style lang="less" scoped>
.login-form {
  text-align: center;
  width: 410px;
  height: 420px;
  // height: 534px;

  background: white;
  margin-top: 50px;
  /* border: .1rem solid white; */
  box-sizing: border-box;

  .form-header {
    padding-top: 15px;
    height: 22px;
    font-size: 16px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #222226;
    line-height: 1.5;

    .introduce {
      color: #fc5531;
    }
  }
}

.form-fields {
  padding: 50px 24px 0;
}

.form-footer {
  margin-top: -15px;
}

.button {
  width: 362px;
  border-radius: 20px;
  font-size: 16px;
}

.button-top {
  display: flex;
  align-items: center; /* 垂直居中 */
  margin-top: -10px;
  display: flex;

  .left-element {
    margin-right: 10px; /* 可以调整间距 */
  }

  .right-element {
    margin-left: auto; /* 推到容器的右侧 */
  }
}

.login-third-items {
  display: flex;
  align-items: center; /* 垂直居中 */
  a {
    margin-left: 85px;
  }
}
.text {
  margin-top: 10px;
  font-size: 14px;
  display: flex;
  justify-content: center;

  .el-text {
    color: #222226;
    margin-right: 5px;
  }
  .el-link__inner {
    color: #222226;
  }
  .h {
    font-size: 14px;
    margin-left: 5px;
    margin-right: 5px;
  }
}

.forget {
  cursor: pointer; /* 将鼠标指针设置为手指形状 */
}

.code {
  cursor: pointer; /* 将鼠标指针设置为手指形状 */
}
</style>