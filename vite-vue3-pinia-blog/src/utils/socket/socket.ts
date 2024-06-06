import { io } from 'socket.io-client'
import { getAccessToken } from "/@/utils/auth";

// // 连接服务器
let socketHttpUrl = 'http://101.37.165.220:9974/Chat'    // 命名空间为/Chat
// let socketHttpUrl = 'http://localhost:9074/Test' // 命名空间为/Test
// let socketHttpUrl = 'http://localhost:9074'     // 无命名空间

const socket = io(socketHttpUrl, {
    query: "Authorization=" + getAccessToken(),  // 参数  // 改成动态的
    autoConnect: false,         // 是否自动连接
    reconnection: true,       // 是否自动重新连接
    reconnectionAttempts: 3,    // 放弃之前尝试重新连接的次数，默认值 Infinity
    reconnectionDelay: 3000,    // 每次连接等待时间 默认值 1000
    transports: ["websocket", "polling"], // 优先使用websocket连接方式
  });

//监听器，不论事件名称是什么，都会执行相同的回调函数
socket.onAny((event, ...args) => {
    // console.log(event, args);
});

//当客户端与服务器成功建立连接时，Socket.IO 客户端会触发名为 'connect' 的事件
socket.on('connect', () => { }) //连接socket

socket.io.on("reconnect", (attempt) => {
    ElMessage({
        message: '重新连接成功',
        type: 'success',
    })
});

socket.io.on('error', (error) => {
    ElMessage.error('网络连接错误' + error)
})

export {
    socket
}

/**
 * 使用
// socket.connect();     // 手动触发连接
// socket.disconnect()   // 手动断开连接
// 接收消息   socket.on(事件的名称, (data...)=>{ //回调数据 })
// 发送消息   socket.emit(事件的名称, 发送的数据, (data...)=>{//回调数据})
 */

