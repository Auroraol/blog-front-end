package com.common.filter;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.common.constant.UserConstant;
import com.common.util.JWTUtil;
import com.common.vo.JWTToken;
import com.entity.sys.SysToken;
import com.entity.sys.SysUser;
import com.common.util.RedisUtil;
import com.common.util.SpringUtil;
import com.common.vo.ResultVo;
import com.service.sys.SysTokenService;
import com.service.sys.SysUserService;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTFilter extends BasicHttpAuthenticationFilter {


    //是否允许访问，如果带有 token，则对 token 进行检查，否则直接通过
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req= (HttpServletRequest) request;
        //判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)){
            String loginType=req.getHeader("loginType");
            //判断登录终端是否是app端登录
            if (UserConstant.APP.equals(loginType)){
                try {
                    //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
                    executeLogin(request, response);
                    return true;
                }catch (Exception e){
                    return refreshTokenApp(request,response);
                }
            }else {
                try {
                    //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
                    executeLogin(request, response);
                    return true;
                }catch (Exception e){
                    /*
                     * 注意这里捕获的异常其实是在Realm抛出的，但是由于executeLogin（）方法抛出的异常是从login（）来的，
                     * login抛出的异常类型是AuthenticationException，所以要去获取它的子类异常才能获取到我们在Realm抛出的异常类型。
                     * */
                    Throwable cause = e.getCause();
                    if (cause!=null&&cause instanceof TokenExpiredException){
                        //AccessToken过期，尝试去刷新token
                        String result = refreshToken(request, response);
                        if (result.equals("success")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req= (HttpServletRequest) request;
        String token=req.getHeader("Authorization");
        return token !=null;
    }
    /*
     * executeLogin实际上就是先调用createToken来获取token，这里我们重写了这个方法，就不会自动去调用createToken来获取token
     * 然后调用getSubject方法来获取当前用户再调用login方法来实现登录
     * 这也解释了我们为什么要自定义jwtToken，因为我们不再使用Shiro默认的UsernamePasswordToken了。
     * */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response){
        HttpServletRequest req= (HttpServletRequest) request;
        String token=req.getHeader("Authorization");
        String loginType=req.getHeader("loginType");
        JWTToken jwt=new JWTToken(token,loginType);
        //交给自定义的realm对象去登录，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwt);
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse res= (HttpServletResponse) response;
        res.setHeader("Access-control-Allow-Origin",req.getHeader("Origin"));
        res.setHeader("Access-control-Allow-Methods","GET,POST,OPTIONS,PUT,DELETE");
        res.setHeader("Access-control-Allow-Headers",req.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * isAccessAllowed返回false时，执行该方法
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //这里是个坑，如果不设置的接受的访问源，那么前端都会报跨域错误，因为这里还没到corsConfig里面
        httpServletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(1003);
        resultVo.setMessage("用户未登录，请进行登录");
        httpServletResponse.getWriter().write(JSONObject.toJSON(resultVo).toString());
        return false;
    }

    //刷新token
    private String refreshToken(ServletRequest request,ServletResponse response) {
        HttpServletRequest req= (HttpServletRequest) request;
        RedisUtil redisUtil= SpringUtil.getBean(RedisUtil.class);
        //获取传递过来的accessToken
        String accessToken=req.getHeader("Authorization");
        String loginType=req.getHeader("loginType");
        //获取token里面的用户名
        String userName = JWTUtil.getUserName(accessToken);
        String userNameType = userName+"_"+loginType;
        //判断refreshToken是否过期了，过期了那么所含的username的键不存在
        if (redisUtil.hasKey(userNameType)){
            //判断refresh的时间节点和传递过来的accessToken的时间节点是否一致，不一致校验失败
            long current= (long) redisUtil.hget(userNameType,"current");

            if (current==JWTUtil.getExpire(accessToken)){
                //获取当前时间节点
                long currentTimeMillis = System.currentTimeMillis();
                //生成刷新的token
                String token=JWTUtil.createToken(userName,currentTimeMillis,loginType);
                //刷新redis里面的refreshToken,过期时间是30min
                Map<String,Object> setMap = new HashMap<>();
                setMap.put("current",currentTimeMillis);
                setMap.put("userInfo",redisUtil.hget(userNameType,"userInfo"));
                redisUtil.hmset(userNameType,setMap,30*60);
                //再次交给shiro进行认证
                JWTToken jwtToken=new JWTToken(token,loginType);
                try {
                    getSubject(request, response).login(jwtToken);
                    // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.setHeader("Authorization", token);
                    httpServletResponse.setHeader("loginType", loginType);
                    httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                    return "success";
                }catch (Exception e){
                    return e.getMessage();
                }
            }
        }
        return "token认证失效，token过期，重新登陆";
    }


    /**
     * app刷新token
     */
    private Boolean refreshTokenApp(ServletRequest request,ServletResponse response) {
        HttpServletRequest req= (HttpServletRequest) request;
        RedisUtil redisUtil=SpringUtil.getBean(RedisUtil.class);

        // 如果是app端登录，则根据token去数据库查询，有数据则刷新token。
        // 并将新的token保存到数据库中，没有数据则提示用户重新登录。
        SysTokenService tokenService = SpringUtil.getBean(SysTokenService.class);
        SysUserService userService = SpringUtil.getBean(SysUserService.class);
        String token = req.getHeader("Authorization");
        String loginType=req.getHeader("loginType");
        SysToken sysToken = tokenService.getByToken(token);
        if (sysToken != null) {
            SysUser user = userService.getById(sysToken.getUserId());
            long currentTimeMillis = System.currentTimeMillis();
            String newToken = JWTUtil.createToken(user.getUserName(), currentTimeMillis,UserConstant.APP);
            sysToken.setLoginTime(new Date());
            sysToken.setToken(newToken);
            tokenService.updateById(sysToken);

            Map<String,Object> setMap = new HashMap<>();
            setMap.put("current",currentTimeMillis);
            setMap.put("userInfo",user);
            redisUtil.hmset(user.getUserName()+"_"+UserConstant.APP,setMap,30*60);
            JWTToken jwtToken = new JWTToken(newToken,loginType);
            try {
                getSubject(request, response).login(jwtToken);
                // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader("Authorization", newToken);
                httpServletResponse.setHeader("loginType", loginType);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                return true;
            }catch (Exception e){
                e.getMessage();
            }
        }
        return false;
    }

}
