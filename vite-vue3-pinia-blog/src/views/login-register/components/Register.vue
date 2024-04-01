<template>
  <div class="register-form">
    <div class="form-header">
      <div>
        <span>注册新账号</span>
      </div>
      <span class="sub-header">加入我们，体验更多功能 </span>
      <span class="sub-header introduce">与创作者共同成长 </span>
      <span> </span>
    </div>
    <div class="form-fields">
      <!-- <div class="form-fields">
      <el-input
        size="large"
        v-model="registerForm.username"
        placeholder="用户名字母开头, 允许2-16字节"
      />
      <el-input
        size="large"
        v-model="registerForm.mobile"
        placeholder="手机号用于登录和找回密码"
      />
      <el-input
        size="large"
        v-model="registerForm.code"
        placeholder="请输入验证码"
        inline-message
      >
        <template #suffix>
          <i
            v-show="!codeCount"
            class="code"
            style="font-style: normal; margin-right: 10px"
            @click="getCode"
            >获取验证码</i
          >
          <el-text v-show="codeCount" type="primary">{{ codeCount }}s</el-text>
        </template>
      </el-input>
      <el-input
        size="large"
        v-model="registerForm.password"
        placeholder="密码不能少于6位数"
      />
    </div> -->

      <el-form :model="registerForm" :rules="formRules">
        <el-form-item prop="username">
          <el-input
            size="large"
            v-model="registerForm.username"
            placeholder="用户名字母开头，允许2-16字节"
          ></el-input>
        </el-form-item>
        <el-form-item prop="mobile">
          <el-input
            size="large"
            v-model="registerForm.mobile"
            placeholder="手机号用于登录和找回密码"
          ></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input
            size="large"
            v-model="registerForm.code"
            placeholder="请输入验证码"
            inline-message
          >
            <template #suffix>
              <i
                v-show="!codeCount"
                class="code"
                style="font-style: normal; margin-right: 10px"
                @click="getCode"
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
            v-model="registerForm.password"
            placeholder="密码不能少于6位数"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            style="margin-top: 10px"
            size="large"
            type="primary"
            class="button"
            @click="submit"
            :disabled="!isFormValid"
            :style="{ cursor: isFormValid ? 'pointer' : 'not-allowed' }"
            :loading="loading"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="form-footer">
      <div class="text">
        <el-text>注册登录即表示同意 </el-text>
        <el-link @click="terms">用户协议</el-link>
        <el-text class="h">和</el-text>
        <el-link @click="privacy">隐私政策</el-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { validMobile } from "/@/utils/validate.js";
import { sendCode } from "/@/api/sms/sms";
import { register } from "/@/api/user/user"
import { useUserStore } from "/@/store/index"

const router = useRouter();
const toPage = (url) => {
  router.push(url); // 字符串形式跳转  //路由地址
};

const useUserPinia = useUserStore();

const loading = ref(false);

const registerForm = reactive({
  username: "",
  mobile: "",
  code: "",
  password: "",
});

const formRules = ref({
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    {
      pattern: /^[a-zA-Z][a-zA-Z0-9_]{1,15}$/,
      message: "用户名格式不正确",
      trigger: "blur",
    },
  ],
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
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, message: "密码不能少于6位数", trigger: "blur" },
  ],
});

const codeCount = ref(0);
const timer = ref(0);

// const clearRegisterForm = ()=>{
//    registerForm
// }

// 按钮使能
const isFormValid = computed(() => {
  return (
    registerForm.username &&
    registerForm.mobile &&
    registerForm.code &&
    registerForm.password.length >= 6
  );
});

// 发送验证码
const getCode = () => {
  const mobile = registerForm.mobile;
  if (mobile === "") {
    ElMessage.warning("请输入手机号");
    return;
  }
  if (!validMobile(mobile)) {
    ElMessage.error("手机号格式不正确");
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
        timer.value = 0;
      }
    }, 1000);
  }

  // 发送短信api
  const params = { mobile: registerForm.mobile };
  sendCode(params)
    .then((value) => {
      //ElMessage.success('发送成功');
    })
    .catch((error) => {
      // console.error("发送失败:", error);
      ElMessage.error("发送失败");
    });
};

// // 点击注册按钮
// const submit = () => {
//   const registerInfo = toRaw(registerForm); // 将一个由生成的响应式转化为对象普通对象
//   loading.value = true; //按钮显示加载
//   register(registerInfo).then(
//     // 响应成功
//     async (res) => {
//       const loginParams = {
//         username: registerForm.username,
//         password: registerForm.password,
//       };
//       // 注册成功, 发起请求登录请求获得token
//       try {
//         await useUserPinia.accountLogin(loginParams);
//          await useUserPinia.getUserInfo();
//         //  TODO const accessRoutes = await this.$store.dispatch('permission/generateRoutes', roles)
//         // 登录成功
//         ElMessage.success("注册成功");
//         loading.value = false;
//         // 跳转到首页
//         toPage("/index");
//       } catch (error) {
//         loading.value = false;
//         // 登录失败
//         console.error("登录失败", error);
//       }
//     },
//     (error) => {
//       // 注册失败
//       loading.value = false;
//       console.error(error);
//     }
//   );
// };

const submit = async () => {
  const registerInfo = toRaw(registerForm);
  loading.value = true;

  try {
    await register(registerInfo);
    // 注册成功, 发起请求登录请求获得token
    const loginParams = {
      username: registerForm.username,
      password: registerForm.password,
    };
    // 获得用户信息, 确定roles, 实现动态路由
    await useUserPinia.accountLogin(loginParams);
    let {roles} = await useUserPinia.getUserInfo();//
    console.log(roles);
    
    //  TODO const accessRoutes = await this.$store.dispatch('permission/generateRoutes', roles)

    ElMessage.success("注册成功");
    loading.value = false;
    // 跳转到登录页
    toPage("/login-register/login");
  } catch (error) {
    // 统一处理所有错误
    loading.value = false;
    console.error("错误原因：", error);
  }
};

// 路由跳转
const terms = () => {
  toPage("/terms");
};

const privacy = () => {
  toPage("/privacy");
};
</script>

<style lang="less" scoped>
.register-form {
  text-align: center;
  width: 410px;
  height: 420px;
  background: white;
  margin-top: 50px;
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
  .el-form-item {
    margin-bottom: 8px;
  }
}

.form-footer {
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

.el-input--large {
  margin-top: 10px;
}

.code {
  cursor: pointer; /* 将鼠标指针设置为手指形状 */
}
.form-footer {
  margin-top: 15px;

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
}
</style>