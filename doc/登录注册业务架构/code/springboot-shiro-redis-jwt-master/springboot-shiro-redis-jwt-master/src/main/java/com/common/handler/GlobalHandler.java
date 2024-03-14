package com.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GlobalHandler implements HandlerInterceptor {

    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalHandler.class);

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
    }

    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        String reqURL = request.getRequestURL().toString();
        if (arg2 instanceof HandlerMethod) {
            StringBuilder stringbuilder = new StringBuilder(1000);
            stringbuilder.append("\n\n\033[1;31m ==============================开始=============================== \033[0m\n");
            HandlerMethod h = (HandlerMethod) arg2;
            stringbuilder.append("\033[1;32m Controller: \033[0m").append("\033[1;34m ").append(h.getBean().getClass().getName()).append(" \033[0m").append("\n");
            stringbuilder.append("\033[1;32m Method    : \033[0m").append("\033[1;34m ").append(h.getMethod().getName()).append(" \033[0m").append("\n");
            stringbuilder.append("\033[1;32m URL       : \033[0m").append("\033[1;34m ").append(request.getRequestURI()).append(" \033[0m").append("\n");
            stringbuilder.append("\033[1;32m AllURL    : \033[0m").append("\033[1;34m ").append(reqURL).append(" \033[0m").append("\n");
            stringbuilder.append("\033[1;31m ==============================结束============================== \033[0m\n");
            LOGGER.debug(stringbuilder.toString());
        }
        return true;
    }
}
