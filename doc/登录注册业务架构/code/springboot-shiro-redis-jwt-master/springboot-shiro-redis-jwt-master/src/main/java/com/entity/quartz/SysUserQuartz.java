package com.entity.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fuHua
 * @date 2021年05月13日 14:11
 */
public class SysUserQuartz extends QuartzJobBean {

    /**
     * 这里写需要执行的业务
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //获取JobDetail中传递的参数
        String userName = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("userName");
        String realName = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("realName");

        //获取当前时间
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //打印信息
        System.out.println("用户名称：" + userName);
        System.out.println("真实姓名：" + realName);
        System.out.println("当前时间：" + dateFormat.format(date));
        System.out.println("----------------------------------------");

    }
}
