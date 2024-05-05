package com.lfj.blog.common.security.details;

import com.lfj.blog.common.security.details.vo.UserVo;
import com.lfj.blog.service.IUserService;
import com.lfj.blog.utils.BeanCopyUtil;
import com.lfj.blog.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 确认用户数据的来源
 **/
@Service
public class UserDetailsServiceimpl implements UserDetailsService {

	@Autowired
	private IUserService userService; //用来查询数据库用户信息


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//通过用户名或者手机从数据库里查询用户对象
		boolean isMobile = ValidateUtil.validateMobile(username);
		UserVo userVo;
		if (isMobile) {
			userVo = userService.selectUserVoByUsernameOtherwiseMobile(null, Long.parseLong(username));
		} else {
			userVo = userService.selectUserVoByUsernameOtherwiseMobile(username, null);
		}
		//如果存在则返回用户对象，不存在则抛出异常
		if (userVo == null) {
			throw new UsernameNotFoundException("user not found:" + username);
		}
		return BeanCopyUtil.copyObject(userVo, CustomUserDetails.class);
	}
}
