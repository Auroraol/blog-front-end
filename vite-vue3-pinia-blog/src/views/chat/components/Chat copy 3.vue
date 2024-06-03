<template>
  <el-row :gutter="20">
    <el-col :span="12" :offset="6">
      <el-card shadow="hover" class="box-card">
        <div slot="header" class="clearfix">
          <span>消息列表</span>
          <el-button
            v-show="logOutShowFlag"
            style="float: right; padding: 3px 0"
            @click="logOut"
            type="text"
            >注销</el-button
          >
          <el-button
            style="float: right; padding: 3px 8px"
            @click="cleanMsg"
            type="text"
            >清空消息</el-button
          >
        </div>
        <div id="msgList" style="overflow: auto; height: 450px">
          <div v-for="msg in msgList" :key="msg" class="text item">
            <b>{{ msg.msgDate }}</b
            >&nbsp;
            <span v-if="msg.msgType == '00'"
              ><span style="color: blue">{{ msg.sourceUserName }}</span
              ><b>: </b>{{ msg.msgContent }}</span
            >
            <span v-if="msg.msgType == '01'" style="color: blue"
              >{{ msg.sourceUserName }}进入了聊天室</span
            >
            <span v-if="msg.msgType == '02'" style="color: gray"
              >{{ msg.sourceUserName }}离开了聊天室</span
            >
          </div>
        </div>
      </el-card>
      <el-card shadow="hover" class="box-card">
        <el-row :gutter="10">
          <el-col :span="21">
            <el-input
              v-model.trim="msgContent"
              placeholder="请输入内容"
              @keyup.enter.native="sendMsg"
              clearable="true"
            ></el-input>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" :loading="sendLoading" @click="sendMsg"
              >发送</el-button
            >
          </el-col>
        </el-row>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { io } from "socket.io-client";

// 按钮加载条控制
const sendLoading = ref(false);
// 当前用户名
const userName = ref("刘分解");
// socket 实例
let socket = null;
// 消息类型
const msgType = ref("00");
// 消息数据
const msgContent = ref("");
// 消息列表
const msgList = ref([]);
// 注销按钮是否显示
const logOutShowFlag = ref(false);

// 连接Socket
const connect = () => {
  logOutShowFlag.value = true;

  socket = io("http://localhost:9974", {
    query: "userName=刘丰洁",
    // 放弃之前尝试重新连接的次数，默认值 Infinity
    reconnectionAttempts: 3,
    // 每次连接等待时间 默认值 1000
    reconnectionDelay: 3000,
    // transports: ["websocket"],
    transports: ["websocket", "polling"], // 优先使用websocket连接方式
  });

  // 监听message事件
  socket.on("message", (message) => {
    console.log(message);
  });

  // 接受消息 eventName事件名, data数据
  socket.on("bbbb", (data) => {
    //....收到消息后具体操作

    alert(data);
  });

  // 监听receiveMsg接收消息事件
  socket.on("receiveMsg", (data) => {
    console.error(data);
    data.msgDate = moment().format("HH:mm:ss");
    if (data.msgType === "00") {
      // 发送消息给全部人
      showNotification(
        data.sourceUserName,
        "<b>在" + data.msgDate + "说" + data.msgContent + "</b>"
      );
    } else if (data.msgType === "01") {
      // 上线通知
      showNotification(
        data.sourceUserName,
        data.msgDate + "<b>进入了聊天室</b>"
      );
    } else if (data.msgType === "02") {
      // 下线通知
      showNotification(
        data.sourceUserName,
        data.msgDate + "<b>离开了聊天室</b>"
      );
    }
    msgList.value.push(data);
    nextTick(() => {
      const msgListDiv = document.getElementById("msgList");
      msgListDiv.scrollTop = msgListDiv.scrollHeight;
    });
  });
  /*showNotification({
        title: userName.value,
        message: '<b>欢迎来到聊天室</b>',
        duration: 0
    });*/
};

// 显示通知
const showNotification = (title, message) => {
  // 使用您的通知库显示通知
};

onMounted(() => {
  connect();
});

//组件销毁要退出连接
onBeforeUnmount(() => {
  socket.disconnect();
});

// 启动时执行
const mounted = () => {
  connect();
  //   // 获取Cookie的UserName
  //   const userNameCookie = getCookie("00-Netty-SocketIO-UserName");
  //   if (userNameCookie) {
  //     // 存在直接连接
  //     userName.value = userNameCookie;
  //     connect();
  //   } else {
  //     // 不存在需要输入用户名
  //     getUser();
  //   }
};

// // 输入用户名
// const getUser = () => {
//   const value = prompt("请输入用户名进入聊天室");
//   if (value) {
//     userName.value = value;
//     setCookie("00-Netty-SocketIO-UserName", userName.value, 1);
//     connect();
//   } else {
//     alert("用户名不能为空");
//   }
// };

const sendMsg = () => {
  //   if (userName.value) {
  sendLoading.value = true;
  // 消息不能为空
  if (msgContent.value) {
    socket.emit("sendMsg", {
      sourceUserName: userName.value,
      msgType: msgType.value,
      msgContent: msgContent.value,
    });
  } else {
    console.error("消息不能为空");
  }
  msgContent.value = "";
  sendLoading.value = false;
  //   } else {
  // getUser();
  //   }
};

// 清空消息
const cleanMsg = () => {
  // this.$nextTick Dom渲染完执行
  nextTick(() => {
    confirm("你确定清空消息吗", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      msgList.value = [];
    });
  });
};

// 注销
const logOut = () => {
  // 删除Cookie
  delCookie("00-Netty-SocketIO-UserName");
  // 刷新页面
  document.location.reload();
};

// 设置Cookie
const setCookie = (cname, cvalue, exdays) => {
  const d = new Date();
  d.setTime(d.getTime() + exdays * 24 * 60 * 60 * 1000);
  const expires = "expires=" + d.toGMTString();
  document.cookie = cname + "=" + cvalue + "; " + expires;
};

// 获取Cookie
const getCookie = (cname) => {
  const name = cname + "=";
  const ca = document.cookie.split(";");
  for (let i = 0; i < ca.length; i++) {
    const c = ca[i].trim();
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
};
</script>


<style scoped>
body {
  background-color: #ececec;
  overflow: hidden;
}

.box-card {
  margin-bottom: 10px;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

::-webkit-scrollbar {
  width: 15px;
  height: 15px;
}

/* 谷歌浏览器滚动条美化 */
::-webkit-scrollbar-track,
::-webkit-scrollbar-thumb {
  border-radius: 999px;
  border: 5px solid transparent;
}

::-webkit-scrollbar-track {
  box-shadow: 1px 1px 5px rgba(143, 143, 143, 0.2) inset;
}

::-webkit-scrollbar-thumb {
  min-height: 20px;
  background-clip: content-box;
  box-shadow: 0 0 0 5px rgba(143, 143, 143, 0.466) inset;
}

::-webkit-scrollbar-corner {
  background: transparent;
}
</style>