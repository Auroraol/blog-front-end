// 模块 API
import { request } from '/@/utils/network/axios'
import { ResponseResult} from './data'

//
export async function getLogin(data: any){
  return request<ResponseResult>(import.meta.env.VITE_APP_BASE_API + '/user/login', {
    method: 'post',
    data: data,
  })
}


/* POST http://localhost:9000/user/login
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