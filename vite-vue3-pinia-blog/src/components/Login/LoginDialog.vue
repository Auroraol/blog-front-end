<!-- 登录弹框组件 -->
<template>
  <el-dialog v-model="dialogFormVisible"  
   top="25vh"
    width="318px"
    custom-class="login-dialog"
    :close-on-click-modal="false"
    :show-close="false"
    :visible="true"
    :lock-scroll="false"
 >
    <span>
      <span
        v-for="(item, index) in tabs"
        :key="index"
        class="btn tab"
        :class="{ active: active === index }"
        @click="tabClick(index)"
        >{{ item }}</span
      >
      <button
        type="button"
        aria-label="Close"
        class="el-dialog__headerbtn"
        @click="bClose"
      >
        <i class="el-dialog__close el-icon el-icon-close" />
      </button>
    </span>

    <el-input
      v-if="active === 0"
      v-model="username"
      placeholder="用户名或手机号"
    />
    <el-input v-else v-model="mobile" placeholder="手机号" />
    <el-input v-if="active === 0" v-model="password" placeholder="密码" />
    <el-input v-else v-model="code" placeholder="验证码">
      <span
        v-show="!codeCount"
        class="code-btn btn"
        @click="sendSmsCode"
        >获取验证码</span
      >
      <el-button
        v-show="codeCount"
        type="primary"
        size="mini"
        disabled
        style="margin-top: 6px"
        >{{ codeCount }}s</el-button
      >
    </el-input>
    <el-button type="primary" size="medium" :loading="loading" @click="login"
      >登录</el-button>

    <p class="tip">
      <el-checkbox v-if="active === 0" v-model="checked">记住密码</el-checkbox>
      <span
        class="active btn"
        :class="{ right: active === 0 }"
        @click="forgetClick"
        >忘记密码^_^?</span
      >
    </p>
    <p style="clear: both">
      注册登录即表示同意
      <span style="color: #007fff">
        <span class="btn" @click="terms">用户协议</span>
        <span class="btn" @click="privacy">隐私政策</span>
      </span>
    </p>
    <div class="third-login">
      <p class="name">社交账号登录</p>
      <div class="icon-box">
        <a
          href="https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101881036&redirect_uri=https://www.poile.cn/oauth&state=1"
        >
          <svg-icon icon-class="qq-login" class="icon" />
        </a>
        <a
          href="https://github.com/login/oauth/authorize?client_id=bded74b0f0213e6d1a85&scope=user:email&state=2"
        >
          <svg-icon icon-class="github-login" class="icon" />
        </a>
        <a
          href="https://gitee.com/oauth/authorize?client_id=18348ed893d47d047a79fb0a395fe1c8c481720021096bc5afe2a90a4e6ec557&redirect_uri=https%3A%2F%2Fwww.poile.cn%2Foauth&response_type=code&state=3"
        >
          <svg-icon icon-class="gitee-login" class="icon" />
        </a>
      </div>
    </div>
  <!-- </el-dialog> -->
  </el-dialog>
</template>

<script setup>
import { validMobile } from "/@/utils/validate.js";
import { setRemember, getRemember } from "/@/utils/auth.js";
import { sendCode } from "/@/api/sms.js";
// import { mapGetters } from 'vuex'
import { useAuthStore } from "/@/store";
const dialogFormVisible = ref(true)
const username = ref("");
const password = ref("");
const mobile = ref("");
const code = ref("");
const tabs = ["密码登录", "免密登录"];
const active = ref(0);
const checked = ref(false);
const loading = ref(false);
let codeCount = 0;
let timer = null;

const login_visible = computed(() => useAuthStore.visible);
const login_username = computed(() => useAuthStore.username);
const login_password = computed(() => useAuthStore.password);

const mounted = () => {
  username.value = login_username || "";
  password.value = login_password || "";
  checked.value = getRemember() === "1";
};

const terms = () => {
  login_visible.value = false;
  router.push("/terms");
};

const privacy = () => {
  login_visible.value = false;
  router.push("/privacy");
};

const bClose = () => {
  code.value = "";
  active.value = 0;
  login_visible.value = false;
};

const tabClick = (index) => {
  active.value = index;
};

const login = () => {
  $store.commit("user/SET_TOKEN", null);
  if (active.value === 0) {
    passwordLogin();
  } else {
    codeLogin();
  }
};

const passwordLogin = async () => {
  const username = username.value;
  const password = password.value;
  if (username === "") {
    $message("请输入用户名");
    return;
  }
  if (password === "") {
    $message("请输入密码");
    return;
  }
  const params = {
    username: username,
    password: password,
  };
  loading.value = true;
  try {
    await $store.dispatch("user/accountLogin", params);
    const { roles } = await $store.dispatch("user/getUserInfo");
    const accessRoutes = await $store.dispatch(
      "permission/generateRoutes",
      roles
    );
    $router.addRoutes(accessRoutes);
    const checked = checked.value;
    setRemember(checked ? "1" : "0");
    if (checked) {
      $store.dispatch("login/setUsernameAndPassword", params);
    } else {
      $store.dispatch("login/clearUsernameAndPassword");
    }
    loading.value = false;
    bClose();
  } catch (error) {
    loading.value = false;
    console.error(error);
  }
};

const codeLogin = async () => {
  const mobile = mobile.value;
  const codeValue = code.value;
  if (mobile === "") {
    $message("请输入手机号");
    return;
  }
  if (!validMobile(mobile)) {
    $message("手机号格式不正确");
    return;
  }
  if (codeValue === "") {
    $message("请输入验证码");
    return;
  }
  const params = {
    mobile: mobile,
    code: codeValue,
  };
  loading.value = true;
  try {
    await $store.dispatch("user/codeLogin", params);
    const { roles } = await $store.dispatch("user/getUserInfo");
    const accessRoutes = await $store.dispatch(
      "permission/generateRoutes",
      roles
    );
    $router.addRoutes(accessRoutes);
    loading.value = false;
    bClose();
  } catch (error) {
    loading.value = false;
    console.error(error);
  }
};

const sendSmsCode = () => {
  const mobileValue = mobile.value;
  if (mobileValue === "") {
    $message("请输入手机号");
    return;
  }
  if (!validMobile(mobileValue)) {
    $message("手机号格式不正确");
    return;
  }

  // 120倒数计时
  const TIME_COUNT = 120;
  if (!timer.value) {
    codeCount.value = TIME_COUNT;
    timer.value = setInterval(() => {
      if (codeCount.value > 0 && codeCount.value <= TIME_COUNT) {
        codeCount.value--;
      } else {
        clearInterval(timer.value);
        timer.value = null;
      }
    }, 1000);
  }

  const params = { mobile: mobileValue };
  sendCode(params).then((res) => {
    $message({
      message: "发送成功",
      type: "success",
    });
  });
};

const forgetClick = () => {
  login_visible.value = false;
  router.push("/reset-password");
};
</script>


<style lang="scss" scoped>
.login-dialog {
  font-size: 14px;

  .tab {
    padding-right: 10px;
    font-weight: bold;
  }

  .active {
    color: #007fff;
  }

  .el-input {
    margin-bottom: 10px;
  }

  .el-button {
    width: 100%;
  }

  .code-btn {
    color: #007fff;
    position: relative;
    top: 10px;
    right: 5px;
  }

  .tip {
    margin-bottom: 20px;
    padding: 0 2px;

    .right {
      float: right;
    }
  }

  .third-login {
    width: 100%;

    .name {
      color: #999;
      display: flex;
      align-items: center;
      font-size: 16px;
      margin: 23px 0;

      &:before {
        content: "";
        height: 1px;
        background: #999;
        flex: 1;
        margin-right: 10px;
        opacity: 0.8;
      }

      &:after {
        content: "";
        height: 1px;
        flex: 1;
        background: #999;
        margin-left: 10px;
        opacity: 0.8;
      }
    }

    .icon-box {
      display: flex;
      box-sizing: border-box;
      justify-content: center;
      margin-top: 10px;
      margin-bottom: 5px;

      .icon {
        height: 28px;
        width: 28px;
        margin: 0 15px;
      }
    }
  }
}
</style>
