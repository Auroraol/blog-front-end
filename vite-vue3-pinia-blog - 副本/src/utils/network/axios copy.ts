import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import qs from "qs";
import { handleRefreshToken } from "/@/services/api";
import { useRouter } from "vue-router";

const router = useRouter();
// 导入配置的环境变量url
// import baseURL from "./base-url";

// axios.defaults.headers.post["Content-Type"] =
//   "application/json;charset=utf-8";
let isRefreshToken = 0;
const refreshToken = getTokenDebounce();

const axiosInstance = axios.create({
  timeout: 10000,
  // axios中请求配置有baseURL选项,表示请求URL公共部分,每个请求将会带该部分
  // baseURL: import.meta.env.VITE_API_URL,//配置了跨域这里不用写会冲突
});

// TODO 这里扩展里InternalAxiosRequestConfig类型
// 目的用接口类型进行typeScript的类型覆盖, config.xxxx是会有提示，不会报错的
interface InternalAxiosRequestConfig extends AxiosRequestConfig {
  // 表示发起axios请求时,可以带的参数
  interceptors?: InternalAxiosRequestConfig;
  headers?: any; // 可选属性
  needToken?: boolean; // 需要token认证的接口携带这个
}

// 请求拦截
axiosInstance.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // //TODO 这里写前端请求 headers 需要携带的东西
    // const isPutPost = config.method === "put" || config.method === "post";
    // const isJson = config.headers["Content-Type"] === "application/json";
    // const isFile = config.headers["Content-Type"] === "multipart/form-data";

    // if (isPutPost && isJson) {
    //   config.data = JSON.stringify(config.data);
    // }
    // if (isPutPost && !isFile && !isJson) {
    //   //前后端交互时,将对象序列化为url形式的数据，用&拼接
    //   config.data = qs.stringify(config.data, {
    //     arrayFormat: "repeat",
    //   });
    // }

    // 获取访问Token
    let accessToken = localStorage.getItem("accessToken");
    // let accessToken =  getAccessToken();
    let refreshToken = localStorage.getItem("refreshToken");

    // console.log("refreshToken:" + refreshToken);
    if (accessToken && config.needToken) {
      config.headers["accessToken"] = accessToken; // 前端发送请求的时候携带这个 token
      // 解析当前token时间
      let jwtData = JSON.parse(
        decodeURIComponent(
          encodeURIComponent(window.atob(accessToken.split(".")[1]))
        ) //对 JWT 中的 payload 部分进行解码和编码操作
      );
      // 认证时间失效
      // if (jwtData.exp < Math.round(new Date().getTime() / 1000)) {
      refresh(config);
      // }
    }

    return config;
  },
  (error) => {
    // 当请求失败时做一些处理 抛出错误
    return Promise.reject(error);
  }
);

async function refresh(error) {
  // 刷新
  const getTokenRes = await refreshToken();
  // console.log("3333:"+getTokenRes)
  if (getTokenRes === "success") {
    console.log(isRefreshToken);

    // 刷新token
    if (isRefreshToken === 1) {
      error.response.config.headers.accessToken =
        localStorage.getItem("accessToken");
      return axiosInstance(error.config);
    } else {
      router.go(0);
    }
  } else {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("userAccount");
  }
}

// 响应拦截
axiosInstance.interceptors.response.use(
  (response) => {
    //console.log("Response Data:", response.data); // 打印响应数据
    if (response?.status === 200) {
      return Promise.resolve(response);
    }
  },
  (error) => {
    // 打印错误状态码
    const status = error.response ? error.response.status : null;
    console.log(status);

    if (error.message.includes("timeout")) {
      console.log("timeout");
    } else if (status === 403) {
      // 尝试刷新 token
      isRefreshToken++;

      if (isRefreshToken === 1) {
        // refresh(error)
        isRefreshToken = 0;
      }
    } else if (status === 404) {
       // 避免刷新token时也提示报错信息
      // 特定错误处理
    } else {
      console.log(error);
    }
    Promise.reject(error);
  }
);

// 定义一个泛型函数 request，用于发送 HTTP 请求
const request = <ResponseType = unknown>(
  url: string,
  // options?: AxiosRequestConfig<unknown>
  options?: InternalAxiosRequestConfig
): Promise<ResponseType> => {
  // 返回一个 Promise 对象，Promise 的泛型参数是 ResponseType，即期望的响应数据类型
  return new Promise((resolve, reject) => {
    // 使用 axiosInstance 发送 HTTP 请求
    axiosInstance({
      url,
      ...options, // 将传入的 options 合并到请求配置中
    })
      .then((res) => {
        // 请求成功时，将解析后的响应数据传递给 Promise 的 resolve 函数
        //res.data 得到响应数据
        //console.log(res.data);
        resolve(res.data as ResponseType);
      })
      .catch((err) => {
        // 请求失败时，将错误信息传递给 Promise 的 reject 函数
        reject(err);
      });
  });
};

// 防抖闭包
function getTokenDebounce() {
  let lock = false;
  let success = false;
  return function () {
    if (!lock) {
      lock = true;
      let oldRefreshToken = localStorage.getItem("refreshToken");
      if (oldRefreshToken) {
        // console.log(oldRefreshToken);
        handleRefreshToken(oldRefreshToken)
          .then((res) => {
            // console.log(res);
            if (res.code === 200000) {
              console.log(res.code);

              let { accessToken, refreshToken } = res.data;
              // console.log(accessToken);
              // console.log(refreshToken);
              localStorage.setItem("accessToken", accessToken);
              localStorage.setItem("refreshToken", refreshToken);
              success = true;
              lock = false;
            } else {
              success = false;
              lock = false;
              // router.push('/login')
            }
          })
          .catch((err) => {
            console.log(err);
            success = false;
            lock = false;
          });
      }
    }
    return new Promise((resolve) => {
      // 一直看lock,直到请求失败或者成功
      const timer = setInterval(() => {
        if (!lock) {
          clearInterval(timer);
          if (success) {
            resolve("success");
          } else {
            resolve("fail");
          }
        }
      }, 500); // 轮询时间间隔
    });
  };
}

export { axiosInstance, request };
