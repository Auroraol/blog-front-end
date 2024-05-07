
<template>
  <div class="articleLeft">
    <!-- 名片卡片 -->
    <el-card style="max-width: 25rem; margin-left: 20px" shadow="always">
      <template #header>
        <div class="card_head">
          <div class="left-element">
            <svg-icon name="mingpian" width="30px" height="30px"></svg-icon>
          </div>
          <div class="right-element"><span>名片</span></div>
        </div>
      </template>

      <el-row>
        <el-col :span="6">
          <div class="grid-content ep-bg-purple">
            <div class="block">
              <el-avatar
                v-if="userInfo"
                style="cursor: pointer"
                :size="45"
                :src="userInfo.avatar || defaultAvatar"
              />
              <el-avatar
                v-else
                style="cursor: pointer"
                :size="45"
                :src="defaultAvatar"
              />
            </div>
          </div>
        </el-col>
        <el-col :span="18">
          <div class="grid-content ep-bg-purple-light">
            <div class="personal-card-show-name">
              <h4 style="font-weight: 500" v-if="userInfo">
                {{ userInfo.nickname }}
              </h4>

              <h4 style="font-weight: 500" v-else>游客</h4>
            </div>
            <div class="personal-card-show-words" style="margin-top: 8px">
              <small style="font-size: 13px; color: #808080ad" v-if="userInfo">
                {{ userInfo.brief }}
              </small>
              <small style="font-size: 13px; color: #808080ad" v-else>
                昨日之深渊，今日之浅谈
              </small>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <div
            class="grid-content ep-bg-purple-dark"
            style="margin-top: 10px; display: flex; justify-content: center"
          >
            <span style="color: #009688" v-if="userInfo"
              >邮箱: {{ userInfo.email }}</span
            >
            <span style="color: #009688" v-else>邮箱: xxxxxxxxxx</span>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <div
            class="grid-content ep-bg-purple-dark"
            style="margin-top: 10px; display: flex; justify-content: center"
          >
            <span style="color: #009688" v-if="userInfo"
              >电话: {{ userInfo.mobile }}</span
            >
            <span style="color: #009688" v-else>电话: xxxxxxxxxx</span>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24"
          ><div
            class="grid-content ep-bg-purple-dark"
            style="margin-top: 10px; display: flex; justify-content: center"
          >
            <el-button type="primary" round size="small" @click="showQRCode"
              >扫描二维码关注我</el-button
            >
          </div>
        </el-col>
      </el-row>
      <template #footer>
        <div style="display: flex; justify-content: center">
          <el-check-tag>
            <a href="https://github.com/Auroraol">
              <svg
                t="1709531883797"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="5949"
                width="200"
                height="200"
              >
                <path
                  d="M512 0C229.283787 0 0.142041 234.942803 0.142041 524.867683c0 231.829001 146.647305 428.553077 350.068189 497.952484 25.592898 4.819996 34.976961-11.38884 34.976961-25.294314 0-12.45521-0.469203-45.470049-0.725133-89.276559-142.381822 31.735193-172.453477-70.380469-172.453477-70.380469-23.246882-60.569859-56.816233-76.693384-56.816234-76.693385-46.493765-32.58829 3.540351-31.948468 3.540351-31.948467 51.356415 3.71097 78.356923 54.086324 78.356923 54.086324 45.683323 80.19108 119.817417 57.072162 148.993321 43.593236 4.649376-33.91059 17.915029-57.029508 32.50298-70.167195-113.675122-13.222997-233.151301-58.223843-233.1513-259.341366 0-57.285437 19.919806-104.163095 52.678715-140.846248-5.246544-13.265652-22.820334-66.626844 4.990615-138.884127 0 0 42.996069-14.076094 140.760939 53.787741 40.863327-11.644769 84.627183-17.445825 128.177764-17.6591 43.465272 0.213274 87.271782 6.014331 128.135109 17.6591 97.679561-67.906489 140.59032-53.787741 140.59032-53.787741 27.938914 72.257282 10.407779 125.618474 5.118579 138.884127 32.844219 36.683154 52.593405 83.560812 52.593405 140.846248 0 201.586726-119.646798 245.990404-233.663158 258.957473 18.341577 16.208835 34.721032 48.199958 34.721032 97.210357 0 70.167195-0.639822 126.7275-0.639823 143.960051 0 14.033439 9.213443 30.370239 35.190235 25.209005 203.250265-69.527373 349.769606-266.123484 349.769605-497.867175C1023.857959 234.942803 794.673558 0 512 0"
                  fill="#3E75C3"
                  p-id="5950"
                ></path>
              </svg>
            </a>
          </el-check-tag>
          <el-check-tag>
            <a href="https://gitee.com/jingdao">
              <svg
                t="1709531932097"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="6903"
                width="200"
                height="200"
              >
                <path
                  d="M512 1024A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z m259.2-568.896H480.32a25.28 25.28 0 0 0-25.28 25.28v63.232c0 13.952 11.328 25.28 25.28 25.28h177.024c13.952 0 25.28 11.328 25.28 25.28v12.672c0 41.856-33.92 75.84-75.84 75.84H366.592a25.28 25.28 0 0 1-25.28-25.28V417.216c0-41.92 33.92-75.84 75.84-75.84h353.92a25.28 25.28 0 0 0 25.28-25.344l0.064-63.168a25.28 25.28 0 0 0-25.216-25.28H417.152a189.632 189.632 0 0 0-189.632 189.632v353.92c0 14.016 11.328 25.28 25.28 25.28h372.992a170.624 170.624 0 0 0 170.624-170.624V480.384a25.28 25.28 0 0 0-25.28-25.28z"
                  fill="#C71D23"
                  p-id="6904"
                ></path>
              </svg>
            </a>
          </el-check-tag>
          <el-check-tag>
            <a href="#">
              <svg
                t="1709533450533"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="8080"
                width="200"
                height="200"
              >
                <path
                  d="M512 512m-512 0a512 512 0 1 0 1024 0 512 512 0 1 0-1024 0Z"
                  fill="#FFFFFF"
                  p-id="8081"
                ></path>
                <path
                  d="M769.9456 348.928H261.3248a27.8528 27.8528 0 0 0-28.4672 27.648v337.152c0 15.872 13.0048 27.4432 28.4672 27.4432h508.6208a26.5216 26.5216 0 0 0 27.0336-27.4432V376.5248a26.624 26.624 0 0 0-27.0336-27.5968zM295.9872 473.6l143.36-27.4944 10.752 53.8112-141.824 27.4432-12.288-53.76z m220.928 163.8912c-44.032 48.0256-90.2656-15.1552-90.2656-15.1552l23.5008-15.1552s31.3856 56.6784 66.4064-18.432c33.5872 72.96 70.7584 19.1488 70.7584 19.456l21.3504 13.7728s-39.7312 63.488-91.7504 15.5136z m212.6848-110.08l-142.2848-27.4944 11.264-53.76 142.9504 27.4432-11.9296 53.76z"
                  fill="#00B7FF"
                  p-id="8082"
                ></path>
                <path
                  d="M512 0C229.2224 0 0 229.2224 0 512s229.2224 512 512 512 512-229.2224 512-512S794.7776 0 512 0z m269.6192 809.472c-35.7888-1.1776-47.8208 0-47.8208 0s-2.6624 41.0112-37.6832 41.7792c-35.4304 0.3584-40.704-28.672-41.8304-39.5776-21.504 0-279.552 1.1264-279.552 1.1264s-4.5056 38.0928-39.5776 38.0928c-35.3792 0-37.2736-31.6928-39.5264-38.0928-23.04 0-53.9136-0.7168-53.9136-0.7168s-77.568-16.2304-87.7568-117.1968c1.1264-100.9664 0-300.6464 0-300.6464s-7.168-93.0304 85.504-119.808c28.672-1.1264 90.4192-1.536 162.048-1.536L335.5648 208.896s-10.1888-12.8 7.168-27.136c17.664-14.336 18.432-8.4992 24.4736-4.352s98.304 95.1296 98.304 95.1296H453.12c35.3792 0 71.936 0.5632 107.008 0.5632 13.5168-13.568 90.7776-89.1904 96.0512-92.928 6.0416-3.7888 7.168-10.1376 24.4736 4.1472 17.3568 14.336 7.168 27.136 7.168 27.136l-64.4096 62.208c88.5248 0.768 156.672 1.1264 156.672 1.1264s87.4496 19.2512 89.7024 119.4496c-1.1264 100.1984 0.3584 301.7728 0.3584 301.7728s-4.864 97.9456-88.5248 113.408z"
                  fill="#00B7FF"
                  p-id="8083"
                ></path>
              </svg>
            </a>
          </el-check-tag>
        </div>
      </template>
    </el-card>

    <!-- 公告卡片 -->
    <el-card
      style="max-width: 25rem; margin-left: 20px; margin-top: 30px"
      shadow="always"
      body-style="background-color: #f2dede;"
    >
      <template #header>
        <div class="Announcement_card">
          <div class="left-element">
            <svg-icon name="gonggao" width="30px" height="30px"></svg-icon>
          </div>
          <div class="right-element"><span>公告</span></div>
        </div>
      </template>
      <div>
        <svg
          t="1709536615164"
          class="icon"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="9033"
          width="200"
          height="200"
        >
          <path
            d="M437 227.4c-146.9 94.9-226.5 220.3-235.6 376.4 12.2-6.1 27.5-6.1 45.9-6.1 45.9 0 82.7 12.2 113.2 39.8 27.5 27.5 42.8 64.3 42.8 110.2 0 48.9-15.3 85.7-39.8 116.3-30.6 24.5-70.4 39.8-113.2 39.8-125.5 0-186.7-85.7-186.7-251 0-244.8 101-422.3 306-532.5L437 227.4z m523.4 0c-146.9 94.9-226.5 220.3-235.6 376.4 12.2-6.1 27.5-6.1 45.9-6.1 45.9 0 82.7 12.2 113.2 39.8 27.5 27.5 42.8 64.3 42.8 110.2 0 48.9-15.3 85.7-39.8 116.3-30.6 24.5-70.4 39.8-113.2 39.8-125.5 0-186.7-85.7-186.7-251 0-244.8 101-422.3 306-532.5l67.4 107.1z"
            p-id="9034"
          ></path>
        </svg>
        <div style="text-align: center; line-height: normal">
          <p>恰到好处的喜欢最舒服</p>
          <p>你不用多好</p>
          <p>我喜欢就好</p>
          <p>我没有很好</p>
          <p>你不嫌弃就好</p>
          <p style="text-align: right; padding-top: 10px">--- xxx</p>
        </div>
        <div style="display: flex; justify-content: right; padding-top: 10px">
          <svg
            t="1709536629527"
            class="icon"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="9168"
            width="200"
            height="200"
          >
            <path
              d="M586.99999999 796.6c146.9-94.9 226.49999999-220.3 235.60000001-376.4-12.20000001 6.1-27.5 6.1-45.9 6.1-45.90000001 0-82.69999999-12.2-113.2-39.8-27.5-27.5-42.8-64.30000001-42.8-110.2 0-48.9 15.3-85.7 39.8-116.3 30.6-24.49999999 70.4-39.80000001 113.2-39.8 125.5 0 186.7 85.7 186.7 250.99999999 0 244.79999999-100.99999999 422.3-306 532.50000001L586.99999999 796.6z m-523.39999999 0c146.9-94.9 226.49999999-220.3 235.6-376.4-12.20000001 6.1-27.5 6.1-45.9 6.1-45.90000001 0-82.69999999-12.2-113.2-39.8-27.5-27.5-42.8-64.30000001-42.8-110.2 0-48.9 15.3-85.7 39.8-116.3 30.6-24.49999999 70.4-39.80000001 113.2-39.8 125.5 0 186.7 85.7 186.7 251 0 244.79999999-100.99999999 422.3-306 532.5l-67.4-107.1z"
              p-id="9169"
            ></path>
          </svg>
        </div>
        <div style="text-align: center; padding-top: 5px">
          <p><strong>Welcome to my blog！！！</strong></p>
          <p style="line-height: 21px; font-style: italic; padding-top: 5px">
            平淡对待一切<br />努力做最好的自己
          </p>
        </div>
      </div>
    </el-card>
    <!--日历-->
    <el-card shadow="hover" class="calendar_card" style="margin-left: 20px">
      <cus-calendar />
    </el-card>
    <!-- <div class="calendar_card "  style="margin-left: 20px">
      <el-calendar  v-model="value"   >
        <template #date-cell="{ data }">
          <div class="calendar-day">
            {{ data.day.split("-").slice(2).join("-") }}
          </div>
        </template>
      </el-calendar>
    </div> -->
  </div>
</template>


<script setup>
import { ref } from "vue";
import gsap from "gsap";
import { useUserStore, useSettingsStore } from "/@/store/index";
import { useGetters } from "/@/store/getters";

const value = ref(new Date());
const showQRCode = () => {
  // Handle showing QR code
};

const router = useRouter();
// pinia
const useGettersPinia = useGetters();
const useSettingsStorePinia = useSettingsStore();

const defaultAvatar = computed(() => useSettingsStorePinia.defaultAvatar);

// 计算属性
const userInfo = computed(() => {
  const info = useGettersPinia.userInfo;
  return Object.keys(info).length === 0 ? null : info;
});

onMounted(() => {
  //动画
  gsap.from(".articleLeft", {
    duration: 0.5,
    x: -50,
    opacity: 0.2,
  });
});
</script>
<style lang="less" scoped>
.card_head,
.Announcement_card {
  font-size: 1.8rem;
  display: flex;
  align-items: center; /* 垂直居中 */
  .left-element {
    // margin-right: 10px; /* 可以调整间距 */
  }

  .right-element {
    // margin-left: auto; /* 推到容器的右侧 */
    margin-left: 0.5rem;
  }
}
.calendar_card {
  // width: 500px;
  width: 324px;

  height: 630px;
  display: block;
  float: left;
  margin: 0px;
  margin-top: 30px;

  // 圆圈
  .calendar-day {
    text-align: center;
    line-height: 30px;
    width: 30px;
    height: 30px;
    border: 1px solid rgb(172, 165, 165);
    border-radius: 50%;
  }
}

// .el-calendar-table .el-calendar-day{
//   width: 100%;
//   height: 30px;
//   text-align: center;
// }

:v-deep .el-calendar-table tr td:first-child {
  border-left: none !important;
}

:v-deep .el-calendar-table tr:first-child td {
  border-top: none;
}

:v-deep .el-calendar-table td {
  border-bottom: none;
  border-right: none;
  vertical-align: top;
  -webkit-transition: background-color 0.2s ease;
  transition: background-color 0.2s ease;
}

:v-deep .el-calendar-table .el-calendar-day {
  width: 45px;
  height: 45px;
}

:v-deep .el-calendar-table thead th {
  padding: 12px 0;
  color: #606266;
  font-weight: normal;
  background-color: #f8f4f4;
}

.header {
  span {
    display: block;
    width: 100%;
    height: 0.5rem;
    background-image: linear-gradient(
      to left,
      var(--gradient-start-one),
      var(--gradient-end-one)
    );
    border-radius: 1rem;
  }
}

.tag-img {
  margin-top: 8px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-weight: bold;
  font-size: 10px;
}
</style>