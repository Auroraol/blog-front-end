import axios, { AxiosRequestConfig, AxiosResponse } from "axios";
import { getAccessToken } from "/@/utils/auth";
import { ElMessageBox, ElMessage } from "element-plus";
import axiosRetry from "axios-retry";
import { useUserStore } from "/@/store/index";

const axiosInstance = axios.create({
  timeout: 10000,
  // axios中请求配置有baseURL选项,表示请求URL公共部分,每个请求将会带该部分
  // baseURL: import.meta.env.VITE_API_URL,//配置了跨域这里不用写会冲突
  validateStatus(status) {
    // console.error(status);
    // 状态码
    return status >= 200 && status < 500; // code 200 - 500  范围之外的才走重试流程
  },
});

const whiteRetry = new Set(["ECONNABORTED", undefined]);

// 配置axios-retry插
axiosRetry(axiosInstance, {
  retries: 3, // 最多重试3次
  shouldResetTimeout: true, //  重置超时时间
  retryDelay: (retryCount) => {
    return retryCount * 10000; // 重复请求延迟，每次请求之间间隔10s
  },
  retryCondition: (err) => {
    console.error(err);
    // true为打开自动发送请求，false为关闭自动发送请求
    // 自定义重试条件，只重试网络错误
    // return !err.response || err.response.status === 500;
    //这里的code是状态码, code 200 - 500  范围之外的才走重试流程
    const { code, message } = err;
    return whiteRetry.has(<string>code) || message.includes("timeout");
  },
});

// 请求拦截
axiosInstance.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    // 当请求失败时做一些处理 抛出错误
    // console.log(error);
    return Promise.reject(error);
  }
);

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response) => {
    const res = response.data; // 响应数据

    // 确保前端得到的res都是成功响应
    if (res.code !== 200000) {
      // 凭证无效或过期
      if (res.code === 400007 || res.code === 4000010) {
        useUserStore().resetToken();
        ElMessageBox.confirm("登录信息已过期", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            location.reload();
          })
          .catch(() => {
            location.reload();
          });
      } else {
        // 其他, 返回失败message
        // ElMessage({
        //   message: res.message || "Error",
        //   type: "error",
        //   duration: 5 * 1000,
        // });
      }
      return Promise.reject(new Error(res.message || "Error"));
    } else {
      return res;
    }
  },
  (error) => {
    console.log("err" + error);
    ElMessage({
      message: error.message,
      type: "error",
      duration: 5 * 1000,
    });
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
    axiosInstance.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${accessToken}`;
  }
};

// 定义一个泛型函数 request，用于发送 HTTP 请求
const request = <ResponseType = unknown>(
  url: string,
  options?: AxiosRequestConfig<unknown>,
  isNeedToken: boolean = false // 默认不需要 token
): Promise<ResponseType> => {
  // 返回一个 Promise 对象，Promise 的泛型参数是 ResponseType，即期望的响应数据类型
  return new Promise((resolve, reject) => {
    // 使用 axiosInstance 发送 HTTP 请求
    setHeaderToken(isNeedToken);
    axiosInstance({
      url,
      ...options, // 将传入的 options 合并到请求配置中
    })
      .then((res) => {
        // 请求成功时，将解析后的响应数据传递给 Promise 的 resolve 函数
        //这里的res得到响应数据  而res.data得到的是响应数据中的data属性数据
        resolve(res.data as ResponseType);
        //console.log(res.data);
      })
      .catch((err) => {
        // 请求失败时，将错误信息传递给 Promise 的 reject 函数
        reject(err);
      });
  });
};

// 有些 api 并不需要用户授权使用，则无需携带 access_token；默认不携带，需要传则设置第三个参数为 true
const get = (url, params = {}, isNeedToken = false) => {
  return new Promise((resolve, reject) => {
    setHeaderToken(isNeedToken);
    axiosInstance({
      url,
      method: "get",
      params,
    })
      .then((res) => {
        resolve(res.data as ResponseType);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

const post = (url, data = {}, isNeedToken = false) => {
  setHeaderToken(isNeedToken);
  return new Promise((resolve, reject) => {
    setHeaderToken(isNeedToken);
    axiosInstance({
      url,
      method: "post",
      data: data,
    })
      .then((res) => {
        resolve(res.data as ResponseType);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export { axiosInstance, request, get, post };

/*
注: 封装抛出的是响应数据中的data属性, 并且封装后,确保前端拿到的数据一定是成功响应数据
    不定义响应数据类型则自动匹配,就没有自动补全提示,不安全

使用
①axiosInstance
和axios使用一致

②request
// 定义数据响应的类型, 不定义则自动匹配,就没有自动补全提示
interface DataResponseType {
  // 在这里定义响应数据的结构
}

// 可选的 Axios 请求配置
const options: AxiosRequestConfig = {
  method: "GET", // 请求方法，例如 GET、POST 等
  params: { // 请求参数，可以是 params、data 等
    // 具体参数
  },
  // 其他 Axios 请求配置，例如 headers、timeout 等
};

// 发送请求并指定响应数据的类型
request<DataResponseType>(url, options, isNeedToken)
  .then((data) => {
    // 请求成功时，处理返回的数据
    console.log("响应数据:", data);
  })
  .catch((error) => {
    // 请求失败时，处理错误信息
    console.error("请求失败:", error);
  });

③get
get(url, params, isNeedToken)
  .then(response => {
    // 处理请求成功的响应
    console.log('GET 请求成功:', response);
  })
  .catch(error => {
    // 处理请求失败
    console.error('GET 请求失败:', error);
  });

④post
post(url, data, isNeedToken)
  .then(response => {
    // 处理请求成功的响应
    console.log('POST 请求成功:', response);
  })
  .catch(error => {
    // 处理请求失败
    console.error('POST 请求失败:', error);
  });
*/
