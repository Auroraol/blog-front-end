package com.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.entity.sys.SysUser;
import com.service.sys.SysRoleService;
import org.apache.shiro.SecurityUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 */
public class JWTUtil {

    //token有效时长
    private static final long EXPIRE=30*60*1000L;
    //token的密钥
    private static final String SECRET="jwt+shiro";

    private static SysRoleService roleService = SpringUtil.getBean(SysRoleService.class);

    public static String createToken(String userName,Long current,String loginType) {
        //token过期时间
        Date date=new Date(current+EXPIRE);

        //jwt的header部分
        Map<String ,Object>map=new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");

        //使用jwt的api生成token
        String token= null;//签名
        try {
            token = JWT.create()
                    .withHeader(map)
                    .withClaim("userName", userName+"_"+loginType)//私有声明
                    .withClaim("current",current)//当前时间截点
                    .withExpiresAt(date)//过期时间
                    .withIssuedAt(new Date())//签发时间
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return token;
    }

    //校验token的有效性，1、token的header和payload是否没改过；2、没有过期
    public static boolean verify(String token){
        try {
            //解密
            JWTVerifier verifier=JWT.require(Algorithm.HMAC256(SECRET)).build();
            verifier.verify(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //无需解密也可以获取token的信息
    public static String getUserName(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            String userName = jwt.getClaim("userName").asString();
            userName = userName.substring(0,userName.lastIndexOf("_"));
            return userName;
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getUserNameType(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userName").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static SysUser getUserInfo(){
        SysUser user =(SysUser) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    public static Long getUserId(){
        return getUserInfo().getId();
    }

    /**
     * 判断当前用户是否是管理员
     */
    /*public static Boolean isAdmin(){
        List<SysRole> roles = roleService.selectRoleByUserId(getUserInfo());
        boolean flag = false;
        for (SysRole role:roles) {
            if (role.getRoleKey().equals(UserConstant.ADMIN)){
                flag = true;
            }
        }
        return flag;
    }*/

    /*public static Boolean isAdmin(SysUser user){
        List<SysRole> roles = roleService.selectRoleByUserId(user);
        boolean flag = false;
        for (SysRole role:roles) {
            if (role.getRoleKey().equals(UserConstant.ADMIN)){
                flag = true;
            }
        }
        return flag;
    }*/

    //获取过期时间
    public static Long getExpire(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("current").asLong();
        }catch (Exception e){
            return null;
        }
    }
}
