import { request, get, post } from "/@/utils/network/axios";
// 均是管理员操作
/**
 *
 * @data {Object} data
 */
export function pageClient(params) {
  return get(import.meta.env.VITE_APP_BASE_API + "/client/page", params, true);
}

/**
 * 保存客户端
 * @param {Object} data
 */
export function saveClient(data) {
  return post(import.meta.env.VITE_APP_BASE_API + "/client/save", data, true);
}

/**
 * 删除客户端
 * @param {Object} id
 */
export function deleteClient(id) {
  return request(
    import.meta.env.VITE_APP_BASE_API + "/client/delete/" + id,
    {
      method: "delete",
    },
    true
  );
}
