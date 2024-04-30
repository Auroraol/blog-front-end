import {request, get, post} from '/@/utils/network/axios'
import { categoryItem} from './data'

/**
 * 获取分类列表
 */
export function categoryList() {
  return request<categoryItem[]>(import.meta.env.VITE_APP_BASE_API + '/category/list')
}

/**
 * 新增分类
 * @data {Object} data
 */
export function addCategory(data) {
  return post(import.meta.env.VITE_APP_BASE_API + '/category/add', data, true)
}

/**
 * 分页获取分类列表
 * @param {Object} params
 */
export function pageCategory(params) {
  return get(import.meta.env.VITE_APP_BASE_API +'/category/page', params, true)
}


/**
 * 更新分类
 * @param {Object} params
 */
export function updateCategory(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/category/update', {
    method: 'post',
    params: params
  }, true)
}

/**
 * 删除分类
 * @param {Object} id
 */
export function deleteCategory(id) {
  return request(import.meta.env.VITE_APP_BASE_API +'/category/delete/' + id, {
    method: 'delete'
  })
}
