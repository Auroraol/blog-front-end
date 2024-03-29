<template>
  <el-row class="content">
    <el-col :span="16" :xs="0" class="content-left"> </el-col>
    <el-col :span="8" :xs="24" class="content-right">
      <div class="registerContent">
        <div class="registerContentTop">
          <div>
            <span>注册新账号</span>
          </div>
          <span class="fontSize">加入我们，体验更多功能 </span>
          <span class="introduce">与创作者共同成长 </span>
          <span> </span>
        </div>

        <div class="registerContentForm">
          <el-form :model="registerForm" :rules="registerFormRules">
            <el-form-item label="" prop="username">
              <el-input
                size="large"
                :prefix-icon="User"
                placeholder="用户名"
                v-model="registerForm.username"
                inline-message
              ></el-input>
            </el-form-item>
            <el-form-item label="" prop="email">
              <el-input
                size="large"
                :prefix-icon="Email"
                placeholder="邮箱"
                v-model="registerForm.email"
                inline-message
              ></el-input>
            </el-form-item>
            <el-form-item label="" prop="password">
              <el-input
                size="large"
                :prefix-icon="Lock"
                placeholder="密码"
                show-password
                v-model="registerForm.password"
                inline-message
              ></el-input>
            </el-form-item>
          </el-form>
        </div>

        <div class="registerContentButton">
          <el-button
            size="large"
            type="primary"
            class="button"
            @click="register"
          >
            注册
          </el-button>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { ref } from "vue";

const registerForm = ref({
  username: "",
  email: "",
  password: "",
});

const registerFormRules = ref({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "请输入有效的邮箱地址", trigger: ["blur", "change"] },
  ],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
});

const register = () => {
  // Your registration logic here
};
</script>

<style lang="less" scoped>
.content {
  height: 644px;
  /* background-image: "/image/imgs/1.png" */
  background-image: url("/image/imgs/1.png");
  margin-bottom: -13px;
}

.registerContent {
  text-align: center;
  width: 410px;
  height: 420px;
  background: white;
  margin-top: 50px;
  box-sizing: border-box;

  .registerContentTop {
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

.registerContentForm {
  padding: 50px 24px 0;
}

.registerContentButton {
  padding: 0px 24px 0;
}

.button {
  width: 362px;
  border-radius: 20px;
  font-size: 16px;
}

.register-third-items {
  display: flex;
  align-items: center;
  a {
    margin-left: 73px;
  }
}
</style>
