package com.common.config;

import com.common.constant.UserConstant;
import com.common.filter.JWTFilter;
import com.common.realm.MobileRealm;
import com.common.realm.ModularRealm;
import com.common.realm.WebRealm;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * @Description Shiro配置类
 */
@Configuration
public class ShiroConfig {

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro-aop注解支持
     * @Attention 使用代理方式所以需要开启代码支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro基础配置
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 注意过滤器配置顺序不能颠倒
        // 配置过滤:不会被拦截的链接
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");

//        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/uploads/**", "anon");
        filterChainDefinitionMap.put("/api/login/getCode", "anon");
        filterChainDefinitionMap.put("/api/login/web", "anon");
        filterChainDefinitionMap.put("/api/login/app", "anon");
        filterChainDefinitionMap.put("/api/user/exportWord", "anon");
        //将所有请求指向我们自己定义的jwt过滤器
        filterChainDefinitionMap.put("/**", "jwt");
        //获取filters
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        //设置我们自定义的JWT过滤器，并且取名为jwt
        filters.put("jwt",new JWTFilter());
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public Authenticator authenticator() {
        ModularRealm modularRealm = new ModularRealm();
        modularRealm.setRealms(Arrays.asList(webRealm(), mobileRealm()));
        modularRealm.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealm;
    }

    /**
     * 安全管理器
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //多realm
        Set<Realm> realms = new HashSet<Realm>();
        realms.add(mobileRealm());
        realms.add(webRealm());
        securityManager.setRealms(realms);
        //关闭session
        DefaultSubjectDAO subjectDAO=new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator=new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        securityManager.setAuthenticator(authenticator());//解决多realm的异常问题重点在此
        return securityManager;
    }

    /**
     * app端的身份验证器
     */
    @Bean
    public MobileRealm mobileRealm() {
        MobileRealm mobileRealm = new MobileRealm();
        mobileRealm.setName(UserConstant.APP);
        return mobileRealm;
    }
    /**
     * web端的身份验证器
     */
    @Bean
    public WebRealm webRealm() {
        WebRealm webRealm = new WebRealm();
        webRealm.setName(UserConstant.WEB);
        return webRealm;
    }
}