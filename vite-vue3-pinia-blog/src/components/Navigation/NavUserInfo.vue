<template>
  <div class="console">
    <router-link to="/personalcenter">个人中心</router-link>
  </div>
  <el-dropdown class="infoBox" size="large">
    <el-avatar :size="45" :src="props.userInfo.avatar" @error="errorHandler"/>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item @click="dialogFormVisible = true"
          >更换昵称
        </el-dropdown-item>
        <router-link v-if="!props.userInfo.mobile" to="/bind-mobile">
          <el-dropdown-item>绑定手机号</el-dropdown-item>
        </router-link>
        <router-link v-if="!props.userInfo.email" to="/email-validate">
          <el-dropdown-item>绑定邮箱</el-dropdown-item>
        </router-link>
        <el-dropdown-item>
          <span style="display: block" @click="logout"
            >退 出</span
          ></el-dropdown-item
        >
      </el-dropdown-menu>
    </template>
  </el-dropdown>
  <el-dialog v-model="dialogFormVisible" title="更换昵称" width="500">
    <el-form :model="form">
      <el-input
        v-model="form.newNickName"
        autocomplete="off"
        :maxlength="pinia.nickNameLength"
        placeholder="请输入新的昵称"
        show-word-limit
        type="text"
      />
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm"> 提交 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
// import { useStore } from "/@/store";
import { useRouter } from "vue-router";
import { getUpdateNickName, getUserInfo } from "/@/components/Login/services";
// import { removeUserAccountInfo  } from '/@/utils/network/auth.js'
import { useUserStore } from "/@/store/index";

const router = useRouter();

// pinia
const useUserPinia = useUserStore();

// 更改昵称对话框
const dialogFormVisible = ref(false);
const form = reactive({
  newNickName: "",
});

onMounted(async () => {});

// // 更改昵称
// const submitForm = async () => {
//   if (form.newNickName === "") {
//     alert("昵称不能为空");
//   } else {
//     const { data, run } = useRequest(getUpdateNickName, {
//       manual: true, // 手动触发请求
//       onSuccess: (data) => {
//         if (data.code === 200000) {
//           userInfo.value.nickname = form.newNickName;
//           router.go(0);
//         } else if (data.code === 400006) {
//           alert("昵称更新失败");
//         }
//       },
//       onError: (error) => {
//         alert(error);
//       },
//     });

//     run(userInfo.value.username, form.newNickName);
//   }
// };

interface userInfoType {
  // 自定义数据类型的属性
  id: number;
  username: string;
  password: string;
  mobile: number;
  nickname: string;
  gender: number;
  birthday: string;
  email?: string;
  brief?: string;
  avatar?: string;
  status: number;
  admin: number;
  createTime: string;
  roles: string[];
}

const props = defineProps<{
  userInfo: userInfoType;
}>();

const errorHandler = () => true

// 退出
const logout = async () => {
  try {
    await useUserPinia.logout();
    // router.push("/index");
  } catch (error) {
    ElMessage.error("退出失败");
  }
};
</script>

<style scoped lang="less">
.infoBox {
  margin-left: 10px;
  border: none;
}

.el-dropdown-link,
.console {
  display: flex;
  align-items: center;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-weight: bold;
  font-size: 15px;
  color: #fff;
  cursor: pointer;
  border: none;
}

.example-showcase {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
  border: none;
}

.el-dropdown-link:hover {
  border: none;
}

:deep(:focus-visible) {
  outline: none;
}

.console {
  padding: 0 8px;
  margin-right: 8px;
}
</style>
