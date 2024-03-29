// 导入请求的方法

// 不使用箭头函数
// export function home() {
//     return request({ url: '/home', })
// }

import request from "/@/utils/network/request";
// 用户登陆的接口  post请求使用data请求体
//  export const login = (data) => request({ method: "post", url: '/login', data: data });

// 获取用户列表的接口 ,需要有默认参数. pagenum: 1,pagesize: 10,,所以要接收, 默认为get 如果是get请求不需要写methiod有默认值了
//  export const getUserList = (params) => request({ url: '/users', params });

// put请求修改用户id users/:uId/state/:type
//     export const getEdit = (data) => request({
//      method: "put",
//      data,
//      url: "users/" + data.uid + "/state/" + data.type,
//    })

// 轮播
export const getBanner = () => request({ url: '/carousel/getReleaseList' });

// 实例接口
export const order = () => request({
    url: "/home/floordata"
});

// 媒体咨询
export const news = (params) => request({
    url: "/article/getReleaseList",
    params: params
})

// 媒体查询详情
export const newsDetail = (params) => request({
    url: "/article/detail",
    params: params
})