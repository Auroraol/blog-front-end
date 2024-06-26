import {request, get, post} from '/@/utils/network/axios'
/**
 * 保存文章
 * @data {Object} data
 */
export function saveArticle(data) {
  return post(import.meta.env.VITE_APP_BASE_API + '/article/save', data, true)
}

/**
 * 文章详情（后台）
 * @param {Object} id
 */
export function articleDetail(id) {
  return get(import.meta.env.VITE_APP_BASE_API +`/article/detail/${id}`, true)
}


/**
 * 分页获取已发布文章（前台）
 * @param {Object} params
 */
export function pagePublishedArticle(params) {
  return get(import.meta.env.VITE_APP_BASE_API + '/article/published/page', params )
}

/**
 * 添加或更新推荐
 * @param {Object} params
 */
export function addRecommend(params) {
  return request(import.meta.env.VITE_APP_BASE_API +  '/article/recommend/save',{
    method: 'post',
    params: params
  }, 
  true)
}

/**
 * @description 推荐列表
 */
export function recommendList() {
  return get(import.meta.env.VITE_APP_BASE_API + '/article/recommend/list')
}

/**
 * 移除推荐
 * @param {Number} id
 */
export function deleteRecommend(id) {
  return request(import.meta.env.VITE_APP_BASE_API + '/article/recommend/delete/' + id,{ 
    method:"delete"}, true)
}

/**
 * @description 浏览文章
 * @param {Object} id
 */
export function viewArtilce(id) {
  return get(import.meta.env.VITE_APP_BASE_API +  '/article/view/' + id)
}

/**
 * 自增浏览次数
 * @param {Object} id
 */
export function incrementView(id) {
  return request(import.meta.env.VITE_APP_BASE_API +'/article/increment_view/' + id,{
    method:"put"})
}

/**
 * @description 相关文章
 * @param {Object} params
 */
export function interrelated(params) {
  return get(import.meta.env.VITE_APP_BASE_API +   '/article/interrelated/list',  params )
}

/**
 * 分页获取归档
 * @data {Object} data
 * @returns yearMonth and articleCount
 */
export function pageArchives(params) {
  return get(import.meta.env.VITE_APP_BASE_API +  '/article/archives/page',  params )
}

/**
 * 分页获取文章（后台）
 * @param {Object} params
 */
export function pageArticle(params) {
  return get(import.meta.env.VITE_APP_BASE_API + '/article/page', params, true )
}

/**
 * 状态修改
 * @param {Object} params 
 */
export function updateStatus(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/article/status/update', {
    method: 'post',
    params: params
  }, true)
}


/**
 * 删除文章
 * @param {Number} id
 */
export function deleteArticle(id) {
  return request(import.meta.env.VITE_APP_BASE_API + `/article/delete/${id}`,{
    method: "delete"
  }, true)
}
