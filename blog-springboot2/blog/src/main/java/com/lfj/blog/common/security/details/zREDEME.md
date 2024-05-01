# 数据库校验用户

作用: 就是通过用户名从数据库中查到用户数据保存到UserDetails实现类中

SpringSecurity有用于认证的用户类, 需要继承数据库用户实体和实现 UserDetails接口

SpringSecurity需要确认用户数据的来源, 需要实现UserDetailsService接口

注:

+ **UserDetails接口** : 用来定义用户表的结构的
+ **UserDetailsService接口**: 确认用户数据的来源, 就是实现loadUserByUsername()

