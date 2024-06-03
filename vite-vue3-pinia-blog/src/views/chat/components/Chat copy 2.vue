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
    
<script setup lang='ts'>
import { socket } from "/@/utils/socket/socket";

onMounted(() => {
  socket.connect(); //连接socket服务器,登陆者账号也被发送到后端
});

//组件销毁要退出连接
onBeforeUnmount(() => {
  socket.disconnect();
});

const initSocket = () => {
  socket = io("http://localhost:8974/chat?Authorization=" + new Date()); //正式发布环境
  socket = io("http://localhost:8974/test"); //正式发布环境
  socket.on("connect", function () {
    console.log("socket连接成功");
  });

  socket.on("disconnect", function () {
    console.log("socket断开连接");
  });

  // ==============以下使用命名空间chat========================

  //监听广播消息
  socket.on("testNamespace", function (data) {
    console.log("接收到消息:" + data);
  });

  //监听点对点消息
  socket.on("bbbb", function (data) {
    //....收到消息后具体操作
    //  console.log(data);
    console.log(data);
  });

  //监听后端无限推送的点对点消息
  socket.on("testPush", function (data) {
    console.log("接收到消息的次数:" + ++i);
  });

  //监听加入房间的反馈
  socket.on("testJoinRoom", function (data) {
    console.log("接收到消息:" + data);
  });

  //监听房间消息
  socket.on("testRoom", function (data) {
    console.log("接收到消息:" + data);
  });

  //监听广播消息
  socket.on("testBroadcast", function (data) {
    console.log("接收到消息:" + data);
  });
};

// 发送消息
const send1 = () => {
  socket.emit("aaaa", "aaaaaa");
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
</script>
    
<style>
</style>