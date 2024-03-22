package com.lfj.blog.common.security.details.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @author: yaohw
 * @create: 2019-10-24 16:45
 * SpringSecurity自己的用户信息只包含了Username，password，roles，
 * 假如我希望用户的实体类中还有性别sex字段，那么就没有办法了，所以SpringSecurity提供了UserDetails接口，
 * <p>
 * getAuthorities() 方法返回用户的权限集合，通过 roles 字段创建了对应的 GrantedAuthority 对象。
 * isAccountNonExpired() 方法通过判断用户的状态来确定用户账户是否过期。
 * isAccountNonLocked() 方法通过判断用户的状态来确定用户账户是否被锁定。
 * isCredentialsNonExpired() 方法始终返回 true，表示用户的凭据（密码）不会过期。
 * isEnabled() 方法通过判断用户的状态来确定用户账户是否可用。
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomUserDetails extends UserVo implements UserDetails {

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (!CollectionUtils.isEmpty(roles)) {
			return roles.stream().map(this::createAuthority).collect(Collectors.toSet());
		}
		return Collections.emptyList();
	}

	private GrantedAuthority createAuthority(String authority) {
		return (() -> authority);
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return !getStatus().equals(3);
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return !getStatus().equals(1);
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return !getStatus().equals(2);
	}
}
