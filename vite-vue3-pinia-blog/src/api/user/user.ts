import {request, post, get} from '/@/utils/network/axios'
import { LoginResponseType, userInfoResponseType} from './data'
/**
 * 账号登录
 * @param {Object} params
 */
export function accountLogin(params) {
  return request<LoginResponseType>(import.meta.env.VITE_APP_BASE_API +  '/account/login',{
    headers: {
      'Authorization': 'Basic cGM6MTIzNDU2'
    },
    method: 'post',
    params: params
  })
}

/**
 * 第三方登录
 * @param {Object} params
 */
export function thirdLogin(params) {
  return request<LoginResponseType>(import.meta.env.VITE_APP_BASE_API +'/oauth', {
    headers: {
      'Authorization': 'Basic cGM6MTIzNDU2'
    },
    method: 'post',
    params: params
  })
}

/**
 * 手机号验证码登录
 * @param {Object} params
 */
export function codeLogin(params) {
  return request<LoginResponseType>(import.meta.env.VITE_APP_BASE_API + '/mobile/login',{
    headers: {
      'Authorization': 'Basic cGM6MTIzNDU2'
    },
    method: 'post',
    params: params
  })
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  return request<userInfoResponseType>(import.meta.env.VITE_APP_BASE_API + '/user/info' ,{
    method: 'get'
  }, true)
}

/**
 * @description 退出
 * @param {Object} params
 */
export function logout(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/logout', {
    headers: {
      'Authorization': 'Basic cGM6MTIzNDU2'
    },
    method: 'delete',
    params: params
  })
}

/**
 * 用户注册
 * @param {Object} data
 */
export function register(data) {
  return request(import.meta.env.VITE_APP_BASE_API + '/user/register', {
    method: 'post',
    data: data
    })
}

/**
 * 更新用户（非空项更新）
 * @param {Object} data
 */
export function updateUser(data) {
  return post(import.meta.env.VITE_APP_BASE_API + '/user/update', data, true)
}

/**
 * 更新密码
 * @param {Object} params
 */
export function updatePassword(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/user/password/update', {
    method: 'post',
    params: params
  }, true)
}


// 需要手机验证码所以不用携带token, 也安全
/**
 * 重置密码
 * @param {Object} params
 */
export function resetPassword(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/user/password/reset',{
    method: 'post',
    params: params
  })
}

/**
 * 验证邮箱（发送验证链接）
 * @param {Object} params
 */
export function validateEmail(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/user/email/validate',{
    method: 'post',
    params: params
  }, true)
}

/**
 * code绑定邮箱
 * @param {Object} params
 */
export function bindEmail(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/user/email/bind', {
    method: 'post',
    params: params
  })
}

/**
 * 验证原手机号
 * @param {Object} params
 */
export function validateMobile(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/user/mobile/validate', {
    method: 'post',
    params: params
  }, true)
}

/**
 * 绑定手机号
 * @param {Object} params
 */
export function bindMobile(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/user/mobile/bind',{
      method: 'post',
      params: params
    }, true)
}


/**
 * 绑定新手机号
 * @param {Object} params
 */
export function rebindMobile(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/user/mobile/rebind',{
    method: 'post',
    params: params
  }, true)
}

/**
 * 绑定用户名
 * @param {Object}  params
 */
export function bindUsername(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/user/username/bind',{
    method: 'post',
    params: params
  }, true)
}

// 管理员
/**
 * 分页获取用户
 * @param {Object} params
 */
export function pageUser(params) {
  return get(import.meta.env.VITE_APP_BASE_API +'/user/page', params, true)
}

/**
 * 修改用户状态
 * @param {Object} params
 */
export function updateStatus(params) {
  return request(import.meta.env.VITE_APP_BASE_API +'/user/status/update', {
    method: 'post',
    params: params
  },true)
}

/**
 * 获取用户信息
 */
export function getChatUserInfo(params) {
  return request<userInfoResponseType>(import.meta.env.VITE_APP_BASE_API + '/user/chat/info' ,{
    method: 'get',
    params: params
  },true)
}
