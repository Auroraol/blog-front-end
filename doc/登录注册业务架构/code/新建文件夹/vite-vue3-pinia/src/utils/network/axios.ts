import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import store from '@/store'
import { getAccessToken } from '@/utils/auth'
import { ElMessageBox, ElMessage} from "element-plus";

const axiosInstance = axios.create({
  timeout: 10000,
    // axios中请求配置有baseURL选项,表示请求URL公共部分,每个请求将会带该部分
  // baseURL: import.meta.env.VITE_API_URL,//配置了跨域这里不用写会冲突
});


// 请求拦截
axiosInstance.interceptors.request.use(
  (config) => {
     // 添加请求头, 有token就携带
     if (store.getters.token) {
        config.headers['Authorization'] = 'Bearer ' + getAccessToken();
    }
    return config
  },
  (error) => {
    // 当请求失败时做一些处理 抛出错误
    console.log(error)
    return Promise.reject(error);
  }
);

// 响应拦截器
axiosInstance.interceptors.response.use(
  response => {
    const res = response.data

    if (res.code !== 0) {
      // 凭证无效或过期
      if (res.code === 400007 || res.code === 4000010) {
        store.dispatch('user/resetToken')
        ElMessageBox.confirm('登录信息已过期', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          location.reload()
        }).catch(() => {
          location.reload()
        })  
      } else {
        // 其他
        ElMessage({
          message: res.message || 'Error',
          type: 'error',
          duration: 5 * 1000
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error)
    ElMessage({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

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





