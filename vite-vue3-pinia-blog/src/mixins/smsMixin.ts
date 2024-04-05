
import { ref } from 'vue';
import { sendCode } from '/@/api/sms/sms'; // 发送验证码的API接口
import { validMobile } from "/@/utils/validate";

// 定义 mixin 
export function useSmsCodeMixin() {
    const codeCount = ref(0); // 使用 ref 创建响应式计时器变量

    /**
     * 
     * @param mobile 手机号码
     * @param timeCount 倒计时, 默认120s
     * @returns 
     */
    const getCode = (mobile: any, timeCount: number = 120) => {
       
      if (mobile === "") {
        ElMessage.warning("请输入手机号");
        return;
      }
      if (!validMobile(mobile)) {
        ElMessage.error("手机号格式不正确");
        return;
      } 
  
      // 倒数计时
      codeCount.value = timeCount;
      const timer = setInterval(() => {
        if (codeCount.value > 0 && codeCount.value <= timeCount) {
            codeCount.value--;
        } else {
          clearInterval(timer);
        }
      }, 1000);
  
      // 发送短信api
      const params = { mobile: mobile };
      sendCode(params)
        .then(() => {
          ElMessage.success('发送成功');
        })
        .catch((error) => {
          ElMessage.error("发送失败");
        });
    };
  
    return {
      codeCount,
      getCode
    };
  }