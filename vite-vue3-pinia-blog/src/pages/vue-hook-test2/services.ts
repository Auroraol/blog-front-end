// 模块 API
import { request } from '/@/utils/network/axios'
import { HitokotoType } from './data'

export async function getUserInfo(c: string):Promise<HitokotoType>{
  return request<HitokotoType>('https://v1.hitokoto.cn/', {
    params: {
      c,
    },
  })
}

/*
/* GET https://v1.hitokoto.cn/?c=b  
响应结果如下:
{
    "id": 5963,
    "uuid": "b25ebe3d-d031-493c-a129-61ba3b7c0a23",
    "hitokoto": "此时此刻的咱啊，能和汝在一起，是最幸福的了。",
    "type": "b",
    "from": "狼与香辛料",
    "from_who": "支仓冻砂",
    "creator": "人活着就是为了贤狼赫萝",
    "creator_uid": 5896,
    "reviewer": 4756,
    "commit_from": "web",
    "created_at": "1586916630",
    "length": 22
}
*/
