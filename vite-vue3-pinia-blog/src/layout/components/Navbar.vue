<template>
  <div class="navbar">
    <hamburger
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img :src="userInfo.avatar || defaultAvatar" class="user-avatar" />
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <router-link to="/">
            <el-dropdown-item> 首页 </el-dropdown-item>
          </router-link>
          <span class="btn" @click="visible = true">
            <el-dropdown-item>修改密码</el-dropdown-item>
          </span>
          <el-dropdown-item divided>
            <span style="display: block" @click="logout">退 出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog
      title="修改密码"
      top="30vh"
      width="320px"
      :modal="true"
      :visible.sync="visible"
      :modal-append-to-body="false"
      :close-on-click-modal="false"
      :show-close="false"
      :lock-scroll="false"
    >
      <el-form ref="form" label-position="left" :model="form" :rules="rules">
        <el-form-item prop="oldpwd">
          <el-input v-model="form.oldpwd" placeholder="输入原密码" />
        </el-form-item>
        <el-form-item prop="newpwd">
          <el-input v-model="form.newpwd" placeholder="输入新密码" />
        </el-form-item>
        <el-form-item prop="newpwd2">
          <el-input v-model="form.newpwd2" placeholder="确认密码" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="medium" @click="dialogClose">取 消</el-button>
        <el-button
          size="medium"
          type="primary"
          :loading="btnLoading"
          @click="saveSubmit"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
// import { mapGetters } from "vuex";
import { ref } from "vue";
import Breadcrumb from "/@/components/Breadcrumb.vue";
import Hamburger from "/@/components/Hamburger.vue";
import { updatePassword } from "/@/api/user/user";

const validatePass2 = (rule, value, callback) => {
  if (value !== form.newpwd) {
    callback(new Error("两次输入密码不一致"));
  } else {
    callback();
  }
};

const visible = ref(false);
const btnLoading = ref(false);
const form = ref({
  oldpwd: "",
  newpwd: "",
  newpwd2: "",
});
const rules = ref({
  oldpwd: [{ required: true, message: "请输入原密码", trigger: "blur" }],
  newpwd: [{ required: true, message: "请输入新密码", trigger: "blur" }],
  newpwd2: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { validator: validatePass2, trigger: "blur" },
  ],
});

// const { sidebar, userInfo, defaultAvatar } = mapGetters([
//   "sidebar",
//   "userInfo",
//   "defaultAvatar",
// ]);

const toggleSideBar = () => {
  $store.dispatch("app/toggleSideBar");
};

const dialogClose = () => {
  form.value = {
    oldpwd: "",
    newpwd: "",
    newpwd2: "",
  };
  this.$refs["form"].resetFields();
  this.$refs["form"].clearValidate();
  visible.value = false;
};

const saveSubmit = () => {
  $refs.form.validate((valid) => {
    if (valid) {
      btnLoading.value = true;
      const params = {
        oldPassword: form.value.oldpwd,
        newPassword: form.value.newpwd2,
      };
      updatePassword(params).then(
        (res) => {
          btnLoading.value = false;
          this.$message({
            message: "修改成功",
            type: "success",
          });
          form.value = {
            oldpwd: "",
            newpwd: "",
            newpwd2: "",
          };
          this.$refs["form"].resetFields();
          this.$refs["form"].clearValidate();
          visible.value = false;
        },
        (error) => {
          console.error(error);
          btnLoading.value = false;
        }
      );
    }
  });
};

const logout = () => {
  $store.dispatch("user/logout").then((res) => {
    $router.push("/");
  });
};
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;
        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 50%;
          border: 1px solid rgba(0, 0, 0, 0.1);
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
