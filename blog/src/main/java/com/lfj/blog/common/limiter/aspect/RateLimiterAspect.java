package com.lfj.blog.common.limiter.aspect;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lfj.blog.common.constant.RedisPrefixConstant;
import com.lfj.blog.common.limiter.ExtraLimiter;
import com.lfj.blog.common.limiter.Limit;
import com.lfj.blog.common.limiter.annotation.RateLimiter;
import com.lfj.blog.common.response.enums.ResponseCodeEnum;
import com.lfj.blog.exception.ApiException;
import com.lfj.blog.utils.IpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 * <p>
 * 2019/11/5 10:49 下午
 */
@Log4j2
@Aspect
@Order
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RateLimiterAspect implements ApplicationContextAware {

	private final static String SEPARATOR = ":";

	@Resource
	private final StringRedisTemplate stringRedisTemplate;

	//	private final RedisScript<Long> limitRedisScript;
	private DefaultRedisScript<Long> getRedisScript;

	private ApplicationContext applicationContext;

	/**
	 * 用于SpEL表达式解析.
	 */
	private SpelExpressionParser parser = new SpelExpressionParser();
	/**
	 * 用于获取方法参数定义名字.
	 */
	private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

	@PostConstruct
	public void init() {
		getRedisScript = new DefaultRedisScript<>();
		getRedisScript.setResultType(Long.class);
		// 限流脚本
		getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/redis/limit.lua")));
		log.info("RateLimterAspect[分布式限流处理器]脚本加载完成");
	}

	/**
	 * 定义切入点----以aspect包下带有 @RateLimter注解
	 */
	@Pointcut("@annotation(com.lfj.blog.common.limiter.annotation.RateLimiter)")
	public void rateLimiter() {

	}

	// implements ApplicationContextAware
	//当一个类需要与 Spring 容器进行交互时，可以通过实现 ApplicationContextAware 接口并重写 setApplicationContext 方法来实现。
	// 这种方式可以让类获得对应用程序上下文的引用，从而更容易地进行依赖注入和实现一些与容器交互的功能。
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Around("rateLimiter()")
	public Object pointcut(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		// 通过 AnnotationUtils.findAnnotation 获取 RateLimiter 注解
		RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
		// @RateLimter注解携带的参数
		if (rateLimiter != null && !rateLimiter.onlyExtra()) {
			/**
			 * 根据限流类型获取不同的key ,如果不传我们会以方法名作为key
			 */
			String key = rateLimiter.key();
			if (StringUtils.isBlank(key)) {
				key = rateLimiter.name();
			} else {
				key = rateLimiter.name() + SEPARATOR + generateKeyBySpEL(key, point);
			}
			if (rateLimiter.appendIp()) {
				key = key + SEPARATOR + IpUtil.getIpAddress();  //ip
			}
			long max = rateLimiter.max();
			long timeout = rateLimiter.timeout();
			TimeUnit timeUnit = rateLimiter.timeUnit();

			Limit limit = new Limit();
			limit.setKey(key);
			limit.setMax(max);
			limit.setTimeout(timeout);
			limit.setTimeUnit(timeUnit);
			limit.setMessage(rateLimiter.message());
			//调用脚本并执行
			handleLimit(limit);
		}

		// 附加限流
		if (rateLimiter != null && !StringUtils.isBlank(rateLimiter.extra())) {
			// 获取bean
			ExtraLimiter limiter = (ExtraLimiter) applicationContext.getBean(rateLimiter.extra());
			if (limiter != null) {
				List<Limit> limit = limiter.limit(rateLimiter, point);
				limit.forEach(this::handleLimit);
			}
		}
		return point.proceed();
	}

	/**
	 * SpEL表达式缓存Key生成器.
	 * 注解中传入key参数，则使用此生成器生成缓存.
	 *
	 * @param spELString
	 * @param joinPoint
	 * @return
	 */
	private String generateKeyBySpEL(String spELString, ProceedingJoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
		Expression expression = parser.parseExpression(spELString);
		EvaluationContext context = new StandardEvaluationContext();
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			context.setVariable(paramNames[i], args[i]);
		}
		return expression.getValue(context).toString();
	}

	/**
	 * 限流处理
	 *
	 * @return
	 */
	private void handleLimit(Limit limit) {
		String key = RedisPrefixConstant.REDIS_LIMIT_KEY_PREFIX + limit.getKey();
		long ttl = limit.getTimeUnit().toMillis(limit.getTimeout());
		long now = Instant.now().toEpochMilli();
		long max = limit.getMax();
		long expired = now - ttl;
		// 注意这里必须转为 String,否则会报错 java.lang.Long cannot be cast to java.lang.String
		Long executeTimes = stringRedisTemplate.execute(getRedisScript, Collections.singletonList(key), now + "", ttl + "", expired + "", max + "");
		if (executeTimes != null) {
			if (executeTimes == 0) {
				log.error("【{}】在单位时间 {} 毫秒内已达到访问上限，当前接口上限 {}", key, ttl, max);
				throw new ApiException(ResponseCodeEnum.REQUEST_LIMIT.getCode(), limit.getMessage());
			} else {
				log.info("【{}】在单位时间 {} 毫秒内访问 {} 次", key, ttl, executeTimes);
			}
		}
	}
}
