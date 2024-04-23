<!-- 文章归档页面 -->
<template>
  <div ref="container" class="container">
    <div class="content-container">
      <!--  左-->
      <div class="left-side">
        <h1 class="left-side-title">
          <img :src="randomImageSrc" />
          归档
        </h1>
        <ul class="tab-list">
          <li
            v-for="(tab, index) in tabs"
            :key="index"
            class="list-tab"
            :class="{ 'tab-active': tabActive === index }"
            @click="tabClick(index, tab)"
          >
            {{ tab.date }}
          </li>
        </ul>
        <el-button
          type="text"
          :disabled="tabCurrent === 1"
          @click="tabPageChange(-1)"
        >
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <el-button
          type="text"
          :disabled="tabPages === tabCurrent"
          @click="tabPageChange(1)"
        >
          <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>

      <!-- 右 -->
      <div class="right-side">
        <el-timeline>
          <el-timeline-item
            id="first"
            color="#1d8dff"
            icon="MoreFilled"
            size="large"
            :timestamp="'# 当前月份一共' + total + '篇文章！'"
            placement="top"
          />
          <transition-group name="fade">
            <el-timeline-item
              v-for="(item, index) in artList"
              :key="index"
              color="#0bbd87"
              :timestamp="item.date"
              placement="top"
              :type="primary"
            >
              <el-card shadow="hover">
                <router-link :to="'/article/' + item.id" class="title">{{
                  item.title
                }}</router-link>
                <div class="content">
                  <p class="abstract multi-ellipsis--l3">
                    {{ item.summary }}
                  </p>
                  <div class="wrap-img">
                    <img :src="item.cover" />
                  </div>
                </div>
              </el-card>
            </el-timeline-item>
          </transition-group>
        </el-timeline>
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
      <!-- 移动tab菜单图标 -->
      <svg-icon
        v-if="device !== 'desktop'"
        icon-class="archives-menu"
        class="menu-svg"
        @click="drawer = !drawer"
      />
      <!-- 移动tab菜单抽屉 -->
      <el-drawer
        v-if="device !== 'desktop'"
        :visible="drawer"
        direction="ltr"
        size="40%"
        :show-close="false"
      >
        <ul class="menu-list">
          <li
            v-for="(tab, index) in tabs"
            :key="index"
            class="list-tab"
            :class="{ 'tab-active': tabActive === index }"
            @click="tabClick(index, tab)"
          >
            {{ tab.date }}
          </li>
        </ul>
        <div class="page-wraper">
          <el-button
            type="text"
            :disabled="tabCurrent === 1"
            @click="tabPageChange(-1)"
          >
            <i class="el-icon-arrow-left el-icon" />
          </el-button>
          <el-button
            type="text"
            :disabled="tabPages === tabCurrent"
            @click="tabPageChange(1)"
          >
            <i class="el-icon-arrow-right el-icon" />
          </el-button>
        </div>
      </el-drawer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import {
  pageArchives as apiPageArchives,
  pagePublishedArticle,
} from "/@/api/article/article";
import { formatPast, formatDate } from "/@/utils/format/format-time";
import { MoreFilled } from "@element-plus/icons-vue";

const drawer = ref(false);
const tabActive = ref(0);
const loading = ref(true);
const tabs = ref([]);
const tabSize = ref(12); //归档
const tabCurrent = ref(1);
const tabPages = ref(1);
const yearMonth = ref("");
const current = ref(1);
const size = ref(5); // 文章
const total = ref(0);
const artList = ref([]);
const images = ref([
  "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAAXNSR0IArs4c6QAABKFJREFUSA3tVl1oFVcQnrMbrak3QUgkya1akpJYcrUtIqW1JvFBE9LiQ5v6JmJpolbMg32rVrhgoYK0QiMY6i9Y6EMaW5D+xFJaTYItIuK2Kr3+BJNwkxBj05sQY3b3nM6cs2dv9t7NT/vQJw/sndk5M/PNzJkzewGerP+pAmy+ON8lLzUJgA8ZYxYIYZmGYRnctDaWvJJAmTtfP1pvXsBCCPP8QFcCaRkZYACgDZFO4stNIcBCajEOlmmC9XpJ9bAGCaPaPmzPl32dvLSVu3BWCTQs0XQQ6g0DYgwLIoAZbBCdW/i+781o1VVlm/410mw4h06Y7bIPHNyWDyL4FHkX03Q8SrzNhZTZriieckWt7cL6MM85YcLpsi/7O9/iXFT6MswI0DmmpkSaJ0qLxFIm3+i1THHB3zmBH3PYx9CcykcLOeQVVa7QtdxTgQgEleX2AjHYfwA+2ddV77ruGoJUbhGDI09YSNXyMpUt5ylOzxgbUmtOp7NmbNt8v3arjTBfYELmLUV+M+nSawNNAUqpT3ClJWg5I3BLT+cGW/DXNGCa6tx1aakCGEigArTn4TDIPdrXXYKCZNrHLMCOEPvHBlLQ99s9eHB7EB6NTki73CVPQ2F5MSx/uRQixfmq7rK0wYD8w8E905bnPDfwoWs/rfv93NWN/ZfvwsLIU7A09gxECyISeGJkHAau98L97tuw7NXnoPyNF8FcYGLGKsOs0mN3OEyec9esGW/ZEl945dTP34wlR2FZVQWU1q0Cw8Tr7p+hgLLNL0FPxx/Q35mA8aEUrH6nCgwEl0tn7wUiZYJnNRh6DK4UH/k0lfyrsBKdPVv/AriGIQcEDQZ65LBAGe2Rzui9Ybjz7XUppz1/uKBbyVPGkN3ZAeC6hr0x7Nr38N5+EqkoOm17xpoqR9ohQF55ERSvr4Dkr3chNfC3DMzGJlNBElW8w9nsGQvhNGIzDkXzCg8cLK951xHsFBlTJspJNi3ZFIMF2AeDV3q8DNOB+YHi6QTrChDIWDBRi5U5f+ZMfJLu3ccrqxtdxk4SKH336LFxSmkqefwU5T8fhdSdQf9IVKD6aNiwI/hnmcAZ91isYMJIaCUCx9W098+LgruikeTqzqqxKPUwqJyCPJiyemVVZBOijDGjD38Os0jOiSPL1z3SPjXNANbiNPXAdzTfukjjuknNBbyz3nwgTd3AVFqUJ5hpHlq9MveLnWwttUfoygBmvVjuikxND3znrhsELnZk7k+OjIGxeNEkomyLVta0xxn+HZhjBc4YZ/AFjHjz9u3xRZl2BN4aq9nFwWh16IrQ1aHHEd3j1+4/dB9OtH4e29A2H1DyHQRmOSfQZ1Fy7MHBTGB6J/Djq6p3OxyO2cB+4Car7v/o3GXgfAkj23+x9ID1Teoamo/SXcbvSf2PX7Vc8DdCmE1vN9di+32P9/5YR3vLnhCVGUWBjEkr3yh4H8v9CzmsbdhzOKzsJKM90iFdaTMjRPhGVsakRvOaRidljo6H6G7j+ctrJpsP+4COhDIl0La2+FS4+5mlocBaXY5QnGZysIBYoeSsl5qQzrSj/cgNrfuEzlWBfwA+EjrZyWUvpAAAAABJRU5ErkJggg==",
  "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAAXNSR0IArs4c6QAABGpJREFUSA3tVVtoXFUU3fvOI53UlmCaKIFmwEhsE7QK0ipFEdHEKpXaZGrp15SINsXUWvBDpBgQRKi0+KKoFeJHfZA+ED9KKoIU2gYD9UejTW4rVIzm0VSTziPzuNu1z507dibTTjL4U/DAzLn3nL3X2o91ziX6f9wMFdh6Jvbm9nNSV0msViVO6tN1Rm7NMu2OpeJ9lWBUTDxrJbYTS0hInuwciu9eLHlFxCLCZEk3MegsJmZ5K/JD6t7FkFdEvGUo1g7qJoG3MHImqRIn8/nzY1K9UPKKiJmtnUqHVE3Gbuay6vJE/N2FEmuxFjW2nUuE0yQXRRxLiTUAzs36zhZvOXJPdX850EVnnLZkB8prodQoM5JGj7Xk2mvC7JB8tG04Ef5PiXtG0UtxupRQSfTnBoCy554x18yJHI6I+G5Eru4LHmPJZEQsrvPUbMiA8G/WgMK7w7I+ez7++o2ANfbrjvaOl1tFMs+htG3IrZH9/hDX1Pr8Tc0UvH8tcX29KzAgIGcEkINyW5BF9x891hw6VYqgJHEk0huccS7vh3C6gTiODL+26huuBtbct8eZnqLML8PkxGYpuPZBqtqwkSjgc4mB5gbgig5i+y0UDK35LMxXisn9xQtK+nd26gTIHsHe/oblK/b29fUmN/8Y+9jAQrnBp56m1LcDlDp9irKTExSKduXJVWSqdBMA08pEJnEIOB3FPPMybu/oeV8zFeYN3xx576Q6RH+VmplE4ncQV5v+5rzSoyOU7PuEAg8g803PwBJ0CExno/jcMbN8tONYeOmHiuUNryvm3fRUy4tMPVLdAGkUhNWuggGrJcXPv+ouCjz0MKUHz1J2/E8IC9nqTabcxgaBYM0hPhD5Y65FsbxRQKxCQrDjDctW7PUM3HuZunFyifSAqEfuzCp48Il24luWUWZoyJCaPR82jE0+kFA643wRFVni4RYSq3ohJO2pZ7B5dO4xkDWbEpossJPLSrPjYID8rS2UHTlvyNxqIGsg674XJJ7vnh5L7PNwC4hh2sjCI96mzszOTpxLF0T7l88Yz7lAuK6OnL8gXLOnTvpzSb22YG8W7us3jSebFHeeqnXRG1vt+MoUM84LQIBmMsCTAcOauTh0T0l0neQK7m2bLMt2mGxU3HYssS0J2cdv5wljlPsrIuZLAG/2DOZIXgCYT8uMGZN+e2kSirfxZOPCsC0f24nTZzspnVn9VePS1Z5vubmAGGXG8ZFno9Hel0yfA5ZPhF7Dh972BQJ2qCpgH67lmWtBYbvk6sz02wjky2vXyz0XErP/kFB619js1BtwfOV4OPRqOQBjy3Qbk18vigUPPSD5ceHnwck7W9bhAqZdd7SuG7w4/P2F/GaJh8c7e9qgow+Q7cGBo+98WsLkuktFqiZabtXuQTu/Y5ETbR0v7tNSFnvrmu6pjdoan2KjMu8q/Hmj1EfCO2ZGfEIbIXKUlw8qaX9/b2oeSJmFksSeT/Fn0V3nSypChh4Gjh74ybO9aeZ/AN2dwciu2/MhAAAAAElFTkSuQmCC",
  "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAAXNSR0IArs4c6QAABH1JREFUSA3tVl1oHFUUPmdmd2ltklqbpJDiNnXFmgbFktho7YMPNiJSSZM0+CAYSkUELVhM6YuwIPpgoOKDqOBDC0XE2CQoNtQXBUFTTcCi+Wlh1V2TQExsUzcltd3M9Tt3ZjZzZ2fT+OJTL8yeM+eee757fmeJbq//KQL8X3DUSFOcfr7cRsRtxNQMWueeVzOkaITIGqQHNg5y8+jNW9ldM7A6nTpAjuolUikAwq7CE3WcM2RRDz+XGVgN3FptU/aUSlvq9Pa3iZ1+sgAqJyyAFqkipd9dqiwHF3P65YycLWc/6sqGrvoEoIp6DOFaX5h6+dnfjkWprwqsPk0dUGq5vySwDImC10KxFHgGL1SWoc92O3eVht09qdXNH11I2SsTsJYqMWzihqGMi+A+Garf3BAuuLI5oGlULyNfyB/HYNujwktOfRrMr5t77NmevqaUopx0grnKAyvVpmwUDB4x6FPXuGvYLTDwWsejwgtgkYKPqRJg8SV6xaiZ3ZTppGneS4yfH5/66fZSDHv+QZci/+h5c5UHtpy67JUqGppM0sh0Nc1dW6/N1W5Yoqat8/TU/VnadmdeW2PLLSyh0cvxBs3KbqTmwYPpxN4do/mzE8nEpvX/UMu2Wbp74zUAK5q6WkHns7V0eWkdPbPzd3rxkTGybadYySumVzhcaJFbs5UrEkQ/+CK8gF5dnh/6ciIZ73gwQ927L1IitoxKLXYP3SjYdOrHHfTZhRRlFyrorafPk20B3HPD1y2G3qKZME5Jcf3t/HUC13/8tSd++vqFveMUTwAUxSUFI1QekR1+bIze3D9MF2aq6cPvG72CgnldWCFqyRw3lwH8ZMerjTD9ElRO7Gv44wNpC90aASqGfVlz/Rx17srQ57/UU26hkhQqUB7dBR71WmzQhHUnblGmVOEw0jhbV1n9OlXUDCIRGaNV5Jp43N516fN7JmnTHdfp7Hgy0luO4aMhtkLL8Bi3bUWYvzh5Mn1dTxrL6QmGuRhGL/TiTTxRoEdTszSaq9GR0NGA3KdkOz3hqSV3MIDhQ5IVX/Ivx3umBti2es2h4eZby7x8br1rkf7Mo90AqC8aQ3sJeNzqFRu+vSANAQe3PL7l0HGOAdwDCeZYvNKeoZp1Qfs6Aipndh86HmFRi0LAnEO47wsqM6cdfjh3jBPUzhZy7nvlUfFsamED1VQt6aISHVymXZ/B2aCtIG8AI8xfobj2d3en1wWVhOeHELKmLQ1s211s88comkv4UCwWyF787mJdYXtNfhKAXVqnKTq8QZvGAGGOfaTo5pGZ/PwbUCr5+DPr/1J92JNHr9aOl/F3iI5+O1nfybsGxoimvZ3ViWSluDITw3P37mypheDIPY0tw7+O/5ApbkYw+zpfaUVu32Pi98+defdUhEpZkRFq0aqyNh9FuL9hpYbEm6iwi0z2REd09ZmyENEbuhjDWzKvZXTqKYaBIr3tt5kuPtQBZFvEUwHt60vfCNu41XsksH9Ij1BMMz1Y0OOunHNShFIP5868g5zeXmuLwL9T4b6Q2+KejgAAAABJRU5ErkJggg==",
]);
const currentImageIndex = ref(0);

const randomImageSrc = computed(() => images.value[currentImageIndex.value]);

// Mounted hook
onMounted(() => {
  changeImage();
  pageArchives();
});

const changeImage = () => {
  // 生成一个随机的索引值
  const newIndex = Math.floor(Math.random() * images.value.length);
  currentImageIndex.value = newIndex;
  console.error(currentImageIndex.value);
};

// Methods
const pageArchives = async () => {
  loading.value = true;
  try {
    const res = await apiPageArchives({
      size: tabSize.value,
      current: tabCurrent.value,
    });
    const records = res.records; // 有两个数据yearMonth articleCount
    records.forEach((ele) => {
      const arr = ele.yearMonth.split("-");
      ele.date = arr[0] + "年" + arr[1] + "月";
    });

    tabs.value = records;
    tabPages.value = res.pages;
    tabClick(0, tabs.value[0]); //点击
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// 文章
const pageArticle = async () => {
  loading.value = true;
  try {
    const res = await pagePublishedArticle({
      current: current.value,
      size: size.value,
      yearMonth: yearMonth.value,
    });
    const records = res.records;
    records.forEach((ele) => {
      ele.date = formatDate(
        new Date(ele.publishTime.replace(/-/g, "/")), // 解决ios 日期NAN问题
        "YYYY-mm-dd HH:MM"
      );
    });

    artList.value = records;
    total.value = res.total;
    loading.value = false;
  } catch (error) {
    console.error(error);
    loading.value = false;
  }
};

// Event Handlers
const currentChange = (newCurrent) => {
  current.value = newCurrent;
  pageArticle();
};

// 归档翻页
const tabPageChange = (val) => {
  tabCurrent.value += val;
  drawer.value = false;
  pageArchives();
};

const tabClick = (index, tab) => {
  tabActive.value = index;
  yearMonth.value = tab.yearMonth;
  current.value = 1;
  drawer.value = false;
  pageArticle(); //显示文章
};
</script>

<style lang="less" scoped>
.container {
  width: 100%;
  height: 100vh;
  @media screen and (max-width: 960px) {
    background: #fff;
  }

  .content-container {
    margin: 0 auto;
    margin-top: 50px;
    box-sizing: border-box;
    display: flex;

    @media screen and (max-width: 960px) {
      margin-top: 20px;
    }

    // 左
    .left-side {
      position: fixed;
      top: 110px;
      left: 200px;
      z-index: 999;
      padding: 0;
      padding-top: 20px;
      padding-bottom: 5px;
      background: #fff;
      text-align: center;
      min-width: 150px;

      .left-side-title {
        margin-left: -10px;
        margin-top: -15px;
        color: #303133;
      }

      .tab-list {
        margin-top: 15px;
        font-size: 15px;
        text-align: center;
        color: #909090;
        // min-height: 370px;
        .list-tab {
          list-style: none;
          position: relative;
          padding: 5px;
          border-radius: 3px;
          cursor: pointer;
          margin: 5px 20px;
          margin-top: 0;

          &:hover {
            color: #007fff;
            background: #f4f5f5;
          }
        }

        .tab-active {
          color: #fff;
          background: #007fff;

          &:hover {
            color: #fff;
            background: #007fff;
          }
        }
      }

      .el-icon {
        font-weight: 700;
      }
    }

    //右
    .right-side {
      margin-left: 400px;
      margin-right: 190px;
      flex: 1;

      //过渡
      .fade-enter-active,
      .fade-leave-active {
        transition: opacity 0.3s; /* 调整过渡持续时间 */
      }
      .fade-enter,
      .fade-leave-to {
        opacity: 0;
      }
      .el-card {
        position: relative;

        @media screen and (max-width: 922px) {
          width: 80vw;
        }

        .title {
          margin: 0;
          font-size: 14px;
          font-weight: 700;
          color: #2f2f2f;

          &:hover {
            text-decoration: underline;
          }
        }

        .content {
          display: flex;

          .abstract {
            flex: 1;
            margin: 0;
            margin-top: 8px;
            padding-right: 200px;
            line-height: 20px;
            color: #999;

            @media screen and (max-width: 960px) {
              padding-right: 0;
            }
          }

          .wrap-img {
            position: absolute;
            right: 30px;
            top: 50%;
            transform: translateY(-50%);
            width: 150px;
            height: 70px;
            border-radius: 4px;
            border: 1px solid #f3f7fa;
            overflow: hidden;

            @media screen and (max-width: 960px) {
              display: none;
            }

            img {
              width: 100%;
              height: 100%;
            }
          }
        }
      }

      .el-pagination {
        text-align: center;
      }

      @media screen and (max-width: 960px) {
        margin-left: 0;
      }

      #first {
        width: 100%;
        height: 70px;

        /deep/ .el-timeline-item__timestamp {
          color: #303133;
          font-size: 16px;
          font-weight: bold;
        }

        /deep/ .el-timeline-item__node--large {
          left: -8px;
          width: 23px;
          height: 23px;
        }
      }

      @media screen and (max-width: 960px) {
        /deep/ .el-timeline {
          padding: 15px;
        }
      }

      // 左边连线的样式
      @media screen and (min-width: 960px) {
        /deep/ .el-timeline-item__tail {
          border-left: 2px solid #c0c4cc;
        }
      }

      // 右边日期的样式
      @media screen and (min-width: 960px) {
        /deep/ .el-timeline-item__timestamp {
          color: #444;
          font-weight: 700;
        }
      }
    }
  }

  // 移动端tab menu图标
  .menu-svg {
    position: fixed;
    right: 20px;
    bottom: 20px;
    fill: #333;
    width: 25px;
    height: 25px;
  }

  // 移动端tab
  .menu-list {
    margin: 0;
    padding: 0;
    font-size: 15px;
    text-align: center;
    color: #909090;

    @media screen and (min-width: 922px) {
      display: none;
    }

    .list-tab {
      list-style: none;
      position: relative;
      margin: 0 auto;
      padding: 5px;
      border-radius: 3px;
      cursor: pointer;
      margin-top: 5px;
      width: 100px;

      &:hover {
        color: #007fff;
        background: #f4f5f5;
      }
    }

    .tab-active {
      color: #fff;
      background: #007fff;

      &:hover {
        color: #fff;
        background: #007fff;
      }
    }

    .page-wraper {
      text-align: center;
    }
  }

  /deep/ .el-drawer__body > * {
    text-align: center;
  }
}
</style>
