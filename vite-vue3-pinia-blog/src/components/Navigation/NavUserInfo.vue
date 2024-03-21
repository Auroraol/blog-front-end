<template>
  <el-avatar @click="goPersonalCenter" :size="45" :src="userInfo.avatarimgurl" />
  <el-dropdown class="infoBox" @command="handleCommand"  size="large">
    <span class="el-dropdown-link">
      {{ userInfo.nickname }}
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item @click="dialogFormVisible = true" command="a"
          >更换昵称
        </el-dropdown-item>
        <el-dropdown-item command="b"><el-icon><Avatar /></el-icon>个人中心</el-dropdown-item>
        <el-dropdown-item command="c"><el-icon><CircleCloseFilled /></el-icon>退出登录</el-dropdown-item>
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
import { useStore } from "/@/store";
import { useRouter } from "vue-router";
import { UserType } from "/@/typings";
import { getUpdateNickName,getUserInfo,  } from "/@/components/Login/services";
import { removeUserAccountInfo  } from '/@/utils/network/auth.js'


const router = useRouter();
const pinia = useStore();

// 更改昵称对话框
const dialogFormVisible = ref(false);
const form = reactive({
  newNickName: "",
});

const userInfo = ref<UserType>({
  id: -1,
  phone: "",
  username: "account",
  password: "password",
  gender: "",
  nickname: "",
  birthday: "",
  email: "",
  personalbrief: "",
  avatarimgurl: "",
  recentlylanded: "",
}); //给一个初始值作为key，当请求到数据时更新这个变量，key也就随之更新，就会更新dom

onMounted(async () => {
  // 重写发起URL请求
  getInformation()
});


const getInformation = () => {
  const { data: responseData, run: infoRun } = useRequest(getUserInfo, {
    onSuccess: (responseData) => {
      if (responseData && responseData.data) {
        userInfo.value = responseData.data;
      } else {
        console.error("未获取到有效的用户信息数据");
      }
    },
  });

  infoRun();
};



const handleCommand = (command: string | number | object) => {
  switch (command) {
    // 更改昵称
    case "a":
      break;
    // 个人中心
    case "b":
      router.push("/personalcenter");
      break;
    // 退出登录
    case "c":
      // localStorage.removeItem("userAccount");
      removeUserAccountInfo()
      pinia.userInfo = "";
      // alert('注销成功')
      router.go(0);
      break;
    default:
      break;
  }
};

// 更改昵称
const submitForm = async () => {
  if (form.newNickName === "") {
    alert("昵称不能为空");
  } else {
    const { data, run } = useRequest(getUpdateNickName, {
      manual: true, // 手动触发请求
      onSuccess: (data) => {
        if (data.code === 200000) {
          userInfo.value.nickname = form.newNickName
          router.go(0);
        } else if (data.code === 400006) {
          alert("昵称更新失败");
        } 
      },
      onError: (error) => {
        alert(error);
      },
    });

    run(userInfo.value.username, form.newNickName);
  }
};

// 点击头像进入个人中心
const goPersonalCenter = () => {
  router.push("/personalcenter");
};

</script>

<style scoped lang="less">
.infoBox{
  margin-left: 10px;
    border: none; 
}

.el-dropdown-link{
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

.el-dropdown-link:hover  {
    border: none; 
}

:deep(:focus-visible) {

 outline: none;

}

</style>