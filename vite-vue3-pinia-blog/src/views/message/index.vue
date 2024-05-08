<template>
  <div class="container" v-if="isReady">
    <div class="content-container">
      <div class="content-head">
        <p class="main-tip-label">温馨提示</p>
        <p class="main-tip-text">
          本站欢迎交换友链，请在下面留言备注即可，请先在您博客上添加本站的友链。<span
            >😘</span
          >
        </p>
        <P class="main-tip-text"> 💖 </P>
        <div class="edit-container">
          <MdEditor
            class="editor"
            v-model="content"
            previewTheme="vuepress"
            codeTheme="a11y"
            placeholder="客官，来都来了，怎么不给博主留个言呢？"
            :toolbars="[0]"
            :preview="false"
            :footers="[]"
          >
            <template #defToolbars>
              <Emoji
                class="emoji"
                :emojis="emojis"
                :selectAfterInsert="false"
              />
            </template>
          </MdEditor>
        </div>

        <div class="main-tools-box">
          <el-button
            :loading="cloading"
            type="danger"
            size="mini"
            @click="messageSubmit"
            >留个言</el-button
          >
        </div>
      </div>

      <!-- 留言列表 -->
      <div class="content-box">
        <p class="main-tip-label">
          留言列表
          <span class="right">共{{ total }}条留言</span>
        </p>
        <ul class="content-list" v-if="loading">
          <transition-group name="fade-list">
            <li
              v-for="(comment, index1) in commentList"
              :key="index1"
              class="list-item"
            >
              <div class="cmt-li-title">
                <div class="headimg">
                  <img :src="comment.fromUser.avatar || defaultAvatar" />
                </div>
              </div>
              <div class="cmt-li-r">
                <div class="top">
                  <p
                    class="nickname"
                    :style="comment.fromUser.admin === 1 ? 'color:#e74851' : ''"
                  >
                    {{ comment.fromUser.nickname }}
                    <el-tag
                      v-if="comment.fromUser.admin === 1"
                      type="info"
                      effect="light"
                      size="small"
                      >管理员</el-tag
                    >
                  </p>
                  <p class="date">{{ parseDate(comment.createTime) }}</p>
                </div>
                <p class="body-text">{{ comment.content }}</p>
                <!-- 删除 -->
                <div class="btns-bar">
                  <el-popover
                    v-if="
                      userInfo &&
                      (userInfo.id === comment.fromUser.id ||
                        userInfo.roles?.includes('admin'))
                    "
                    :visible="comment.del_visible"
                    placement="bottom"
                  >
                    <p style="margin: 8px">确定删除这一条留言吗？</p>
                    <div style="text-align: right; margin: 0">
                      <el-button
                        style="color: #999; font-size: 12px"
                        size="mini"
                        type="text"
                        @click="comment.del_visible = false"
                        >取消</el-button
                      >
                      <el-button
                        style="font-size: 12px"
                        type="text"
                        size="mini"
                        @click="delSubmit(comment)"
                        >确定</el-button
                      >
                    </div>
                    <template #reference>
                      <span
                        class="reply-btn"
                        @click="comment.del_visible = true"
                        >删除</span
                      >
                    </template>
                  </el-popover>

                  <span
                    class="reply-btn"
                    @click="reClick(comment.id, comment.fromUser.id)"
                    >回复</span
                  >
                </div>

                <!-- 留言回复列表 -->
                <ul class="reply-list">
                  <li
                    v-for="(reply, index2) in comment.replyList"
                    :key="index2"
                    class="reply-item"
                  >
                    <div class="reply-date">
                      {{ parseDate(reply.createTime) }}回复
                    </div>
                    <div class="reply-content">
                      <div class="headimg">
                        <img :src="reply.fromUser.avatar || defaultAvatar" />
                      </div>
                      <div class="nickname">
                        <span
                          :style="
                            reply.fromUser.admin === 1 ? 'color:#e74851' : ''
                          "
                          >{{ reply.fromUser.nickname }}</span
                        >
                        <span style="color: #000000">回复</span>
                        <span
                          :style="
                            reply.toUser.admin === 1 ? 'color:#e74851' : ''
                          "
                          >@{{ reply.toUser.nickname }}</span
                        >
                      </div>
                      <p class="reply-text" v-html="reply.content" />
                    </div>
                    <div class="btns-bar">
                      <el-popover
                        v-if="
                          userInfo &&
                          (userInfo.id === reply.fromUser.id ||
                            userInfo.roles?.includes('admin'))
                        "
                        :visible="reply.del_visible"
                        placement="bottom"
                      >
                        <p style="margin: 8px">确定删除这一条回复吗？</p>
                        <div style="text-align: right; margin: 0">
                          <el-button
                            style="color: #999; font-size: 12px"
                            size="mini"
                            type="text"
                            @click="reply.del_visible = false"
                            >取消</el-button
                          >
                          <el-button
                            style="font-size: 12px"
                            type="text"
                            size="mini"
                            @click="delSubmit(reply)"
                            >确定</el-button
                          >
                        </div>
                        <template #reference>
                          <span
                            class="reply-btn"
                            @click="reply.del_visible = true"
                            >删除</span
                          >
                        </template>
                      </el-popover>
                      <span
                        class="reply-btn"
                        @click="reClick(comment.id, reply.fromUser.id)"
                        >回复</span
                      >
                    </div>
                  </li>
                </ul>
              </div>
            </li>
          </transition-group>
        </ul>
        <div
          v-show="current === 1 && loading"
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="#fff"
          style="color: #fff; width: 100%; height: 100px"
        >
          正在加载
        </div>
      </div>
      <div v-if="!loading && commentList.length === 0" class="list-empty">
        还没有回复哦~
      </div>
    </div>

    <!-- 分页 -->
    <el-pagination
      v-if="device === 'desktop'"
      background
      layout="prev, pager, next"
      :page-size="size"
      :current-page="current"
      :total="total"
      @current-change="currentChange"
    />

    <el-pagination
      v-else
      background
      layout="prev, pager, next"
      hide-on-single-page
      :page-size="size"
      :current-page="current"
      :total="total"
      :pager-count="3"
      @current-change="currentChange"
    />

    <!-- 回复弹框 -->
    <el-dialog
      v-model="reEditVisible"
      title="提示"
      :width="device === 'desktop' ? '700px' : '80%'"
      top="45vh"
      :modal="false"
      :show-close="false"
      class="rely-dialog"
      @before-close="bClose"
    >
      <div class="re-editor-container">
        <MdEditor
          class="editor"
          v-model="recontent"
          previewTheme="vuepress"
          codeTheme="a11y"
          placeholder="回复点啥子呢"
          :toolbars="[0]"
          :preview="false"
          :footers="[]"
        >
          <template #defToolbars>
            <Emoji />
          </template>
        </MdEditor>
      </div>
      <template #footer>
        <el-button type="info" size="mini" @click="bClose">取 消</el-button>
        <el-button
          :loading="rloading"
          type="danger"
          size="mini"
          @click="reSubmit"
          >确 定</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>


<script setup lang="ts">
import { formatPast } from "/@/utils/format/format-time";
import { MdEditor } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import { Emoji } from "@vavt/v3-extension";
import "@vavt/v3-extension/lib/asset/Emoji.css";
// import "@vavt/v3-extension/lib/asset/style.css";
import { useRouter } from "vue-router";
import { useGetters } from "/@/store/getters";
import { useSettingsStore } from "/@/store/index";
import {
  pageMessage as apiPageMessage,
  addMessage,
  addReply,
  deleteMessageReply,
} from "/@/api/message/message";

// 引入 ref 函数来创建响应式数据
const isReady = ref(false);

const router = useRouter();
// 通过 useGetters() 获取 getters store 的实例
const useGettersPinia = useGetters();

const content = ref("");
const recontent = ref("");
const reEditVisible = ref(false); //弹框
const current = ref(1);
const size = ref(10);
const total = ref(0);
const commentList = ref([]);
const pid = ref(0);
const toUserId = ref(0);
const loading = ref(false);
const cloading = ref(false);
const rloading = ref(false);
const visible = ref(false);

const emojis = ref([
  "😀",
  "😃",
  "😄",
  "😁",
  "😆",
  "😅",
  "🤣",
  "😂",
  "🙂",
  "😉",
  "😊",
  "😇",
  "🥰",
  "😍",
  "🤩",
  "😘",
  "😗",
  "😚",
  "😙",
  "😏",
  "😋",
  "😛",
  "😜",
  "🤪",
  "😝",
  "🤗",
  "🤭",
  "🤔",
  "🤤",
  "🥳",
  "😎",
  "🙃",
  "🤐",
  "🤨",
  "😑",
  "😶",
  "😶‍🌫️",
  "😒",
  "🙄",
  "😬",
  "😮‍💨",
  "🤥",
  "😌",
  "😔",
  "😪",
  "😴",
  "🤒",
  "🤕",
  "🤢",
  "🤮",
  "🤧",
  "🥵",
  "🥶",
  "🥴",
  "😵",
  "🤯",
  "🥱",
  "😕",
  "😟",
  "🙁",
  "☹️",
  "😮",
  "😯",
  "😲",
  "😳",
  "🥺",
  "😦",
  "😧",
  "😰",
  "😥",
  "😭",
  "😱",
  "😖",
  "😣",
  "😞",
  "😓",
  "😡",
]);

const useSettingsStorePinia = useSettingsStore();

// 计算属性
const userInfo = computed(() => {
  const info = useGettersPinia.userInfo;
  return Object.keys(info).length === 0 ? null : info;
});

const defaultAvatar = computed(() => useSettingsStorePinia.defaultAvatar);
const device = computed(() => useGettersPinia.device);

// 在组件挂载后执行获取数据的操作
onMounted(async () => {
  try {
    pageMessage();
    // 数据获取完成，将 isReady 置为 true，让组件渲染
    isReady.value = true;
  } catch (error) {
    console.error("Error fetching data:", error);
  }
});

// 日期转换
const parseDate = (str) => {
  // 解决ios 日期NAN问题
  str = str.replace(/-/g, "/");
  return formatPast(new Date(str), "YYYY-mm-dd HH-MM-SS");
};

// 获取分页数据
const pageMessage = async () => {
  const params = {
    current: current.value,
    size: size.value,
  };

  try {
    const res = await apiPageMessage(params);
    total.value = res.total;
    const commentList1 = res.records;
    const clen = commentList1.length; //获取评论列表的长度，即评论数量。

    for (let i = 0; i < clen; i++) {
      commentList1[i].del_visible = false; //用来控制评论是否可见或是否可以被删除等功能的标识。
      const replyList = commentList1[i].replyList; //获取当前评论的回复列表。
      const rlen = replyList.length;
      for (let j = 0; j < rlen; j++) {
        replyList[j].del_visible = false; //用来控制回复是否可见或是否可以被删除等功能的标识。
      }
    }

    commentList.value = commentList1;
    loading.value = true;
  } catch (error) {
    console.error(error);
    loading.value = false;
  }
};

// 重写获取数据
const reload = () => {
  current.value = 1;
  pageMessage();
};

// 点击下一页
const currentChange = (currentNum) => {
  current.value = currentNum;
  pageMessage();
};

// 点击发送留言
const messageSubmit = () => {
  // 用户登录了才能发起留言
  if (!userInfo.value) {
    // $store.commit('login/CHANGE_VISIBLE', true)
    return;
  }
  let contentValue = content.value.replace(/<\/?p[^>]*>/gi, "");
  if (!contentValue) {
    ElMessage("还没输入内容呢~");
    return;
  }
  const params = { content: contentValue };
  const email = userInfo.value.email;
  if (!email) {
    ElMessageBox.confirm("没绑定邮箱接收不到回复提醒哦~", "提示", {
      confirmButtonText: "马上绑定",
      cancelButtonText: "下次一定",
      showClose: false,
      type: "warning",
    })
      .then(() => {
        // 用户点击确认按钮时的逻辑
        // 假设 $router 是 Vue Router 的实例
        router.push("/email-validate");
      })
      .catch(() => {
        // 用户点击取消按钮时的逻辑
        cloading.value = true; //显示加载
        addMessage(params).then(
          (res) => {
            cloading.value = false;
            ElMessage({
              message: "留言成功",
              type: "success",
            });
            content.value = "";
            reload();
          },
          (error) => {
            console.log(error);
            cloading.value = false;
          }
        );
      });
  } else {
    cloading.value = true;
    addMessage(params).then(
      (res) => {
        cloading.value = false;
        ElMessage({
          message: "留言成功",
          type: "success",
        });
        content.value = "";
        reload();
      },
      (error) => {
        console.log(error);
        cloading.value = false;
      }
    );
  }
};

// 删除留言
const delSubmit = (item) => {
  deleteMessageReply(item.id).then((res) => {
    ElMessage({
      message: "删除成功",
      type: "success",
    });
    reload();
  });
};

// 点击回复
const reClick = (clickedPid, clickedToUserId) => {
  if (!userInfo.value) {
    // $store.commit('login/CHANGE_VISIBLE', true)
    return;
  }
  reEditVisible.value = true;
  pid.value = clickedPid;
  toUserId.value = clickedToUserId;
};

const bClose = () => {
  pid.value = 0;
  toUserId.value = 0;
  reEditVisible.value = false;
  recontent.value = "";
};

// 回复提交
const reSubmit = () => {
  let contentValue = recontent.value.replace(/<\/?p[^>]*>/gi, "");
  if (!contentValue) {
    ElMessage("还没输入内容呢~");
    return;
  }
  // 构造回复参数
  const params = {
    pid: pid.value,
    toUserId: toUserId.value,
    content: recontent.value,
  };
  const email = userInfo.value.email;
  if (!email) {
    ElMessageBox.confirm("没绑定邮箱接收不到回复提醒哦~", "提示", {
      confirmButtonText: "马上绑定",
      cancelButtonText: "下次一定",
      showClose: false,
      type: "warning",
    })
      .then(() => {
        // 用户点击确认按钮时的逻辑
        router.push("/email-validate");
      })
      .catch(() => {
        rloading.value = true;
        addReply(params).then(
          (res) => {
            rloading.value = false;
            reEditVisible.value = false;
            recontent.value = "";
            ElMessage({
              message: "回复成功",
              type: "success",
            });
            reload();
          },
          (error) => {
            console.error(error);
            rloading.value = false;
          }
        );
      });
  } else {
    rloading.value = true;
    addReply(params).then(
      (res) => {
        rloading.value = false;
        reEditVisible.value = false;
        recontent.value = "";
        ElMessage({
          message: "回复成功",
          type: "success",
        });
        reload();
      },
      (error) => {
        console.error(error);
        rloading.value = false;
      }
    );
  }
};
</script>

<style lang="less" scoped>
.container {
  @import "/@/assets/styles/variables.css";
  width: 80%;
  margin: 0 auto;

  @media screen and (max-width: 922px) {
    width: 90%;
    margin-top: 5px;
  }

  //回复对话框
  .re-editor-container {
    margin: 0 auto;
    width: 90%;
    .editor {
      height: 180px;
      border: 1px solid #e74851;
      border-radius: 5px;
      min-height: 120px;
    }
  }

  .content-container {
    background: #fff;
    margin: 0 auto;
    margin-top: 5px;
    border-radius: 2px;

    @media screen and (max-width: 960px) {
      margin-top: 0;
    }

    .content-head {
      padding: 10px 20px;

      .main-tip-label {
        border-left: 5px solid #00a77c;
        border-bottom: 1px solid #00a77c;
        font-size: 18px;
        font-weight: bold;
        line-height: 2;
        color: #545454;
        padding-left: 10px;
      }

      .main-tip-text {
        color: #545454;
        font-size: 17px;
        text-indent: 50px;
        padding: 20px 0 0 0;
      }

      .edit-container {
        margin: 0 auto;
        padding: 0 50px;

        @media screen and (max-width: 960px) {
          padding: 0;
        }

        .editor {
          height: 195px;
          border: 1px solid #e74851;
          border-radius: 5px;
          min-height: 120px;
        }
      }

      .main-tools-box {
        display: flex;
        flex-direction: row-reverse;
        padding: 10px;
        padding-right: 50px;

        @media screen and (max-width: 960px) {
          padding-right: 0;
        }

        .submit-btn {
          text-align: center;
          cursor: pointer;
          width: 100px;
          line-height: 30px;
          border-radius: 4px;
          border: none;
          color: #fff;
          background: #e74851;
          font-size: 14px;
        }
      }
    }

    .content-box {
      padding: 10px 20px;

      .main-tip-label {
        border-left: 5px solid #00a77c;
        border-bottom: 1px solid #00a77c;
        font-size: 18px;
        font-weight: bold;
        line-height: 2;
        color: #545454;
        padding-left: 10px;

        .right {
          float: right;
          padding-right: 5px;
          font-weight: normal;
          font-size: 14px;
          position: relative;
          top: 10px;
        }
      }

      //留言列表
      .content-list {
        margin: 0;
        padding: 0;

        //过渡
        .fade-list-enter-active,
        .fade-list-leave-active {
          transition: opacity 0.3s;
        }
        .fade-list-enter, .fade-list-leave-to /* .fade-list-leave-active in <2.1.8 */ {
          opacity: 0;
        }

        //
        .list-item {
          list-style: none;
          border-bottom: 1px dashed #e5e5e5;
          margin-bottom: 2px;
          padding-bottom: 0px;
          overflow: hidden;
          margin-top: 15px;

          .cmt-li-title {
            height: 70px;
            float: left;

            .headimg {
              margin-top: 15px;
              height: 44px;
              width: 44px;
              border-radius: 50%;
              overflow: hidden;
              border: 1px solid rgba(0, 0, 0, 0.1);

              img {
                height: 100%;
                width: 100%;
              }
            }
          }

          .cmt-li-r {
            margin-left: 15px;
            width: 1100px;
            float: left;

            @media screen and (max-width: 960px) {
              width: 82.9%;
            }

            @media screen and (max-width: 375px) {
              width: 79.5%;
            }

            .top {
              height: 40px;
              width: 100%;
              margin-top: 5px;
              font-size: 12px;

              .nickname {
                float: left;
                color: #007fff;
                font-weight: 700;
                font-size: 14px;
                padding: 0;
                margin: 0;
                margin-top: 10px;

                .el-tag {
                  margin-left: 2px;
                  font-weight: normal;
                  font-size: 12px;
                }

                .el-tag--mini {
                  height: 18px;
                  line-height: 17px;
                  padding: 0 3px;
                }
              }

              .date {
                font-size: 13px;
                float: right;
                color: silver;
              }
            }

            .body-text {
              font-size: 15px;
              color: #2a2929;
              max-width: 1000px;
            }

            .btns-bar {
              height: 20px;
              overflow: hidden;
              margin-top: -10px;
              margin-bottom: 4px;

              .reply-btn {
                font-size: 12px;
                color: silver;
                cursor: pointer;
                float: right;
                margin-left: 10px;

                &:hover {
                  color: #007fff;
                }
              }
            }

            .reply-list {
              margin: 0;
              padding: 0;
              padding-bottom: 5px;

              .reply-item {
                list-style: none;
                border-top: 1px dashed #e5e5e5;
                margin-bottom: 2px;
                padding-bottom: 0px;
                overflow: hidden;
                margin-top: 10px;
                font-size: 12px;

                .reply-date {
                  padding-top: 10px;
                  float: right;
                  color: silver;
                  font-size: 12px;

                  @media screen and (max-width: 960px) {
                    display: none;
                  }
                }

                .reply-content {
                  float: left;
                  width: 100%;
                  padding-top: 10px;
                  display: flex;
                  align-items: flex-start;

                  .headimg {
                    height: 36px;
                    width: 36px;
                    border-radius: 50%;
                    overflow: hidden;
                    border: 1px solid rgba(0, 0, 0, 0.1);

                    img {
                      height: 100%;
                      width: 100%;
                    }
                  }

                  .nickname {
                    font-weight: 700;
                    padding-top: 13px;
                    color: #007fff;
                    padding-left: 10px;

                    &:after {
                      content: ":";
                      margin: 0 3px;
                      font-weight: bold;
                    }
                  }

                  .reply-text {
                    margin: 0;
                    padding-top: 10px;
                    flex: 1;
                    font-size: 14px;
                    color: #333;
                  }

                  @media screen and (max-width: 960px) {
                    display: block;
                    float: left;
                    padding: 5px 0;

                    .headimg {
                      display: inline-block;
                    }

                    .nickname {
                      display: inline-block;
                      position: relative;
                      bottom: 12px;
                    }

                    .reply-text {
                      padding-top: 5px;
                    }
                  }
                }

                .btns-bar {
                  clear: both;
                  height: auto !important;
                  margin-bottom: 4px;

                  .reply-btn {
                    font-size: 12px;
                    color: silver;
                    cursor: pointer;
                    float: right;
                    margin-left: 10px;

                    &:hover {
                      color: #007fff;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  .el-pagination {
    margin: 30px;
    text-align: center;
  }
}
</style>