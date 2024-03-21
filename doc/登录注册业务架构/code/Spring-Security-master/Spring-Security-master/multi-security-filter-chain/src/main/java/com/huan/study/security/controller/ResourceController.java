package com.huan.study.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 资源控制器
 *
 * @author huan.fu 2021/7/13 - 下午9:33
 */
@Controller
public class ResourceController {

    /**
     * 返回用户信息
     */
    @GetMapping("/api/userInfo")
    @ResponseBody
    public Authentication showUserInfoApi() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("username","张三");
        return "index";
    }
}
