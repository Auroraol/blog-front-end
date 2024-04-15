import {request, get, post} from '/@/utils/network/axios'
/**
 * @param {Object} params
 * @description 分页获取评论回复列表
 */
export function pageComment(params) {
  return get(import.meta.env.VITE_APP_BASE_API +'/article/comment/page',  params,true )
}

/**
 * @description 新增评论
 * @param {Object} params
 */
export function addComment(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/article/comment/add',{
    method: 'post',
    params: params
  }),true
}

/**
 * @description 新增回复
 * @param {Object} params
 */
export function addReply(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/article/reply/add', {
    method: 'post',
    params: params
  }, true)
}

/**
 * @description 删除评论
 * @param {Object} params
 */
export function deleteComment(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/article/comment/delete', { 
    method: 'delete',
    params: params }, true)
}

/**
 * @description 删除回复
 * @param {Object} params
 */
export function deleteReply(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/article/reply/delete', {
    method: 'delete',
    params: params }, true)
}

/**
 * @description 最新评论
 * @param {Object} params
 */
export function latestCommentList(params) {
  return get(import.meta.env.VITE_APP_BASE_API +'/article/comment/latest', params, true)
}
