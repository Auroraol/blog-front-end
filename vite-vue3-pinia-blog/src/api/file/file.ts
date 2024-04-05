import {request, post} from '/@/utils/network/axios'

/**
 * @param {Object} params
 * @description 删除文件
 */
export function deleteFile(params) {
  return request.delete('/file/delete', { params })
}

/**
 * 分页获取文件
 * @param {Object} params
 */
export function pageFile(params) {
  return request.get('/file/page', { params })
}


/**
 * 上传文件 请求数据格式：multipart/form-data, 用axios分钟后不好弄,直接使用表单默认方式进行  
 *
 */

