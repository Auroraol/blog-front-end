package com.common.realm;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.common.constant.UserConstant;
import com.common.enums.ResultEnum;
import com.common.util.JWTUtil;
import com.common.util.RedisUtil;
import com.common.vo.CustomException;
import com.common.vo.JWTToken;
import com.entity.sys.SysUser;
import com.service.sys.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * app端登录的Realm管理
 */
public class MobileRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService userService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 使用JWT替代原生Token
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    private static final String ADMIN_LOGIN_TYPE = UserConstant.APP;
    {
        super.setName("app");    //设置realm的名字，非常重要
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String jwt= (String) authenticationToken.getCredentials();
        String userName= JWTUtil.getUserName(jwt);
        SysUser user = userService.getUserByName(userName);
        //判断账号是否存在
        if (user == null ) {
            throw new CustomException(ResultEnum.USER_NOT_ERROR,"");
        }
        String userNameType = userName+"_"+UserConstant.APP;
        if (redisUtil.hasKey(userNameType)){
            //判断AccessToken有无过期
            if (!JWTUtil.verify(jwt)){
                throw new TokenExpiredException("token认证失效，token过期，重新登陆");
            }else {
                //判断AccessToken和refreshToken的时间节点是否一致
                long current = (long) redisUtil.hget(userNameType, "current");
                if (current==JWTUtil.getExpire(jwt)){
                    return new SimpleAuthenticationInfo(user,jwt,getName());
                }
            }
        }
        return null;
    }
}
