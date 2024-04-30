import {request, get, post} from '/@/utils/network/axios'


/**
 * @param {Object} params
 * @description 分页获取留言
 */
export function pageMessage(params) {
  return get(import.meta.env.VITE_APP_BASE_API +  '/leave/message/page', params)
}

/**
 * @description 新增留言
 * @param {Object} params
 */
export function addMessage(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/leave/message/add', {
    method: 'post',
    params: params
  })
}

/**
 * @description 新增回复
 * @param {Object} params
 */
export function addReply(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/leave/message/reply',{
    method: 'post',
    params: params
  })
}

/**
 * @description 删除留言或回复
 * @param {Object} id
 */
export function deleteMessageReply(id) {
  return request(import.meta.env.VITE_APP_BASE_API + '/leave/message/delete/' + id, {
    method: 'delete'
  })
}
