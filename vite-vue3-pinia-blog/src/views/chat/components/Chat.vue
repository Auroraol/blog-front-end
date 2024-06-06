<template>
  <div class="chat-container">
    <!-- 左侧公告和房间 -->
    <div class="roomsListBox">
      <!-- 房间列表标题 -->
      <div
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
          box-sizing: border-box;
          padding: 0.5rem 1rem;
          border-bottom: 1px solid rgb(184 181 181);
        "
      >
        <span style="font-size: 1.2rem">房间列表</span>
        <span
          style="font-weight: 700; font-size: 1.5rem; cursor: pointer"
          @click="addRoom"
          >+</span
        >
      </div>
      <div v-if="addNewRoom" class="addRoomBox">
        <el-input
          v-model="inputNewRoom"
          size="small"
          maxlength="8"
          minlength="1"
          :input-style="{
            marginBottom: '.2rem',
          }"
          style="padding-left: 0.5rem; padding-right: 0.5rem"
          placeholder="添加房间"
          :show-word-limit="true"
        />
        <button
          style="
            margin-left: 0.5rem;
            background-color: #fff;
            padding: 0.1rem 0.8rem;
            cursor: pointer;
            margin-right: 1rem;
            border-radius: 1rem;
          "
          @click="cancelAddRoom"
        >
          取消
        </button>
        <button
          style="
            background-color: var(--special-font-color);
            color: #fff;
            padding: 0.1rem 0.8rem;
            cursor: pointer;
            border-radius: 1rem;
          "
          @click="setNewRoom"
        >
          确定
        </button>
      </div>
      <!-- 房间列表 -->
      <div class="roomsList">
        <router-link
          :class="`roomsItem ${
            route.query.name === item.name ? 'routerLinkActive' : ''
          }`"
          v-for="(item, index) in allRooms"
          :key="index"
          :to="{ path: '/chat', query: { name: item } }"
          @click="clickRoomLog(item)"
        >
          <div class="svg-icon-container">
            <span>{{ item }}</span>
            <el-popconfirm
              width="160"
              :icon="InfoFilled"
              icon-color="#626AEF"
              title="确定要删除房间？"
              @confirm="delRoom(item)"
            >
              <template #reference>
                <svg-icon class="delBtn" color="#fff" name="shanchu"></svg-icon>
              </template>
            </el-popconfirm>
          </div>
        </router-link>
      </div>
    </div>
    <!-- 中间聊天区域 -->
    <div class="main">
      <ChatWindow :key="uploadChatWindow" :logInfo="chatLogInfo"></ChatWindow>
    </div>
    <!-- 右侧在线列表 -->
    <div class="onlineList">
      <span>在线 - {{ onlineList.length }}</span>
      <div
        class="userList"
        v-for="(item, index) in onlineList"
        :key="index"
        @click="goPersonalCenter(item)"
      >
        <img
          style="
            width: 3.5rem;
            border-radius: 50%;
            margin: 0.2rem;
            margin-right: 0.5rem;
          "
          :src="item[1] || defaultAvatar"
          alt=""
        />
        <span
          style="
            font-size: 1.2rem;
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden;
          "
          >{{ item[0] }}
          <span v-if="item.root" style="color: red">*</span>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { socket } from "/@/utils/socket/socket";
import ChatWindow from "./chatWindow.vue";

import { useRoute, useRouter } from "vue-router";
import { getAccessToken } from "/@/utils/auth";
import { ChatLog, ChatLogBox, ChatInfo, ChatUserInfo } from "/@/types/chat";
import { useChatStore, useSettingsStore } from "/@/store/index";
import {
  chatList,
  addChatRoom,
  deleteChatRoom,
  getChatListAPI,
} from "/@/api/chat/chat";

const defaultAvatar = computed(() => useSettingsStore().defaultAvatar);

const router = useRouter();
const route = useRoute();

let allRooms = ref();
let onlineList = ref<any>([]); //在线列表
const inputNewRoom = ref(""); //添加的新房间的名字
const addNewRoom = ref(false); //添加房间
const uploadChatWindow = ref(0); //刷新子组件
let chatLogInfo = ref<ChatLog[]>([]); //消息记录，传给子组件

onMounted(() => {
  socket.connect(); //连接socket服务器,登陆者账号token发送到后端
  getAllRooms();
});

//组件销毁要退出连接
onBeforeUnmount(() => {
  socket.disconnect();
});

//进入页面主动获取列表
socket.emit("getUsers", (res: any) => {
  onlineList.value = res;
});

// 有人登录/退出 被动获取当前在线列表
socket.on("loginUsers", (data) => {
  onlineList.value = data;
});

//向后端发送emit，然后向所有前端广播，所有前端都更新房间列表
const sendChangeRoom = () => {
  socket.emit("roomsChange");
};

socket.on("changeRoomsBroadcast", () => {
  getAllRooms();
});

//进入页面, 从数据获取房间列表
const getAllRooms = async () => {
  const res = await chatList();
  allRooms.value = res;
  // console.error(res);

  getRoomLog("默认房间"); //刚进入页面默认请求默认房间的消息记录
};

//请求对应的房间消息记录
const getRoomLog = async (name: string) => {
  try {
    const res = await getChatListAPI({ name: name });
    chatLogInfo.value = res as {
      roomName: string;
      account: string;
      content: string;
      date: string;
    }[];
    uploadChatWindow.value++;
  } catch (error) {
    console.error(error);
  }
};

//点击房间名字要请求对应的消息记录
const clickRoomLog = (name: string) => {
  //如果点击的房间就是现在所在的房间就不请求
  const nowUrl = decodeURI(window.location.href).split("/");
  const nowRoom = nowUrl[nowUrl.length - 1];
  if (name === nowRoom) return;
  //否则请求
  getRoomLog(name);
};

//添加房间
const addRoom = () => {
  addNewRoom.value = true;
};

//取消添加房间
const cancelAddRoom = () => {
  addNewRoom.value = false;
};

//确定添加房间
const setNewRoom = async () => {
  if (inputNewRoom.value === "") {
    ElMessage({
      message: "房间名不能为空",
      type: "warning",
    });
    return;
  }
  const params = {
    name: inputNewRoom.value,
  };
  try {
    const res = await addChatRoom(params);
    ElMessage({
      message: "添加成功",
      type: "success",
    });
    cancelAddRoom();
    sendChangeRoom(); //更新
    inputNewRoom.value = "";
  } catch (error) {
    console.error(error);
  }
};

//删除房间
const delRoom = async (roomName: string) => {
  const params = {
    name: roomName,
  };

  try {
    const res = await deleteChatRoom(params);
    if (res) {
      ElMessage({
        message: "删除成功",
        type: "success",
      });
      sendChangeRoom();
    } else {
      ElMessage.error("删除失败");
    }
  } catch (error) {
    console.error(error);
  }
};

//点击头像去个人空间
const goPersonalCenter = (item: any) => {
  // goToPersonalCenterHook(item.account);
};
</script>

<style scoped lang="less">
.chat-container {
  box-sizing: border-box;
  padding: 0.5rem;
  width: 80%;
  // height: 84vh;
  height: 86vh;
  margin: 0 auto;
  border-right: 1px solid #eee;
  background-color: #2b333e;
  display: grid;
  grid-template-columns: 15rem 1fr 16rem;
  border-radius: 1rem;

  span {
    color: #fff;
  }

  .roomsListBox {
    display: flex;
    flex-direction: column;

    .roomsList {
      display: flex;
      flex-direction: column;
      position: relative;
      overflow-y: scroll;

      .roomsItem {
        box-sizing: border-box;
        padding: 0.6rem;
        margin: 0.2rem 0;
        border-radius: 0.5rem;
        transition: all 0.3s;
        color: #fff;
        position: relative;

        &:hover {
          cursor: pointer;
          background-color: var(--chat-gray-back);
        }

        .svg-icon-container {
          display: flex; /* 使用 Flex 布局 */
          align-items: center; /* 垂直居中对齐 */
        }

        .delBtn {
          position: absolute;
          top: 50%;
          right: 5%;
          transform: translateY(-50%);
          color: #fff;
          background-color: transparent;

          &:hover {
            cursor: pointer;
            color: @color;
          }
        }
      }
    }
  }

  .main {
    // overflow-y: scroll;
    height: 100%;
    overflow-y: auto;
  }

  .onlineList {
    overflow-y: auto;
    box-sizing: border-box;
    padding: 0.5rem;
    border-left: 1px solid var(--chat-gray-back);

    .userList {
      display: flex;
      align-items: center;
      transition: all 0.3s;

      &:hover {
        cursor: pointer;
        color: var(--special-font-color);
      }
    }
  }
}

.routerLinkActive {
  background-color: var(--chat-gray-back);
}

@media screen and (max-width: 1050px) {
  .chat-container {
    width: 100%;
  }
}

@media screen and (max-width: 800px) {
  .chat-container {
    grid-template-columns: none;
    grid-template-rows: 10rem 1fr;

    .roomsListBox {
      overflow-y: scroll;
    }

    .onlineList {
      display: none;
    }
  }
}
</style>