import {request, get, post} from '/@/utils/network/axios'
import {Chat} from '/@/types/chat';
import {Page} from '/@/typings.d';



// 添加房间
export function addChatRoom(params) {
    return request(import.meta.env.VITE_APP_BASE_API +  "/chat/room/add",{
        method: 'post',
        params: params
      }) 
}

// 删除房间
export function deleteChatRoom(params) {
    return request(import.meta.env.VITE_APP_BASE_API +  "/chat/room/delete",{
        method: 'post',
        params: params
      }) 
}

// 获取全部房间
export function chatList() {
    return get(import.meta.env.VITE_APP_BASE_API+ "/chat/room/all",{
    })
}


// 添加聊天记录
export function addChatDataAPI(data) {
    return post(import.meta.env.VITE_APP_BASE_API+"/chat/add", data)
}

// 获取全部聊天记录
export function getChatListAPI(params) {
    return get(import.meta.env.VITE_APP_BASE_API+ "/chat/all", params)
}
