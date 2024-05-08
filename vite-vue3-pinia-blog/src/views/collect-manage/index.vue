<template>
  <div class="app-container">
    <div
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="#fff"
      class="card-container"
    >
      <!-- 卡片 -->
      <el-card
        v-for="(item, index) in artList"
        :key="index"
        :body-style="{ padding: '0px' }"
        shadow="hover"
      >
        <img :src="item.cover" class="image" />
        <div class="content">
          <router-link :to="'/article/' + item.id" class="title">{{
            item.title
          }}</router-link>
          <p class="abstract multi-ellipsis--l2">{{ item.summary }}</p>
          <div class="meta">
            <!-- <span>{{ item.date }} </span> -->
            <el-text size="small">{{ item.date }}</el-text>
            <!-- 下拉框 -->
            <el-popover
              :visible="item.del_visible"
              placement="bottom"
              style="background: red"
            >
              <p style="margin: 8px">确定要删除该收藏吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button
                  size="small"
                  type="text"
                  style="color: #999; font-size: 12px"
                  @click="item.del_visible = false"
                  >取消</el-button
                >
                <el-button
                  type="text"
                  size="small"
                  style="font-size: 12px"
                  @click="delSubmit(item.id)"
                  >确定</el-button
                >
              </div>
              <template #reference>
                <el-button
                  type="text"
                  class="delete"
                  @click="item.del_visible = true"
                  >删除</el-button
                >
              </template>
            </el-popover>
          </div>
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
      :pager-count="3"
      :hide-on-single-page="true"
      @current-change="currentChange"
    />

    <!-- 没有收藏 -->
    <div v-if="!loading && artList.length === 0" class="list-none">
      <img
        src="http://tva1.sinaimg.cn/large/9150e4e5gy1ftwxi6tc2lg204i04mjra.gif"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { formatDate } from "/@/utils/format/format-time";
import {
  pageCollect as apiPageCollect,
  cancelCollected,
} from "/@/api/collect/collect";

const loading = ref(true);
const artList = ref([]);
// 分页
const current = ref(1);
const size = ref(10);
const total = ref(0);

onMounted(() => {
  pageCollect();
});

const pageCollect = () => {
  loading.value = true;
  const params = { current: current.value, size: size.value };
  apiPageCollect(params).then((res) => {
    loading.value = false;
    const records = res.records;
    records.forEach((ele) => {
      ele.date = formatDate(
        new Date(ele.publishTime.replace(/-/g, "/")),
        "YYYY-mm-dd"
      );
      ele.del_visible = false;
    });
    total.value = res.total;
    artList.value = records;
  });
};

const currentChange = (val) => {
  current.value = val;
  pageCollect();
};

// 删除收藏
const delSubmit = (id) => {
  const params = { articleId: id };
  cancelCollected(params).then((res) => {
    ElMessage({
      message: "删除成功",
      type: "success",
    });
    current.value = 1;
    pageCollect();
  });
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

  .list-none {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, 50%);
  }

  .card-container {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    align-items: flex-start;

    @media screen and (max-width: 922px) {
      padding-top: 15px;
      justify-content: space-around;
    }

    .el-card {
      position: relative;
      margin: 0;
      padding: 0;
      max-width: 200px;
      min-height: 260px;
      margin-bottom: 20px;
      margin-right: 20px;

      @media screen and (max-width: 922px) {
        max-width: 46%;
        margin: 0px;
        margin-bottom: 15px;
      }

      /deep/ .more-popper {
        padding: 0 !important;
      }

      .image {
        width: 100%;
        height: 120px;
        min-width: 198.4px;
      }

      .content {
        color: #999;
        padding: 5px;

        .title {
          color: #2f2f2f;
          font-weight: 700;
          font-size: 14px;
          line-height: 20px;
        }

        .abstract {
          font-size: 12px;
          margin-top: 5px;
          line-height: 15px;
          color: #999;
          margin-bottom: 8px;
        }

        .meta {
          width: 100%;
          font-size: 12px;
          padding: 5px;
          left: 0;
          display: flex;
          align-items: center;

          .delete {
            margin-left: 80px;
            color: #999;
            font-size: 12px;
          }
        }
      }
    }
  }
}
</style>
