

# 安全架构

## 认证： Authentication

who are you?
登录系统，用户系统

认证就是让系统知道我们是谁。认证的方式有很多，常见的账号密码登录，手机验证码登录，指纹登录，刷脸登录等等

![img](SpringSecurity.assets/1667737807166-7e3d7227-7dfd-4813-9f47-8bf22efe5f23.png)

## 授权：  Authorization

what are you allowed to do?
权限管理，用户授权

 授权是用户认证通过后根据用户的权限来控制用户访问资源的过程，拥有资源的访问权限则正常访问，没有权限则拒绝访问。

![img](SpringSecurity.assets/1667737821930-2ef90891-5801-4c29-b4b4-9f4025374260.png)

![image.png](SpringSecurity.assets/1667781052294-95ee4c11-16d2-4ed8-9cee-9b05ff52ca4b.png)

# Spring Security 原理

Spring Security利用 FilterChainProxy 封装一系列拦截器链，实现各种安全拦截功能

Servlet三大组件：Servlet、Filter、Listener

## 1. 过滤器链架构

![img](SpringSecurity.assets/1682513527616-e1e9054a-9049-4005-8c92-86392f3012a2.png)

## 2. FilterChainProxy

![img](SpringSecurity.assets/1682513900851-013516c0-b3d4-4a09-823a-e924a5fa8f2c.png)

## 3. SecurityFilterChain

![image.png](SpringSecurity.assets/1683548784456-8c66fd8e-783e-4f89-b81f-d3f2771a3ef9.png)

#  常用函数

## 配置HttpSecurity 配置WebSecurity

### HttpSecurity和WebSecurity的区别

WebSecurity不仅通过HttpSecurity定义某些请求的安全控制，也通过其他方式定义其他某些请求可以忽略安全控制;

HttpSecurity仅用于定义需要安全控制的请求(当然HttpSecurity也可以指定某些请求不需要安全控制);

可以认为HttpSecurity是WebSecurity的一部分，WebSecurity是包含HttpSecurity的更大的一个概念;

构建目标不同

+ WebSecurity构建目标是整个Spring Security安全过滤器FilterChainProxy,

+ 而HttpSecurity的构建目标仅仅是FilterChainProxy中的一个SecurityFilterChain。

### < Spring Security 5.7.0

```JAVA
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // configure HTTP security...         
    }
 
    @Override
    public void configure(WebSecurity web) throws Exception {         
        // configure Web security...  
         web.ignoring().antMatchers("/ignore1", "/ignore2");
    }      
}
```

例子

```java
package net.javaguides.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import net.javaguides.springboot.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(
                 "/registration**",
                    "/js/**",
                    "/css/**",
                    "/img/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .and()
        .logout()
        .invalidateHttpSession(true)
        .clearAuthentication(true)
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login?logout")
        .permitAll();
    }

}
```

### > Spring Security 5.7.0

```JAVA
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
         
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      // configure HTTP security...     
    }
     
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
          // configure Web security...  
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }    	
}
```

例子

```java
package net.javaguides.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

//    @Autowired
//    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // configure SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/register/**").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/users").hasRole("ADMIN")
                .and()
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/users")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()

                );
        return http.build();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
//        builder.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }
}
```

## Spring Security JWT

### 旧方式

```java
package com.springboot.blog.config;

import com.springboot.blog.security.CustomUserDetailsService;
import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return  new JwtAuthenticationFilter();
    }

     /**
	 * 配置密码加密对象（解密时会用到PasswordEncoder的matches判断是否正确）
	 * 用户的password用到，所以存的时候存该bean encode过的密码
	 *
	 * @return
	 */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .anyRequest()
                .authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails ramesh = User.builder().username("ramesh").password(passwordEncoder()
//                .encode("password")).roles("USER").build();
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder()
//                .encode("admin")).roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(ramesh, admin);
//    }
}
```

例子

封装UserDetails对象

```java
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private User user;
    private List<String> permissions;
    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}

1234567891011121314151617181920212223242526272829303132333435363738394041424344454647
```

实现UserDetailsService接口，这里使用[Dubbo](https://so.csdn.net/so/search?q=Dubbo&spm=1001.2101.3001.7020)远程调用会员服务从数据库查用户信息并返回封装成LoginUser对象

```java
public class UserDetailsServiceImpl implements UserDetailsService {

    @DubboReference
    private MemberService MemberService;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        //远程调用查询用户信息
        User user = null;
        try {
            user = MemberService.findMemberByUsername(username);
        } catch (InterruptedException e) {
            System.out.println("服务器超时");
        }
        //TODO 查询用户权限信息
        //把数据封装成UserDetails返回
        Long id=user.getId();
        List<String> perms=MemberService.findPermsByUserId(id);
        return new LoginUser(user,perms);
    }
}
123456789101112131415161718192021
```

自定义jwt过滤器

```java
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            System.out.println("token非法");
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userid;
        String user = redisTemplate.opsForValue().get(redisKey);
        LoginUser loginUser = JSONObject.parseObject(user,LoginUser.class);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
123456789101112131415161718192021222324252627282930313233343536373839
```

实现注册登录登出业务逻辑

```java
public CommonResult<Map> login(String name, String password) {

        //AuthenticationManager authenticate进行认证
        // 把用户名和密码交给manager,此Manager会调用UserDetailsService从数据库获取数据来对比认证。
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(name, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        //如果认证通过，使用UserId生成jwt，jwt作为result返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String uid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(uid);
        //把完整的用户信息存入redis，UserId做为key
        String s = JSONObject.toJSONString(loginUser);
        redisTemplate.opsForValue().set("login:" + uid, s, 12, TimeUnit.HOURS);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return CommonResult.success(map);
    }

    @Override
    public CommonResult<String> logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        //删除redis中的值
        String redisKey = "login:" + userid;
        redisTemplate.delete(redisKey);
        return CommonResult.success("注销成功");
    }

    @Override
    public CommonResult<String> register(String name, String password) {

        String username = MemberService.registerMember(name, password);
        if ("null".equals(username)) {
            return CommonResult.failed("用户名：" + name + "已存在，请重新注册");
        }
        return CommonResult.success("注册成功，" + name);
    }
12345678910111213141516171819202122232425262728293031323334353637383940414243
配置过滤器链
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
//    @Autowired
//    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    //创建BCryptPasswordEncoder注入容器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                // 对于登录注册接口 允许匿名访问
                .antMatchers("/auth/login").anonymous()
                .antMatchers("/auth/register").anonymous()
                //设置需要权限的接口
                .antMatchers("/auth/hello2").hasAuthority("haha")
//                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        //添加过滤器,放在UsernamePasswordAuthenticationFilter之前，否则jwt过滤器就没意义了
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//
//        //配置异常处理器
        http.exceptionHandling()
                //配置认证失败处理器
//                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        //允许跨域
        http.cors();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

### 新方式

```java
package com.springboot.blog.config;

import com.springboot.blog.security.CustomUserDetailsService;
import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return  new JwtAuthenticationFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests((authorize) -> authorize
                        .antMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                        .antMatchers("/api/v1/auth/**").permitAll()
                        .antMatchers("/v2/api-docs/**").permitAll()
                        .antMatchers("/swagger-ui/**").permitAll()
                        .antMatchers("/swagger-resources/**").permitAll()
                        .antMatchers("/swagger-ui.html").permitAll()
                        .antMatchers("/webjars/**").permitAll()
                        .anyRequest()
                        .authenticated()
                );
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
//                .antMatchers("/api/v1/auth/**").permitAll()
//                .antMatchers("/v2/api-docs/**").permitAll()
//                .antMatchers("/swagger-ui/**").permitAll()
//                .antMatchers("/swagger-resources/**").permitAll()
//                .antMatchers("/swagger-ui.html").permitAll()
//                .antMatchers("/webjars/**").permitAll()
//                .anyRequest()
//                .authenticated();
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    //    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails ramesh = User.builder().username("ramesh").password(passwordEncoder()
//                .encode("password")).roles("USER").build();
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder()
//                .encode("admin")).roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(ramesh, admin);
//    }
}

```









# 用于认证的用户类继承 UserDetails接口和UserDetailsService接口

**UserDetails接口** 

> 这个接口是我们自己用来定义用户表的结构的
>
> SpringSecurity自己的用户信息只包含了Username，password，roles，假如我希望用户的实体类中还有性别sex字段，那么就没有办法了，所以SpringSecurity提供了UserDetails接口，我们可以自己新建一个包含sex字段的类，然后该类implements UserDetails接口，就可以获取我们说的这个sex字段
>
> UserDetails实例是通过UserDetailsService接口的loadUserByUsername方法返回的，可以参考这篇文章创建自己的UserDetails(本文中包含sex的那个类)
>
> UserDetails接口中有一个getAuthorities方法，这个方法返回的是权限，但是我们返回的权限必须带有“ROLE_”开头才可以，spring会自己截取ROLE_后边的字符串，也就是说，比如：我的权限叫ADMIN，那么，我返回告诉spring security的时候，必须告诉他权限是ROLE_ADMIN，这样spring security才会认为权限是ADMIN

示例代码：
假设一个用户拥有两个权限，一个叫权限1，一个叫权限2，下面的代码演示了如何使用UserDetails接口获取这两个权限，其本质就是实现getAuthorities()方法

```java
public MyUser implements UserDetails{
		// 这个GrantedAuthority是什么呢？其实它就是权限(Role)，
		// 因为本例中该用户有两个权限，所以这个Collection的长度是2
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities(){
				String role1="ROLE_权限1";
				String role2="ROLE_权限2";
				GrantedAuthority authority1=new SimpleGrantedAuthority(role1);
				GrantedAuthority authority2=new SimpleGrantedAuthority(role2);
				List<GrantedAuthority> list=new ArrayList<>();
				list.add(authority1);
				list.add(authority2);
				return list;				
		}
}
```

根据UserDetails接口，我们知道spring更推荐将权限用逗号隔开的方式存到数据库user表中，而不推荐使用1对多的方式来存储权限（单独的一个user表和权限表，然后使用一个关系表将两个表关联起来）

注：
spring security提供了逗号工具类，可以将逗号格式的字符串转换成GrantedAuthority

```java
AuthorityUtils.commaSeparatedStringToAuthorityList
```

**UserDetailsService接口**

> 这个接口是用户数据的来源，没错，就是登录用户的数据从哪里来，是从数据库来？还是从你电脑的TXT文本上过来的，还是从其他地方，反正这个来源最终必须要实现UserDetailsService才可以，至于具体来源的数据长什么样，有哪些字段，那就看不同的实现了(UserDetails接口的实现)
>
> 任何实现了UserDetailsService接口的实现类，都可以作为认证数据源(登录用户存在哪)，只要把这个实现类的实例注入到spring容器中，SpringSecurity便可以自动发现并使用该实例

下面是使用SpringSecurity实现的认证数据源之一JdbcUserDetailsManager的示例代码

```java
@Autowired
DataSource dataSource;
 
@Bean
public UserDetailsService userDetailsService(){
		JdbcUserDetailsManager manager=new JdbcUserDetailsManager();
		manager.setDataSource(dataSource);
		if(!manager.userExists("登录账号")){
				manager.crearteUser(User.withUsername("登录账号").password("密码").roles("权限1","权限2").build());
		}
		//接下来可以多个if
}
```

介绍该接口的方法loadUserByUsername

```java
//mybatis的mapper，对应我数据库中一个叫user的表
@Autowired
private UserMapper userMapper;
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		User user=userMapper.findByName(username);
		if(user==null)
				throw new UsernameNotFoundException("用户不存在");
		return user;
}
```

## 例子

### ①  实现 UserDetails接口 (自定义认证类对象)

> UserDetails接口是用来定义用户表的结构的, 本质就是实现getAuthorities()方法

**UserVo对象**

```java
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "UserVo对象", description = "用户详细信息")
public class UserVo extends User {

   /**
    * 角色列表
    */
   @ApiModelProperty(value = "角色列表")
   protected List<String> roles;

}
```

**CustomUserDetails对象**

```java
package com.lfj.blog.service.vo;

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

```

### ② 实现 UserDetailsService

> 任何实现了UserDetailsService接口的实现类，都可以作为认证数据源(登录用户存在哪)，只要把这个实现类的实例注入到spring容器中，SpringSecurity便可以自动发现并使用该实例

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean isMobile = ValidateUtil.validateMobile(username);
        UserVo userVo;
        if (isMobile) {
            userVo = userService.selectUserVoByUsernameOtherwiseMobile(null,Long.parseLong(username));
        } else {
            userVo = userService.selectUserVoByUsernameOtherwiseMobile(username,null);
        }
        if (userVo == null) {
            throw new UsernameNotFoundException("user not found:" + username);
        }
        UserDetails userDetails = new CustomUserDetails();
        BeanUtils.copyProperties(userVo,userDetails);
        return userDetails;
    }
}
```

## 例子2

```java
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private User user;
    private List<String> permissions;
    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

实现UserDetailsService接口，这里使用[Dubbo](https://so.csdn.net/so/search?q=Dubbo&spm=1001.2101.3001.7020)远程调用会员服务从数据库查用户信息并返回封装成LoginUser对象

```java
public class UserDetailsServiceImpl implements UserDetailsService {

    @DubboReference
    private MemberService MemberService;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        //远程调用查询用户信息
        User user = null;
        try {
            user = MemberService.findMemberByUsername(username);
        } catch (InterruptedException e) {
            System.out.println("服务器超时");
        }
        //TODO 查询用户权限信息
        //把数据封装成UserDetails返回
        Long id=user.getId();
        List<String> perms=MemberService.findPermsByUserId(id);
        return new LoginUser(user,perms);
    }
}
```













核心

- **WebSecurityConfigurerAdapter**
- @**EnableGlobalMethodSecurity**： 开启全局方法安全配置

- - @Secured
  - @PreAuthorize
  - @PostAuthorize

- **UserDetailService： 去数据库查询用户详细信息的service（用户基本信息、用户角色、用户权限）**



