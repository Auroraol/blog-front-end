import axios, { AxiosRequestConfig, AxiosResponse } from "axios";

// axios.defaults.headers.post["Content-Type"] =
//   "application/x-www-form-urlencoded;charset=UTF-8";
  

const axiosInstance = axios.create({
  timeout: 10000,
});

// 请求拦截
axiosInstance.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    // 当请求失败时做一些处理 抛出错误
    return Promise.reject(error);
  }
);

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
  options?: AxiosRequestConfig<unknown>
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


