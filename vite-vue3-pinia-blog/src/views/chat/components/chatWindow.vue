<template>
  <div class="chatWindow-container">
    <!-- <el-divider /> -->
    <div class="header"></div>

    <!-- 聊天窗口 -->
    <div class="chatMain" ref="chatMainWindow">
      <!-- 消息盒子 -->
      <div class="chatLogBox" v-for="item in chatLogBox.logInfo" :key="item.id">
        <!-- 头像 -->
        <img
          style="cursor: pointer"
          :src="item.head"
          alt=""
          @click="goPersonalCenter(item.account)"
        />
        <!-- 右侧 -->
        <div class="right">
          <!-- 昵称和时间 -->
          <div class="nameAndTme">
            <span>{{ item.nickName }}</span>
            <span>{{ item.date }}</span>
          </div>
          <!-- 内容 -->
          <div class="chatContent" style="white-space: pre-line">
            {{ item.content }}
          </div>
        </div>
      </div>
    </div>
    <!-- 输入窗口 -->
    <div class="iptChat">
      <el-input
        v-model="chatLogIpt"
        :input-style="{ borderRadius: '1rem', flex: '1', height: '100px' }"
        resize="none"
        :maxlength="1000"
        :show-word-limit="true"
        type="textarea"
        :autosize="{ minRows: 4, maxRows: 6 }"
        placeholder="发送消息 按Ctrl + Enter发送"
        @keydown="sendKeyDown"
      />
      <button @click="sendChatLog">
        <div class="svg-icon-container">
          <svg-icon name="fasong2" style="margin-top: 7px; color: #fff">
          </svg-icon>
          <span style="margin-left: 0.3rem; color: #fff">发送</span>
        </div>
      </button>
    </div>
  </div>

  <!-- 登记弹窗 -->
  <el-dialog
    v-model="registerModel"
    title="选择一个身份"
    width="500"
    @close="close"
  >
    <el-form ref="form" :model="chatUserInfo" :rules="rules">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="chatUserInfo.name"
          autocomplete="off"
          style="width: 300px"
        />
      </el-form-item>

      <el-form-item label="头像">
        <el-radio-group v-model="chatUserInfo.avatar" class="ml-4">
          <el-radio
            :label="item"
            size="large"
            v-for="(item, index) in avatars"
            :key="index"
          >
            <img :src="avatarFilter(item)" v-avatar alt="" />
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          size="large"
          style="width: 100%"
          @click="submit"
          >选择</el-button
        >
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, toRaw, reactive, onMounted } from "vue";

// import useAxios from '@/hooks/axios/axios'
// import { ElMessage } from 'element-plus'
// import { ChatLog, ChatLogBox } from '@/hooks/Types/types'
// import { useStore } from "@/store/count";
// import { socket } from "@/hooks/socket/socket";
// import { goToPersonalCenterHook } from "@/hooks/goToPersonalCenter/goToPersonalCenter";
import { useRoute } from "vue-router";
import { ChatLog, ChatLogBox, ChatUserInfo } from "/@/types/chat";
import { useChatStore } from "/@/store/index";
import { useGetters } from "/@/store/getters";
import { FormInstance } from "element-plus";

import { getChatListAPI, addChatDataAPI } from "/@/api/chat/chat";
import { socket } from "/@/utils/socket/socket";
import { formatDate } from "/@/utils/format/format-time";

const useChatStorePinia = useChatStore();

const chatUserInfoPinia = computed(() => useGetters().chatUserInfo);

const route = useRoute();

type Props = {
  logInfo: Array<ChatLog>;
};
const props = defineProps<Props>();
const logInfoProps = toRaw(props.logInfo);

const chatLogIpt = ref(""); //输入的聊天内容
const chatMainWindow = ref(); //获取聊天视窗的dom，自动滚动

// 聊天数据
let chatLogBox = reactive({
  logInfo: new Array<ChatLogBox>(),
});

// 登记框
const form = ref<FormInstance>();
const registerModel = ref<boolean>(false);
// 头像列表
const avatars = [
  "Ginger",
  "Patches",
  "Sadie",
  "Casper",
  "Molly",
  "Smokey",
  "Lilly",
];
const avatarFilter = (v: string) =>
  `https://api.dicebear.com/7.x/fun-emoji/svg?seed=${v}`;

// 名称校验规则
const rules = reactive({
  name: [
    { required: true, message: "名称不能为空", trigger: "blur" },
    {
      min: 1,
      max: 10,
      message: "名称长度限制为 1 ~ 10个字符",
      trigger: "blur",
    },
  ],
});

// 用户信息
const chatUserInfo = reactive<ChatUserInfo>({
  name: "",
  avatar: "Ginger",
});

// 关闭弹框触发
const close = () => {
  form.value?.resetFields();
  chatUserInfo.name = "";
  chatUserInfo.avatar = "Ginger";
};

// 提交表单触发
const submit = async () => {
  if (!form.value) return;
  await form.value.validate((valid, fields) => {
    if (valid) {
      useChatStorePinia.updateChatUserInfo2(
        chatUserInfo.name,
        chatUserInfo.avatar
      );
      registerModel.value = false;

      ElMessage({
        message: "🎉选择成功~",
        type: "success",
      });
    } else {
      console.log("error submit!", fields);
    }
  });
};

//
onMounted(() => {
  //把聊天窗口滚动到最底部
  const chatDom = chatMainWindow.value as HTMLElement;
  const observer = new MutationObserver((mutationsList) => {
    // 滚动到最底部
    chatDom.scrollTop = chatDom.scrollHeight;
  });
  observer.observe(chatDom, { childList: true });
  //
  shiftPropsLogInfo();
});

//定义新消息盒子
const newChatLogInfo = async (newLog: ChatLog) => {
  //   const { data: res } = await useAxios.get("/userinfo", {
  //     params: { account: newLog.account },
  //   });
  const info: ChatLogBox = {
    // id: newLog.id,
    account: newLog.account,
    // head: res.data.headImg,
    // nickName: res.data.nickName,
    date: newLog.date,
    content: newLog.content,
  };
  return info;
};

//拿到props传来的消息记录，然后请求对应的用户进行补全，最后更新要渲染的消息记录的数组
const shiftPropsLogInfo = async () => {
  for (let i = 0; i < logInfoProps.length; i++) {
    chatLogBox.logInfo.push(await newChatLogInfo(logInfoProps[i]));
  }
};

//从url获取当前房间名字
const herfRoomName = () => {
  const roomName = route.query.name; //房间名字
  return roomName;
};

//清空输入框
const clearChatLog = () => {
  chatLogIpt.value = "";
};

//发送消息
const sendChatLog = async () => {
  // 没有选择身份，不允许发送消息
  //   if (useChatStorePinia.isNull) {
  //     console.error(chatUserInfoPinia.value);
  //     registerModel.value = true;
  //     return;
  //   }

  if (chatLogIpt.value === "") {
    ElMessage({
      message: "不能发送空消息",
      type: "warning",
    });
    return;
  }
  const roomName = herfRoomName();
  //   const account = JSON.parse(window.atob(localStorage.getItem("userAccount")!));
  const account = "userAccount";
  try {
    //     const roomArr = await getChatListAPI({ name: roomName });
    //     let newId;
    //     if (!roomArr)
    //       return ElMessage.error(`你发送消息的房间 ${herfRoomName()} 已经被删除`);
    //     if (roomArr.length === 0) {
    //       newId = 0;
    //     } else {
    //       newId = Number(roomArr[roomArr.length - 1].id) + 1;
    //     }

    //更新数据库的聊天记录
    const newLog = {
      roomName: roomName,
      account: account,
      content: chatLogIpt.value,
      date: formatDate(new Date(), "YYYY-mm-dd HH:MM:SS"),
    };

    //   try {
    //  保存到数据库
    await addChatDataAPI(newLog);

    // socket广播给所有用户更新本地记录，不需要重新请求渲染消息记录
    socketUploadLog(newLog);

    //清空输入框
    clearChatLog();
  } catch (error) {
    console.error(error);
  }
};

//socket广播更新记录
const socketUploadLog = async (newLog: any) => {
  // 参数传递的是一个数组
  socket.emit("uploadLocalLog", herfRoomName(), await newChatLogInfo(newLog));
};

socket.on("uploadLocalLogBroadcast", (data1, data2) => {
  if (herfRoomName() === data1) {
    chatLogBox.logInfo.push(data2);
  }
});

//键盘enter发送消息
const sendKeyDown = (e: any) => {
  //按 Ctrl + Enter
  if (e.ctrlKey && e.key === "Enter") {
    sendChatLog();
  }

  // 按Enter
  //   if (e.key === "Enter") {
  //     e.preventDefault(); //阻止默认的换行行为
  //   }
};

// 点击头像
const goPersonalCenter = (account: any) => {
  //   goToPersonalCenterHook(account);
};
</script>

<style scoped lang="less">
.chatWindow-container {
  box-sizing: border-box;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border-bottom: 1px solid #eee;
  background-color: #fff;

  .header {
    height: 30px;
    line-height: 80px;
    padding-left: 40px;
    font-weight: 900;
    font-size: 18px;
  }

  .chatMain {
    background-color: #f9f9f9;
    flex: 1;
    overflow-y: auto;
    scroll-behavior: smooth;
    .chatLogBox {
      display: flex;
      align-items: center;
      box-sizing: border-box;
      padding: 1rem;
      border-radius: 1rem;
      transition: all 0.3s;

      &:hover {
        background-color: var(--chat-gray-back);
      }

      img {
        align-self: flex-start;
        width: 4rem;
        border-radius: 50%;
      }

      .right {
        margin-left: 1rem;

        .nameAndTme {
          :nth-child(1) {
            font-weight: 600;
            font-size: 1.3rem;
            color: red;
          }

          :nth-child(2) {
            color: rgb(33, 33, 33);
            margin-left: 0.5rem;
          }
        }

        .chatContent {
          font-size: 1.1rem;
        }
      }
    }
  }

  .iptChat {
    display: flex;
    align-items: center;
    margin-left: 0.5rem;
    margin-right: 0.5rem;
    margin-top: 0.5rem;
    button {
      box-sizing: border-box;
      height: 3rem;
      border-radius: 1rem;
      margin-left: 0.5rem;
      background-color: var(--chat-gray-back);
      color: #fff;
      transition: all 0.3s;
      padding: 0 0.8rem;

      &:hover {
        cursor: pointer;
        background-color: var(--special-font-color);
      }
    }
  }
}

.svg-icon-container {
  display: flex; /* 使用 Flex 布局 */
  align-items: center; /* 垂直居中对齐 */
}

@media screen and (max-width: 800px) {
  .el-divider--horizontal {
    display: none;
  }
}

:deep(.el-form-item__content) {
  .el-radio.el-radio--large {
    margin-bottom: 30px;
    margin-top: 10px;

    img {
      width: 50px;
      height: 50px;
      border-radius: 15px;
    }
  }
}
</style>