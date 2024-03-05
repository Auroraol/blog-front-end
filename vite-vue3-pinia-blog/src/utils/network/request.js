// 封装 get/post/...
import http from "./http";
// get请求(默认): 使用params接受
// post请求: 要使用data请求体接受,
// put 也相当与post请求,如果报参数错误,就是接受参数的请求体错了post/put用data,get请求用params
function request({ method = 'get', url, data = {}, params = {} }) {
    return http({
        method,
        url,
        data,
        params
    })
}

export default request;