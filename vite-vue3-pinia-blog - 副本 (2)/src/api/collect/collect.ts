import {request, get, post} from '/@/utils/network/axios'

/**
 * @description 是否已收藏
 * @param {Object} articleId
 */
export function isCollected(articleId) {
  return get(import.meta.env.VITE_APP_BASE_API + `/article/collect/collected/${articleId}`, true)
}

/**
 * @description 新增收藏
 * @param {Object} params
 */
export function addCollect(params){
  return request(import.meta.env.VITE_APP_BASE_API + '/article/collect/add', {
    method: 'post',
    params: params
  }, true)
}

/**
 * @description 取消收藏
 * @param {Object} params
 */
export function cancelCollected(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/article/collect/delete', {
    method: 'delete',
    params: params
    
    }, true)
}

/**
 * @description 分页获取收藏
 * @param {Object} params
 */
export function pageCollect(params) {
  return get(import.meta.env.VITE_APP_BASE_API + '/article/collect/page', params, true)
}
