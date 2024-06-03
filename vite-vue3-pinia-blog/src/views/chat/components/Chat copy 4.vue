<template>
  <input type="button" value="发送点对点消息" @click="send1" />
  <br /><br />
  <input type="button" value="开启无限推送测试" @click="send2" />
  <br /><br />
  <input type="button" value="测试加入房间" @click="send3" />
  <br /><br />
  <input type="button" value="测试房间内发消息" @click="send4" />
  <br /><br />
  <input type="button" value="测试发送广播消息" @click="send5" />
  <br /><br />
</template>

<script setup>
import { ref, onMounted } from "vue";
// import { socket } from "/@/utils/socket/socket";
import { io } from "socket.io-client";

// socket 实例
let socket = null;
let socket2 = null;
let socket3 = null;

onMounted(() => {
  // socket.connect(); //连接socket服务器,登陆者账号也被发送到后端
  // socket = io("http://localhost:9974/chat?userName=" + new Date()); //正式发布环境

  socket = io("http://localhost:9974/Chat", {
    // note changed URL here
    // path: "/socket.io/chat",
    query: "Authorization=刘丰洁",
    // autoConnect: false,
    transports: ["websocket"],
  });

  socket2 = io("http://localhost:9974/mynamespace", {
    // note changed URL here
    // path: "/socket.io/chat",
    query: "Authorization=刘丰洁",
    // autoConnect: false,
    transports: ["websocket"],
  });

  socket3 = io("http://localhost:9974", {
    // note changed URL here
    // path: "/socket.io/chat",
    query: "Authorization=刘丰洁",
    // autoConnect: false,
    transports: ["websocket"],
  });

  listen();
});

//组件销毁要退出连接
onBeforeUnmount(() => {
  // socket.disconnect();
});

const listen = () => {
  // ==============以下使用命名空间chat========================

  //监听点对点消息
  socket.on("bbbb", function (data) {
    //....收到消息后具体操作
    console.error(data);
  });

  //监听后端无限推送的点对点消息
  socket.on("testPush", function (data) {
    console.error("接收到消息的次数:" + ++i);
  });

  //监听加入房间的反馈
  socket.on("testJoinRoom", function (data) {
    console.error("接收到消息:" + data);
  });

  //监听房间消息
  socket.on("testRoom", function (data) {
    console.error("接收到消息:" + data);
  });

  //监听广播消息
  socket.on("testBroadcast", function (data) {
    console.error("接收到消息:" + data);
  });

  //监听广播消息
  socket.on("testNamespace", function (data) {
    console.error("接收到消息:" + data);
  });

  // 监听receiveMsg接收消息事件
  socket3.on("receiveMsg", (data) => {
    console.error(data);
  });
};

// 发送消息
const send1 = () => {
  socket.emit("aaaa", "aaaaaa", (data) => {
    console.error(data);
  });

  socket2.emit("message", "傻傻傻傻呼呼");

  socket3.emit("sendMsg", {
    sourceUserName: "userName.value",
    msgType: "msgType.value",
    msgContent: "msgContent.value",
  });
};

// 触发无限推送
const send2 = () => {
  socket.emit("testPush", "begin");
};

// 发送加入房间消息
const send3 = () => {
  socket.emit("joinRoom", "room1");
};

// 发送房间消息
const send4 = () => {
  socket.emit("testRoom", "testRoomData");
};

// 发送广播消息
const send5 = () => {
  socket.emit("testBroadcast", "testBroadCastData");
};

const sendMsg = () => {};
</script>


<style scoped>
</style>