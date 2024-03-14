package com.common.realm;

import com.common.vo.JWTToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;
import java.util.HashMap;

/**
 * 自定义的Realm管理，主要针对多realm
 */
public class ModularRealm extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        HashMap<String, Realm> realmHashMap = new HashMap<>(realms.size());
        for (Realm realm : realms) {
            realmHashMap.put(realm.getName(), realm);
        }
        JWTToken token = (JWTToken) authenticationToken;
        // 登录类型
        String type = token.getLoginType();
        if (realmHashMap.get(type) != null) {
            return doSingleRealmAuthentication(realmHashMap.get(type), token);
        } else {
            return doMultiRealmAuthentication(realms, token);
        }
    }
}
