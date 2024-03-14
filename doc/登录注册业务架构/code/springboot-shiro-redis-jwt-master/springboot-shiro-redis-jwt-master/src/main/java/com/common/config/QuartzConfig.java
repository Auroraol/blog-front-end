package com.common.config;

import com.entity.quartz.SysUserQuartz;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fuHua
 * @date 2021年05月13日 14:08
 */
@Configuration
public class QuartzConfig {
    private static String JOB_GROUP_NAME = "sysUserQuartz";

    /**
     * 定时任务1：
     * 同步用户信息Job（任务详情）
     */
    @Bean
    public JobDetail syncUserJobDetail(){
        JobDetail jobDetail = JobBuilder.newJob(SysUserQuartz.class)
                .withIdentity("sysUserQuartz",JOB_GROUP_NAME)
                .usingJobData("userName", "fuHua") //设置参数（键值对）
                .usingJobData("realName","符华")
                .storeDurably() //即使没有Trigger关联时，也不需要删除该JobDetail
                .build();
        return jobDetail;
    }

    /**
     * 定时任务1：
     * 同步用户信息Job（触发器）
     */
    @Bean
    public Trigger syncUserJobTrigger(){
        //每隔30秒执行一次，格式：[秒] [分] [小时] [日] [月] [周] [年]
        //0 31 14 ? * 5 每周四下午两点31分执行
        //0 15 10 15 * ? 每月15号上午10点15分执行
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/30 * * * * ?");
        //创建触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(syncUserJobDetail())//关联上述的JobDetail
                .withIdentity("sysUserQuartz",JOB_GROUP_NAME)//给Trigger起个名字
                .withSchedule(cronScheduleBuilder)
                .build();
        return trigger;
    }
}
