import {request} from '/@/utils/network/axios'

/**
 * 发送验证码
 * @param {Object} params
 */
export function sendCode(params) {
  return request(import.meta.env.VITE_APP_BASE_API + '/sms/send',{
    method: 'post',
    params: params // 将参数放在 params 中传递
  })
}