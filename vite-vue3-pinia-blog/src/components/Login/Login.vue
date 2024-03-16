<template>
  <div class="container">
    <div class="loginImgBox">
      <div class="loginImg">
        <!-- <img src="public/image/imgs/loginBg.jpg" alt="loginImg" /> -->
      </div>
    </div>

    <div class="logBox" v-if="ifLog">
      <div class="top">
        <div class="title">
          <h5>登录</h5>
          <p>Login to Impero's Blog</p>
        </div>
        <form class="box">
          <div class="iptUser">
            <label for="username">用户名</label>
            <el-input
              v-model.trim="logInfo.username"
              maxlength="16"
              placeholder="输入用户名"
              show-word-limit
              type="text"
              name="username"
            />
          </div>
          <div class="iptUser">
            <label for="password">密码</label>
            <el-input
              v-model.trim="logInfo.password"
              maxlength="16"
              placeholder="输入密码"
              show-word-limit
              type="text"
              name="password"
              :show-password="true"
            />
          </div>
        </form>
        <div class="logBtn">
          <div @click="login">登录</div>
          <span @click="goRegister">没有账号？点击注册</span>
        </div>
      </div>
      <div class="message">
        <div>登录后可在留言板块发布留言</div>
        <div>可以发布文章</div>
        <div>可加入在线聊天室</div>
      </div>
    </div>
    <div class="registerBox logBox" v-else>
      <div class="top">
        <div class="title">
          <h5>注册</h5>
          <p>Register to Impero's Blog</p>
        </div>
        <div v-if="name">
          <form class="box">
            <div class="iptUser">
              <label for="registerUsername">用户名</label>
              <el-input
                v-model.trim="registerInfo.username"
                maxlength="16"
                placeholder="输入注册账号(这不是昵称)"
                show-word-limit
                type="text"
                name="registerUsername"
                onkeyup="this.value=this.value.replace(/[^a-z0-9]/g,'');"
              />
            </div>
            <div class="iptUser">
              <label for="registerPassword">密码</label>
              <el-input
                v-model.trim="registerInfo.password"
                maxlength="16"
                placeholder="输入注册密码"
                show-word-limit
                type="text"
                name="registerPassword"
                :show-password="true"
              />
            </div>
            <div class="iptUser">
              <label for="registerPasswordAgain">确认密码</label>
              <el-input
                v-model.trim="registerInfo.againPassword"
                maxlength="16"
                placeholder="再次输入注册密码"
                show-word-limit
                type="text"
                name="registerPasswordAgain"
                :show-password="true"
              />
            </div>
          </form>
          <div class="logBtn">
            <div @click="register">注册</div>
            <span @click="goLogin">已有账号，点击登录</span>
          </div>
        </div>
        <div v-else>
          <div class="nickName">
            <label for="nickname">昵称:</label>
            <el-input
              v-model.trim="nickname"
              :maxlength="pinia.nickNameLength"
              placeholder="起个昵称吧！"
              show-word-limit
              type="text"
              name="nickname"
            />
          </div>
          <div class="logBtn nickNameBtn">
            <div @click="updateNickName">完成</div>
            <div @click="skipNickName">跳过</div>
          </div>
        </div>
      </div>
      <div class="message">
        <div>登录后可在留言板块发布留言</div>
        <div>用户名只能输入小写字母和数字</div>
        <div>密码只能输入大小写字母、数字、下划线</div>
        <div>用户名和密码都要至少6位</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, toRaw } from "vue";
import { useStore } from "/@/store";
import { useRouter } from "vue-router";
import qs from "qs";
import { ElNotification } from "element-plus";
import { useRequest } from "vue-hooks-plus";
import {
  getSysLogin,
  getUserInfo,
  getSysRegister,
  getUpdateNickName,
} from "./services";

const pinia = useStore();
const router = useRouter();
const ifLog = ref(true);
const name = ref(true); // 昵称

//登录信息
const logInfo = reactive({
  username: "",
  password: "",
});

//注册信息
const registerInfo = reactive({
  username: "",
  password: "",
  againPassword: "", // 重复密码
});

//昵称信息
const nickname = ref();

//点击登录
const login = async () => {
  const userLogInfo = toRaw(logInfo); // 将一个由生成的响应式转化为对象普通对象
  // 发起请求
  const { data, error, loading, run } = useRequest(getSysLogin, {
    manual: true, // 手动触发请求
    devKey: "demo1", // 开发者密钥
    onSuccess: (data) => {
      //注意: 在手动触发的情况下, ts中使用data,error,....中的属性
      if (data.code === 400002) {
        alert("账号不存在");
      } else if (data.code === 400004) {
        alert("账户密码不匹配");
      } else if (data.code === 200000) {
        //前端接收到JWT后，将其存储在本地
        localStorage.setItem("accessToken", data.data.accessToken);
        localStorage.setItem("refreshToken", data.data.refreshToken);
        // 得到用户信息
        getInformation();
        router.replace("/index");
      }
    },
    onError: (error) => {
      alert(error);
    },
  });

  run(userLogInfo);
};

const getInformation = () => {
  const { data: responseData, run: infoRun } = useRequest(getUserInfo, {
    onSuccess: (responseData) => {
      if (responseData && responseData.data) {
        // console.table(responseData.data);
        const userAccount = window.encodeURIComponent(  // 加密保存
          JSON.stringify(responseData.data) //即使后端发送的数据已经是 JSON 格式，使用 JSON.stringify() 仍然是一个常见的做法，因为它可以确保数据被正确地序列化为 JSON 字符串。
        );
        // console.log(userAccount);
        // 保存到浏览器和pinia中
        localStorage.setItem("userInfo", userAccount);
        pinia.setUserInfo(userAccount);
      } else {
        console.error("未获取到有效的用户信息数据");
      }
    },
  });

  infoRun();
};

//点击注册
const register = async () => {
  const registerform = toRaw(registerInfo); // 将一个由生成的响应式转化为对象普通对象
  if (registerform.password.length < 6 || registerform.username.length < 6) {
    alert("用户名和密码都不能小于6位");
  } else {
    if (registerform.password === registerform.againPassword) {
      // 发起请求
      const { data, run } = useRequest(getSysRegister, {
        manual: true, // 手动触发请求
        onSuccess: (data) => {
          if (data.code === 200000) {
            alert("注册成功!");
            name.value = false;
            router.replace("/index");
          } else if (data.code === 400005) {
            alert("用户名已被注册");
          } else {
            alert("失败了..");
          }
        },
        onError: (error) => {
          alert(error);
        },
      });

      run(registerform);
    } else {
      alert("两次输入的密码不一致 ");
    }
  }
};

const goRegister = () => {
  ifLog.value = false;
};

const goLogin = () => {
  ifLog.value = true;
};

const skipNickName = () => {
  registerInfo.password = "";
  registerInfo.username = "";
  nickname.value = "";
  name.value = true;
  ifLog.value = true;
};

//更新昵称
const updateNickName = async () => {
  if (nickname.value === "") {
    alert("昵称不能为空");
  } else {
    const uploadNick = toRaw(nickname.value);

    const { data, run } = useRequest(getUpdateNickName, {
      manual: true, // 手动触发请求
      onSuccess: (data) => {
        if (data.code === 200000) {
          alert("昵称更新成功");
          registerInfo.password = "";
          registerInfo.username = "";
          nickname.value = "";
          name.value = true;
          ifLog.value = true;
          router.replace("/index");
        } else if (data.code === 400006) {
          alert("昵称更新失败");
          nickname.value = "";
        } else {
          alert("失败了..");
          nickname.value = "";
        }
      },
      onError: (error) => {
        alert(error);
      },
    });

    run(registerInfo.username, uploadNick);
  }
};
</script>

<style scoped lang="less">
.container {
  width: 100vw;
  height: 100vh;

  .loginImgBox {
    transform-style: preserve-3d;
    perspective: 1000px;
    display: flex;
    justify-content: center;
    align-items: center;

    .loginImg {
      margin-top: 2rem;
      width: 80vw;
      height: 80vh;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 2rem;
      }
    }
  }

  .logBox {
    position: absolute;
    top: 50%;
    right: 20rem;
    transform: translateY(-50%);
    width: 40rem;
    height: 45rem;
    border-radius: 1rem;
    background-color: #fff;
    border: 0.1rem solid black;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    box-shadow: 0.1rem 0.1rem 0.5rem var(--gray-sahdow);

    .top {
      display: flex;
      flex-direction: column;

      .title {
        padding: 2rem;

        h5 {
          font-size: 3.5rem;
        }
      }

      .box {
        margin-top: 1rem;
        display: flex;
        flex-direction: column;

        .iptUser {
          display: flex;
          align-items: center;
          padding: 0.5rem 1.5rem;

          label {
            width: 20%;
            font-size: 1.4rem;
          }
        }
      }

      .logBtn {
        margin-top: 2rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        box-sizing: border-box;
        padding: 0 1rem;

        div {
          width: 100%;
          border: 0.1rem solid black;
          border-radius: 0.4rem;
          box-shadow: 0.1rem 0.1rem 0.3rem var(--gray-sahdow);
          padding: 0.5rem 0;
          font-size: 1.5rem;
          transition: all 0.1s;
          display: flex;
          justify-content: center;
          align-items: center;

          &:hover {
            background-color: var(--box-shadow);
            color: #fff;
            cursor: pointer;
          }
        }

        span {
          margin-top: 0.5rem;
          color: var(--special-font-color);
          cursor: pointer;
        }
      }

      .nickName {
        display: flex;
        justify-content: center;
        align-items: center;
        box-sizing: border-box;
        padding: 0 1rem;

        label {
          width: 4rem;
          font-size: 1.4rem;
        }
      }

      .nickNameBtn {
        div {
          margin: 0.5rem 0;
        }
      }
    }

    .message {
      justify-content: flex-end;
      font-size: 1.2rem;
      padding: 1rem;
      color: var(--font-gray-color);

      img {
        width: 70%;
        margin-top: 2rem;
      }
    }
  }
}

@media screen and (max-width: 800px) {
  .container {
    display: flex;
    justify-content: center;
    align-items: center;

    .loginImgBox {
      display: none;
    }

    .logBox {
      position: absolute;
      top: 50%;
      right: 50%;
      transform: translateX(50%) translateY(-50%);
      width: 80%;
    }
  }
}
</style>