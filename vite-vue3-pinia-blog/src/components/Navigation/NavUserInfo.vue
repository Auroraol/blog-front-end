<template>
  <div class="console">
    <router-link to="/user">个人中心</router-link>
  </div>
  <el-dropdown class="infoBox" size="large">
    <el-avatar
      style="cursor: pointer"
      :size="45"
      :src="props.userInfo.avatar || defaultAvatar"
    />
    <template #dropdown>
      <el-dropdown-menu class="content">
        <router-link v-if="!props.userInfo.mobile" to="/bind-mobile">
          <el-dropdown-item>绑定手机号</el-dropdown-item>
        </router-link>
        <router-link v-if="!props.userInfo.email" to="/bind-email">
          <el-dropdown-item>绑定邮箱</el-dropdown-item>
        </router-link>
        <el-dropdown-item @click="logout">
          <span style="display: block">退 出</span>
        </el-dropdown-item>
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
import { useRouter, useRoute } from "vue-router";
import { useUserStore, useSettingsStore } from "/@/store/index";

const router = useRouter();
const route = useRoute();

// pinia
const useUserPinia = useUserStore();
const useSettingsStorePinia = useSettingsStore();

const defaultAvatar = computed(() => useSettingsStorePinia.defaultAvatar);

// 更改昵称对话框
const dialogFormVisible = ref(false);
const form = reactive({
  newNickName: "",
});

onMounted(async () => {
  // console.error(props.userInfo.avatar);
  // console.error(defaultAvatar.value);
});

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

// 退出
const logout = async () => {
  try {
    await useUserPinia.logout();
    sessionStorage.setItem("articleDraft", ""); // 清空草稿
    location.assign("/");
  } catch (error) {
    console.error(error);
    ElMessage.error("退出失败");
  }
};
</script>

<style scoped lang="less">
// css
.el-dropdown {
  positive: relative;
  .el-dropdown__popper {
    position: absolute !important; // 这句可能会因为组件bug丢失，没有的话就自己加下
    top: -20px; // top和left的值可以根据自己的页面效果去做调整
    left: 20px;
  }
}

.el-dropdown-link,
.console {
  display: flex;
  align-items: center;
  // font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  //   "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-family: "LXGW Wenkai";
  font-weight: bold;
  font-size: 15px;
  color: #fff;
  border: none;
  padding: 0 8px;
  margin-right: 8px;

  @media screen and (max-width: 960px) {
    margin: 0px;
    padding: 0px;
  }
}

.infoBox {
  margin-left: 10px;
  .content {
    color: #fff;
    font-weight: normal;
  }
}

.example-showcase {
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
</style>
