// 模块 API
import { get, request } from '../../utils/network/axios'
import { ResponseResult,UserType,TokenType } from "/@/typings";

//
export async function getSysLogin(data: any){
  return request<ResponseResult<TokenType>>(import.meta.env.VITE_APP_BASE_API + '/login/web', {
    method: 'post',
    data: data,
    // headers: {
    //   'Content-Type': 'application/json'
    // }
  })
}
/* POST http://localhost:9000/login/web
响应结果如下:
{
    "code": 200000,
    "data": {
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQ29udGV4dCI6IntcImlkXCI6XCIxXCIsXCJpc1N1cGVyXCI6ZmFsc2UsXCJsb25nVGVybVwiOmZhbHNlLFwibmlja05hbWVcIjpcImxmalwiLFwicm9sZVwiOjEsXCJ1c2VybmFtZVwiOlwibGZqXCJ9Iiwic3ViIjoibGZqIiwiZXhwIjoxNzEwNzY4NzgwfQ.fqFWSfSznm0dwZg6LSEmOO2VeiWs-ZtO4Vr0MeZPoOM",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQ29udGV4dCI6IntcImlkXCI6XCIxXCIsXCJpc1N1cGVyXCI6ZmFsc2UsXCJsb25nVGVybVwiOmZhbHNlLFwibmlja05hbWVcIjpcImxmalwiLFwicm9sZVwiOjEsXCJ1c2VybmFtZVwiOlwibGZqXCJ9Iiwic3ViIjoibGZqIiwiZXhwIjoxNzExNDU5OTgxfQ.qnHTwncQSG8F4kSunz5eCbcSHZon9BFpKPCeQKETqxk"
    },
    "message": "响应成功"
}
*/


// export async function getUserInfo(){
//   return request<ResponseResult<UserType>>(import.meta.env.VITE_APP_BASE_API + '/user/info', {
//     method: 'get',
//   }, true) 
// }
export async function getUserInfo(){
  return request<ResponseResult<UserType>>(import.meta.env.VITE_APP_BASE_API + '/user/info', {
    method: 'get',
    isNeedToken: true
  }) 
}
/**
{
    "code": 200000,
    "data": {
        "id": 1,
        "phone": "15775984866",
        "username": "lfj",
        "password": "$2a$10$blFi3r.6OgxqXAtOrkq9C..0zbkor0fvR15tohFCLoXtkCF9mhfru",
        "gender": "male",
        "nickname": "收待发送",
        "birthday": "2002-11-06",
        "email": "1665834268@qq.com",
        "personalbrief": "",
        "avatarimgurl": "https://zhy-myblog.oss-cn-shenzhen.aliyuncs.com/public/user/avatar/张海洋/1575283189.png",
        "recentlylanded": "2024-03-16 10:44:20"
    },
    "message": "响应成功"
}
 */


export async function getSysRegister(data: any){
  return request<ResponseResult<string>>(import.meta.env.VITE_APP_BASE_API + '/register/web', {
    method: 'post',
    data: data,
  }) 
}
/**
{
    "code": 200000,
    "data": "success",
    "message": "响应成功"
}
*/


// export async function getUpdateNickName(userName: string, nickName:string){
//   return request<ResponseResult<string>>(import.meta.env.VITE_APP_BASE_API + '/user/nickName', {
//     method: 'get',
//     params : {
//       userName,
//       nickName
//     },
//   }, true) 
// }

export async function getUpdateNickName(userName: string, nickName:string){
  return request<ResponseResult<string>>(import.meta.env.VITE_APP_BASE_API + '/user/nickName', {
    method: 'get',
    params : {
      userName,
      nickName
    },
    isNeedToken: true
  }) 
}


/**
 *GET http://localhost:9000/user/nickName?username=lfj&nickName=ssss
 {
    "code": 200000,
    "data": "success",
    "message": "响应成功"
}
 */