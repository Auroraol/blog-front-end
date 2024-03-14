package com.controller.sys;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.common.constant.UserConstant;
import com.common.enums.ResultEnum;
import com.common.util.*;
import com.common.vo.CustomException;
import com.common.vo.ResultVo;
import com.entity.sys.SysToken;
import com.entity.sys.SysUser;
import com.service.sys.SysTokenService;
import com.service.sys.SysUserService;
import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/${api.url.prefix}/login")
public class SysLoginController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysTokenService tokenService;

    //密码最大错误次数
    private static int ERROR_COUNT = 3;

    /**
     * web端登录
     */
    @PostMapping("/web")
    public ResultVo web(String userName, String password,String code){
        try {
            Object verCode = redisUtil.get("verCode");
            if (null == verCode)
                return ResultUtil.error("验证码已失效，请重新输入");
            String verCodeStr = verCode.toString();
            if (verCodeStr == null || StringUtils.isEmpty(code) || !verCodeStr.equalsIgnoreCase(code))
                return ResultUtil.error("验证码错误");
            else if (!redisUtil.hasKey("verCode"))
                return ResultUtil.error("验证码已过期,请重新输入");
            else
                redisUtil.del("verCode");

            String salt = userService.getSalt(userName);
            password = SHA256Util.sha256(password, salt);
            //验证用户名和密码
            SysUser user = passwordErrorNum(userName,password);

            long currentTimeMillis = System.currentTimeMillis();
            String token= JWTUtil.createToken(user.getUserName(),currentTimeMillis,UserConstant.WEB);
            Map<String, Object> map = new HashMap<>();
            map.put("current",currentTimeMillis);
            map.put("userInfo",user);
            redisUtil.hmset(userName+"_"+UserConstant.WEB,map,60*30);

            return ResultUtil.success(token);
        }catch (IncorrectCredentialsException e) {
            return ResultUtil.error(1000,e.getMessage());
        } catch (LockedAccountException e) {
            return ResultUtil.error(1004,e.getMessage());
        } catch (AuthenticationException e) {
            return ResultUtil.error(ResultEnum.USER_NOT_ERROR);
        } catch (Exception e) {
            return ResultUtil.error(ResultEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * web登录获取验证码
     */
    @RequestMapping(value = "/getCode", method = RequestMethod.GET)
    public void getCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            // 生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
            redisUtil.del("verCode");
            //将验证码存入redis中，设置有效期为一分钟
            redisUtil.set("verCode",verifyCode,60);
            // 生成图片
            int w = 200, h = 50;
            OutputStream out = response.getOutputStream();
            VerifyCodeUtils.outputImage(w, h, out, verifyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * app端登录
     */
    @PostMapping("/app")
    public ResultVo app(String userName, String password){
        try {
            String salt = userService.getSalt(userName);
            password = SHA256Util.sha256(password, salt);
            //验证用户名和密码
            SysUser user = passwordErrorNum(userName,password);
            long currentTimeMillis = System.currentTimeMillis();
            String token= JWTUtil.createToken(user.getUserName(),currentTimeMillis,UserConstant.APP);
            SysToken sysToken = new SysToken();
            sysToken.setUserId(user.getId());
            sysToken.setToken(token);
            sysToken.setType(2);
            sysToken.setStatus(1);
            sysToken.setLoginTime(new Date());
            tokenService.save(sysToken);

            Map<String, Object> map = new HashMap<>();
            map.put("current",currentTimeMillis);
            map.put("userInfo",user);
            redisUtil.hmset(userName+"_"+UserConstant.APP,map,60*30);

            return ResultUtil.success(token);
        }catch (IncorrectCredentialsException e) {
            return ResultUtil.error(1000,e.getMessage());
        } catch (LockedAccountException e) {
            return ResultUtil.error(1004,e.getMessage());
        } catch (AuthenticationException e) {
            return ResultUtil.error(ResultEnum.USER_NOT_ERROR);
        } catch (Exception e) {
            return ResultUtil.error(ResultEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 退出登录
     */
    @DeleteMapping("/logout")
    @RequiresAuthentication
    public ResultVo logout(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String loginType = request.getHeader("loginType");
        if (UserConstant.APP.equals(loginType)){
            SysToken sysToken = tokenService.getByToken(token);
            if (sysToken != null){
                tokenService.removeById(sysToken.getId());
            }
        }
        String username=JWTUtil.getUserName(token);
        redisUtil.del(username+"_"+loginType);
        return ResultUtil.success("退出登录成功");
    }

    /**
     * 密码错误次数验证
     * @param userName
     * @param password
     * @return
     */
    private SysUser passwordErrorNum(String userName,String password){
        //查询用户
        SysUser user = userService.getUserByName(userName);
        if (null == user){
            throw new AuthenticationException();
        }
        /*Safe securitySet = securitySetService.getById(1);
        //密码登录限制（0：连续错3次，锁定账号15分钟。1：连续错5次，锁定账号30分钟）
        if (securitySet.getPwdLoginLimit()==1){
            ERROR_COUNT = 5;
        }*/
        //登录时间
        Date allowTime = user.getLoginDate() == null ? new Date() : user.getLoginDate();
        //当前时间
        Date currentTime = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            allowTime = sdf.parse(sdf.format(allowTime));
        }catch (ParseException e){
            throw new CustomException(-1,"日期转换异常","");
        }
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        //如果当前登录时间大于允许登录时间
        if (allowTime == null || currentTime.getTime() > allowTime.getTime()) {
            // 判断用户账号和密码是否正确
            user = userService.getUserByPass(userName, password);
            if (user != null) {
                //正确密码错误次数清零
                updateWrapper.set("error_num",0);
                updateWrapper.set("login_date",new Date());
                updateWrapper.eq("id",user.getId());
                userService.update(updateWrapper);
            } else {
                //登录错误次数
                int errorNum =  user.getErrorNum();
                //最后登录的时间
                long allowTimes = user.getLoginDate() == null ? 0 : user.getLoginDate().getTime();
                //错误的次数
                if (errorNum < ERROR_COUNT-1) {
                    int surplusCount = ERROR_COUNT - errorNum-1;
                    boolean result;
                    //每次输入错误密码间隔时间在2分钟内 （如果上次登录错误时间距离相差小于定义的时间（毫秒））
                    if ((currentTime.getTime() - allowTimes) <= 120000) {
                        updateWrapper.set("error_num",errorNum + 1);
                        updateWrapper.set("login_date",new Date());
                        updateWrapper.eq("id",user.getId());
                        result = userService.update(updateWrapper);
                    } else {
                        updateWrapper.set("error_num",1);
                        updateWrapper.set("login_date",new Date());
                        updateWrapper.eq("id",user.getId());
                        result = userService.update(updateWrapper);
                    }
                    if (result) {
                        //抛出密码错误异常
                        throw new IncorrectCredentialsException("密码错误，总登录次数"+ERROR_COUNT+"次，剩余次数: " + surplusCount);
                    }
                } else {
                    //错误3次，锁定15分钟后才可登陆 允许时间加上定义的登陆时间（毫秒）
                    Date dateAfterAllowTime = new Date(currentTime.getTime() + 900000);
                    String str = "15";
                    if (ERROR_COUNT == 5){
                        //错误5次，锁定30分钟后才可登陆 允许时间加上定义的登陆时间（毫秒）
                        dateAfterAllowTime = new Date(currentTime.getTime() + 1800000);
                        str = "30";
                    }
                    updateWrapper.set("error_num",0);
                    updateWrapper.set("login_date",dateAfterAllowTime);
                    updateWrapper.eq("id",user.getId());
                    if (userService.update(updateWrapper)) {
                        throw new LockedAccountException("您的密码已错误"+ERROR_COUNT+"次，现已被锁定，请"+str+"分钟后再尝试");
                    }
                }
            }
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(allowTime);
            long time1 = calendar.get(Calendar.MINUTE);
            calendar.setTime(currentTime);
            long time2 = calendar.get(Calendar.MINUTE);
            long between_minute=(time1-time2);
            throw new LockedAccountException("账号锁定，还没到允许登录的时间，请"+between_minute+"分钟后再尝试");
        }
        return user;
    }

}
