package com.common.realm;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.common.constant.UserConstant;
import com.common.enums.ResultEnum;
import com.common.util.JWTUtil;
import com.common.util.RedisUtil;
import com.common.vo.CustomException;
import com.common.vo.JWTToken;
import com.entity.sys.SysMenu;
import com.entity.sys.SysRole;
import com.entity.sys.SysUser;
import com.service.sys.SysMenuService;
import com.service.sys.SysRoleService;
import com.service.sys.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * web端登录的Realm管理
 */
public class WebRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysMenuService menuService;
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

    private static final String ADMIN_LOGIN_TYPE = UserConstant.WEB;
    {
        super.setName("web");    //设置realm的名字，非常重要
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser user = (SysUser)principalCollection.getPrimaryPrincipal();
        //这里可以进行授权和处理
        Set<String> rolesSet = new HashSet<>();
        Set<String> permsSet = new HashSet<>();
        /*if (JWTUtil.isAdmin()){
            permsSet.add("*:*:*");
            rolesSet.add("admin");
        }else {
            //查询角色和权限(这里根据业务自行查询)
            *//*List<SysRole> roleList = roleService.selectRoleByUserId(user);
            for (SysRole role:roleList) {
                rolesSet.add(role.getRoleName());
                List<SysMenu> menuList = menuService.selectMenuByRoleId(role.getRoleId());
                for (SysMenu menu :menuList) {
                    permsSet.add(menu.getPerms());
                }
            }*//*
        }*/
        //将查到的权限和角色分别传入authorizationInfo中
        authorizationInfo.setStringPermissions(permsSet);
        authorizationInfo.setRoles(rolesSet);
        return authorizationInfo;
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
        String userNameType = userName+"_"+UserConstant.WEB;
        if (redisUtil.hasKey(userNameType)){
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
