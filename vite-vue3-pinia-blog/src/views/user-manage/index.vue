<template>
  <div class="app-container">
    <div
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="#fff"
      class="card-container"
    >
      <el-card
        v-for="(item, index) in userList"
        :key="index"
        :body-style="{ padding: '0px' }"
        shadow="hover"
        class="card-item"
      >
        <img :src="item.avatar || defaultAvatar" class="avatar" />
        <div class="content-box">
          <div class="row">昵称:&ensp;{{ item.nickname }}</div>
          <div class="row">
            用户名:&ensp;{{ item.username
            }}<span v-if="!item.username" style="color: #999">未绑定</span>
          </div>
          <div class="row">
            手机号:&ensp;{{ sensitiveMobile(item.mobile)
            }}<span v-if="!item.mobile" style="color: #999">未绑定</span>
          </div>
          <div class="row">
            邮箱:&ensp;{{ sensitiveEmail(item.email)
            }}<span v-if="!item.email" style="color: #999">未绑定</span>
          </div>
          <div class="row">
            性别:&ensp;{{ item.gender === 1 ? "男" : "女" }}
          </div>
          <div class="row">
            用户状态:&ensp;
            <el-switch
              v-model="item.enable"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="启用"
              inactive-text="禁用"
              @change="enableChange(item)"
            />
          </div>
          <div class="content-item">
            角色:&ensp;
            <el-tag type="success" size="small">{{
              item.admin === 1 ? "管理员" : "游客"
            }}</el-tag>
          </div>
          <div class="content-item">注册时间:&ensp;{{ item.createTime }}</div>
        </div>
      </el-card>
    </div>
    <!-- 分页 -->
    <el-pagination
      background
      layout="prev, pager, next"
      :page-size="size"
      :current-page="current"
      :total="total"
      :hide-on-single-page="true"
      @current-change="currentChange"
    />
  </div>
</template>

<script setup lang="ts">
import { formatDate } from "/@/utils/format/format-time";
import { pageUser as apiPageUser, updateStatus } from "/@/api/user/user";
import { useSettingsStore } from "/@/store/index";

const useSettingsStorePinia = useSettingsStore();

const loading = ref(true);
const value2 = ref(true);
const userList = ref([]);
// 分页
const current = ref(1);
const size = ref(6);
const total = ref(0);

// 计算属性
const defaultAvatar = computed(() => useSettingsStorePinia.defaultAvatar);

onMounted(() => {
  pageUser();
});
// 分页获取数据
const pageUser = () => {
  loading.value = true;
  const params = { current: current.value, size: size.value };
  apiPageUser(params).then((res) => {
    loading.value = false;
    const records = res.records;
    records.forEach((ele) => {
      const str = ele.createTime.replace(/-/g, "/");
      ele.createTime = formatDate(new Date(str), "YYYY-mm-dd");
      ele.enable = ele.status === 0;
    });
    total.value = res.total;
    userList.value = records;
  });
};

// 分页变化
const currentChange = (val) => {
  current.value = val;
  pageUser();
};

// 禁用启用开关
const enableChange = async (item) => {
  const status = item.enable ? 0 : 2;
  const params = { userId: item.id, status: status };
  try {
    updateStatus(params);
  } catch (error) {
    console.error(error);
  }
};

// 邮箱脱敏 --> 16****@qq.com
const sensitiveEmail = (email) => {
  return email
    ? email.substr(0, 2) + "****" + email.substr(email.indexOf("@"))
    : "";
};

// 手机号脱敏 --> 157****4866
const sensitiveMobile = (mobile) => {
  var pat = /(\d{3})\d*(\d{4})/;
  return mobile ? mobile.toString().replace(pat, "$1****$2") : "";
};
</script>

<style lang="less" scoped>
.app-container {
  margin: 10px;
  position: relative;

  @media screen and (max-width: 922px) {
    padding: 0;
    margin: 0;
  }

  .card-container {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    align-items: flex-start;

    // 在屏幕宽度小于或等于922像素时生效
    @media screen and (max-width: 922px) {
      padding-top: 15px;
      justify-content: space-around;
    }

    .card-item {
      flex: 1 1 300px; /* 控制每个卡片的宽度，这里设置为自动扩展，最小宽度为300px */
      margin: 10px; /* 调整卡片之间的间距 */
    }

    .el-card {
      position: relative;
      margin: 0;
      padding: 0;
      min-width: 300px;
      min-height: 260px;
      margin-bottom: 20px;
      margin-right: 20px;
      text-align: center;

      @media screen and (max-width: 922px) {
        max-width: 46%;
        margin: 0px;
        margin-bottom: 15px;
      }

      .avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        border: 1px solid #ccc;
        margin: 0 auto;
        margin-top: 30px;
      }

      .content-box {
        font-weight: 500;
        text-align: left;
        padding: 15px;
        line-height: 30px;
        color: #409eff;
        padding-top: 20px;
      }
    }
  }
}
</style>
/*
// 管理员用户管理 
*/