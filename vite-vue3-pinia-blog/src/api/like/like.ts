import {request, get, post} from '/@/utils/network/axios'

/**
 * @description 是否已点赞
 * @param {Object} articleId
 */
export function isLiked(articleId) {
  return get(import.meta.env.VITE_APP_BASE_API + `/article/like/liked/${articleId}`, true)
}

/**
 * @description 新增点赞
 * @param {Object} params
 */
export function addLike(params){
  return request(import.meta.env.VITE_APP_BASE_API +'/article/like/add', {
    method: 'post',
    params: params
  }, true)
}

/**
 * @description 取消点赞
 * @param {Object} params
 */
export function cancelLiked(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/article/like/cancel',{  
    method: 'delete',
    params: params

  }, true)
}

