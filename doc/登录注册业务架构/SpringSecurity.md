# SpringSecurity官网

[Spring Security](https://spring.io/projects/spring-security)

[Java Configuration :: Spring Security](https://docs.spring.io/spring-security/reference/5.7/servlet/configuration/java.html#page-title)

[Spring Security :: Spring Security](https://docs.spring.io/spring-security/reference/index.html)

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





# 用于认证的用户类实现 UserDetails接口和UserDetailsService接口

SpringSecurity用于认证的用户类, 需要继承用户实体和实现 UserDetails接口

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
 * 该类是SpringSecurity用于认证的用户类, 需要继承用户实体和实现 UserDetails接口
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
package com.lfj.blog.common.security.details;

import com.lfj.blog.common.security.details.vo.UserVo;
import com.lfj.blog.service.IUserService;
import com.lfj.blog.utils.ValidateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author: yaohw
 * @create: 2019-10-24 16:40
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private IUserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		boolean isMobile = ValidateUtil.validateMobile(username);
		UserVo userVo;
		if (isMobile) {
			userVo = userService.selectUserVoByUsernameOtherwiseMobile(null, Long.parseLong(username));
		} else {
			userVo = userService.selectUserVoByUsernameOtherwiseMobile(username, null);
		}
		if (userVo == null) {
			throw new UsernameNotFoundException("user not found:" + username);
		}
		UserDetails userDetails = new CustomUserDetails();
		BeanUtils.copyProperties(userVo, userDetails);
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









SpringSecurity 有用于认证的用户类, 需要继承用户实体和实现 UserDetails接口





# UsernamePasswordAuthenticationToken使用

例如，以下代码演示了如何使用 UsernamePasswordAuthenticationToken 对象进行认证：

```java
// 构造用户名密码认证信息
Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

// 对认证信息进行认证
authentication = authenticationManager.authenticate(authentication);

// 将认证信息存储在 SecurityContextHolder 中
SecurityContextHolder.getContext().setAuthentication(authentication);
```

在这个例子中，authenticationManager 是一个 AuthenticationManager 对象，用于对认证请求进行认证。如果认证成功，authenticate() 方法会返回一个 Authentication 对象，其中包含了认证请求的详细信息，例如认证主体、认证凭证、授权信息等。最后，将认证信息存储在 SecurityContextHolder 中，用于在应用程序中获取当前的认证息。



# 实战

## Authentication

Spring Security为我们提供了一个专门的`org.springframework.security.core.Authentication`接口来代表认证；它最常用的实现类有`UsernamePasswordAuthenticationToken`。

一旦请求被认证后，`Authentication`对象就会自动存储在由`SecurityContextHolder`管理的`SecurityContext`中。

认证的原理通过下面类的处理顺序来进行的：

- `FilterChainProxy`：Servlet过滤器（`Filter`）`springSecurityFilterChain`实际类型是`FilterChainProxy`，它可能包含多个过滤器链（`DefaultSecurityFilterChain`），每个过滤器链包含多个过滤器。特定的过滤器会将请求中的认证信息（如用户名、密码）构造成`Authentication`对象交由`AuthenticationManager`的`authenticate`方法处理。主要的过滤器有：
  - `UsernamePasswordAuthenticationFilter`：使用表单（用户名、密码）提交进行认证信息，构造的`Authentication`对象类型为`UsernamePasswordAuthenticationToken`；并调用`AuthenticationManager`的`authenticate`来进行认证操作。
  - `BasicAuthenticationFilter`：使用HTTP请求的基础授权头提交认证信息，同样构造的`Authentication`对象的类型为`UsernamePasswordAuthenticationToken`；并调用`AuthenticationManager`的`authenticate`来进行认证操作。
  - ExceptionTranslationFilter：处理过滤器链中的异常
    - `AuthenticationException`：认证异常，返回`401`状态码
    - `AccessDeniedException`：授权异常，返回`403`状态码
  - `FilterSecurityInterceptor`：它是`AbstractSecurityInterceptor`的子类，当认证成功后，再使用`AccessDecisionManager`对Web路径资源（web URI）进行授权操作。
- `AuthenticationManager`：`AuthenticationManager`接口的实现为`ProviderManager`，我们使用`AuthenticationManagerBuilder`来定制构建`AuthenticationManager`
- `ProviderManager`：`ProviderManager`通过它`authenticate`方法将认证交给了一组顺序的`AuthenticationProvider`来完成认证。
- `AuthenticationProvider`：`AuthenticationProvider`接口包含两个方法：
  - `supports`：是否支持认证安全过滤器缓解构造的`Authentication`；
  - `authenticate`：对`Authentication`进行认证，若认证通过返回`Authentication`，若不通过则抛出异常。
- `DaoAuthenticationProvider`：`DaoAuthenticationProvider`是`AuthenticationProvider`接口的实现，他支持认证的`Authentication`类型为`UsernamePasswordAuthenticationToken`。它在认证中主要用到了下面三个部分：
  - `UserDetailsService`：从指定的位置（如数据库）获得用户信息；通过比较用户信息和`Authentication`（`UsernamePasswordAuthenticationToken`）中的用户名和密码信息，若认证通过则构建新的`Authentication`（`UsernamePasswordAuthenticationToken`），包含用户的权限信息。
  - `PasswordEncoder`：使用`PasswordEncoder`将请求传来的明文密码和存储的编码后的密码进行匹配比较。

### 配置`AuthenticationManager`

我们可以重载`WebSecurityConfigurerAdapter`类的方法，使用`AuthenticationManagerBuilder`来配置`AuthenticationManager`。

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.
    }

}
```

我们可以通过配置`UserDetailsService`或`AuthenticationProvider`定制认证。

###  `UserDetailsService`

本例定制一个`UserDetailsService`通过Spring Data JPA从数据库中获取用户。

基本外部配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/first_db?useSSL=false
    username: root
    password: zzzzzz
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
```

我们用户的实体：

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SysUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String realName;

    @Column(unique = true)
    private String username;

    private String password;
  
    public SysUser(String realName, String username, String password) {
        this.realName = realName;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //1
        return null;
    }

    @Override
    public String getPassword() { //2
        return this.password;
    }

    @Override
    public String getUsername() { //3
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() { //4
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //5
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //6
        return true;
    }

    @Override
    public boolean isEnabled() { //7
        return true;
    }
}
```

实现`UserDetails`接口的用户，通过接口的方法构建`Authentication`对象的用户信息。

1. `getAuthorities`方法获得用户的权限信息；
2. `getPassword`获得用户的密码，使用存储的密码；
3. `getUsername`获得用户名，使用存储的用户名；
4. `isAccountNonExpired`是否账户未过期，设置为`true`，即未过期；
5. `isAccountNonLocked`是否账户未被锁定，设置为`true`，即未被锁定；
6. `isCredentialsNonExpired`是否密码未过期，设置为`true`，即未过期；
7. `isEnabled`用户是否弃用，设置为`true`，即弃用。

用户的Repository：

```java
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
   Optional<SysUser> findByUsername(String username);
}
```

我们自定义`UserDetailsService`：

```java
public class CusotmUserDetailsService implements UserDetailsService {

    SysUserRepository sysUserRepository;

    public CusotmUserDetailsService(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository; //1
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SysUser> sysUserOptional = sysUserRepository.findByUsername(username); //2
        return  sysUserOptional
                   .orElseThrow(() -> new UsernameNotFoundException("Username not found")); //3 
    }
}
```

1. 注入`SysUserRepository`用来查询数据库用户信息；
2. 通过用户名从数据库里查询用户对象；
3. 如果存在则返回用户对象，不存在则抛出异常；本方法的返回值要求为`UserDetails`类型，我们的用户也是`UserDetails`可直接返回。

我们可以通过配置`AuthenticationManager`来注册`UserDetailsService`，它会替换`。

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SysUserRepository sysUserRepository; //1

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new CusotmUserDetailsService(sysUserRepository)); //2
    }

    @Bean
    PasswordEncoder passwordEncoder(){ //3
        return new BCryptPasswordEncoder();
    }
    
}
```

1. 注入`SysUserRepository`给`CusotmUserDetailsService`的构造使用；
2. 使用`AuthenticationManagerBuilder`的`userDetailsService`方法注册自定义的`UserDetailsService`；
3. 使用`BCrypt`作为我们的密码编码加密算法给`DaoAuthenticationProvider`使用；

我们还可以更简单的，直接通过声明`UserDetailsService`的Bean让`DaoAuthenticationProvider`使用自定义`UserDetailsService`。

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    UserDetailsService userDetailsService(SysUserRepository sysUserRepository){
        return new CusotmUserDetailsService(sysUserRepository);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
```

建立一个控制器用来测试访问：

```java
@RestController
public class IndexController {

    @GetMapping("/")
    public String hello(){
        return "Hello Spring Security";
    }
}
```

应用启动时，向系统添加一个用户：

```java
@Bean
CommandLineRunner createUser(SysUserRepository sysUserRepository, PasswordEncoder passwordEncoder){
   return args -> {
      SysUser user = new SysUser("wangyunfei", "wyf", passwordEncoder.encode("111111"));
      sysUserRepository.save(user);
   };
}
```

Spring Security默认使用表单登陆，且为我们自动提供了一个表单，我们访问http://localhost:8080/会自动转向http://localhost:8080/login；当我们用错误的账号密码登陆时会提示“用户名或密码错误”，用正确的账号密码访问显示测试的控制器返回内容。
![image-20240324210419848](SpringSecurity.assets/image-20240324210419848.png)

### `AuthenticationProvider`

上面自定义`UserDetailsService`实际上还是`AuthenticationProvider`的一部分，我们也可以通过的Bean来使用自定义的`AuthenticationManager`来设置`AuthenticationProvider`。

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SysUserRepository sysUserRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(new CusotmUserDetailsService(sysUserRepository));
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(authProvider); //
    }
}
```

若我们自定义`AuthenticationProvider`，则完全使用自己的验证逻辑。

```java
public class CustomAuthenticationProvider implements AuthenticationProvider {

    SysUserRepository sysUserRepository;

    PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(SysUserRepository sysUserRepository, PasswordEncoder passwordEncoder) {
        this.sysUserRepository = sysUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String usernameFromRequest = authentication.getName(); //1
        String passwordFromRequest = authentication.getCredentials().toString(); //2
        Optional<SysUser> sysUserOptional = sysUserRepository.findByUsername(usernameFromRequest);
        SysUser sysUser = sysUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("Username not found")); //3
        if(passwordEncoder.matches(passwordFromRequest, sysUser.getPassword()) && //4
            sysUser.isAccountNonExpired() &&
            sysUser.isAccountNonLocked() &&
            sysUser.isCredentialsNonExpired() &&
            sysUser.isEnabled())
            return new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword(), sysUser.getAuthorities()); //5
        else
            throw new BadCredentialsException("Bad Credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) { //6
        return (UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication));
    }
}
```

1. 获取请求传递的用户名；
2. 获取请求传递的密码；
3. 检查用户是否存在于数据库中；
4. 比较传递密码和存储的编码密码是否一致，包含用户的有效性的比对；
5. 构建新的`Authentication`：`UsernamePasswordAuthenticationToken`对象；
6. 声明当前自定义`AuthenticationProvider`能处理`Authentication`类型为`UsernamePasswordAuthenticationToken`的认证。

使用这个`AuthenticationProvider`同样可通过`AuthenticationManager`来配置：

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SysUserRepository sysUserRepository;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new CustomAuthenticationProvider(sysUserRepository, passwordEncoder()));
    }
}
```

更简单的注册`AuthenticationProvider`的Bean的效果也是一致的：

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider(SysUserRepository sysUserRepository){
        return new CustomAuthenticationProvider(sysUserRepository, passwordEncoder());
    }
}
```

运行的结果和自定义`UserDetailsService`一致，普通情况下我们使用自定义`UserDetailsService`就可以了；只有涉及到复杂的认证逻辑时才需要自定义`AuthenticationProvider`。

### HTTP基础认证

`HttpSecurity`用来针对不同的HTTP请求进行Web安全配置。我们前面的认证都是通过表单登陆进行认证，我们可以通过`HttpSecurity`配置在请求时将账号信息放置于头部进行登录。在现代应用中，服务端和客户端（web、app等）分离，一般都是通过类似的方式来请求服务端的接口的。

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated() //1 
                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint()); //2
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint(){ //3
        BasicAuthenticationEntryPoint authenticationEntryPoint = new BasicAuthenticationEntryPoint();
        authenticationEntryPoint.setRealmName("wisely");
        return authenticationEntryPoint;
    }

    @Bean
    UserDetailsService userDetailsService(SysUserRepository sysUserRepository){
        return new CusotmUserDetailsService(sysUserRepository);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
```

1. 设置所有的请求都要认证后才能访问；
2. 设置登录方式为HTTP Basic，设置认证入口点为`BasicAuthenticationEntryPoint`；
3. `BasicAuthenticationEntryPoint`将认证信息放置于头部，当认证不通过返回状态值为`401`；

Postman支持Basic Auth：

![image-20240324210548964](SpringSecurity.assets/image-20240324210548964.png)

HTTP Basic实际上是在请求头的`Authorization`中添加值为`Basic 账号:密码的Base64编码`，`wyf:111111`的Base64编码为`d3lmOjExMTExMQ==`，那我们请求头为`Authorization: Basic d3lmOjExMTExMQ==`即可完成认证。
![image-20240324210601359](SpringSecurity.assets/image-20240324210601359.png)

### 获取用户信息

我们可以通过`SecurityContextHolder`中获取`SecurityContext`从而获得`Authentication`：

```java
SecurityContext context = SecurityContextHolder.getContext(); //获得SecurityContext
Authentication auth = context.getAuthentication(); //获得Authentication
Object principal = auth.getPrincipal(); //获取用户信息 
Object details = auth.getDetails(); //认证请求的更多信息
```

Spring Security给我们注册了`ArgumentResolvers`，我们可以直接通过`@CurrentSecurityContext`注解获得`SecurityContext`，`@CurrentSecurityContext`注解还支持表达式获得`Authentication`、`principal`和`details`。

我们更简单的通过使用`@AuthenticationPrincipal`注解获得用户信息。

```java
    @GetMapping("/user")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal SysUser sysUser, //1
                           @CurrentSecurityContext SecurityContext securityContext, //2
  @CurrentSecurityContext(expression = "authentication")  Authentication authentication, //3
      @CurrentSecurityContext(expression = "authentication.principal") Object principal,//4
         @CurrentSecurityContext(expression = "authentication.details") Object details){ //5
        Map<String, Object> map = new HashMap<>();
        map.put("sysUser", sysUser);
        map.put("authentication", authentication);
        map.put("principal", principal);
      	map.put("details", details);
        return map;
    }
```

1. `@AuthenticationPrincipal`注册系统用户可获得用户对象；
2. `@CurrentSecurityContext`可直接获得`SecurityContext`对象；
3. 使用表达式`expression = "authentication"`，可获得`Authentication`对象
4. 使用表达式`expression = "authentication.principal"`，可获得用户信息；
5. 使用表达式`expression = "authentication.details"`，可获得认证请求的额外信息；

我们用Postman访问http://localhost:8080/user，并使用`Basic Auth`认证。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618090324143.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dpc2VseW1hbg==,size_16,color_FFFFFF,t_70#pic_center)

那我们上一章“审计功能”获得用户的代码修改为：

```java
@Bean
AuditorAware<String> auditorProvider(){
   return () -> Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
}
```

### 密码编码

在生产中我们肯定会对密码进行编码，Spring Security给我们提供了`PassowrdEncoder`接口用来编码密码和匹配密码；在生产中建议使用工业标准的`Bcrypt`的实现`BCryptPasswordEncoder`。

```java
public interface PasswordEncoder {
   String encode(CharSequence rawPassword); //将明文密码进行编码
   boolean matches(CharSequence rawPassword, String encodedPassword); //匹配明文密码和编码密码
   default boolean upgradeEncoding(String encodedPassword) { //更新密码编码机制
      return false;
   }
}
```

前面我们在应用中已经注册了`BCryptPasswordEncoder`的Bean：

```java
@Bean
PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}
```

我们用代码检验一下：

```java
@Bean
CommandLineRunner passwordOperation(PasswordEncoder passwordEncoder){
   return args -> {
      String passwordPlain = "123456";
      String passwordEncoded = passwordEncoder.encode(passwordPlain);
      boolean isMatched = passwordEncoder.matches(passwordPlain, passwordEncoded);
      System.out.println("明文密码为：" + passwordPlain);
      System.out.println("编码密码为：" + passwordEncoded);
      System.out.println("密码是否匹配：" + isMatched);
   };
}
```

![image-20240324210644744](SpringSecurity.assets/image-20240324210644744.png)





# 深入理解SpringSecurity中的Authentication信息与登录流程和过滤器的配置：addFilterBefore

## Authentication

![img](SpringSecurity.assets/9c69f2cd1a11eb52120d5c8fa33c3350.png)

使用[SpringSecurity](https://so.csdn.net/so/search?q=SpringSecurity&spm=1001.2101.3001.7020)可以在任何地方注入Authentication进而获取到当前登录的用户信息，可谓十分强大。

在Authenticaiton的继承体系中，实现类UsernamePasswordAuthenticationToken 算是比较常见的一个了，在这个类中存在两个属性：principal和credentials，其实分别代表着用户和密码。【当然其他的属性存在于其父类中，如`authorities`和`details`。】

我们需要对这个对象有一个基本地认识，它保存了用户的基本信息。用户在登录的时候，进行了一系列的操作，将信息存与这个对象中，后续我们使用的时候，就可以轻松地获取这些信息了。

## 登录流程

### 一、与认证相关的UsernamePasswordAuthenticationFilter

通过Servlet中的Filter技术进行实现，通过一系列内置的或自定义的安全Filter，实现接口的认证与授权。

比如：`UsernamePasswordAuthenticationFilter`

```java
//尝试验证	
public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
		//获取用户名和密码
		String username = obtainUsername(request);
		String password = obtainPassword(request);
 
		if (username == null) {
			username = "";
		}
 
		if (password == null) {
			password = "";
		}
		username = username.trim();
		//构造UsernamePasswordAuthenticationToken对象
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);
 
		// 为details属性赋值
		setDetails(request, authRequest);
		// 调用authenticate方法进行校验
		return this.getAuthenticationManager().authenticate(authRequest);
	}
```

#### 获取用户名和密码

从request中提取参数，这也是SpringSecurity默认的表单登录需要通过key/value形式传递参数的原因。

```java
	@Nullable
	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}
	@Nullable
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}
```

#### 构造UsernamePasswordAuthenticationToken对象

传入获取到的用户名和密码，而用户名对应UPAT对象中的principal属性，而密码对应credentials属性。

```java
UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
    username, password);
 
//UsernamePasswordAuthenticationToken 的构造器
public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
    super(null);
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(false);
}
```

#### 为details属性赋值

```typescript
// Allow subclasses to set the "details" property 允许子类去设置这个属性
setDetails(request, authRequest);
 
protected void setDetails(HttpServletRequest request,
                          UsernamePasswordAuthenticationToken authRequest) {
    authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
}
 
//AbstractAuthenticationToken 是UsernamePasswordAuthenticationToken的父类
public void setDetails(Object details) {
    this.details = details;
}
```

details属性存在于父类之中，主要描述两个信息，一个是remoteAddress 和sessionId。

```kotlin
	public WebAuthenticationDetails(HttpServletRequest request) {
		this.remoteAddress = request.getRemoteAddr();
 
		HttpSession session = request.getSession(false);
		this.sessionId = (session != null) ? session.getId() : null;
	}
```

#### 调用authenticate方法进行校验

```kotlin
this.getAuthenticationManager().authenticate(authRe
```

### 二、ProviderManager的校验逻辑



# 笔记

## 快速入门

### 准备工作

**1. 创建工程，引入依赖**

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.0</version>
    </parent>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
```

**2. 编写主启动类**

```java
@SpringBootApplication
public class SecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
}
```

**3. 编写一个简单的接口测试访问**

```java

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
```

<img src="SpringSecurity.assets/image-20240324223238604.png" alt="image-20240324223238604" style="zoom: 67%;" />

### 引入SpringSecurity

**1. 引入依赖**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

**2. 引入依赖之后需要认证登录才能访问原来的接口** 

引入依赖后我们在尝试去访问之前的接口就会自动跳转到一个SpringSecurity的默认登陆页面，默认用户名是user,密码会输出在控制台。

<img src="SpringSecurity.assets/image-20240324223320951.png" style="zoom:67%;" />

默认用户名为：user
默认密码在控制台：
                 <img src="SpringSecurity.assets/3b381748b2b94be98f88a23764bd38b3.png" alt="在这里插入图片描述" style="zoom:67%;" />

退出，在路径后输入**logout**
                <img src="SpringSecurity.assets/image-20240324223347556.png" alt="image-20240324223347556" style="zoom:67%;" />

## 2. 认证

### 2.1 登陆校验流程

![image-20211215094003288](SpringSecurity.assets/2359319-20221124082454331-731489385.png)

### 2.2 SpringSecurity完整流程

 SpringSecurity的原理其实就是一个过滤器链，内部包含了提供各种功能的过滤器。这里我们可以看看入门案例中的过滤器。

![image-20211214144425527](SpringSecurity.assets/2359319-20221124082454055-1048019440.png)图中只展示了核心过滤器，其它的非核心过滤器并没有在图中展示。

**UsernamePasswordAuthenticationFilter**:负责处理我们在登陆页面填写了用户名密码后的登陆请求。入门案例的认证工作主要有它负责。

**ExceptionTranslatio  nFilter：**处理过滤器链中抛出的任何AccessDeniedException和AuthenticationException 。

**FilterSecurityInterceptor：**负责权限校验的过滤器。

#### 2.2.1 认证流程理论详解

![image-20240324224501754](SpringSecurity.assets/image-20240324224501754.png)

**Authentication**接口: 它的实现类，表示当前访问系统的用户，封装了用户相关信息。

**AuthenticationManager**接口：定义了认证Authentication的方法

**UserDetailsService**接口：加载用户特定数据的核心接口。里面定义了一个根据用户名查询用户信息的方法。

**UserDetails接口**：提供核心用户信息。通过UserDetailsService根据用户名获取处理的用户信息要封装成UserDetails对象返回。然后将这些信息封装到Authentication对象中。

#### 2.2.2 思路分析

![image-20240324224941182](SpringSecurity.assets/image-20240324224941182.png)

登录

 ①自定义登录接口 （即controller接口）然后配置放行（不需要认证就可访问此接口）

+ 调用ProviderManager的方法进行认证 如果认证通过生成jwt

+ 把用户信息存入redis中

 ②自定义UserDetailsService

+ 在这个实现类中去查询数据库

校验：

 ①定义Jwt认证过滤器

+ 获取token

+ 解析token获取其中的userid

+ 根据userId, 从redis中获取用户信息, 存入SecurityContextHolder

### 2.3 实现

#### 2.3.1 准备工作

准备依赖、工具类、pojo、dto等

#### 2.3.2 依赖

##### ①添加依赖

```xml
        <!--redis依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!--fastjson依赖-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.33</version>
        </dependency>
        <!--jwt依赖-->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.0</version>
        </dependency>
```

##### ② 添加Redis相关配置

```java
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.util.Assert;
import java.nio.charset.Charset;

/**
 * Redis使用FastJson序列化
 * 
 * @author sg
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T>
{

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

    static
    {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    public FastJsonRedisSerializer(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length <= 0)
        {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz);
    }


    protected JavaType getJavaType(Class<?> clazz)
    {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}
```

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
```

##### ③ 响应类

```java
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String msg;
    /**
     * 查询到的结果数据，
     */
    private T data;

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
```

##### ④工具类

###### JWT工具类

```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //有效期为
    public static final Long JWT_TTL = 60 * 60 *1000L;// 60 * 60 *1000  一个小时
    //设置秘钥明文
    public static final String JWT_KEY = "sangeng";

    public static String getUUID(){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }
    
    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("sg")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjYWM2ZDVhZi1mNjVlLTQ0MDAtYjcxMi0zYWEwOGIyOTIwYjQiLCJzdWIiOiJzZyIsImlzcyI6InNnIiwiaWF0IjoxNjM4MTA2NzEyLCJleHAiOjE2MzgxMTAzMTJ9.JVsSbkP94wuczb4QryQbAke3ysBDIL5ou8fWsbt_ebg";
        Claims claims = parseJWT(token);
        System.out.println(claims);
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
    
    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


}
```

###### RedisCache：自定义操作redis的工具类

```java
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key)
    {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext())
        {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 删除Hash中的数据
     * 
     * @param key
     * @param hkey
     */
    public void delCacheMapValue(final String key, final String hkey)
    {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hkey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern)
    {
        return redisTemplate.keys(pattern);
    }
}
```

将字符串渲染到客户端工具类

```java
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils
{
    /**
     * 将字符串渲染到客户端
     * 
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
```

##### ⑤实体类

```0
import java.io.Serializable;
import java.util.Date;


/**
 * 用户表(User)实体类
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    
    /**
    * 主键
    */
    private Long id;
    /**
    * 用户名
    */
    private String userName;
    /**
    * 昵称
    */
    private String nickName;
    /**
    * 密码
    */
    private String password;
    /**
    * 账号状态（0正常 1停用）
    */
    private String status;
    /**
    * 邮箱
    */
    private String email;
    /**
    * 手机号
    */
    private String phonenumber;
    /**
    * 用户性别（0男，1女，2未知）
    */
    private String sex;
    /**
    * 头像
    */
    private String avatar;
    /**
    * 用户类型（0管理员，1普通用户）
    */
    private String userType;
    /**
    * 创建人的用户id
    */
    private Long createBy;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 更新人
    */
    private Long updateBy;
    /**
    * 更新时间
    */
    private Date updateTime;
    /**
    * 删除标志（0代表未删除，1代表已删除）
    */
    private Integer delFlag;
}
```

#### 2.3.3 具体代码实现

##### 2.3.3.1 数据库校验用户

自定义的UserDetailsService可以从数据库中查询用户名和密码。

###### 数据库准备工作

 我们先创建一个用户表， 建表语句如下：

```sql
CREATE TABLE `sys_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
  `password` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `status` CHAR(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `email` VARCHAR(64) DEFAULT NULL COMMENT '邮箱',
  `phonenumber` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
  `sex` CHAR(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `avatar` VARCHAR(128) DEFAULT NULL COMMENT '头像',
  `user_type` CHAR(1) NOT NULL DEFAULT '1' COMMENT '用户类型（0管理员，1普通用户）',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` INT(11) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户表'
```

 引入MybatisPuls和mysql驱动的依赖

```xml
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.3</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
```

 配置数据库信息

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sg_security?characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

 定义Mapper接口

```java
public interface UserMapper extends BaseMapper<User> {
}
```

 修改User实体类

```java
类名上加@TableName(value = "sys_user") ,id字段上加 @TableId
```

 配置Mapper扫描

```java
@SpringBootApplication
@MapperScan("com.sangeng.mapper")
public class SimpleSecurityApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SimpleSecurityApplication.class);
        System.out.println(run);
    }
}
```

 添加junit依赖

```java
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
```

 测试MP是否能正常使用

```java
/**
 */
@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
}
```

###### 核心代码实现 :crossed_swords:

创建一个类实现UserDetailsService接口，重写其中的方法。增加用户名从数据库中查询用户信息

```java
/**
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
        
        //封装成UserDetails对象返回 
        return new LoginUser(user);
    }
}
```

因为UserDetailsService方法的返回值是UserDetails类型（是接口），所以需要我们自定义一个类，实现该接口，把用户信息封装在其中。

```java
/**
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
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

注意：如果要测试，需要往用户表中写入用户数据，并且如果你想让用户的密码是明文存储，需要在密码前加{noop}。例如

![image-20211216123945882](SpringSecurity.assets/2359319-20221124082453169-2046738345.png)

这样登陆的时候就可以用sg作为用户名，1234作为密码来登陆了。

##### 2.3.3.2 密码加密存储

 实际项目中我们不会把密码明文存储在数据库中。

 默认使用的PasswordEncoder要求数据库中的密码格式为：{id}password 。它会根据id去判断密码的加密方式。但是我们一般不会采用这种方式。所以就需要替换PasswordEncoder。

 我们一般使用SpringSecurity为我们提供的BCryptPasswordEncoder。

 我们只需要使用把BCryptPasswordEncoder对象注入Spring容器中，SpringSecurity就会使用该PasswordEncoder来进行密码校验。

 我们可以定义一个SpringSecurity的配置类，SpringSecurity要求这个配置类要继承WebSecurityConfigurerAdapter。

```java
/**
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
```

##### 2.3.3.3 登陆接口:crossed_swords:

 接下我们需要自定义登陆接口，然后让SpringSecurity对这个接口放行,让所有用户访问这个接口的时候不用登录也能访问。

 在此登录接口中我们通过**AuthenticationManager的authenticate()方法来进行用户认证**,所以需要在SecurityConfig中配置把AuthenticationManager注入容器。

 认证成功的话要生成一个jwt，放入响应中返回前端页面。并且为了让用户下回请求时能通过jwt识别出具体的是哪个用户，我们需要把用户信息存入redis，可以把用户id作为key。

```java
@RestController
public class LoginController {

    @Autowired
    private LoginServcie loginServcie;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginServcie.login(user);
    }
}
```

SecurityConfig配置类

```java
/**
 * 
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
    * 配置过滤规则
    */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/user/login").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

 登录接口实现类LoginServiceImpl

> 在此类中我们会将用户名和密码放入AuthenticationManager类的authenticate()方法，然后此方法会调用我们之前写的UserDetailsService的实现类中**UserDetailsServiceImpl中的方法进行校验**（进行用户认证）

```java
@Service
public class LoginServiceImpl implements LoginServcie {

    @Autowired
    private AuthenticationManager authenticationManager; // 此对象是前面
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //AuthenticationManager 用authenticate()方法进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()); // 将用户名和密码放入，然后此方法会调用我们之前写的UserDetailsService的实现类中UserDetailsServiceImpl中的方法进行校验（PS： 在后面JwtAuthenticationTokenFilter类中使用此UsernamePasswordAuthenticationToken的时候，构造的入参也可以放（用户LoginUser）类型
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();  // 获取查询到的数据
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return new ResponseResult(200,"登陆成功",map);
    }
}
```

##### 2.3.3.4 认证过滤器:crossed_swords:

经过上面的登录之后，现在前端已经登录成功，拿到了返回的token，现在前端的请求则会带着token来访问其他接口，那么现在我们需要**自定义一个过滤器**：

 这个过滤器会去获取请求头中的token，对token进行解析取出其中的userid。

 然后使用userid去redis中获取对应的LoginUser对象。

 然后封装Authentication对象存入SecurityContextHolder

> 此处解释为什么不去实现Filter接口，因为在某些情况下会过滤两次，执行两次Filter里面的方法，所以我们选择继承SpringSecurity中的OncePerRequestFilter
>
> 具体代码如下：

```java
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
// 不去实现Filter接口，因为在某些情况下会过滤两次，执行两次Filter里面的方法，所以选择继承SpringSecurity中的OncePerRequestFilter
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return; // 此处加上return好处是后面结果返回的时候就不会再走一遍此过滤器的方法了
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        //存入SecurityContextHolder，以供后面的过滤器使用
        //TODO 后面会补充：获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
```

> PS:
>
> 注意点：
>
> - filterChain.doFilter(request, response);
>   return; // 此处加上return好处是后面结果返回的时候就不会再走一遍此过滤器的方法了
>
> - //从redis中获取用户信息
>
> - //存入SecurityContextHolder，以供后面的过滤器使用
>
>   - 在此方法中，我们必须用3个参数的构造方法：
>
>     ```java
>      new UsernamePasswordAuthenticationToken(loginUser,null,null);
>     // 第一个参数是我们主体，将loginUser放入即可
>     // 第三个参数是主体拥有的权限集合，但是目前我们还没有写，先写null即可，先标记TODO后面再补充
>     ```
>
>   - 点进去看源码：因为我们需要将 super.setAuthenticated(true);这个是否已经认证标志设置为true，如下图示：
>
>     ![1669210876643](SpringSecurity.assets/2359319-20221124082452853-1403720786.png)

仅仅通过@Component注解放入容器还不够，此时我们还需要对SecurityConfig类进行更新： //把token校验过滤器添加到过滤器链中，并**指定在过滤器链中的位置顺序**

```java
/**
 * 
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // 注入上边我们放入容器中的JwtAuthenticationTokenFilter，在下面代码中添加到过滤器链中
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/user/login").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        //把token校验过滤器添加到过滤器链中
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

##### 2.3.3.5 退出登陆

 我们只需要定义一个登陆接口，然后获取SecurityContextHolder中的认证信息，删除redis中对应的数据即可。

```java
/**
 * 
 */
@Service
public class LoginServiceImpl implements LoginServcie {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return new ResponseResult(200,"登陆成功",map);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+userid);
        return new ResponseResult(200,"退出成功");
    }
}
```

### 2.4 认证配置类WebSecurityConfigurerAdapter详解

```scala
class SecurityConfig extends WebSecurityConfigurerAdapter
```

源码中的注释也建议我们不要使用父类WebSecurityConfigurerAdapter中的配置，建议我们重写覆盖此方法（避免使用当中的默认配置），使用我们自己的配置：源码如下图：

![1669211872495](SpringSecurity.assets/2359319-20221124082452521-945478850.png)

[22.认证配置详解_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1mm4y1X7Hc?p=23&spm_id_from=pageDriver&vd_source=a3ca5632ce12ee8045822c508dc81551)

![1669212363650](SpringSecurity.assets/2359319-20221124082452122-1045889974.png)

## 3. 授权

### 3.0 权限系统的作用

 例如一个学校图书馆的管理系统，如果是普通学生登录就能看到借书还书相关的功能，不可能让他看到并且去使用添加书籍信息，删除书籍信息等功能。但是如果是一个图书馆管理员的账号登录了，应该就能看到并使用添加书籍信息，删除书籍信息等功能。

 总结起来就是**不同的用户可以使用不同的功能**。这就是权限系统要去实现的效果。

不能只依赖前端去判断用户的权限来选择显示哪些菜单哪些按钮。因为如果只是这样，如果有人知道了对应功能的接口地址就可以不通过前端，直接去发送请求来实现相关功能操作。

 所以我们还需要在后台进行用户权限的判断，判断当前用户是否有相应的权限，必须具有所需权限才能进行相应的操作。

### 3.1 授权基本流程

 在SpringSecurity中，会使用默认的FilterSecurityInterceptor来进行权限校验。在FilterSecurityInterceptor中会从SecurityContextHolder获取其中的Authentication，然后获取其中的权限信息。当前用户是否拥有访问当前资源所需的权限。

 所以我们在项目中只需要把当前登录用户的权限信息也存入Authentication即可。

 然后在代码中设置我们的资源所需要的权限即可（可使用权限注解）。

> 其实我们之前留下的两个TODO只要完善了即可
>
> ![1669212613874](SpringSecurity.assets/2359319-20221124082451746-2008698453.png)
>
> ![1669212634929](SpringSecurity.assets/2359319-20221124082451273-927230055.png)

### 3.2 授权实现 :crossed_swords:

#### 3.2.1 限制访问资源所需权限

 SpringSecurity为我们提供了基于注解的权限控制方案，这也是我们项目中主要采用的方式。我们可以使用注解去指定访问对应的资源所需的权限。（还有一种方式就是我们在SecurityConfig类中的configure()方法中配置的.somthing()等，这种方式不怎么常用，主要就是用来配置放行一些静态资源，比如css、js、html文件等）

在Security配置类上开启相关配置。

```java
@EnableGlobalMethodSecurity(prePostEnabled = true)
```

 然后就可以使用对应的注解。**@PreAuthorize **

```java
@RestController
public class HelloController {

    @RequestMapping("/hello")
    @PreAuthorize("hasAuthority('test')")  // 需要的权限
    public String hello(){
        return "hello";
    }
}
```

#### 3.2.2 封装权限信息

 我们前面在写UserDetailsServiceImpl的时候说过，在查询出用户后还要获取对应的权限信息，封装到UserDetails中返回。

 我们先直接把权限信息写死封装到UserDetails中进行测试。

 我们之前定义了UserDetails的实现类LoginUser，想要让其能封装权限信息就要对其进行修改。

```java
package com.sangeng.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private User user;
        
    //存储权限信息
    private List<String> permissions;
    
    
    public LoginUser(User user,List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }


    //存储SpringSecurity调用getAuthorities()方法获取的权限信息的集合
    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities;

    @Override
    public  Collection<? extends GrantedAuthority> getAuthorities() {
        
        // 优化为只需要第一次获取的时候进行遍历，后面再用就会从authorities这个成员变量中获取了，不会再进行Stream循环遍历了
        if(authorities!=null){
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        authorities = permissions.stream().
                map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
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

> 主要是这个getAuthorities()方法会被框架调用拿到用户拥有的权限，所以我们需要**重写此方法**
>
> ```java
> // 因为SimpleGrantedAuthority是Spring中的类，并没有实现序列化接口，所以在存入redis的过程中序列化会报错，所以我们加上注解就不会把此成员变量存入redis （再者permissions变量也存着权限信息，重复了）
>     @JSONField(serialize = false)
>     private List<GrantedAuthority> authorities;
> ```







 LoginUser修改完后我们就可以在UserDetailsServiceImpl中去把权限信息封装到LoginUser中了。我们写死权限进行测试，后面我们再从数据库中查询权限信息。

```java
package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.sangeng.domain.LoginUser;
import com.sangeng.domain.User;
import com.sangeng.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
        List<String> list = new ArrayList<>(Arrays.asList("test"));
        return new LoginUser(user,list);
    }
}
```

#### 3.2.3 从数据库查询权限信息

##### 3.2.3.1 RBAC权限模型

 RBAC权限模型（Role-Based Access Control）即：基于角色的权限控制。这是目前最常被开发者使用也是相对易用、通用权限模型。

 ![image-20211222110249727](SpringSecurity.assets/2359319-20221124082450772-928989035.png)

##### 3.2.3.2 准备工作

```sql
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sg_security` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `sg_security`;

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '菜单名',
  `path` varchar(200) DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int(11) DEFAULT '0' COMMENT '是否删除（0未删除 1已删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `role_key` varchar(100) DEFAULT NULL COMMENT '角色权限字符串',
  `status` char(1) DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
  `del_flag` int(1) DEFAULT '0' COMMENT 'del_flag',
  `create_by` bigint(200) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(200) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` bigint(200) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `menu_id` bigint(200) NOT NULL DEFAULT '0' COMMENT '菜单id',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
  `password` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `phonenumber` varchar(32) DEFAULT NULL COMMENT '手机号',
  `sex` char(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `avatar` varchar(128) DEFAULT NULL COMMENT '头像',
  `user_type` char(1) NOT NULL DEFAULT '1' COMMENT '用户类型（0管理员，1普通用户）',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(11) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `user_id` bigint(200) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `role_id` bigint(200) NOT NULL DEFAULT '0' COMMENT '角色id',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
SELECT 
	DISTINCT m.`perms`
FROM
	sys_user_role ur
	LEFT JOIN `sys_role` r ON ur.`role_id` = r.`id`
	LEFT JOIN `sys_role_menu` rm ON ur.`role_id` = rm.`role_id`
	LEFT JOIN `sys_menu` m ON m.`id` = rm.`menu_id`
WHERE
	user_id = 2
	AND r.`status` = 0
	AND m.`status` = 0
package com.sangeng.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单表(Menu)实体类
 *
 * @author makejava
 * @since 2021-11-24 15:30:08
 */
@TableName(value="sys_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu implements Serializable {
    private static final long serialVersionUID = -54979041104113736L;
    
        @TableId
    private Long id;
    /**
    * 菜单名
    */
    private String menuName;
    /**
    * 路由地址
    */
    private String path;
    /**
    * 组件路径
    */
    private String component;
    /**
    * 菜单状态（0显示 1隐藏）
    */
    private String visible;
    /**
    * 菜单状态（0正常 1停用）
    */
    private String status;
    /**
    * 权限标识
    */
    private String perms;
    /**
    * 菜单图标
    */
    private String icon;
    
    private Long createBy;
    
    private Date createTime;
    
    private Long updateBy;
    
    private Date updateTime;
    /**
    * 是否删除（0未删除 1已删除）
    */
    private Integer delFlag;
    /**
    * 备注
    */
    private String remark;
}
```

##### 3.2.3.3 代码实现

 我们只需要根据用户id去查询到其所对应的权限信息即可。

 所以我们可以先定义个mapper，其中提供一个方法可以根据userid查询权限信息。

```java
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.Menu;

import java.util.List;

/**
 * 
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long id);
}
```

 尤其是自定义方法，所以需要创建对应的mapper文件，定义对应的sql语句

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.sangeng.mapper.MenuMapper">


    <select id="selectPermsByUserId" resultType="java.lang.String">
        SELECT
            DISTINCT m.`perms`
        FROM
            sys_user_role ur
            LEFT JOIN `sys_role` r ON ur.`role_id` = r.`id`
            LEFT JOIN `sys_role_menu` rm ON ur.`role_id` = rm.`role_id`
            LEFT JOIN `sys_menu` m ON m.`id` = rm.`menu_id`
        WHERE
            user_id = #{userid}
            AND r.`status` = 0
            AND m.`status` = 0
    </select>
</mapper>
```

 在application.yml中配置mapperXML文件的位置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sg_security?characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  redis:
    host: localhost
    port: 6379
mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml 
```

 然后我们可以在UserDetailsServiceImpl中去调用该mapper的方法查询权限信息封装到LoginUser对象中即可。

```java
/**
 * 
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        List<String> permissionKeyList =  menuMapper.selectPermsByUserId(user.getId());
//        //测试写法
//        List<String> list = new ArrayList<>(Arrays.asList("test"));
        List<String> list = menuMapper.selectPermsByUserId(user.getId());
        
        //把数据封装成UserDetails返回
        return new LoginUser(user,permissionKeyList);
    }
}
```

## 4. 自定义失败处理:crossed_flags:

 我们还希望在认证失败或者是授权失败的情况下也能和我们的自定义的controller接口一样返回相同结构的json，这样可以让前端能对响应进行统一的处理。要实现这个功能我们需要知道SpringSecurity的异常处理机制。

 在SpringSecurity中，如果我们在认证或者授权的过程中出现了异常会被ExceptionTranslationFilter捕获到。在ExceptionTranslationFilter中会去判断是认证失败还是授权失败出现的异常。

+ 如果是认证过程中出现的异常会被封装成AuthenticationException然后调用**AuthenticationEntryPoint**对象的方法去进行异常处理。

+ 如果是授权过程中出现的异常会被封装成AccessDeniedException然后调用**AccessDeniedHandler**对象的方法去进行异常处理。(当全局异常处理和 @PreAuthorize 注解结合使用时，抛出 AccessDeniedException 异常，不会被 accessDeniedHandler 捕获，而是会被全局异常捕获。)

需要自定义异常处理，只需要自定义AuthenticationEntryPoint和AccessDeniedHandler然后**配置给SpringSecurity**即可。

①自定义实现类

认证过程中出现的异常

```java
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "认证失败请重新登录");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
```

授权过程中出现的异常

```java
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(), "权限不足");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);

    }
}
```

②配置给SpringSecurity

 在SpringSecurity配置类中先注入对应的处理器

```java
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
```

 然后在下面我们可以使用HttpSecurity对象的方法去配置。

```java
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).
                accessDeniedHandler(accessDeniedHandler);
```

③ 全局异常配置

```java
	/**
	 * 处理AccessDeineHandler无权限异常
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public void accessDeniedException(AccessDeniedException e) throws AccessDeniedException {
		// 将 Spring Security 异常继续抛出，以便交给自定义处理器处理
		throw e;
	}
```

## 5. 跨域

 浏览器出于安全的考虑，使用 XMLHttpRequest这个异步请求对象发起 HTTP请求时必须遵守同源策略，否则就是跨域的HTTP请求，默认情况下是被禁止的。 同源策略要求源相同才能正常进行通信，即协议、域名、端口号都完全一致。

 前后端分离项目，前端项目和后端项目一般都不是同源的，所以肯定会存在跨域请求的问题。

 所以我们就要处理一下，让前端能进行跨域请求。

①先对SpringBoot配置，运行跨域请求

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
      // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}
```

②开启SpringSecurity的跨域访问

由于我们的资源都会收到SpringSecurity的保护，所以想要跨域访问还要让SpringSecurity运行跨域访问。

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/user/login").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        //添加过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器
        http.exceptionHandling()
                //配置认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        //允许跨域
        http.cors();
    }
```

> //允许跨域
> http.cors();

## 6. 遗留小问题

### 其它权限校验方法

 我们前面都是使用@PreAuthorize注解，然后在在其中使用的是hasAuthority方法进行校验。SpringSecurity还为我们提供了其它方法例如：hasAnyAuthority，hasRole，hasAnyRole等。

 这里我们先不急着去介绍这些方法，我们先去理解hasAuthority的原理，然后再去学习其他方法你就更容易理解，而不是死记硬背区别。并且我们也可以选择定义校验方法，实现我们自己的校验逻辑。

 hasAuthority方法实际是执行到了SecurityExpressionRoot的hasAuthority，大家只要断点调试既可知道它内部的校验原理。

 它内部其实是调用authentication的getAuthorities方法获取用户的权限列表。然后判断我们存入的方法参数数据在权限列表中。

 hasAnyAuthority方法可以传入多个权限，只有用户有其中任意一个权限都可以访问对应资源。

```java
    @PreAuthorize("hasAnyAuthority('admin','test','system:dept:list')")
    public String hello(){
        return "hello";
    }
```

 hasRole要求有对应的角色才可以访问，但是它内部会把我们传入的参数拼接上 **ROLE_** 后再去比较。所以这种情况下要用用户对应的权限也要有 **ROLE_** 这个前缀才可以。

```java
    @PreAuthorize("hasRole('system:dept:list')")
    public String hello(){
        return "hello";
    }
```

> hasRole有些特殊，我们需要在方法注解上的值里填写有 **ROLE_** 这个前缀才可以，下面的hasAnyRole 也是一样

 hasAnyRole 有任意的角色就可以访问。它内部也会把我们传入的参数拼接上 **ROLE_** 后再去比较。所以这种情况下要用用户对应的权限也要有 **ROLE_** 这个前缀才可以。

```java
    @PreAuthorize("hasAnyRole('admin','system:dept:list')")
    public String hello(){
        return "hello";
    }
```

### 自定义权限校验方法

 我们也可以定义自己的权限校验方法，在@PreAuthorize注解中使用我们的方法。

```java
@Component("ex")
public class SGExpressionRoot {

    public boolean hasAuthority(String authority){
        //获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        //判断用户权限集合中是否存在authority
        return permissions.contains(authority);
    }
}
```

 在SPEL表达式中使用 @ex相当于获取容器中bean的名字未ex的对象。然后再调用这个对象的hasAuthority方法

```java
    @RequestMapping("/hello")
    @PreAuthorize("@ex.hasAuthority('system:dept:list')")
    public String hello(){
        return "hello";
    }
```

![1669216419075](SpringSecurity.assets/2359319-20221124082449427-729215541.png)

### 基于配置的权限控制

 我们也可以在配置类中使用使用配置的方式对资源进行权限控制。

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/user/login").anonymous()
                .antMatchers("/testCors").hasAuthority("system:dept:list222")
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        //添加过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器
        http.exceptionHandling()
                //配置认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        //允许跨域
        http.cors();
    }
```

### CSRF

 CSRF是指跨站请求伪造（Cross-site request forgery），是web常见的攻击之一。

 https://blog.csdn.net/freeking101/article/details/86537087

 SpringSecurity去防止CSRF攻击的方式就是通过csrf_token。后端会生成一个csrf_token，前端发起请求的时候需要携带这个csrf_token,后端会有过滤器进行校验，如果没有携带或者是伪造的就不允许访问。

 我们可以发现CSRF攻击依靠的是cookie中所携带的认证信息。但是在前后端分离的项目中我们的认证信息其实是token，而token并不是存储中cookie中，并且需要前端代码去把token设置到请求头中才可以，所以CSRF攻击也就不用担心了。

### 认证成功处理器

 实际上在UsernamePasswordAuthenticationFilter进行登录认证的时候，如果登录成功了是会调用AuthenticationSuccessHandler的方法进行认证成功后的处理的。AuthenticationSuccessHandler就是登录成功处理器。

 我们也可以自己去自定义成功处理器进行成功后的相应处理。

```java
@Component
public class SGSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("认证成功了");
    }
}
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().successHandler(successHandler);

        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

### 认证失败处理器

 实际上在UsernamePasswordAuthenticationFilter进行登录认证的时候，如果认证失败了是会调用AuthenticationFailureHandler的方法进行认证失败后的处理的。AuthenticationFailureHandler就是登录失败处理器。

 我们也可以自己去自定义失败处理器进行失败后的相应处理。

```java
@Component
public class SGFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("认证失败了");
    }
}
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
//                配置认证成功处理器
                .successHandler(successHandler)
//                配置认证失败处理器
                .failureHandler(failureHandler);

        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

### 登出成功处理器

```java
@Component
public class SGLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("注销成功");
    }
}
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
//                配置认证成功处理器
                .successHandler(successHandler)
//                配置认证失败处理器
                .failureHandler(failureHandler);

        http.logout()
                //配置注销成功处理器
                .logoutSuccessHandler(logoutSuccessHandler);

        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

### 其他认证方案畅想



## 7. 自定义认证提供者











![image-20240325175446056](SpringSecurity.assets/image-20240325175446056.png)





# 认证

![image-20240325225256375](SpringSecurity.assets/image-20240325225256375.png)

# 授权

![image-20240325223843507](SpringSecurity.assets/image-20240325223843507.png)



# 用户上下文

### ServerSecurityContext

```java
package com.lfj.blog.common.security;

import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.common.security.details.CustomUserDetails;
import com.lfj.blog.common.security.token.AuthenticationToken;
import com.lfj.blog.exception.ApiException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 服务安全上下文, 任务上下文
 **/
public class ServerSecurityContext {
	/**
	 * 获取当前用户相信信息
	 *
	 * @return
	 */
	public static CustomUserDetails getUserDetail(boolean throwEx) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if (authentication == null && throwEx) {
			throw new ApiException(ResponseCodeEnum.CREDENTIALS_INVALID.getCode(), ResponseCodeEnum.CREDENTIALS_INVALID.getMessage());
		}
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal == null && throwEx) {
			throw new ApiException(ResponseCodeEnum.CREDENTIALS_INVALID.getCode(), ResponseCodeEnum.CREDENTIALS_INVALID.getMessage());
		}
		String noneUser = "anonymousUser";
		if (noneUser.equals(principal)) {
			return null;
		}
		return (CustomUserDetails) principal;
	}

	/**
	 * 获取认证信息
	 *
	 * @return org.springframework.security.core.Authentication
	 */
	public static Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		return context.getAuthentication();
	}

	/**
	 * 获取AuthenticationToken
	 *
	 * @return con.lgj.blog.common.security.AuthenticationToken
	 */
	public static AuthenticationToken getAuthenticationToken(boolean throwEx) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if (authentication == null && throwEx) {
			throw new ApiException(ResponseCodeEnum.CREDENTIALS_INVALID.getCode(), ResponseCodeEnum.CREDENTIALS_INVALID.getMessage());
		}
		if (authentication == null) {
			return null;
		}
		Object details = authentication.getDetails();
		if (details == null && throwEx) {
			throw new ApiException(ResponseCodeEnum.CREDENTIALS_INVALID.getCode(), ResponseCodeEnum.CREDENTIALS_INVALID.getMessage());
		}
		return (AuthenticationToken) details;
	}

}

```

### 使用

```java
// 请求携带token, 可以用任务上下文获得authenticationToken
		AuthenticationToken authenticationToken = ServerSecurityContext.getAuthenticationToken(true);

// 获取任务上下文, 得到当前用户信息
CustomUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
article.setUserId(userDetail.getId());
```



```java
	/**
	 * 发送邮箱验证链接
	 *
	 * @param email
	 * @return void
	 */
	@Override
	public void validateEmail(String email) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(User::getEmail, email);
		long count = count(queryWrapper);
		if (count != 0) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "邮箱已被使用,请换个绑定");
		}
		// 请求携带token, 可以用任务上下文获得authenticationToken
		AuthenticationToken authenticationToken = ServerSecurityContext.getAuthenticationToken(true);
		Map<String, Object> params = new HashMap<>(1);
		// 生成模板参数
		RandomValueStringGenerator generator = new RandomValueStringGenerator();
		String code = generator.generate();
		String accessToken = authenticationToken.getAccessToken();
		String checkUrl = prefix + "?code=" + code;
		params.put("checkUrl", checkUrl);
		// 服务端生成Redis缓存
		String key = REDIS_MAIL_CODE_PREFIX + code;
		Map<String, String> map = new HashMap<>(2);
		map.put("access_token", accessToken);
		map.put("email", email);
		stringRedisTemplate.opsForHash().putAll(key, map);
		stringRedisTemplate.expire(key, 2L, TimeUnit.HOURS);
		// 发送邮箱
		emailService.asyncSendHtmlMail(email, "邮箱验证", "email", params);
	}
```

```java

	/**
	 * 绑定邮箱
	 *
	 * @param code
	 * @return void
	 */
	@Override
	public void bindEmail(String code) {
		Map<Object, Object> resultMap = stringRedisTemplate.opsForHash().
				entries(REDIS_MAIL_CODE_PREFIX + code);
		if (resultMap.isEmpty()) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "code无效或code已过期");
		}
		String accessToken = (String) resultMap.get("access_token");
		String email = (String) resultMap.get("email");
		stringRedisTemplate.delete(REDIS_MAIL_CODE_PREFIX + code);
		// 请求没有直接携带token, 不能用任务上下文
		AuthenticationToken authToken = tokenStore.readByAccessToken(accessToken);
		if (authToken == null) {
			throw new ApiException(ResponseCodeEnum.INVALID_REQUEST.getCode(), "code无效或code已过期");
		}
		// 从Redis中得到保存的用户信息数据
		CustomUserDetails principal = authToken.getPrincipal(); // 得到用户数据, 自定义AuthenticationToken中Principal属性保存着对应的用户数据
		User user = new User();
		Integer userId = principal.getId();
		user.setId(userId);
		user.setEmail(email);  // 更新邮箱
		// 数据库数据更新
		updateById(user);
		// 清空用户缓存
		tokenStore.clearUserCacheById(userId);
	}
```





```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
```



# 从 Spring Security 5 迁移到 Spring Security 6/Spring Boot 3

[Spring Security6版本变化内容_spring security 新版本更新内容-CSDN博客](https://blog.csdn.net/qq_34491508/article/details/132121362)

[从 Spring Security 5 迁移到 Spring Security 6/Spring Boot 3 - spring 中文网 (springdoc.cn)](https://springdoc.cn/spring-security-migrate-5-to-6/)

[Spring Security 6 配置方法，废弃 WebSecurityConfigurerAdapter_websecurityconfigureradapter作废-CSDN博客](https://blog.csdn.net/xieshaohu/article/details/129780439?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2~default~CTRLIST~Rate-1-129780439-blog-130098599.235^v43^pc_blog_bottom_relevance_base1&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2~default~CTRLIST~Rate-1-129780439-blog-130098599.235^v43^pc_blog_bottom_relevance_base1&utm_relevant_index=1)

官网: [Configuration Migrations :: Spring Security](https://docs.spring.io/spring-security/reference/5.8/migration/servlet/config.html)

## 配置HttpSecurity

### 旧版

```java
@Log4j2
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启授权
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
//	配置HttpSecurity
	protected void configure(HttpSecurity http) throws Exception {
		List<String> ignorePropertiesList = ignoreProperties.getList();
		int size = ignorePropertiesList.size();

		http.cors()   //允许跨域
				.and()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(ignorePropertiesList.toArray(new String[size])).permitAll()  //配置的url 不需要授权
				.anyRequest().authenticated() // 剩下的任意请求都需要认证
				.and()
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"));

		//添加认证过滤器(自定义)
		// 第一个参数是要添加的过滤器对象，第二个参数是指定在哪一个现有过滤器之前添加这个自定义过滤器。
		//实现在进行用户名密码认证之前对请求进行自定义的认证逻辑处理。
		http.addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		//配置异常处理器(自定义未认证和未授权异常)
		http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPointHandler).
				accessDeniedHandler(customAccessDeniedHandler);
	}
    
}
```

### 新版

```java
@Log4j2
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 开启授权
public class WebSecurityConfig {

    
//	配置HttpSecurity
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		List<String> ignorePropertiesList = ignoreProperties.getList();
		int size = ignorePropertiesList.size();

		http.httpBasic(AbstractHttpConfigurer::disable)          // 禁用basic明文验证
				.cors(Customizer.withDefaults())  //开启跨域
				.csrf(AbstractHttpConfigurer::disable) //前后端分离架构不需要csrf保护
				.formLogin(AbstractHttpConfigurer::disable)   // 禁用默认登录页
				.authorizeHttpRequests(request ->
						request.requestMatchers(ignorePropertiesList.toArray(new String[size])).permitAll()  //配置的url 不需要授权
								.anyRequest().authenticated()   // 剩下的任意请求都需要认证
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 前后端分离是无状态的，不需要session了，直接禁用。
				.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))); // 退出

		// 添加认证过滤器(自定义)
		// 第一个参数是要添加的过滤器对象，第二个参数是指定在哪一个现有过滤器之前添加这个自定义过滤器。
		// 实现在进行用户名密码认证之前对请求进行自定义的认证逻辑处理。
		http.addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		// 配置异常处理器(自定义未认证和未授权异常)，如果不设置，默认使用Http403ForbiddenEntryPoint
		http.exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(customAuthenticationEntryPointHandler).
				accessDeniedHandler(customAccessDeniedHandler));

		return http.build();
	}
}
```

参考:

```java
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用basic明文验证
                .httpBasic().disable()
                // 前后端分离架构不需要csrf保护
                .csrf().disable()
                // 禁用默认登录页
                .formLogin().disable()
                // 禁用默认登出页
                .logout().disable()
                // 设置异常的EntryPoint，如果不设置，默认使用Http403ForbiddenEntryPoint
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(invalidAuthenticationEntryPoint))
                // 前后端分离是无状态的，不需要session了，直接禁用。
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // 允许所有OPTIONS请求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 允许直接访问授权登录接口
                        .requestMatchers(HttpMethod.POST, "/web/authenticate").permitAll()
                        // 允许 SpringMVC 的默认错误地址匿名访问
                        .requestMatchers("/error").permitAll()
                        // 其他所有接口必须有Authority信息，Authority在登录成功后的UserDetailsImpl对象中默认设置“ROLE_USER”
                        //.requestMatchers("/**").hasAnyAuthority("ROLE_USER")
                        // 允许任意请求被已登录用户访问，不检查Authority
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                // 加我们自定义的过滤器，替代UsernamePasswordAuthenticationFilter
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
 
        return http.build();
    }
```



## 配置WebSecurity

### 旧版

```java
//	配置WebSecurity
	@Override
	public void configure(WebSecurity web) {
		web
				.ignoring()
				.antMatchers(
						HttpMethod.GET,
						"/*.html",
						"/favicon.ico",
						"/**/*.html",
						"/**/*.css",
						"/**/*.js"
				);
	}
```

### 新版

```java
	//	配置WebSecurity
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web
				.ignoring()
				.requestMatchers(
						HttpMethod.GET,
						"/*.html",
						"/favicon.ico",
						"/**/*.html",
						"/**/*.css",
						"/**/*.js"
				);
	}

```

## 配置AuthenticationManager认证

以前可以通过重写父类的方法来获取这个 Bean，类似下面这样

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

6.x中只能自己创建这个 Bean 了

```java
@Configuration
public class SecurityConfig {
 
    @Autowired
    UserService userService;
 
    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        ProviderManager pm = new ProviderManager(daoAuthenticationProvider);
        return pm;
    }
}
```

当然，也可以从 HttpSecurity 中提取出来 AuthenticationManager，如下：

```java
@Configuration
@EnableWebSecurity
public class MySecurityConfig {
   
    AuthenticationManager authenticationManager;
    @Resource
    UserDetailsServiceImpl userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
 
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        authenticationManager = authenticationManagerBuilder.build();
        
        //不需要认证的请求
        return http.authorizeHttpRequests(conf -> conf.requestMatchers("/login", "/info").permitAll().anyRequest().authenticated())
                //通过new JwtFilter()的方式，而不是bean组件注入的方式
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(withDefaults())
                .cors(withDefaults()).build();
    }
 
}
```



 [AuthenticationManager](https://so.csdn.net/so/search?q=AuthenticationManager&spm=1001.2101.3001.7020)的获取





### 旧版

```java
	/**
	 * 配置认证管理器
	 *
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * 这里是对认证管理器的添加配置
	 *
	 * @param auth
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(provider())//自定义手机验证码认证提供者
				.authenticationProvider(provider2())//自定义主键查询用户认证提供者(用于第三方登录)
				.userDetailsService(userDetailsService) // 确认用户数据的来源
				.passwordEncoder(passwordEncoder());
	}

```



### 新版

```java
	/**
	 * 这里是对认证管理器的添加配置
	 * 新版AuthenticationManager认证管理器默认全局）
	 * 调用UserDetailsServiceImp中loadUserByUsername获得UserDetail信息，
	 * 在AbstractUserDetailsAuthenticationProvider里执行用户状态检查
	 *
	 * @return
	 */
	@Bean
	AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService); // 确认用户数据的来源
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());     //加密
		ProviderManager pm = new ProviderManager(daoAuthenticationProvider);
		return pm;
	}

	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(provider())//自定义手机验证码认证提供者
				.authenticationProvider(provider2());//自定义主键查询用户认证提供者(用于第三方登录)
    	// ...........
    
    }

```

