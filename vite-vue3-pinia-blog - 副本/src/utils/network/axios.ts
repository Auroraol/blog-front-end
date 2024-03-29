import axios, { AxiosRequestConfig } from "axios";
import {
  getAccessToken,
  setAccessToken,
  getRefreshToken,
  setRefreshToken,
  removeRefreshToken,
  removeAccessToken,
  removeUserAccountInfo,
} from "/@/utils/network/auth.js";
// import { SoldOut } from '@element-plus/icons-vue/dist/types'
import { useRouter } from "vue-router";
import { ResponseResult, UserType, TokenType } from "/@/typings";

const router = useRouter();
// // 刷新 access_token 的接口
// const refreshToken = () => {
//   let url = import.meta.env.VITE_APP_BASE_API + "/auth/refresh";
//   return instance.post(url, { refreshToken: getRefreshToken() });
// };

// 刷新 access_token 的接口
// const refreshToken = async () => {
//   let url = import.meta.env.VITE_APP_BASE_API + "/auth/refresh";
//   try {
//     const response = await instance.post(url, { refreshToken: getRefreshToken(), isNeedToken: true });
//     if (response.data && response.data.accessToken) {
//       setAccessToken(response.data.accessToken); // 更新本地存储的 accessToken
//       return response.data.accessToken;
//     }
//   } catch (error) {
//     console.error("Token refresh failed:", error);
//     throw error; // 抛出异常，可能需要处理用户登出等逻辑
//   }
// };

function handleRefreshToken(token) {
  return request<ResponseResult<TokenType>>(
    import.meta.env.VITE_APP_BASE_API + "/auth/refresh",
    {
      method: "post",
      data: {
        refreshToken: token,
      },
      // isNeedToken: true,
    }
  );
}

var isRefreshToken = 0;
const refreshToken = getTokenDebounce();

// 创建 axios 实例
const instance = axios.create({
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
  },
  // axios中请求配置有baseURL选项,表示请求URL公共部分,每个请求将会带该部分
  // baseURL: import.meta.env.VITE_API_URL,//配置了跨域这里不用写会冲突
});

// 目的用接口类型进行typeScript的类型覆盖, config.xxxx是会有提示，不会报错的
interface InternalAxiosRequestConfig extends AxiosRequestConfig {
  // 表示发起axios请求时,可以带的参数
  interceptors?: InternalAxiosRequestConfig;
  headers?: any; // 可选属性
  isNeedToken?: boolean; // 需要token认证的接口携带这个
}

instance.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    let accessToken = getAccessToken();
    // 只有在需要Token的情况下才携带检查Token是否过期
    if (accessToken && config.isNeedToken) {
      config.headers["accessToken"] = accessToken;
      // console.log(accessToken);
      // 解析JWT，检查是否过期
      const jwtPayload = JSON.parse(window.atob(accessToken.split(".")[1]));
      const currentTime = Math.floor(Date.now() / 1000); // 当前时间，单位为秒

      if (jwtPayload.exp < currentTime) {
        // Token已过期，尝试刷新
        console.log("Token expired. Refreshing...");
        refresh(config);
      } else {
        // Token未过期，直接设置请求头
        config.headers["accessToken"] = `${accessToken}`;
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

async function refresh(error) {
  const getTokenRes = await refreshToken();
  //console.log("re:"+getTokenRes)
  if (getTokenRes === "success") {
    // 刷新token
    if (isRefreshToken === 1) {
      error.response.config.headers.accessToken = getAccessToken();
      return instance(error.response.config);
    } else {
      router.go(0);
    }
  } else {
    removeAccessToken();
    removeRefreshToken();
    removeUserAccountInfo();
  }
}

// 响应拦截
instance.interceptors.response.use(
  (response) => {
    if (response?.status === 200) {
      const status = response.data.code;
      console.log(status);

      if (status === 403000) {
        // 尝试刷新 token
        isRefreshToken++;
        if (isRefreshToken === 1) {
          // refresh(error)
          isRefreshToken = 0;
        }
      } else if (status === 404000) {
        // 避免刷新token时也提示报错信息
        // 特定错误处理
      }
      return Promise.resolve(response);
    }
  },
  (error) => {
    // 错误处理逻辑保持不变
    if (!error.response) {
      return Promise.reject(error);
    }
    return Promise.reject(error);
  }
);

// // 有些 api 并不需要用户授权使用，则无需携带 access_token；默认不携带，需要传则设置第三个参数为 true
// export const get = (url, params = {}, isNeedToken = false) => {
//   setHeaderToken(isNeedToken);
//   return instance({
//     method: "get",
//     url,
//     params,
//   });
// };

// export const post = (url, params = {}, isNeedToken = false) => {
//   setHeaderToken(isNeedToken);
//   return instance({
//     method: "post",
//     url,
//     data: params,
//   });
// };

// 定义一个泛型函数 request，用于发送 HTTP 请求
export const request = <ResponseType = unknown>(
  url: string,
  // options?: AxiosRequestConfig<unknown>
  options?: InternalAxiosRequestConfig
): Promise<ResponseType> => {
  // 返回一个 Promise 对象，Promise 的泛型参数是 ResponseType，即期望的响应数据类型
  return new Promise((resolve, reject) => {
    // 使用 axiosInstance 发送 HTTP 请求
    instance({
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
      let oldRefreshToken = getRefreshToken();
      if (oldRefreshToken) {
        // console.log(oldRefreshToken);
        handleRefreshToken(oldRefreshToken)
          .then((res) => {
            console.log(res);
            if (res.code === 200000) {
              console.log(res.code);
              let { accessToken, refreshToken } = res.data;
              setAccessToken(accessToken);
              setRefreshToken(refreshToken);
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
