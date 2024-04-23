import {request, get, post} from '/@/utils/network/axios'
import { tagListResponseType} from './data'

/**
 * 新增标签
 * @params {Object} params
 */
export function addTag(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/tag/add',{
    method: 'post',
    params: params
  }, true)
}

/**
 * 获取标签id
 * @params {Object} params
 */
export function getTagId(params) {
  return get(import.meta.env.VITE_APP_BASE_API + '/tag/id',params, true)
}


/**
 * 分页获取标签列表
 * @param {Object} params
 */
export function pageTag(params) {
  return get(import.meta.env.VITE_APP_BASE_API + '/tag/page', params , true)
}

/**
 * 获取标签列表
 */
export function tagList() {
  return request<tagListResponseType[]>(import.meta.env.VITE_APP_BASE_API + '/tag/list')
}

// /**
//  * 修改标签
//  * @param {Object} params
//  */
// export function updateTag(params) {
//   return request({
//     url: '/tag/update',
//     method: 'post',
//     params: params
//   })
// }

// /**
//  * 删除标签
//  * @param {Object} id
//  */
// export function deleteTag(id) {
//   return request.delete('/tag/delete/' + id)
// }
