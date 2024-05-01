//package com.lfj.blog.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//
///**
// * @Author: LFJ
// * @Date: 2024-01-19 13:54
// */
//@Configuration
//public class RedisConfig extends CachingConfigurerSupport {
//
//	/*
//	这种配置适用于需要使用 Redis 作为缓存存储，且希望缓存的数据一直有效的情况。
//	@Bean
//	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(ZERO);
//		return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
//				.cacheDefaults(redisCacheConfiguration).build();
//	}
//	*/
//
//	@Bean
//	public LettuceConnectionFactory lettuceConnectionFactory() {
//		RedisStandaloneConfiguration redisStandaloneConfiguration =
//				new RedisStandaloneConfiguration("localhost", 6379);
//		return new LettuceConnectionFactory(redisStandaloneConfiguration);
//	}
//
////	@Bean
////	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
////		// 1.创建 redisTemplate 模版
////		RedisTemplate<String, Object> template = new RedisTemplate<>();
////		// 2.关联 redisConnectionFactory
////		template.setConnectionFactory(redisConnectionFactory);
////		// 3.创建 序列化类
////		GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);
////		// 6.序列化类，对象映射设置
////		// 7.设置 value 的转化格式和 key 的转化格式
////		template.setValueSerializer(genericToStringSerializer);
////		template.setKeySerializer(new StringRedisSerializer());
////		template.afterPropertiesSet();
////		return template;
////	}
//
//	/**
//	 * *redis序列化的工具定置类，下面这个请一定开启配置
//	 * *127.0.0.1:6379> keys *
//	 * *1) “ord:102” 序列化过
//	 * *2)“\xaclxedlxeelx05tixeelaord:102” 野生，没有序列化过
//	 * *this.redisTemplate.opsForValue(); //提供了操作string类型的所有方法
//	 * *this.redisTemplate.opsForList();// 提供了操作List类型的所有方法
//	 * *this.redisTemplate.opsForset(); //提供了操作set类型的所有方法
//	 * *this.redisTemplate.opsForHash(); //提供了操作hash类型的所有方认
//	 * *this.redisTemplate.opsForZSet(); //提供了操作zset类型的所有方法
//	 * param LettuceConnectionFactory
//	 * return
//	 */
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
//		//pom导入对应的依-->导包
//		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
//		// 设置key序列化方式string
//		redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//		//	// 源代码private RedisSerializer<String> stringSerializer = RedisSerializer.string();
//		// 设置value的序列化方式json，使用GenericJackson2JsonRedisSerializer替换默认序列化
//		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//		redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//		redisTemplate.afterPropertiesSet();
//		return redisTemplate;
//	}
////
//
//	/**
//	 * redis缓存管理器
//	 *
//	 * @param redisConnectionFactory
//	 * @return
//	 */
//	@Bean
//	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//		// 分别创建String和JSON格式序列化对象，对缓存数据key和value进行转换
//		RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
//		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
//
//		// 解决查询缓存转换异常的问题
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//
//		// 定制缓存数据序列化方式及时效
//		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//				.entryTtl(Duration.ofDays(1))
//				// 缓存key序列化方式
//				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
//				// 缓存value序列化方式
//				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
//				.disableCachingNullValues();
//		return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
//	}
//
//}
//////@Configuration
//////public class RedisConfig {
//////
//////	//GenericJackson2JsonRedisSerializer
//////	@Bean
//////	@ConditionalOnMissingBean(name = "redisTemplate")
//////	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
//////		RedisTemplate<String, Object> template = new RedisTemplate<>();
//////		template.setConnectionFactory(factory);
//////
//////		//String的序列化方式
//////		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//////		// 使用GenericJackson2JsonRedisSerializer 替换默认序列化(默认采用的是JDK序列化)
//////		GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//////
//////		//key序列化方式采用String类型
//////		template.setKeySerializer(stringRedisSerializer);
//////		//value序列化方式采用jackson类型
//////		template.setValueSerializer(genericJackson2JsonRedisSerializer);
//////
//////		//hash的key序列化方式也是采用String类型
//////		template.setHashKeySerializer(stringRedisSerializer);
//////		//hash的value也是采用jackson类型
//////		template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
//////		template.afterPropertiesSet();
//////		return template;
//////	}
//////}