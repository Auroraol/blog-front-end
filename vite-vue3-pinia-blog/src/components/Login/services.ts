// 模块 API
import { request } from '/@/utils/network/axios'
import { ResponseResult} from './data'

//
export async function getSysLogin(data: any){
  return request<ResponseResult>(import.meta.env.VITE_APP_BASE_API + '/login/web', {
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


export async function getUserInfo(){
  return request<ResponseResult>(import.meta.env.VITE_APP_BASE_API + '/user/info', {
    method: 'get',
    needToken: true
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
        "truename": "刘丰洁",
        "birthday": "2002-11-06",
        "email": "1665834268@qq.com",
        "personalbrief": "",
        "avatarimgurl": "https://zhy-myblog.oss-cn-shenzhen.aliyuncs.com/public/user/avatar/张海洋/1575283189.png",
        "recentlylanded": "2024-03-13 22:29:33"
    },
    "message": "响应成功"
}
 */