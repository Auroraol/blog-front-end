<template>
  <el-row class="content">
    <!--el-col 列： -->
    <el-col :span="16" :xs="0" class="content-left"> </el-col>
    <el-col :span="8" :xs="24" class="content-right">
      <div class="loginContent">
        <div class="loginContentTop">
          <div>
            <span>登录可享更多权益</span>
          </div>
          <span class="fontSize">与专业的创作者进行 </span>
          <span class="introduce">深度的互动交流 </span>
          <span> </span>
        </div>

        <div class="loginContentForm">
          <div class="loginMethods">
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
                      :prefix-icon="Lock"
                      placeholder="密码"
                      show-password
                      v-model="loginForm.password"
                      inline-message
                    ></el-input>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="手机号码登录" class="toLogin">
                <!-- loginForm: 表单数据对象-->
                <el-form
                  :model="loginFormPhone"
                  :rules="loginFormPhoneRules"
                  prop="phone"
                >
                  <el-form-item label="">
                    <el-input
                      placeholder="请输入手机号"
                      v-model="loginFormPhone.phone"
                      inline-message
                      size="large"
                    >
                      <template #prepend>
                        <span style="width: 0px; margin: 0 0 0 -25px">+86</span>
                      </template>
                    </el-input>
                  </el-form-item>
                  <el-form-item label="" prop="code">
                    <el-input
                      placeholder="请输入验证码"
                      v-model="loginFormPhone.code"
                      inline-message
                      size="large"
                    >
                      <template #suffix>
                        <i style="font-style: normal; margin-right: 10px"
                          >获取验证码</i
                        >
                      </template>
                    </el-input>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>

        <div class="loginContentButton">
          <div class="buttonTop">
            <div class="left-element">
              <el-checkbox v-model="checked" label="记住密码" />
            </div>
            <div class="right-element">
              <el-text class="mx-1" type="primary">忘记密码</el-text>
            </div>
          </div>

          <el-button
            size="large"
            type="primary"
            class="button"
            @click="tologin"
          >
            登录
          </el-button>
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
    </el-col>
  </el-row>
  <!-- <login-dialog></login-dialog> -->
</template>

<script setup lang="ts">
import { ref } from "vue";

const loginForm = ref({
  username: "",
  password: "",
});

const loginFormRules = ref({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
});

const loginFormPhone = ref({
  phone: "",
  code: "",
});

const loginFormPhoneRules = ref({
  phone: [{ required: true, message: "请输入手机号", trigger: "blur" }],
  code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
});

const checked = ref(false);

const toOption = (index) => {
  // Your logic for switching between login options
};
</script>

<style lang="less" scoped>
.loginContent {
  text-align: center;
  width: 410px;
  height: 420px;
  // height: 534px;

  background: white;
  margin-top: 50px;
  /* border: .1rem solid white; */
  box-sizing: border-box;

  .loginContentTop {
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

.loginContentForm {
  padding: 50px 24px 0;
}
.content {
  height: 644px;
  /* background-image: "/image/imgs/1.png" */
  background-image: url("/image/imgs/1.png");
  margin-bottom: -13px;
}

.loginContentButton {
  padding: 0px 24px 0;
}
.button {
  width: 362px;
  border-radius: 20px;
  font-size: 16px;
}

.buttonTop {
  display: flex;
  align-items: center; /* 垂直居中 */

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
    margin-left: 73px;
  }
}
</style>