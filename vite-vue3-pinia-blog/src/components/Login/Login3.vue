
<template>
  <div class="login_container">
    <div class="login_logo">
      <img />
    </div>
    <div class="login_box">
      <!-- 登录表单区域 -->
      <el-tabs :stretch="true">
        <el-tab-pane label="账号密码登录">
          <!-- 账号密码登录表单 -->
          <el-form ref="pwdLoginFormRef" >
            <!-- 用户名 -->
            <el-form-item prop="username">
              <el-input placeholder="用户名/邮箱/手机号" clearable prefix-icon="el-icon-user-solid">
              </el-input>
            </el-form-item>
            <!-- 密码 -->
            <el-form-item prop="password">
              <el-input placeholder="密码" type="password" show-password prefix-icon="el-icon-lock"
                >
              </el-input>
            </el-form-item>
            <!-- 按钮区域 -->
            <el-form-item class="login_btns">
              <el-button type="primary"  :loading="loading">登录</el-button>
              <el-button type="primary">注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="邮箱验证登录">
          <!-- 邮箱验证登录表单 -->
          <el-form ref="emailLoginFormRef" >
            <!-- 邮箱 -->
            <el-form-item prop="email">
              <el-input placeholder="邮箱" clearable prefix-icon="el-icon-message" ></el-input>
            </el-form-item>
            <!-- 邮箱验证码 -->
            <el-form-item prop="emailCode">
              <el-input placeholder="验证码" prefix-icon="el-icon-key" >
                <template #append>
                  <el-button :disabled="disabled" >
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
            <!-- 按钮区域 -->
            <el-form-item class="login_btns">
              <el-button type="primary" >登录</el-button>
              <el-button type="primary">注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <!-- <el-tab-pane label="手机验证登录"> -->
        <!-- 手机验证登录表单 -->
        <!-- <el-form ref="phoneLoginFormRef" :model="phoneLoginForm" :rules="phoneLoginFormRules"> -->
        <!-- 手机号 -->
        <!-- <el-form-item prop="phone">
              <el-input placeholder="手机号" clearable prefix-icon="el-icon-phone" v-model="phoneLoginForm.phone">
              </el-input>
            </el-form-item> -->
        <!-- 手机验证码 -->
        <!-- <el-form-item prop="phoneCode">
              <el-input placeholder="验证码" prefix-icon="el-icon-key" v-model="phoneLoginForm.phoneCode">
                <template #append>
                  <el-button>获取验证码</el-button>
                </template>
              </el-input>
            </el-form-item> -->
        <!-- 按钮区域 -->
        <!-- <el-form-item class="login_btns">
              <el-button type="primary" @click="phoneLogin">登录</el-button>
              <el-button type="success" @click='toRegister'>注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane> -->
      </el-tabs>
    </div>
    <div class="login_footer">
      登录即表示您已阅读并同意
      <a href="#">服务条款</a>
    </div>
  </div>
</template>

<script>

</script>

<style lang="less" scoped>
  .login_container {
    width: 100%;
    height: 100%;
    background: url(../assets/登录页面背景图.png);
    position: fixed;
    background-size: 100% 100%;
  }

  .login_box {
    width: 395px;
    height: 435px;
    background-color: rgba(225, 225, 225, 0);
    border: 1px solid rgba(225, 225, 225, 0);
    box-shadow: 2px 3px 3px #aaaaaa;
    border-radius: 5px;
    position: relative;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
  }

  .login_btns {
    text-align: center;
  }
  
  .login_footer { 
    position: absolute;
    left: 50%;
    top: 70%;
    transform: translate(-50%, -50%);
    border: 1px solid #ffffff;
    width: 395px;
    height: 30px;
    text-align: center;
  }
</style>

<style scoped>
  .login_box {
    backdrop-filter: blur(15px);
    box-shadow: 0 0 5px #fff;
  }
  
  .el-tabs >>> .el-tabs__header { 
    padding: 5% 10% 0 10%;
  }
  
  .el-tabs >>> .el-tabs__item:hover {
      color: #fff;
      cursor: pointer;
  }
  
  .el-tabs >>> .el-tabs__item.is-active {
      color: #c915e5;
  }

  .el-form {
    text-align: center;
  }

  .el-form-item>>>.el-form-item__error {
    left: 10%;
  }

  .el-input {
    width: 80%;
  }
  
  .el-input { 
    --el-input-font-color: black;
  }

  .el-input>>>.el-input__inner {
    background-color: rgba(225, 225, 225, 0);
    box-shadow: 0 0 2px #fff;
    border: 1px solid #fff;
  }

  .el-button {
    border-radius: 10px;
  }

  .el-button--primary {
    --el-button-font-color: #409e40;
    --el-button-background-color: #ffffff;
    --el-button-border-color: #409eff;
    --el-button-hover-color: #66b1ff;
    --el-button-active-font-color: #e6e6e6;
    --el-button-active-background-color: #0d84ff;
    --el-button-active-border-color: #0d84ff;
  }
</style>

