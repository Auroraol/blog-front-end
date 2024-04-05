import {request, post, get} from '/@/utils/network/axios'
/**
 * 保存友链
 * @data {Object} data
 */
export function saveFriendLink(data) {
  return post('/friend/link/save', data, true)
}

/**
 * 分页获取友链
 * @param {Object} params
 */
export function pageFriendLink(params) {
  return get('/friend/link/page', { params })
}

/**
 * 友链列表
 */
export function listFriendLink() {
  return get(import.meta.env.VITE_APP_BASE_API +'/friend/link/list')
}

/**
 * 删除友链
 * @param {Number} id
 */
export function deleteFriendLink(id) {
  return request(`/friend/link/delete/${id}`, {
    method: "delete"
  }, true
  )
}
