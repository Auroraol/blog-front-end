<template>
  <div class="c-container">
    <div class="content-container">
      <div class="content-head">
        <div class="edit-container">
           <MdEditor v-model="content" previewTheme="vuepress" codeTheme="a11y"/>
        </div>
        <div class="main-tools-box">
          <el-button :loading="cloading" type="danger" size="mini" 
          @click="commentSubmit">评论</el-button>
        </div>
      </div>
      <div class="content-box">
        <p class="main-tip-label">评论列表
          <span class="right">共{{ total }}条评论</span>
        </p>

        <!-- 评论列表 -->
        <ul class="content-list">
          <transition-group name="fade-list">
            <li v-for="(comment, index1) in commentList" :key="index1" class="list-item">
              <div class="cmt-li-title">
                <div class="headimg">
                  <img :src="comment.fromUser.avatar || defaultAvatar">
                </div>
              </div>
              <div class="cmt-li-r">
                <div class="top">
                  <p class="nickname" :style="comment.fromUser.id === authorId ? 'color:#e74851' : ''">
                    {{ comment.fromUser.nickname }}

                    <el-tag v-if="comment.fromUser.id === authorId" type="info" size="mini" effect="light">作者</el-tag>
                  </p>
                  <p class="date">{{ parseDate(comment.commentTime) }}评论</p>
                </div>
                <p class="body-text" v-html="comment.content" />
                <div class="btns-bar">
                  <el-popover
                    v-if="userInfo && (
                      userInfo.id === comment.fromUser.id
                      || userInfo.id === authorId
                      || userInfo.roles.includes('admin'))"
                    v-model="comment.del_visible"
                    placement="bottom"
                  >
                    <p style="margin: 8px;">确定删除这一条评论吗？</p>
                    <div style="text-align: right; margin: 0">
                      <el-button style="color:#999;font-size: 12px;" size="mini" type="text" @click="comment.del_visible = false">取消</el-button>
                      <el-button style="font-size: 12px;" type="text" size="mini" @click="delCommentSubmit(comment)">确定</el-button>
                    </div>
                    <span slot="reference" class="reply-btn">删除</span>
                  </el-popover>
                  <span class="reply-btn" @click="reClick(comment.id, comment.fromUser.id)">回复</span>
                </div>

                <!-- 回复列表 -->
                <ul class="reply-list">
                  <li v-for="(reply, index2) in comment.replyList" :key="index2" class="reply-item">
                    <div class="reply-date">{{ parseDate(reply.replyTime) }}回复</div>
                    <div class="reply-content">
                      <div class="headimg">
                        <img :src="reply.fromUser.avatar || defaultAvatar">
                      </div>
                      <div class="nickname">
                        <span :style="reply.fromUser.id === authorId ? 'color:#e74851' : ''">{{ reply.fromUser.nickname }}</span>
                        <span style="color: #000000;">回复</span>
                        <span :style="reply.toUser.id === authorId ? 'color:#e74851' : ''">@{{ reply.toUser.nickname }}</span>
                      </div>
                      <p class="reply-text" v-html="reply.content" />
                    </div>
                    <div class="btns-bar">
                      <el-popover
                        v-if="userInfo &&
                          (userInfo.id === reply.fromUser.id
                          || userInfo.id === authorId
                          || userInfo.roles.includes('admin'))"
                        v-model="reply.del_visible"
                        placement="bottom"
                      >
                        <p style="margin: 8px;">确定删除这一条回复吗？</p>
                        <div style="text-align: right; margin: 0">
                          <el-button style="color:#999;font-size: 12px;" size="mini" type="text" @click="reply.del_visible = false">取消</el-button>
                          <el-button style="font-size: 12px;" type="text" size="mini" @click="delReplySubmit(reply)">确定</el-button>
                        </div>
                        <span class="reply-btn">删除</span>
                      </el-popover>
                      <span class="reply-btn" @click="reClick(comment.id, reply.fromUser.id)">回复</span>
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
          style="color: #fff;width: 100%;height: 100px;"
        >正在加载</div>
      </div>
      <div v-if="!loading && commentList.length === 0" class="list-empty">还没有评论哦~</div>
    </div>
    <el-pagination
      background
      layout="prev, pager, next"
      :page-size="size"
      :current-page="current"
      :hide-on-single-page="true"
      :total="total"
      @current-change="currentChange"
    />

    <!-- 回复弹框 -->
    <el-dialog
      :visible.sync="reEditVisible"
      title="提示"
      :width="device === 'desktop'?'600px':'95%'"
      top="45vh"
      :modal="false"
      :show-close="false"
      class="rely-dialog"
      :before-close="bClose"
    >
      <div class="re-editor-container">
        <quill-editor v-model="recontent" :options="reEditorOption" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="info" size="mini" @click="bClose">取 消</el-button>
        <el-button :loading="rloading" type="danger" size="mini" @click="reSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { formatPast } from '/@/utils/format/format-time';
// import { pageComment, addComment, addReply, deleteComment, deleteReply } from '/@/api/comment/comment';
// import '@/assets/quill-emoji/quill-emoji.js';

import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';


const articleId = ref('');
const authorId = ref('');
const content = ref('');
const recontent = ref('');
const loading = ref(false);
const reEditVisible = ref(false);
const current = ref(1);
const size = ref(10);
const total = ref(0);
const commentList = ref([]);
const commentId = ref(0);
const toUserId = ref(0);
const cloading = ref(false);
const rloading = ref(false);

const editorOption = {
  modules: {
    toolbar: {
      container: [['emoji']]
    },
    'emoji-toolbar': true,
    'emoji-shortname': true
  },
  placeholder: '嗨，你好呀,欢迎在评论区评论哦~'
};

const reEditorOption = {
  modules: {
    toolbar: {
      container: [['emoji']]
    },
    'emoji-toolbar': true,
    'emoji-shortname': true
  },
  placeholder: '回复点啥子呢？'
};

// const userInfo = computed(() => mapGetters('userInfo'));
// const defaultAvatar = computed(() => mapGetters('defaultAvatar'));
// const device = computed(() => mapGetters('device'));

// 日期转换
const parseDate = (str) => {
  // 解决ios 日期NAN问题
  str = str.replace(/-/g, '/');
  return formatPast(new Date(str));
};

// 回复弹框关闭事件
const bClose = () => {
  commentId.value = 0;
  toUserId.value = 0;
  reEditVisible.value = false;
  recontent.value = '';
};

// 监听分页
const currentChange = (currentPage) => {
  current.value = currentPage;
  pageComment();
};

onMounted(() => {
  pageComment();
});
// 获取分页数据
const pageComment = () => {
  loading.value = true;
  const params = {
    articleId: articleId.value,
    current: current.value,
    size: size.value
  };
  // pageComment(params).then(
    // res => {
    //   loading.value = false;
    //   total.value = res.data.total;
    //   const commentListData = res.data.records;
    //   commentListData.forEach(comment => {
    //     comment.del_visible = false;
    //     comment.replyList.forEach(reply => {
    //       reply.del_visible = false;
    //     });
    //   });
    //   commentList.value = commentListData;
    // },
    // error => {
    //   
    //   loading.value = false;
    // }
  // );
};

// 重载
const reload = () => {
  current.value = 1;
  pageComment();
};

// 评论提交
const commentSubmit = () => {
  if (!userInfo.value) {
    // this.$store.commit('login/CHANGE_VISIBLE', true);
    return;
  }
  const commentContent = content.value.replace(/<\/?p[^>]*>/gi, '');
  if (!commentContent) {
    // this.$message('还没输入内容呢~');
    return;
  }
  const params = {
    content: commentContent,
    articleId: articleId.value
  };
  // const email = userInfo.value.email;
  // if (!email) {
  //   this.$confirm('没绑定邮箱接收不到回复提醒哦~', '提示', {
  //     confirmButtonText: '马上绑定',
  //     cancelButtonText: '下次一定',
  //     showClose: false,
  //     type: 'warning'
  //   }).then(() => {
  //     this.$router.push('/email-validate');
  //   }).catch(() => {
  //     cloading.value = true;
  //     addComment(params).then(
  //       res => {
  //         cloading.value = false;
  //         this.$message({
  //           message: '评论成功',
  //           type: 'success'
  //         });
  //         content.value = '';
  //         reload();
  //       },
  //       error => {
  //         
  //         cloading.value = false;
  //       }
  //     );
  //   });
  // } else {
  //   cloading.value = true;
  //   addComment(params).then(
  //     res => {
  //       cloading.value = false;
  //       this.$message({
  //         message: '评论成功',
  //         type: 'success'
  //       });
  //       content.value = '';
  //       reload();
  //     },
  //     error => {
  //       
  //       cloading.value = false;
  //     }
  //   );
  // }
};

// 删除回复提交
const delReplySubmit = (reply) => {
  const params = { replyId: reply.id };
  // deleteReply(params).then(
  //   res => {
  //     this.$message({
  //       message: '删除成功',
  //       type: 'success'
  //     });
  //     reload();
  //   }
  // );
};

// 点击回复
const reClick = (commentId, toUserId) => {
  if (!userInfo.value) {
    this.$store.commit('login/CHANGE_VISIBLE', true);
    return;
  }
  reEditVisible.value = true;
  commentId.value = commentId;
  toUserId.value = toUserId;
};

// 回复提交
const reSubmit = () => {
  const content = recontent.value.replace(/<\/?p[^>]*>/gi, '');
  if (!content) {
    this.$message('还没输入内容呢~');
    return;
  }

  const params = {
    commentId: commentId.value,
    articleId: articleId.value,
    toUserId: toUserId.value,
    content: content
  };
  const email = userInfo.value.email;
  if (!email) {
    this.$confirm('没绑定邮箱接收不到回复提醒哦~', '提示', {
      confirmButtonText: '马上绑定',
      cancelButtonText: '下次一定',
      showClose: false,
      type: 'warning'
    }).then(() => {
      this.$router.push('/email-validate');
    }).catch(() => {
      rloading.value = true;
      addReply(params).then(
        res => {
          rloading.value = false;
          reEditVisible.value = false;
          recontent.value = '';
          this.$message({
            message: '回复成功',
            type: 'success'
          });
          reload();
        },
        error => {
          
          rloading.value = false;
        }
      );
    });
  } else {
    rloading.value = true;
    addReply(params).then(
      res => {
        rloading.value = false;
        reEditVisible.value = false;
        recontent.value = '';
        this.$message({
          message: '回复成功',
          type: 'success'
        });
        reload();
      },
      error => {
        
        rloading.value = false;
      }
    );
  }
};

   
</script>

<style lang="less" scoped>
.c-container {
  @import "/@/assets/styles/variables.css";
  width: 100%;
  overflow-x: hidden;

  .re-editor-container {
    margin: 0 auto;
    width: 90%;
  }

  .content-container {
    background: #fff;
    margin: 0 auto;
    margin-top: 15px;
    border-radius: 2px;

    .content-head {
      padding: 0 20px;

      .main-tip-label {
        border-left: 5px solid #00a77c;
        border-bottom: 1px solid #00a77c;
        font-size: 16px;
        font-weight: bold;
        line-height: 2;
        color: #545454;
        padding-left: 10px;
      }

      .edit-container {
        margin: 0 auto;
        padding: 0 50px;

        @media screen and (max-width: 960px){
          padding: 0;
        }

        /deep/ .ql-container.ql-snow {
          border: none;
        }

        /deep/ .ql-toolbar.ql-snow {
          border: none;
        }

        /deep/ .ql-editor {
          border: 1px solid #e74851;
          border-radius: 5px;
          min-height: 120px;
          padding: 20px;

          @media screen and (max-width: 960px) {
            min-height: 80px;
          }
        }

        /deep/ .ql-stroke {
          stroke: #e74851;
        }

        /deep/ .ql-fill {
          fill: #e74851;
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
          border:none;
          color: #fff;
          background: #e74851;
          font-size: 14px;
        }
      }
    }

    .content-box {

      .main-tip-label {
        border-left: 5px solid #00a77c;
        border-bottom: 1px solid #00a77c;
        font-size: 16px;
        font-weight: bold;
        line-height: 2;
        color: #545454;
        padding-left: 10px;

        .right {
          float: right;
          padding-right: 5px;
          font-weight: normal;
          font-size: 12px;
          position: relative;
          top: 10px;
        }
      }

      .content-list {
        margin: 0;
        padding: 0;

        .list-item {
          list-style: none;
          border-bottom: 1px dashed #e5e5e5;
          margin-bottom: 2px;
          padding-bottom: 0;
          overflow: hidden;
          margin-top: 15px;

          .cmt-li-title {
            float: left;

            .headimg {
              height: 42px;
              width: 42px;
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
            float: left;
            width: 91%;

            @media screen and (max-width: 960px) {
              width: 82.9%;
            }

            @media screen and (max-width: 375px) {
              width: 79.5%;
            }

            .top {
              height: 25px;
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
                float: right;
                color: silver;
              }
            }

            .body-text {
              font-size: 14px;
              color: #333;
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

    .list-empty {
      text-align: center;
      line-height: 50px;
    }
  }

  .el-pagination {
    margin: 30px;
    text-align: center;
  }
}

</style>

<style scoped lang="scss">
.rely-dialog >>> .el-button--text {
  color: #999;
}

.rely-dialog >>> .el-button--text:hover {
  color: #333;
}

.rely-dialog >>> .dialog__body {
  padding: 10px;
}

.rely-dialog >>> .el-dialog__header {
  display: none;
}

.rely-dialog >>> .el-dialog__body {
  padding: 0;
}
.rely-dialog >>> .ql-container.ql-snow {
  border: none;
}

.rely-dialog >>> .ql-toolbar.ql-snow {
  border: none;
}

.rely-dialog >>> .ql-editor {
  border: 1px #e74851 solid;
  border-radius: 5px;
  min-height: 80px;
}

.rely-dialog {
  margin-right: 140px;

  @media screen and (max-width: 960px) {
    margin-right: 0;
  }
}
</style>
