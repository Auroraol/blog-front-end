package com.lfj.blog.common.security.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lfj.blog.common.security.details.vo.UserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * 该类是SpringSecurity用于认证/授权的用户类, 需要继承用户实体和实现 UserDetails接口
 * getAuthorities() 方法返回用户的权限集合，通过 roles 字段创建了对应的 GrantedAuthority 对象。
 * isAccountNonExpired() 方法通过判断用户的状态来确定用户账户是否过期。
 * isAccountNonLocked() 方法通过判断用户的状态来确定用户账户是否被锁定。
 * isCredentialsNonExpired() 方法始终返回 true，表示用户的凭据（密码）不会过期。
 * isEnabled() 方法通过判断用户的状态来确定用户账户是否可用。 *
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomUserDetails extends UserVo implements UserDetails {

	//存储SpringSecurity调用getAuthorities()方法获取的权限信息的集合
	@Override
	@JsonIgnore //因为SimpleGrantedAuthority是Spring中的类并没有实现序列化接口，所以在存入redis的过程中序列化会报错，所以加上注解就不会把此成员变量存入redis
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (!CollectionUtils.isEmpty(roles)) {
			//把roles中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
			return roles.stream().map(this::createAuthority).collect(Collectors.toSet());
		}
		return Collections.emptyList();
	}

	private GrantedAuthority createAuthority(String authority) {
		return (() -> authority);
	}

	/**
	 * 是否账户未过期，设置为true，即未过期；
	 */
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return !getStatus().equals(3);
	}

	/**
	 * 是否账户未被锁定，设置为true，即未被锁定；
	 *
	 * @return
	 */
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return !getStatus().equals(1);
	}

	/**
	 * 是否密码未过期，设置为true，即未过期；
	 *
	 * @return
	 */
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 用户是否弃用，设置为true，即弃用。
	 *
	 * @return
	 */
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return !getStatus().equals(2);
	}
}
