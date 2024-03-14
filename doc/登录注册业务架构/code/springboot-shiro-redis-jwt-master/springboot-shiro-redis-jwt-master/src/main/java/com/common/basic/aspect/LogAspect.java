package com.common.basic.aspect;

import com.common.basic.annotate.Log;
import com.common.util.JWTUtil;
import com.common.util.ServletUtil;
import com.entity.sys.SysLog;
import com.entity.sys.SysUser;
import com.service.sys.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 操作日志记录处理
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private SysLogService logService;

    // 配置织入点
    @Pointcut("@annotation(com.common.basic.annotate.Log)")
    public void logPointCut() { }

    /**
     * 处理完请求后执行
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }



    /**
     * 拦截异常操作
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            // 获取当前的用户
            SysUser loginUser = JWTUtil.getUserInfo();
            SysLog sysLog = new SysLog();
            sysLog.setStatus(0);//设置状态
            if (loginUser != null) {
                //设置操作用户的用户名
                sysLog.setLogName(loginUser.getUserName());
            }
            HttpServletRequest request = ServletUtil.getRequest();
            //设置请求的url
            sysLog.setLogUrl(request.getRequestURI());
            //设置请求的ip
            sysLog.setLogIp(ServletUtil.getIpAddr(request));
            if (e != null) {
                sysLog.setStatus(1);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sysLog.setLogTime(sdf.format(new Date()));
            // 处理设置注解上的参数
            getControllerMethodDescription(controllerLog, sysLog);
            // 保存数据库
            logService.save(sysLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * @param log 日志
     * @param sysLog 操作日志
     */
    public void getControllerMethodDescription(Log log, SysLog sysLog){
        // 设置操作名称
        sysLog.setLogMethod(log.method());
    }


    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }
}
