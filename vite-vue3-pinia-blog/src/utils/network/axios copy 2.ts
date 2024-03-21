import axios, { AxiosRequestConfig } from "axios";
import {
  getAccessToken,
  setAccessToken,
  getRefreshToken,
} from "/@/utils/network/auth.js";
// import { SoldOut } from '@element-plus/icons-vue/dist/types'

// 刷新 access_token 的接口
const refreshToken = () => {
  let url = import.meta.env.VITE_APP_BASE_API + "/auth/refresh";
  return instance.post(url, { refreshToken: getRefreshToken() });
};

// 创建 axios 实例
const instance = axios.create({
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
  },
  // axios中请求配置有baseURL选项,表示请求URL公共部分,每个请求将会带该部分
  // baseURL: import.meta.env.VITE_API_URL,//配置了跨域这里不用写会冲突
});

let isRefreshing = false; // 标记是否正在刷新 token
// let requests = [] // 存储待重发请求的数组
let requests: ((token: string) => void)[] = []; // 存储因 token 过期而挂起的请求

// // 响应拦截
// instance.interceptors.response.use(
//   response => {
//     return response
//   },
//   error => {
//     if (!error.response) {
//       return Promise.reject(error)
//     }
//     if (error.response.status === 401 && !error.config.url.includes('/auth/refresh')) {
//       const { config } = error
//       if (!isRefreshing) {
//         isRefreshing = true
//         return refreshToken()
//           .then(res => {
//             console.log(res.data.data);

//             const { accessToken,refreshToken } = res.data.data
//             setAccessToken(accessToken)
//             // config.headers.Authorization = `Bearer ${access_token}`
//             config.headers.common['accessToken'] = `${accessToken}`;
//             // token 刷新后将数组的方法重新执行
//             requests.forEach(cb => cb(accessToken))
//             requests = [] // 重新请求完清空
//             return instance(config)
//           })
//           .catch(err => {
//             console.log('抱歉，您的登录状态已失效，请重新登录！')
//             return Promise.reject(err)
//           })
//           .finally(() => {
//             isRefreshing = false
//           })
//       } else {
//         // 返回未执行 resolve 的 Promise
//         return new Promise(resolve => {
//           // 用函数形式将 resolve 存入，等待刷新后再执行
//           requests.push(token => {
//             config.headers.common['accessToken'] = `${token}`;
//             resolve(instance(config))
//           })
//         })
//       }
//     }
//     return Promise.reject(error)
//   }
// )\

// // Axios 响应拦截器
// instance.interceptors.response.use(
//   response => {
//     return response; // 如果响应码不是 401000，直接返回响应
//   },
//   error => {
//     const { config, response } = error;
//     if (response && response.data.code === 401000) {
//       if (!response.config.url.includes('/auth/refresh')) {
//         if (!isRefreshing) {
//           isRefreshing = true;
//           return refreshToken().then(res => {
//             console.log(res);

//             const { accessToken } = res.data;
//             // 更新本地存储的 token
//             setAccessToken(accessToken);
//             // 更新默认请求头中的 token
//             instance.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
//             // 重新发起被挂起的请求
//             requests.forEach(cb => cb(accessToken));
//             requests = []; // 清空队列
//             isRefreshing = false;
//             // 重新发起当前失败的请求
//             return instance(config);
//           }).catch(err => {
//             console.error('登录状态已失效，请重新登录');
//             // 处理登录失效逻辑，例如跳转到登录页
//             return Promise.reject(err);
//           });
//         } else {
//           // 如果已经在刷新 token，则将当前请求挂起
//           return new Promise(resolve => {
//             requests.push(token => {
//               config.headers['Authorization'] = `Bearer ${token}`;
//               resolve(instance(config));
//             });
//           });
//         }
//       }
//     }
//     return Promise.reject(error);
//   }
// );

// 响应拦截
instance.interceptors.response.use(
  (response) => {
    // 检查响应体中的 code 是否为自定义的 401000 token 过期,  如果refreshToken都过期了, 则为真正过期
    if (response.data.code === 401000) {
      if (!response.config.url.includes("/auth/refresh")) {
        // 防止多个请求同时到达时重复刷新 token
        if (!isRefreshing) {
          isRefreshing = true;
          return refreshToken()
            .then((res) => {
              if (res.data && res.data.data) {
                const { accessToken, refreshToken } = res.data.data;
                // 使用 accessToken 和 refreshToken
                setAccessToken(accessToken); // 更新本地存储的 token

                // 更新默认请求头中的 token
                instance.defaults.headers.common[
                  "accessToken"
                ] = `${accessToken}`;

                // 重新发起被挂起的请求
                requests.forEach((cb) => cb(accessToken));
                requests = []; // 清空队列
                // isRefreshing = false;
                // 更新默认请求头中的 token
                instance.defaults.headers.common[
                  "accessToken"
                ] = `${accessToken}`;
                return instance(response.config); // 重新发起当前请求
              } else {
                console.error("无法获取accessToken，响应结构可能有误。");
                // 处理错误情况
              }
            })
            .catch((err) => {
              console.error("抱歉，您的登录状态已失效，请重新登录！");
              return Promise.reject(err);
            })
            .finally(() => {
              isRefreshing = false;
            });
        } else {
          // 如果已经在刷新 token，则将当前请求挂起
          return new Promise((resolve) => {
            requests.push((token) => {
              response.config.headers["accessToken"] = `${token}`;
              resolve(instance(response.config));
            });
          });
        }
      }
    }
    // 如果响应体中的 code 不是 401，直接返回响应
    return response;
  },
  (error) => {
    // 错误处理逻辑保持不变
    if (!error.response) {
      return Promise.reject(error);
    }
    return Promise.reject(error);
  }
);

// 给请求头添加 access_token
const setHeaderToken = (isNeedToken: boolean) => {
  // 请求头携带token
  const accessToken = isNeedToken ? getAccessToken() : null;
  if (isNeedToken) {
    // api 请求需要携带 access_token
    if (!accessToken) {
      console.log("不存在 access_token 则跳转回登录页");
    }
    // instance.defaults.headers.common['accessToken'] 中的accessToken叫啥由后端决定
    instance.defaults.headers.common["accessToken"] = `${accessToken}`;
  }
};

// 有些 api 并不需要用户授权使用，则无需携带 access_token；默认不携带，需要传则设置第三个参数为 true
export const get = (url, params = {}, isNeedToken = false) => {
  setHeaderToken(isNeedToken);
  return instance({
    method: "get",
    url,
    params,
  });
};

export const post = (url, params = {}, isNeedToken = false) => {
  setHeaderToken(isNeedToken);
  return instance({
    method: "post",
    url,
    data: params,
  });
};

// 定义一个泛型函数 request，用于发送 HTTP 请求
export const request = <ResponseType = unknown>(
  url: string,
  options?: AxiosRequestConfig<unknown>,
  isNeedToken: boolean = false // 默认不需要 token
): Promise<ResponseType> => {
  // 返回一个 Promise 对象，Promise 的泛型参数是 ResponseType，即期望的响应数据类型
  return new Promise((resolve, reject) => {
    // 使用 instance 发送 HTTP 请求
    setHeaderToken(isNeedToken);
    instance({
      url,
      ...options, // 将传入的 options 合并到请求配置中
    })
      .then((res) => {
        // 请求成功时，将解析后的响应数据传递给 Promise 的 resolve 函数
        resolve(res.data as ResponseType);
      })
      .catch((err) => {
        // 请求失败时，将错误信息传递给 Promise 的 reject 函数
        reject(err);
      });
  });
};
