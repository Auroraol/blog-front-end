<template>
  <div class="navbar">
    <!-- 菜单收缩展开按钮 -->
    <hamburger
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />
    <!-- 后台管理面包屑 -->
    <breadcrumb class="breadcrumb-container" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { updatePassword } from "/@/api/user/user";
import { useAppStore, useUserStore, useSettingsStore } from "/@/store/index";

// const router = useRouter();

// pinia
// const useUserPinia = useUserStore();
// const useSettingsStorePinia = useSettingsStore();

// const validatePass2 = (rule, value, callback) => {
//   if (value !== form.newpwd) {
//     callback(new Error("两次输入密码不一致"));
//   } else {
//     callback();
//   }
// };

// const visible = ref(false);
// const btnLoading = ref(false);
// const form = ref({
//   oldpwd: "",
//   newpwd: "",
//   newpwd2: "",
// });
// const rules = ref({
//   oldpwd: [{ required: true, message: "请输入原密码", trigger: "blur" }],
//   newpwd: [{ required: true, message: "请输入新密码", trigger: "blur" }],
//   newpwd2: [
//     { required: true, message: "请输入新密码", trigger: "blur" },
//     { validator: validatePass2, trigger: "blur" },
//   ],
// });

const useAppStorePinia = useAppStore();
const sidebar = computed(() => useAppStorePinia.sidebar);

const toggleSideBar = () => {
  // $store.dispatch("app/toggleSideBar");
};

// const saveSubmit = () => {
//   $refs.form.validate((valid) => {
//     if (valid) {
//       btnLoading.value = true;
//       const params = {
//         oldPassword: form.value.oldpwd,
//         newPassword: form.value.newpwd2,
//       };
//       updatePassword(params).then(
//         (res) => {
//           btnLoading.value = false;
//           this.$message({
//             message: "修改成功",
//             type: "success",
//           });
//           form.value = {
//             oldpwd: "",
//             newpwd: "",
//             newpwd2: "",
//           };
//           this.$refs["form"].resetFields();
//           this.$refs["form"].clearValidate();
//           visible.value = false;
//         },
//         (error) => {
//           console.error(error);
//           btnLoading.value = false;
//         }
//       );
//     }
//   });
// };

// const logout = () => {
//   $store.dispatch("user/logout").then((res) => {
//     $router.push("/");
//   });
// };
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
