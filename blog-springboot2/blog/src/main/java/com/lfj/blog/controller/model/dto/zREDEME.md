DTO主要用于不同层之间的数据传输

在dto实体需要验证的属性前可以加上@NotBlank、@NotNull、@NotEmpty注解
@NotEmpty 用在集合类上面
@NotBlank 用在String上面
@NotNull 用在八种基本类型上{ byte、shor、int、long、float、double、boolean、char}