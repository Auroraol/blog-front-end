<template>
  <div class="container">
    <!-- 点赞 收藏 -->
    <div class="authorInfo">
      <div class="dianzan">
        <div class="praise" v-if="!ifPraise" @click="praiseClick">
          <svg-icon name="dianzan" width="26px" height="26px"></svg-icon>
        </div>
        <div class="praise" v-else @click="praiseClick">
          <svg-icon name="dianzan2" width="26px" height="26px"></svg-icon>
        </div>
      </div>
      <div class="shoucang">
        <div
          class="praise collection"
          v-if="!ifCollection"
          @click="collectionClick"
        >
          <svg-icon name="shoucang" width="26px" height="26px"></svg-icon>
        </div>
        <div class="praise collection" v-else @click="collectionClick">
          <svg-icon name="shoucang2" width="26px" height="26px"></svg-icon>
        </div>
      </div>
      <div
        class="praise collection"
        v-if="(goTop = 1 == true)"
        @click="goTopBtn"
      >
        <svg-icon name="huojian" width="26px" height="26px"></svg-icon>
      </div>
    </div>

    <!-- 分享  -->
    <div class="share">
      <ul class="extra-cnt">
        <li>
          <div class="item">
            <a
              target="_blank"
              :href="
                'https://connect.qq.com/widget/shareqq/index.html?url=' +
                codedUrl +
                '&title=' +
                codedTitle +
                '&summary=&style=101&width=96&height=24'
              "
            >
              <svg-icon
                name="qq-login"
                color="green"
                width="24"
                height="24"
              ></svg-icon>
            </a>
          </div>
        </li>

        <li>
          <div class="item">
            <el-popover
              placement="left"
              trigger="hover"
              popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 20px;"
            >
              <template #reference>
                <svg-icon
                  name="wx2"
                  color="green"
                  width="24"
                  height="24"
                ></svg-icon>
              </template>
              <template #default>
                <div
                  style="
                    display: flex;
                    justify-content: center;
                    align-items: center;
                  "
                >
                  <img
                    style="width: 120px; height: 120px; margin: 5px"
                    :src="
                      'https://my.tv.sohu.com/user/a/wvideo/getQRCode.do?text=' +
                      codedUrl
                    "
                  />
                </div>
              </template>
            </el-popover>
          </div>
        </li>

        <li @click="touchCopy" class="cp-warpper">
          <div class="item">
            <a>
              <svg-icon name="copy2" width="24" height="24" />
            </a>
          </div>
        </li>
        <ul />
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from "vue";
// import { ElMessage } from 'element-plus'
// import useAxios from '@/hooks/axios/axios';
// import { useUserStore } from '/@/store/index';

const authorInfo = ref<HTMLElement>(); //获取dom

// const pinia = useStore()

const props = defineProps({
  title: {
    type: String,
    default: "",
  },
});

import useClipboard from "vue-clipboard3"; //  copy 功能插件
import { useGetters } from "/@/store/getters";
import { useLoginStore } from "/@/store/index";
import {
  isLiked as checkIsLiked,
  addLike,
  cancelLiked,
} from "/@/api/like/like";
import {
  isCollected as checkIsCollected,
  addCollect,
  cancelCollected,
} from "/@/api/collect/collect";

const { toClipboard } = useClipboard(); // 使用copy 功能插件
const useLoginPinia = useLoginStore();
const useGettersPinia = useGetters();
const router = useRouter();

const url = ref(""); //复制链接
const id = ref(0);
const ifPraise = ref(false); //是否点赞  true是没有
const ifCollection = ref(false); //是否收藏 true是没有

// 计算属性
const token = computed(() => useGettersPinia.token);
const liked = computed(() => token.value && ifPraise.value);
const collected = computed(() => token.value && ifCollection.value);
const codedUrl = computed(() => encodeURIComponent(url.value));
const codedTitle = computed(() => encodeURIComponent(props.title));

// 监视
watch(token, (newValue, oldValue) => {
  if (newValue) {
    isLiked();
    isCollected();
  }
});

// 初始化
onMounted(() => {
  url.value = window.location.href;
  id.value = Number(router.currentRoute.value.params.id) || 0;
  isLiked();
  isCollected();
});

const isLiked = async () => {
  if (token.value) {
    try {
      const res = await checkIsLiked(id.value);
      ifPraise.value = res === 1; //1点赞 0未攒
    } catch (error) {
      console.error(error);
    }
  }
};

const isCollected = async () => {
  if (token.value) {
    try {
      const res = await checkIsCollected(id.value);
      ifCollection.value = res === 1;
    } catch (error) {
      console.error(error);
    }
  }
};

// 点击复制
function touchCopy() {
  copy(url.value);
}

const copy = async (msg) => {
  try {
    // 复制
    await toClipboard(msg);
    // 复制成功
    ElMessage.success("复制成功");
  } catch (e) {
    // 复制失败
  }
};

watch(
  () => Number(router.currentRoute.value.params.id),
  (newValue, oldValue) => {
    // 当路由参数发生变化时执行逻辑
    id.value = newValue;
    url.value = "http://localhost:8888/article/" + id.value;
    isLiked();
    isCollected();
  }
);

//点赞
const praiseClick = async () => {
  // 登录后才能点赞
  if (!token.value) {
    //js中 null、undefined、空字符串均为假值
    useLoginPinia.changeVisible(true);
    return;
  }

  const params = { articleId: id.value };
  if (!liked.value) {
    // 如果没点赞攒
    try {
      await addLike(params);
      // 向父组件传参
      ifPraise.value = true;
    } catch (error) {
      console.log(error);
    }
  } else {
    try {
      cancelLiked(params);
      ifPraise.value = false;
    } catch (error) {
      console.log(error);
    }
  }
};

//收藏
const collectionClick = async () => {
  // 登录后才能收藏
  if (!token.value) {
    useLoginPinia.changeVisible(true);
    return;
  }
  const params = { articleId: id.value };
  if (!collected.value) {
    try {
      await addCollect(params);
      // 向父组件传参
      ifCollection.value = true;
    } catch (error) {
      console.log(error);
    }
  } else {
    try {
      cancelCollected(params);
      ifCollection.value = false;
    } catch (error) {
      console.log(error);
    }
  }
};

// 回到顶部
const goTopBtn = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};
</script>

<style scoped lang="less">
.container {
  display: flex;
  align-items: center;
  flex-direction: column;

  .authorInfo {
    position: fixed;
    width: 45px;
    //   top: 180px;
    top: 200px;
    margin-left: 4rem;
    //   margin-right: 1rem;

    width: 5rem;
    height: 20rem;
    border: 0.2rem solid var();
    border-radius: 0.5rem;
    display: flex;
    flex-direction: column;
    align-items: center;

    .praise {
      background-color: rgb(255, 255, 255);
      width: 4rem;
      height: 4rem;
      border-radius: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 2rem;

      &:hover {
        cursor: pointer;
        // background-color: var(--special-font-color);
        // color: #fff;
        box-shadow: 0 0 0.2rem 0.2rem var(--special-font-color);
      }
    }

    .collection {
      margin-top: 1.5rem;
    }
  }

  // 分享
  .extra-cnt {
    position: fixed;
    color: #b2bac2;
    background: #fff;
    top: 420px;
    width: 52px;

    li {
      padding: 10px 0;
      position: relative;

      &:before {
        content: "";
        position: absolute;
        left: 50%;
        bottom: 0;
        width: 20px;
        height: 1px;
        background-color: #e5e5e5;
        margin-left: -10px;
      }

      &:hover {
        //  box-shadow: 0 0 0.1rem 0.1rem var(--special-font-color);
        color: #007fff;
      }

      .item {
        display: flex;
        justify-content: center;
      }
    }
  }
}
</style>
