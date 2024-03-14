import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import qs from "qs";
// 导入配置的环境变量url
// import baseURL from "./base-url";

// axios.defaults.headers.post["Content-Type"] =
//   "application/json;charset=utf-8";

const axiosInstance = axios.create({
  timeout: 10000,
  // axios中请求配置有baseURL选项,表示请求URL公共部分,每个请求将会带该部分
  // baseURL: import.meta.env.VITE_API_URL,//配置了跨域这里不用写会冲突
});

// TODO 这里扩展里InternalAxiosRequestConfig类型
// 目的用接口类型进行typeScript的类型覆盖, config.xxxx是会有提示，不会报错的
interface InternalAxiosRequestConfig extends AxiosRequestConfig{  
  // 表示发起axios请求时,可以带的参数
  interceptors?: InternalAxiosRequestConfig
  headers?: any; // 可选属性
  needToken?: boolean;  // 需要token认证的接口携带这个
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
    let refreshToken = localStorage.getItem("refreshToken");
    // console.log("refreshToken:" + refreshToken);
    if (accessToken && config.needToken) {
      config.headers["accessToken"] = accessToken; // 前端发送请求的时候携带这个 token  
      // 解析当前token时间
      let jwtData = JSON.parse(
        decodeURIComponent(encodeURIComponent(window.atob(accessToken.split(".")[1])))//对 JWT 中的 payload 部分进行解码和编码操作
      );
      console.log(jwtData.exp );
      
      if (jwtData.exp < Math.round(new Date().getTime() / 1000)) {
        // refresh(config);
      }
    }

    return config;
  },
  (error) => {
    // 当请求失败时做一些处理 抛出错误
    return Promise.reject(error);
  }
);

// async function refresh (error) {
//   const getTokenRes = await refreshToken();
//   console.log("3333:"+getTokenRes)
//   if (getTokenRes === 'success') {
//     // 刷新token
//     if (isRefreshToken === 1) {
//       error.response.config.headers.accessToken = Storage.getItem(
//         'accessToken'
//       );
//       return service(error.response.config);
//     } else {
//       router.go(0);
//     }
//   } else {
//     Storage.removeItem('accessToken');
//     Storage.removeItem('refreshToken');
//     Storage.removeItem('userInfo');
//     Storage.setItem('cartNum', 0);
//     store.commit('SET_CARTNUM', 0);
//     Modal.confirm({
//       title: '请登录',
//       content: '<p>请登录后执行此操作</p>',
//       okText: '立即登录',
//       cancelText: '继续浏览',
//       onOk: () => {
//         router.push({
//           path: '/login',
//           query: {
//             rePath: router.history.current.path,
//             query: JSON.stringify(router.history.current.query)
//           }
//         });
//       },
//       onCancel: () => {
//         Modal.remove();
//       }
//     });
//   }
// }


// 响应拦截
axiosInstance.interceptors.response.use(
  (response) => {
    //console.log("Response Data:", response.data); // 打印响应数据
    if (response?.status === 200) {
      return Promise.resolve(response);
    }
  },
  (error) => {
    if (error?.message?.includes?.("timeout")) {
      console.log("timeout");
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

export { axiosInstance, request };
