import { request, post, get } from "/@/utils/network/axios";

/**
 * @param {Object} params
 * @description 删除文件
 */
export function deleteFile(params) {
  return request(
    import.meta.env.VITE_APP_BASE_API +    "/file/delete",
    {
      method: "delete",
      params: params,
    },
    true
  );
}

/**
 * 分页获取文件
 * @param {Object} params
 */
export function pageFile(params) {
  return get(import.meta.env.VITE_APP_BASE_API +"/file/page", params, true);
}

/**
 * 上传文件 请求数据格式：multipart/form-data, 用axios分钟后不好弄,直接使用表单默认方式进行
 */
