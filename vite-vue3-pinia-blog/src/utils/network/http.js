// 封装axios
import axios from "axios";
// 导入配置的环境变量url
import baseURL from "./base-url";
// 导入路由，没有this，使用路由实例跳转
import router from "../../route/router";
// 导入main全局配置文件
import app from "../../main";
// 开启loading
// app.config.globalProperties.$loading.showLoading();

// 创建axios实例
const http = axios.create({
  baseURL, //配置了跨域,起了别名api,在baseBUL用了别名,这里使用baseURL,配置在下面vue.config.js //实际使用的API的基础URL
  timeout: 6000,
  withDirectives: true, // 是否携带cookies
});

// 请求拦截
http.interceptors.request.use(
  (config) => {
    //  携带token
    const token = localStorage.getItem("remember_token");
    if (token) {
      config.headers["Authorization"] = "Bearer " + token; //Authorization n.授权
    }
    
    // 配置完成将config返回
    return config;
  },
  (err) => {
    // 当请求失败时做一些处理 抛出错误
    throw new Error(err);
  }
);

// 响应拦截
http.interceptors.response.use(
  (res) => {
    // 关闭loading
    app.config.globalProperties.$loading.hideLoading();
    // 根据返回状态码统一封装提示信息(不需要在页面接口在单独写)
    let code = res.data.code;  // 这里根据实际接口的嵌套情况和状态码来写 这里只是我的接口嵌套情况 【重要】
    let msg = res.data.message;
    //key = value  status等于200就执行200下的代码 执行完break退出循环
    switch (code) {
      // case 200:
      //   ElMessage({
      //     showClose: true,
      //     message: msg,
      //     type: "success",
      //   });
      //   break;
      case 500:
        ElMessage({
          showClose: true,
          message: msg,
          type: "warning",
        });
        break;
      case 400:
        ElMessage({
          showClose: true,
          message: msg,
          type: "error",
        });
        // console.log(this);
        // this是undefined所以引入路由;
        // router.push("/login"); //未授权跳转到login
        break;
    }
    // 这里根据接口返回的数据嵌套结构来返回   【重要】
    /*比如我的接口返回的是
    {
      data:{
        message:{
          //...
        }
      }
    } 
    */
    return res.data.data;
  },
  (err) => {
    // 当响应失败时做一些处理 抛出错误
    throw new Error(err);
  }
);

// 导出 http实例 方便调用
export default http;
